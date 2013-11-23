package in.tryjava8.api.controllers;

public class Status {

    private String text;

    public Status() {
    }

    public Status(String text) {
        super();
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
