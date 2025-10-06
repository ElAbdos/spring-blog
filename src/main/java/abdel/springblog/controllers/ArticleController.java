package abdel.springblog.controllers;

import abdel.springblog.dto.ArticleCreateDto;
import abdel.springblog.dto.ArticleDetailsDto;
import abdel.springblog.dto.ArticlePublicDto;
import abdel.springblog.models.ReactionType;
import abdel.springblog.services.ArticleService;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public List<ArticlePublicDto> listPublic() {
        return service.listPublic();
    }

    @GetMapping("/{id}")
    public ArticlePublicDto getPublic(@PathVariable Long id) {
        return service.getPublic(id);
    }

    // Vue détails
    @GetMapping("/{id}/details")
    public ArticleDetailsDto getDetails(@PathVariable Long id) {
        return service.getDetails(id);
    }

    // Créer un article
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticlePublicDto create(@RequestBody ArticleCreateDto dto) {
        return service.create(dto);
    }

    // Modifier contenu
    @PutMapping("/{id}")
    public ArticlePublicDto update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String content = body.get("content");
        return service.updateContent(id, content);
    }

    // Supprimer
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // Like/Dislike:
    @PostMapping("/{id}/reactions")
    public ArticleDetailsDto react(@PathVariable Long id, @RequestParam String username, @RequestParam ReactionType type) {
        return service.react(id, username, type);
    }
}
