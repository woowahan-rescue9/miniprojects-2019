package techcourse.fakebook.service.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationChannelMapperTest {
    private NotificationChannelMapper notificationChannelMapper;

    @BeforeEach
    void setUp() {
        notificationChannelMapper = new NotificationChannelMapper();
    }

    @Test
    void 할당() {
        assertThat(
                notificationChannelMapper.assignTo(1).getAddress().length()
        ).isEqualTo(32);
    }

    @Test
    void 할당_후_찾기() {
        assertThat(
                notificationChannelMapper.assignTo(1)
        ).isEqualTo(
                notificationChannelMapper.retrieveBy(1).get()
        );
    }

    @Test
    void 할당_후_삭제() {
        notificationChannelMapper.assignTo(1);
        notificationChannelMapper.resetBy(1);
        assertThat(
                notificationChannelMapper.retrieveBy(1)
        ).isEqualTo(
                Optional.empty()
        );
    }
}