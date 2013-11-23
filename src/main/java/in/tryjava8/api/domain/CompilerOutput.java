package in.tryjava8.api.domain;

public class CompilerOutput {

    private int result;

    private String message;

    public CompilerOutput() {
        // TODO Auto-generated constructor stub
    }

    public CompilerOutput(int result, String message) {
        this.result = result;
        this.message = message;
    }

    public int getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
