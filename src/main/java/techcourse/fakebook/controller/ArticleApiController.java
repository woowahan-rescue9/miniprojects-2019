package techcourse.fakebook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.fakebook.controller.utils.SessionUser;
import techcourse.fakebook.service.ArticleService;
import techcourse.fakebook.service.dto.ArticleLikeResponse;
import techcourse.fakebook.service.dto.ArticleRequest;
import techcourse.fakebook.service.dto.ArticleResponse;
import techcourse.fakebook.service.dto.UserOutline;

import java.util.List;
import java.net.URI;

@RestController
@RequestMapping("/api/articles")
public class ArticleApiController {
    private ArticleService articleService;

    public ArticleApiController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> findAll() {
        List<ArticleResponse> articles = articleService.findAll();
        return ResponseEntity.ok().body(articles);
    }

    @PostMapping
    public ResponseEntity<ArticleResponse> create(@RequestBody ArticleRequest articleRequest, @SessionUser UserOutline userOutline) {
        ArticleResponse articleResponse = articleService.save(articleRequest, userOutline);
        return ResponseEntity.created(URI.create("/api/articles")).body(articleResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> update(@PathVariable Long id, @RequestBody ArticleRequest articleRequest, @SessionUser UserOutline userOutline) {
        ArticleResponse articleResponse = articleService.update(id, articleRequest, userOutline);
        return ResponseEntity.ok().body(articleResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ArticleResponse> delete(@PathVariable Long id, @SessionUser UserOutline userOutline) {
        articleService.deleteById(id, userOutline);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/like")
    public ResponseEntity<ArticleLikeResponse> checkLike(@PathVariable Long id, @SessionUser UserOutline userOutline) {
        ArticleLikeResponse articleLikeResponse = articleService.isLiked(id, userOutline);
        return ResponseEntity.ok().body(articleLikeResponse);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ArticleLikeResponse> like(@PathVariable Long id, @SessionUser UserOutline userOutline) {
        ArticleLikeResponse articleLikeResponse = articleService.like(id, userOutline);
        return ResponseEntity.ok().body(articleLikeResponse);
    }

    @GetMapping("/{id}/like/count")
    public ResponseEntity<Integer> countLikeOfArticle(@PathVariable Long id) {
        Integer numberOfLike = articleService.getLikeCountOf(id);
        return ResponseEntity.ok().body(numberOfLike);
    }
}
