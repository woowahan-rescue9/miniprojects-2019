package techcourse.fakebook.service.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.fakebook.domain.article.Article;
import techcourse.fakebook.domain.notification.NotificationMessage;
import techcourse.fakebook.domain.notification.NotificationMessageFactory;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.user.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class NotificationMessageFactoryTest {
    static final long USER_ID = 1L;

    @InjectMocks
    private NotificationMessageFactory notificationMessageFactory;
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
                notificationMessageFactory.chat(user.getId(), CHAT_MESSAGE)
        ).isEqualTo(
                new NotificationMessage(
                        NotificationMessage.Type.CHAT,
                        userService.getUserOutline(user.getId()),
                        CHAT_MESSAGE
                )
        );
    }

    @Test
    void 친구_요청_알림_메세지() {
        assertThat(
                notificationMessageFactory.friendRequest(user.getId())
        ).isEqualTo(
                new NotificationMessage(
                        NotificationMessage.Type.FRIEND_REQUEST,
                        userService.getUserOutline(user.getId()),
                        null
                )
        );
    }

    @Test
    void 댓글_알림_메세지_25자_이하_글() {
        final Article ARTICLE = new Article("ㅎㅎㅎ", user);
        assertThat(
                notificationMessageFactory.comment(user.getId(), ARTICLE)
        ).isEqualTo(
                new NotificationMessage(
                        NotificationMessage.Type.COMMENT,
                        userService.getUserOutline(user.getId()),
                        ARTICLE.getContent()
                )
        );
    }

    @Test
    void 댓글_알림_메세지_25자_초과_글() {
        final String ARTICLE_CONTENT = "20대에 운전을 시작한다고 하여 저절로 잘하게 되는 것이 아니듯이, 의식적인 연습을 통해 ~";
        final Article ARTICLE = new Article(ARTICLE_CONTENT, user);
        assertThat(
                notificationMessageFactory.comment(user.getId(), ARTICLE)
        ).isEqualTo(
                new NotificationMessage(
                        NotificationMessage.Type.COMMENT,
                        userService.getUserOutline(user.getId()),
                        ARTICLE_CONTENT.substring(
                                0,
                                notificationMessageFactory.getMaxMessageLength() - 4
                        ) + " ..."
                )
        );
    }

    @Test
    void 좋아요_알림_메세지_25자_이하_글() {
        final Article ARTICLE = new Article("ㅎㅎㅎ", user);
        assertThat(
                notificationMessageFactory.like(user.getId(), ARTICLE)
        ).isEqualTo(
                new NotificationMessage(
                        NotificationMessage.Type.LIKE,
                        userService.getUserOutline(user.getId()),
                        ARTICLE.getContent()
                )
        );
    }

    @Test
    void 좋아요_알림_메세지_25자_초과_글() {
        final String ARTICLE_CONTENT = "20대에 운전을 시작한다고 하여 저절로 잘하게 되는 것이 아니듯이, 의식적인 연습을 통해 ~";
        final Article ARTICLE = new Article(ARTICLE_CONTENT, user);
        assertThat(
                notificationMessageFactory.like(user.getId(), ARTICLE)
        ).isEqualTo(
                new NotificationMessage(
                        NotificationMessage.Type.LIKE,
                        userService.getUserOutline(user.getId()),
                        ARTICLE_CONTENT.substring(
                                0,
                                notificationMessageFactory.getMaxMessageLength() - 4
                        ) + " ..."
                )
        );
    }
}