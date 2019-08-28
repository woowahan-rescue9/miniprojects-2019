package techcourse.fakebook.service.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationChannelRepositoryTest {
    private NotificationChannelRepository notificationChannelRepository;

    @BeforeEach
    void setUp() {
        notificationChannelRepository = new NotificationChannelRepository();
    }

    @Test
    void assignTest() {
        assertThat(
                notificationChannelRepository.assignTo(1).getAddress().length()
        ).isEqualTo(32);
    }

    @Test
    void assignAndRetrieveTest() {
        assertThat(
                notificationChannelRepository.assignTo(1)
        ).isEqualTo(
                notificationChannelRepository.retrieveBy(1).get()
        );
    }
}