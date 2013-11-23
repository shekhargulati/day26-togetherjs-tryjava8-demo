package in.tryjava8.api.domain;

public class ProgramOutput {

    private String result;

    private Verdict verdict;

    public ProgramOutput() {
        // TODO Auto-generated constructor stub
    }

    public ProgramOutput(String result, Verdict verdict) {
        super();
        this.result = result;
        this.verdict = verdict;
    }

    public String getResult() {
        return result;
    }

    public Verdict getVerdict() {
        return verdict;
    }
}
