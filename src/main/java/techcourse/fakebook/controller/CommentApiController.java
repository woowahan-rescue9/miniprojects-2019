package techcourse.fakebook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.fakebook.service.CommentService;
import techcourse.fakebook.service.dto.CommentLikeResponse;
import techcourse.fakebook.service.dto.CommentRequest;
import techcourse.fakebook.service.dto.CommentResponse;
import techcourse.fakebook.service.dto.UserDto;

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
    public ResponseEntity<CommentResponse> create(@PathVariable Long articleId, @RequestBody CommentRequest commentRequest, HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        CommentResponse commentResponse = commentService.save(articleId, commentRequest, userDto);
        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> update(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest, HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        CommentResponse commentResponse = commentService.update(commentId, commentRequest, userDto);
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponse> delete(@PathVariable Long commentId, HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        commentService.deleteById(commentId, userDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<CommentLikeResponse> like(@PathVariable Long commentId, HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("user");
        CommentLikeResponse commentLikeResponse = commentService.like(commentId, userDto);
        return new ResponseEntity<>(commentLikeResponse, HttpStatus.OK);
    }
}
