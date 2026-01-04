package abdel.springblog.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "reactions", uniqueConstraints = @UniqueConstraint(name = "uk_reaction_user_article", columnNames = {"user_id", "article_id"}))
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private ReactionType type;

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_reaction_user"))
    private User user;

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false, foreignKey = @ForeignKey(name = "fk_reaction_article"))
    private Article article;

    @Setter
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Reaction() {}

    public Reaction(ReactionType type, User user, Article article) {
        this.type = type;
        this.user = user;
        this.article = article;
        this.createdAt = LocalDateTime.now();
    }
}
