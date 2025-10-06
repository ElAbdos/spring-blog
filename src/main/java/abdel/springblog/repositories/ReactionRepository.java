package abdel.springblog.repositories;

import abdel.springblog.models.Article;
import abdel.springblog.models.Reaction;
import abdel.springblog.models.ReactionType;
import abdel.springblog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    long countByArticleAndType(Article article, ReactionType type);
    List<Reaction> findByArticleAndType(Article article, ReactionType type);
    Optional<Reaction> findByArticleAndUser(Article article, User user);
}
