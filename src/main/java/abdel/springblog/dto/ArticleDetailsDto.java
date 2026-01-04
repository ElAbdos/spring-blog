package abdel.springblog.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ArticleDetailsDto {
    private final Long id;
    private final String author;
    private final LocalDateTime publishedAt;
    private final String content;
    private final long likeCount;
    private final long dislikeCount;
    private final List<String> likedBy;
    private final List<String> dislikedBy;

    public ArticleDetailsDto(Long id, String author, LocalDateTime publishedAt, String content, long likeCount, long dislikeCount, List<String> likedBy, List<String> dislikedBy) {
        this.id = id;
        this.author = author;
        this.publishedAt = publishedAt;
        this.content = content;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.likedBy = likedBy;
        this.dislikedBy = dislikedBy;
    }
}
