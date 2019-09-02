package techcourse.fakebook.service.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationChannelRepositoryTest {
    private NotificationChannelMapper notificationChannelMapper;

    @BeforeEach
    void setUp() {
        notificationChannelMapper = new NotificationChannelMapper();
    }

    @Test
    void assignTest() {
        assertThat(
                notificationChannelMapper.assignTo(1).getAddress().length()
        ).isEqualTo(32);
    }

    @Test
    void assignAndRetrieveTest() {
        assertThat(
                notificationChannelMapper.assignTo(1)
        ).isEqualTo(
                notificationChannelMapper.retrieveBy(1).get()
        );
    }
}