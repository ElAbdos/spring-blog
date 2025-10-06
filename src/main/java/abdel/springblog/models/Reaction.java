package abdel.springblog.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reactions",
        uniqueConstraints = @UniqueConstraint(name = "uk_reaction_user_article", columnNames = {"user_id", "article_id"}))
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private ReactionType type;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_reaction_user"))
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_reaction_article"))
    private Article article;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Reaction() {}

    public Reaction(ReactionType type, User user, Article article) {
        this.type = type;
        this.user = user;
        this.article = article;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public ReactionType getType() { return type; }
    public void setType(ReactionType type) { this.type = type; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Article getArticle() { return article; }
    public void setArticle(Article article) { this.article = article; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
