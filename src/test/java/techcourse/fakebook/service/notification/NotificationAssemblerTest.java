package techcourse.fakebook.service.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.comment.Comment;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.notification.assembler.NotificationAssembler;
import techcourse.fakebook.service.notification.dto.NotificationResponse;
import techcourse.fakebook.service.user.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class NotificationAssemblerTest {
    static final long USER_ID = 1L;

    @InjectMocks
    private NotificationAssembler notificationAssembler;
    @Mock
    private UserService userService;
    @Mock
    private User user;

    @BeforeEach
    void setUp() {
        when(userService.getUser(USER_ID)).thenReturn(user);
    }

    @Test
    void 채팅_알림_메세지() {
        final String CHAT_MESSAGE = "ㅎㅇㅎㅇ";
        assertThat(
                notificationAssembler.chat(user.getId(), CHAT_MESSAGE)
        ).isEqualTo(
                new NotificationResponse(
                        NotificationResponse.Type.CHAT,
                        userService.getUserOutline(user.getId()),
                        null,
                        CHAT_MESSAGE
                )
        );
    }

    @Test
    void 친구_요청_알림_메세지() {
        assertThat(
                notificationAssembler.friendRequest(user.getId())
        ).isEqualTo(
                new NotificationResponse(
                        NotificationResponse.Type.FRIEND_REQUEST,
                        userService.getUserOutline(user.getId()),
                        null,
                        null
                )
        );
    }

    @Test
    void 댓글_알림_메세지_짧은_글() {
        final Article ARTICLE = new Article("ㅎㅎㅎ", user);
        final Comment COMMENT = new Comment("ㅋㅋㅋㅋ", ARTICLE, user);
        assertThat(
                notificationAssembler.comment(COMMENT, user.getId(), ARTICLE)
        ).isEqualTo(
                new NotificationResponse(
                        NotificationResponse.Type.COMMENT,
                        userService.getUserOutline(user.getId()),
                        ARTICLE.getContent(),
                        COMMENT.getContent()
                )
        );
    }

    @Test
    void 댓글_알림_메세지_긴_글() {
        final Article ARTICLE = new Article("20대에 운전을 시작한다고 하여 저절로 잘하게 되는 것이 아니듯이, 의식적인 연습을 통해 ~", user);
        final Comment COMMENT = new Comment("ㅋㅋㅋㅋ", ARTICLE, user);
        assertThat(
                notificationAssembler.comment(COMMENT, user.getId(), ARTICLE)
        ).isEqualTo(
                new NotificationResponse(
                        NotificationResponse.Type.COMMENT,
                        userService.getUserOutline(user.getId()),
                        ARTICLE.getContent().substring(
                                0,
                                notificationAssembler.getMaxSummaryLength() - 4
                        ) + " ...",
                        COMMENT.getContent()
                )
        );
    }

    @Test
    void 좋아요_알림_메세지_짧은_글() {
        final Article ARTICLE = new Article("ㅎㅎㅎ", user);
        assertThat(
                notificationAssembler.like(user.getId(), ARTICLE)
        ).isEqualTo(
                new NotificationResponse(
                        NotificationResponse.Type.LIKE,
                        userService.getUserOutline(user.getId()),
                        ARTICLE.getContent(),
                        null
                )
        );
    }

    @Test
    void 좋아요_알림_메세지_긴_글() {
        final Article ARTICLE = new Article("20대에 운전을 시작한다고 하여 저절로 잘하게 되는 것이 아니듯이, 의식적인 연습을 통해 ~", user);
        assertThat(
                notificationAssembler.like(user.getId(), ARTICLE)
        ).isEqualTo(
                new NotificationResponse(
                        NotificationResponse.Type.LIKE,
                        userService.getUserOutline(user.getId()),
                        ARTICLE.getContent().substring(
                                0,
                                notificationAssembler.getMaxSummaryLength() - 4
                        ) + " ...",
                        null
                )
        );
    }
}