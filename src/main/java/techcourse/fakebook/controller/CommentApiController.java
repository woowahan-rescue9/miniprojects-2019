package techcourse.fakebook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.fakebook.controller.utils.SessionUser;
import techcourse.fakebook.service.dto.UserOutline;
import techcourse.fakebook.service.CommentService;
import techcourse.fakebook.service.dto.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/articles/{articleId}/comments")
public class CommentApiController {
    private CommentService commentService;

    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> findAllByArticleId(@PathVariable Long articleId) {
        List<CommentResponse> commentResponses = commentService.findAllByArticleId(articleId);
        return new ResponseEntity<>(commentResponses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommentResponse> create(@PathVariable Long articleId, @RequestBody CommentRequest commentRequest, @SessionUser UserOutline userOutline) {
        CommentResponse commentResponse = commentService.save(articleId, commentRequest, userOutline);
        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> update(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest, @SessionUser UserOutline userOutline) {
        CommentResponse commentResponse = commentService.update(commentId, commentRequest, userOutline);
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponse> delete(@PathVariable Long commentId, @SessionUser UserOutline userOutline) {
        commentService.deleteById(commentId, userOutline);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{commentId}/like")
    public ResponseEntity<CommentLikeResponse> checkLike(@PathVariable Long commentId, @SessionUser UserOutline userOutline) {
        CommentLikeResponse commentLikeResponse = commentService.isLiked(commentId, userOutline);
        return new ResponseEntity<>(commentLikeResponse, HttpStatus.OK);
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<CommentLikeResponse> like(@PathVariable Long commentId, @SessionUser UserOutline userOutline) {
        CommentLikeResponse commentLikeResponse = commentService.like(commentId, userOutline);
        return new ResponseEntity<>(commentLikeResponse, HttpStatus.OK);
    }
}
