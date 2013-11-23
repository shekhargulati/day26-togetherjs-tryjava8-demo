package in.tryjava8.api.domain;

public class Result {

    private int compilerOutput;

    private String result;

    private Verdict verdict;

    private String snippetUrl;

    public Result() {
        // TODO Auto-generated constructor stub
    }

    public Result(int compilerOutput, String result, Verdict verdict, String snippetUrl) {
        this.compilerOutput = compilerOutput;
        this.result = result;
        this.verdict = verdict;
        this.snippetUrl = snippetUrl;
    }

    public int getCompilerOutput() {
        return compilerOutput;
    }

    public String getResult() {
        return result;
    }

    public Verdict getVerdict() {
        return verdict;
    }

    public String getSnippetUrl() {
        return snippetUrl;
    }

}
