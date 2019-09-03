package techcourse.fakebook.domain.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query(value = "select * from chat where (from_user_id = ?1 and to_user_id =?2) or (from_user_id = ?2 and to_user_id =?1)", nativeQuery = true)
    List<Chat> findByFromUserAndToUserOrToUserAndFromUser(Long fromUserId, Long toUserId);

    @Modifying
    @Query("UPDATE chat set readable = true where to_user_id = ?1 and from_user_id = ?2")
    void updateReadByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
}
