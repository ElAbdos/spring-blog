package abdel.springblog.dto;

import java.time.LocalDateTime;

public class ArticlePublicDto {
    private final String author;
    private final LocalDateTime publishedAt;
    private final String content;

    public ArticlePublicDto(String author, LocalDateTime publishedAt, String content) {
        this.author = author;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public String getAuthor() { return author; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public String getContent() { return content; }
}
