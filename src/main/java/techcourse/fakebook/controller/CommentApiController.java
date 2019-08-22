package techcourse.fakebook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.fakebook.controller.utils.SessionUser;
import techcourse.fakebook.service.CommentService;
import techcourse.fakebook.service.dto.CommentRequest;
import techcourse.fakebook.service.dto.CommentResponse;
import techcourse.fakebook.service.dto.UserOutline;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentApiController {
    private final CommentService commentService;

    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<List<CommentResponse>> findAllByArticleId(@PathVariable Long articleId) {
        List<CommentResponse> commentResponses = commentService.findAllByArticleId(articleId);
        return ResponseEntity.ok().body(commentResponses);
    }

    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<CommentResponse> create(@PathVariable Long articleId, @RequestBody CommentRequest commentRequest, @SessionUser UserOutline userOutline) {
        CommentResponse commentResponse = commentService.save(articleId, commentRequest, userOutline);
        return ResponseEntity.created(URI.create("/api/articles/" + articleId + "/comments/" + commentResponse.getId())).body(commentResponse);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> update(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest, @SessionUser UserOutline userOutline) {
        CommentResponse commentResponse = commentService.update(commentId, commentRequest, userOutline);
        return ResponseEntity.ok().body(commentResponse);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId, @SessionUser UserOutline userOutline) {
        commentService.deleteById(commentId, userOutline);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comments/{commentId}/like")
    public ResponseEntity<Void> checkLike(@PathVariable Long commentId, @SessionUser UserOutline userOutline) {
        if (commentService.isLiked(commentId, userOutline)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<Void> like(@PathVariable Long commentId, @SessionUser UserOutline userOutline) {
        commentService.like(commentId, userOutline);
        if (commentService.isLiked(commentId, userOutline)) {
            return ResponseEntity.created(URI.create("/api/comments/" + commentId + "/like")).build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comments/{commentId}/like/count")
    public ResponseEntity<Integer> countLikeOfComment(@PathVariable Long commentId) {
        Integer numberOfLike = commentService.getLikeCountOf(commentId);
        return ResponseEntity.ok().body(numberOfLike);
    }

    @GetMapping("/articles/{articleId}/comments/count")
    public ResponseEntity<Integer> countCommentOfArticle(@PathVariable Long articleId) {
        Integer numberOfComment = commentService.getCommentsCountOf(articleId);
        return ResponseEntity.ok().body(numberOfComment);
    }
}
