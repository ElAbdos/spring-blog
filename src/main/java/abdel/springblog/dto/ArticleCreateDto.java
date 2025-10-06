package abdel.springblog.dto;

public class ArticleCreateDto {
    private String authorUsername;
    private String content;

    public ArticleCreateDto() {}

    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
