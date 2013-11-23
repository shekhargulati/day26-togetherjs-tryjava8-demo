package in.tryjava8.api.controllers;

import in.tryjava8.api.domain.CompilerOutput;
import in.tryjava8.api.domain.ProgramOutput;
import in.tryjava8.api.domain.Result;
import in.tryjava8.api.domain.Snippet;
import in.tryjava8.api.domain.Verdict;
import in.tryjava8.api.service.CodeExecutionService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/snippets")
public class SnippetsController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CodeExecutionService codeExecutionService;

    private ExecutorService executorService = Executors.newFixedThreadPool(30);

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result> saveAndRunSnippet(@RequestBody final Snippet snippet,
            final HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) throws Exception {

        final HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");

        mongoTemplate.insert(snippet);

        final String snippetUrl = request.getRequestURL().toString() + "/" + snippet.getId();

        Callable<ResponseEntity<Result>> callable = new Callable<ResponseEntity<Result>>() {

            @Override
            public ResponseEntity<Result> call() throws Exception {

                CompilerOutput compilerOutput = codeExecutionService.compile(snippet);

                if (compilerOutput.getResult() != 0) {

                    Result result = new Result(compilerOutput.getResult(), compilerOutput.getMessage(),
                            Verdict.COMPILATION_FAILED, snippetUrl);

                    ResponseEntity<Result> responseEntity = new ResponseEntity<Result>(result, headers, HttpStatus.OK);
                    return responseEntity;
                }

                ProgramOutput programOutput = codeExecutionService.run(codeExecutionService.getJavaClassName(snippet),
                        snippet);

                Verdict verdict = programOutput.getVerdict();
                Result result = new Result(compilerOutput.getResult(), programOutput.getResult(), verdict, snippetUrl);

                ResponseEntity<Result> responseEntity = new ResponseEntity<Result>(result, headers, HttpStatus.OK);
                return responseEntity;
            }
        };

        List<Callable<ResponseEntity<Result>>> tasks = new ArrayList<Callable<ResponseEntity<Result>>>();
        tasks.add(callable);
        List<Future<ResponseEntity<Result>>> results = executorService.invokeAll(tasks, 1, TimeUnit.MINUTES);

        Future<ResponseEntity<Result>> futureResult = results.get(0);
        try {
            ResponseEntity<Result> result = futureResult.get();
            return result;
        } catch (Exception e) {
            Result result = new Result(0, "Program failed to completed under 1 minute", Verdict.FAILURE, snippetUrl);
            return new ResponseEntity<Result>(result, HttpStatus.OK);
        }

    }

}
