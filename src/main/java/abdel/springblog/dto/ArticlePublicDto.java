package abdel.springblog.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticlePublicDto {
    private final Long id;
    private final String author;
    private final LocalDateTime publishedAt;
    private final String content;

    public ArticlePublicDto(Long id, String author, LocalDateTime publishedAt, String content) {
        this.id = id;
        this.author = author;
        this.publishedAt = publishedAt;
        this.content = content;
    }
}
