package techcourse.fakebook.service.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationChannelRepositoryTest {
    private NotificationChannelRepository notificationChannelRepository;

    @BeforeEach
    void setUp() {
        notificationChannelRepository = new NotificationChannelRepository();
    }

    @Test
    void 할당() {
        assertThat(
                notificationChannelRepository.assignTo(1).getAddress().length()
        ).isEqualTo(32);
    }

    @Test
    void 할당_후_찾기() {
        assertThat(
                notificationChannelRepository.assignTo(1)
        ).isEqualTo(
                notificationChannelRepository.retrieveBy(1).get()
        );
    }

    @Test
    void 할당_후_삭제() {
        notificationChannelRepository.assignTo(1);
        notificationChannelRepository.resetBy(1);
        assertThat(
                notificationChannelRepository.retrieveBy(1)
        ).isEqualTo(
                Optional.empty()
        );
    }
}