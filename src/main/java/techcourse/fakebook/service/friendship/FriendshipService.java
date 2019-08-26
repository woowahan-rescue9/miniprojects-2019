package techcourse.fakebook.service.friendship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.fakebook.domain.friendship.Friendship;
import techcourse.fakebook.domain.friendship.FriendshipRepository;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.service.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FriendshipService {
    private static final Logger log = LoggerFactory.getLogger(FriendshipService.class);

    private final FriendshipRepository friendshipRepository;
    private final UserService userService;

    public FriendshipService(FriendshipRepository friendshipRepository, UserService userService) {
        this.friendshipRepository = friendshipRepository;
        this.userService = userService;
    }

    public List<Long> findFriendIds(Long userId) {
        log.debug("begin");

        return friendshipRepository.findByPrecedentUserIdOrUserId(userId, userId).stream()
                .map(friendship -> friendship.getFriendId(userId))
                .collect(Collectors.toList());
    }

    public void makeThemFriends(Long userId, Long friendId) {
        log.debug("begin");

        friendshipRepository.save(makeFriendship(userId, friendId));
    }

    public void breakFriendship(Long userId, Long friendId) {
        log.debug("begin");

        friendshipRepository.deleteByPrecedentUserIdAndUserId(
                Math.min(userId, friendId),
                Math.max(userId, friendId));
    }

    private Friendship makeFriendship(Long userId, Long friendId) {
        User user = userService.getUser(userId);
        User friend = userService.getUser(friendId);

        return Friendship.from(user, friend);
    }

    public boolean hasFriendship(Long userId, Long friendId) {
        return friendshipRepository.findByPrecedentUserIdAndUserId(
                Math.min(userId, friendId),
                Math.max(userId, friendId))
                .isPresent();
    }
}
