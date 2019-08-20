package techcourse.fakebook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.fakebook.controller.utils.SessionUser;
import techcourse.fakebook.service.ArticleService;
import techcourse.fakebook.service.dto.ArticleLikeResponse;
import techcourse.fakebook.service.dto.ArticleRequest;
import techcourse.fakebook.service.dto.ArticleResponse;
import techcourse.fakebook.service.dto.UserOutline;

@RestController
@RequestMapping("/api/articles")
public class ArticleApiController {
    private ArticleService articleService;

    public ArticleApiController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<ArticleResponse> create(@RequestBody ArticleRequest articleRequest, @SessionUser UserOutline userOutline) {
        ArticleResponse articleResponse = articleService.save(articleRequest, userOutline);
        return new ResponseEntity<>(articleResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> update(@PathVariable Long id, @RequestBody ArticleRequest articleRequest, @SessionUser UserOutline userOutline) {
        ArticleResponse articleResponse = articleService.update(id, articleRequest, userOutline);
        return new ResponseEntity<>(articleResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ArticleResponse> delete(@PathVariable Long id, @SessionUser UserOutline userOutline) {
        articleService.deleteById(id, userOutline);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/like")
    public ResponseEntity<ArticleLikeResponse> checkLike(@PathVariable Long id, @SessionUser UserOutline userOutline) {
        ArticleLikeResponse articleLikeResponse = articleService.isLiked(id, userOutline);
        return new ResponseEntity<>(articleLikeResponse, HttpStatus.OK);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ArticleLikeResponse> like(@PathVariable Long id, @SessionUser UserOutline userOutline) {
        ArticleLikeResponse articleLikeResponse = articleService.like(id, userOutline);
        return new ResponseEntity<>(articleLikeResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}/like/count")
    public ResponseEntity<Integer> countLikeOfArticle(@PathVariable Long id) {
        Integer numberOfLike = articleService.getLikeCountOf(id);
        return ResponseEntity.ok().body(numberOfLike);
    }
}
