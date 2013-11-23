package in.tryjava8.api.controllers;

import in.tryjava8.api.domain.Snippet;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/programs")
public class MyProgramController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "{githubLogin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Snippet>> myPrograms(@PathVariable("githubLogin") String githubLogin) {

        if (StringUtils.isBlank(githubLogin)) {
            ResponseEntity<List<Snippet>> responseEntity = new ResponseEntity<List<Snippet>>(
                    Collections.<Snippet> emptyList(), HttpStatus.OK);

            return responseEntity;
        }
        Criteria criteria = Criteria.where("githubLogin").is(githubLogin);
        Query query = Query.query(criteria);
        query.with(new Sort(Direction.DESC, "createdOn"));
        List<Snippet> snippets = mongoTemplate.find(query, Snippet.class);
        ResponseEntity<List<Snippet>> responseEntity = new ResponseEntity<List<Snippet>>(snippets, HttpStatus.OK);
        return responseEntity;
    }
}
