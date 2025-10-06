package abdel.springblog.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleDetailsDto {
    private String author;
    private LocalDateTime publishedAt;
    private String content;
    private long likeCount;
    private long dislikeCount;
    private List<String> likedBy;
    private List<String> dislikedBy;

    public ArticleDetailsDto() {}

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
