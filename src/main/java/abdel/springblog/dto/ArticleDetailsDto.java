package abdel.springblog.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleDetailsDto {
    private final String author;
    private final LocalDateTime publishedAt;
    private final String content;
    private final long likeCount;
    private final long dislikeCount;
    private final List<String> likedBy;
    private final List<String> dislikedBy;

    public ArticleDetailsDto(String author, LocalDateTime publishedAt, String content,
                             long likeCount, long dislikeCount,
                             List<String> likedBy, List<String> dislikedBy) {
        this.author = author;
        this.publishedAt = publishedAt;
        this.content = content;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.likedBy = likedBy;
        this.dislikedBy = dislikedBy;
    }

    public String getAuthor() { return author; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public String getContent() { return content; }
    public long getLikeCount() { return likeCount; }
    public long getDislikeCount() { return dislikeCount; }
    public List<String> getLikedBy() { return likedBy; }
    public List<String> getDislikedBy() { return dislikedBy; }
}
