package abdel.springblog.dto;

public class ArticleCreateDto {
    private String authorUsername;
    private String content;

    public ArticleCreateDto() {}

    public String getAuthorUsername() { return authorUsername; }
    public String getContent() { return content; }
}
