package abdel.springblog.dto;

import java.time.LocalDateTime;

public class ArticlePublicDto {
    private String author;
    private LocalDateTime publishedAt;
    private String content;

    public ArticlePublicDto() {}

    public ArticlePublicDto(String author, LocalDateTime publishedAt, String content) {
        this.author = author;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public String getAuthor() { return author; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public String getContent() { return content; }
}
