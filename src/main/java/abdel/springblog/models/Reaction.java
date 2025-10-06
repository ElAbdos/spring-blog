package abdel.springblog.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reactions", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "article_id"}))
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Enumerated(EnumType.STRING)
    private ReactionType type;

    private LocalDateTime reactedAt;

    public Reaction() {}

    @PrePersist
    protected void onCreate() {
        this.reactedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Article getArticle() { return article; }
    public void setArticle(Article article) { this.article = article; }
    public ReactionType getType() { return type; }
    public void setType(ReactionType type) { this.type = type; }
    public LocalDateTime getReactedAt() { return reactedAt; }
    public void setReactedAt(LocalDateTime reactedAt) { this.reactedAt = reactedAt; }
}
