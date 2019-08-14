package techcourse.fakebook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.fakebook.service.ArticleService;
import techcourse.fakebook.service.dto.ArticleLikeResponse;
import techcourse.fakebook.service.dto.ArticleRequest;
import techcourse.fakebook.service.dto.ArticleResponse;
import techcourse.fakebook.service.dto.UserDto;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/articles")
public class ArticleApiController {
    private ArticleService articleService;

    public ArticleApiController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<ArticleResponse> create(@RequestBody ArticleRequest articleRequest, HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        ArticleResponse articleResponse = articleService.save(articleRequest, userDto);
        return new ResponseEntity<>(articleResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> update(@PathVariable Long id, @RequestBody ArticleRequest articleRequest, HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        ArticleResponse articleResponse = articleService.update(id, articleRequest, userDto);
        return new ResponseEntity<>(articleResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ArticleResponse> delete(@PathVariable Long id, HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        articleService.deleteById(id, userDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/like")
    public ResponseEntity<ArticleLikeResponse> checkLike(@PathVariable Long id, HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        ArticleLikeResponse articleLikeResponse = articleService.isLiked(id, userDto);
        return new ResponseEntity<>(articleLikeResponse, HttpStatus.OK);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ArticleLikeResponse> like(@PathVariable Long id, HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        ArticleLikeResponse articleLikeResponse = articleService.like(id, userDto);
        return new ResponseEntity<>(articleLikeResponse, HttpStatus.OK);
    }
}
