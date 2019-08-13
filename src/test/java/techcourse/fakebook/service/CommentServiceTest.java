package techcourse.fakebook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.fakebook.service.dto.CommentRequest;
import techcourse.fakebook.service.dto.CommentResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    void 댓글을_잘_작성하는지_확인한다() {
        CommentRequest commentRequest = new CommentRequest("댓글입니다.");
        CommentResponse commentResponse = commentService.save(1L, commentRequest);

        assertThat(commentResponse.getContent()).isEqualTo(commentRequest.getContent());
    }
}
