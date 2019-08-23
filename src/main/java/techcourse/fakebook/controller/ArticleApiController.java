package techcourse.fakebook.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.fakebook.controller.utils.SessionUser;
import techcourse.fakebook.service.ArticleService;
import techcourse.fakebook.service.dto.ArticleRequest;
import techcourse.fakebook.service.dto.ArticleResponse;
import techcourse.fakebook.service.dto.UserOutline;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleApiController {
    private final ArticleService articleService;

    public ArticleApiController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> findAll() {
        List<ArticleResponse> articles = articleService.findAll();
        return ResponseEntity.ok().body(articles);
    }

    @PostMapping
    public ResponseEntity<ArticleResponse> create(ArticleRequest articleRequest, @SessionUser UserOutline userOutline) {
        ArticleResponse articleResponse = articleService.save(articleRequest, userOutline);
        return ResponseEntity.created(URI.create("/api/articles/" + articleResponse.getId())).body(articleResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> update(@PathVariable Long id, @RequestBody ArticleRequest articleRequest,
                                                  @SessionUser UserOutline userOutline) {
        ArticleResponse articleResponse = articleService.update(id, articleRequest, userOutline);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;")
                .body(articleResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @SessionUser UserOutline userOutline) {
        articleService.deleteById(id, userOutline);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/like")
    public ResponseEntity<Void> checkLike(@PathVariable Long id, @SessionUser UserOutline userOutline) {
        if (articleService.isLiked(id, userOutline)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> like(@PathVariable Long id, @SessionUser UserOutline userOutline) {
        articleService.like(id, userOutline);
        if (articleService.isLiked(id, userOutline)) {
            return ResponseEntity.created(URI.create("/api/articles/" + id + "/like")).build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/like/count")
    public ResponseEntity<Integer> countLikeOfArticle(@PathVariable Long id) {
        Integer numberOfLike = articleService.getLikeCountOf(id);
        return ResponseEntity.ok().body(numberOfLike);
    }
}
