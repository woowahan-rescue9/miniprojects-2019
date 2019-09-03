package techcourse.fakebook.domain.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query(value = "select * from Chat where (FROM_USER_ID = ?1 and TO_USER_ID =?2) or (FROM_USER_ID = ?2 and TO_USER_ID =?1) ORDER By id", nativeQuery = true)
    List<Chat> findByFromUserAndToUserOrToUserAndFromUser(Long fromUserId, Long toUserId);

    @Modifying
    @Query("UPDATE Chat set readable = true where TO_USER_ID = ?1 and FROM_USER_ID = ?2")
    void updateReadByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
}
