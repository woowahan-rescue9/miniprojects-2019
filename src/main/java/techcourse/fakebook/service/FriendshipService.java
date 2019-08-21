package techcourse.fakebook.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.fakebook.domain.friendship.Friendship;
import techcourse.fakebook.domain.friendship.FriendshipRepository;
import techcourse.fakebook.domain.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserService userService;

    public FriendshipService(FriendshipRepository friendshipRepository, UserService userService) {
        this.friendshipRepository = friendshipRepository;
        this.userService = userService;
    }

    public List<Long> findFriendIds(Long userId) {
        return friendshipRepository.findByPrecedentUserIdOrUserId(userId, userId).stream()
                .map(friendship -> friendship.getFriendId(userId))
                .collect(Collectors.toList());
    }

    public void makeThemFriends(Long userId1, Long userId2) {
        User user1 = userService.getUser(userId1);
        User user2 = userService.getUser(userId2);
        Friendship friendship = Friendship.from(user1, user2);

        friendshipRepository.save(friendship);
    }
}
