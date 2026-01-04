package abdel.springblog.controllers;

import abdel.springblog.dto.ArticleCreateDto;
import abdel.springblog.dto.ArticleDetailsDto;
import abdel.springblog.dto.ArticlePublicDto;
import abdel.springblog.models.ReactionType;
import abdel.springblog.services.ArticleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    /*
     * Lister tous les articles publics
     */
    @GetMapping
    public List<ArticlePublicDto> listPublic() {
        return service.listPublic();
    }

    /*
     * Obtenir un article public par ID
     */
    @GetMapping("/{id}")
    public ArticlePublicDto getPublic(@PathVariable Long id) {
        return service.getPublic(id);
    }

    /*
     * Obtenir les détails d'un article
     */
    @GetMapping("/{id}/details")
    public ArticleDetailsDto getDetails(@PathVariable Long id) {
        return service.getDetails(id);
    }

    /*
     * Créer un nouvel article
     */
    @PostMapping
    public ArticlePublicDto create(@RequestBody ArticleCreateDto dto) {
        return service.create(dto);
    }

    /*
     * Mettre à jour le contenu d'un article
     */
    @PutMapping("/{id}")
    public ArticlePublicDto update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String content = body.get("content");
        return service.updateContent(id, content);
    }

    /*
     * Supprimer un article
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    /*
     * Réactions (like/dislike)
     */
    @PostMapping("/{id}/reactions")
    public ArticleDetailsDto react(@PathVariable Long id, @RequestParam String username, @RequestParam ReactionType type) {
        return service.react(id, username, type);
    }
}
