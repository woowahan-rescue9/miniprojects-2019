package techcourse.fakebook.domain.friendship;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByPrecedentUserIdOrUserId(Long precedentUserId, Long userId);

    void deleteByPrecedentUserIdAndUserId(Long precedentUserId, Long userId);
}
