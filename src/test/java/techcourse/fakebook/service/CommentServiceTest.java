package techcourse.fakebook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.fakebook.domain.like.CommentLikeRepository;
import techcourse.fakebook.service.dto.UserOutline;
import techcourse.fakebook.exception.NotFoundCommentException;
import techcourse.fakebook.service.dto.CommentLikeResponse;
import techcourse.fakebook.service.dto.CommentRequest;
import techcourse.fakebook.service.dto.CommentResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    private UserOutline userOutline = new UserOutline(1L, "cony", "https");

    @Test
    void 댓글을_잘_불러오는지_확인한다() {
        List<CommentResponse> commentResponses = commentService.findAllByArticleId(1L);

        assertThat(commentResponses).isNotEmpty();
    }

    @Test
    void 댓글을_잘_작성하는지_확인한다() {
        CommentRequest commentRequest = new CommentRequest("댓글입니다.");
        CommentResponse commentResponse = commentService.save(1L, commentRequest, userOutline);

        assertThat(commentResponse.getContent()).isEqualTo(commentRequest.getContent());
    }

    @Test
    void 댓글을_잘_삭제하는지_확인한다() {
        CommentRequest commentRequest = new CommentRequest("댓글입니다.");
        CommentResponse commentResponse = commentService.save(1L, commentRequest, userOutline);
        Long deletedId = commentResponse.getId();

        commentService.deleteById(deletedId, userOutline);

        assertThrows(NotFoundCommentException.class, () -> commentService.findById(deletedId));
    }

    @Test
    void 댓글을_잘_수정하는지_확인한다() {
        CommentRequest commentRequest = new CommentRequest("댓글입니다.");
        CommentResponse commentResponse = commentService.save(1L, commentRequest, userOutline);
        CommentRequest updatedRequest = new CommentRequest("수정된 내용입니다.");

        CommentResponse updatedComment = commentService.update(commentResponse.getId(), updatedRequest, userOutline);

        assertThat(updatedComment.getContent()).isEqualTo(updatedRequest.getContent());
        assertThat(updatedComment.getId()).isEqualTo(commentResponse.getId());
    }

    @Test
    void 좋아요가_잘_등록되는지_확인한다() {
        commentService.like(1L, userOutline);
        assertThat(commentService.isLiked(1L, userOutline)).isTrue();

        assertThat(commentLikeRepository.existsByUserIdAndCommentId(userOutline.getId(), 1L)).isTrue();
    }

    @Test
    void 좋아요가_잘_취소되는지_확인한다() {
        commentService.like(3L, userOutline);
        commentService.like(3L, userOutline);
        assertThat(commentService.isLiked(3L, userOutline)).isFalse();

        assertThat(commentLikeRepository.existsByUserIdAndCommentId(userOutline.getId(), 3L)).isFalse();
    }

    @Test
    void 좋아요_여부를_확인한다() {
        assertThat(commentService.isLiked(4L, userOutline)).isFalse();
    }
}
