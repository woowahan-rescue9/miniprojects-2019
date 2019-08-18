package techcourse.fakebook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.fakebook.controller.utils.SessionUser;
import techcourse.fakebook.service.CommentService;
import techcourse.fakebook.service.dto.CommentLikeResponse;
import techcourse.fakebook.service.dto.CommentRequest;
import techcourse.fakebook.service.dto.CommentResponse;
import techcourse.fakebook.service.dto.UserOutline;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/articles/{articleId}/comments")
public class CommentApiController {
    private CommentService commentService;

    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> findAllByArticleId(@PathVariable Long articleId) {
        List<CommentResponse> commentResponses = commentService.findAllByArticleId(articleId);
        return ResponseEntity.ok().body(commentResponses);
    }

    @PostMapping
    public ResponseEntity<CommentResponse> create(@PathVariable Long articleId, @RequestBody CommentRequest commentRequest, @SessionUser UserOutline userOutline) {
        CommentResponse commentResponse = commentService.save(articleId, commentRequest, userOutline);
        return ResponseEntity.created(URI.create("/api/articles/" + articleId + "/comments")).body(commentResponse);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> update(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest, @SessionUser UserOutline userOutline) {
        CommentResponse commentResponse = commentService.update(commentId, commentRequest, userOutline);
        return ResponseEntity.ok().body(commentResponse);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponse> delete(@PathVariable Long commentId, @SessionUser UserOutline userOutline) {
        commentService.deleteById(commentId, userOutline);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{commentId}/like")
    public ResponseEntity<HttpStatus> checkLike(@PathVariable Long commentId, @SessionUser UserOutline userOutline) {
        if (commentService.isLiked(commentId, userOutline)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<HttpStatus> like(@PathVariable Long articleId, @PathVariable Long commentId, @SessionUser UserOutline userOutline) {
        commentService.like(commentId, userOutline);
        if (commentService.isLiked(commentId, userOutline)) {
            return ResponseEntity.created(URI.create("/api/articles/" + articleId + "/comments/" + commentId + "/like")).build();
        }
        return ResponseEntity.noContent().build();
    }
}
