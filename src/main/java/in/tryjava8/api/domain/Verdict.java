package in.tryjava8.api.domain;

public enum Verdict {

    SUCCESS("Program ran successfully"), FAILURE("Program failed to run"), COMPILATION_FAILED("Compilation Failed");

    private String message;

    private Verdict(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
