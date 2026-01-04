package abdel.springblog.services;

import abdel.springblog.dto.ArticleCreateDto;
import abdel.springblog.dto.ArticleDetailsDto;
import abdel.springblog.dto.ArticlePublicDto;
import abdel.springblog.models.*;
import abdel.springblog.repositories.ArticleRepository;
import abdel.springblog.repositories.ReactionRepository;
import abdel.springblog.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository, ReactionRepository reactionRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.reactionRepository = reactionRepository;
    }

    /*
     * Crée un nouvel article
     */
    public ArticlePublicDto create(ArticleCreateDto dto) {
        User author = userRepository.findByUsername(dto.getAuthorUsername())
                .orElseThrow(() -> new IllegalArgumentException("Auteur introuvable: " + dto.getAuthorUsername()));
        Article article = new Article(author, dto.getContent());
        Article saved = articleRepository.save(article);
        return toPublicDto(saved);
    }

    /*
     * Récupère la liste de tous les articles avec leurs informations publiques
     */
    @Transactional(readOnly = true)
    public List<ArticlePublicDto> listPublic() {
        return articleRepository.findAll().stream().map(this::toPublicDto).collect(toList());
    }

    /*
     * Récupère les informations publiques d'un article par son ID
     */
    @Transactional(readOnly = true)
    public ArticlePublicDto getPublic(Long id) {
        Article a = articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Article introuvable: " + id));
        return toPublicDto(a);
    }

    /*
     * Récupère les détails d'un article, y compris les réactions
     */
    @Transactional(readOnly = true)
    public ArticleDetailsDto getDetails(Long id) {
        Article a = articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Article introuvable: " + id));
        return toDetailsDto(a);
    }

    /*
     * Met à jour le contenu d'un article
     */
    public ArticlePublicDto updateContent(Long id, String newContent, String currentUsername) {
        Article a = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article introuvable: " + id));

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        if (!a.getAuthor().getUsername().equals(currentUsername) && currentUser.getRole() != Role.MODERATOR) {
            throw new SecurityException("Vous n'avez pas le droit de modifier cet article");
        }
        a.setContent(newContent);
        return toPublicDto(a);
    }

    /*
     * Supprime un article par son ID
     */
    public void delete(Long id, String currentUsername) {
        Article a = articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Article introuvable: " + id));
        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        if (!a.getAuthor().getUsername().equals(currentUsername) && currentUser.getRole() != Role.MODERATOR) {
            throw new SecurityException("Vous n'avez pas le droit de supprimer cet article");
        }
        articleRepository.deleteById(id);
    }

    /*
     * Ajoute ou met à jour une réaction (like/dislike) d'un utilisateur sur un article
     */
    public ArticleDetailsDto react(Long articleId, String username, ReactionType type) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new IllegalArgumentException("Article introuvable: " + articleId));
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable: " + username));
        Reaction existing = reactionRepository.findByArticleAndUser(article, user).orElse(null);
        if (existing == null) {
            reactionRepository.save(new Reaction(type, user, article));
        } else if (existing.getType() != type) {
            existing.setType(type);
        }
        return toDetailsDto(article);
    }

    private ArticlePublicDto toPublicDto(Article a) {
        return new ArticlePublicDto(a.getId(), a.getAuthor().getUsername(), a.getPublishedAt(), a.getContent());
    }

    /*
     * Convertit une entité Article en DTO ArticleDetailsDto, en comptant les likes et dislikes
     */
    private ArticleDetailsDto toDetailsDto(Article a) {
        long likes = reactionRepository.countByArticleAndType(a, ReactionType.LIKE);
        long dislikes = reactionRepository.countByArticleAndType(a, ReactionType.DISLIKE);
        List<String> likedBy = reactionRepository.findByArticleAndType(a, ReactionType.LIKE).stream().map(r -> r.getUser().getUsername()).collect(toList());
        List<String> dislikedBy = reactionRepository.findByArticleAndType(a, ReactionType.DISLIKE).stream().map(r -> r.getUser().getUsername()).collect(toList());
        return new ArticleDetailsDto(a.getId(), a.getAuthor().getUsername(), a.getPublishedAt(), a.getContent(), likes, dislikes, likedBy, dislikedBy);
    }
}
