package in.tryjava8.api.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "snippets")
public class Snippet {

    @Id
    private String id;

    private String code;

    private final Date createdOn = new Date();

    private String githubLogin;

    public Snippet() {
        // TODO Auto-generated constructor stub
    }

    public Snippet(String code) {
        this.code = code;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setGithubLogin(String githubLogin) {
        this.githubLogin = githubLogin;
    }

    public String getGithubLogin() {
        return githubLogin;
    }
}
