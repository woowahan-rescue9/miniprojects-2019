package techcourse.fakebook.service.friendship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.domain.user.UserProfileImage;
import techcourse.fakebook.domain.user.UserRepository;
import techcourse.fakebook.service.ServiceTestHelper;
import techcourse.fakebook.service.user.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FriendshipServiceTest extends ServiceTestHelper {
    private static final Logger log = LoggerFactory.getLogger(FriendshipServiceTest.class);

    private static int newUserId = 1000;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private List<User> savedUsers;
    private List<Long> savedUserIds;

    private Long userId;
    private List<Long> friendIds;

    @BeforeEach
    void Setup() {
        유저_10명_추가();
    }

    private void 유저_10명_추가() {
        assertThat(userRepository).isNotNull();
        int numUsers = 4;
        List<User> users = generatesUsers(numUsers);

        savedUsers = users.stream()
                .map(userRepository::save)
                .collect(Collectors.toList());

        savedUserIds = savedUsers.stream()
                .mapToLong(User::getId)
                .boxed()
                .collect(Collectors.toList());
    }

    @Test
    void 유저_친구_추가_및_올바른지_조회() {
        // Arrange
        int userIndex = 0;
        List<Integer> friendIndexes = Arrays.asList(1, 3);
        유저_친구_초기화(userIndex, friendIndexes);

        // Act
        List<Long> foundFriendIds = friendshipService.findFriendIds(userId);

        // Assert
        assertThat(foundFriendIds.size()).isEqualTo(friendIndexes.size());
        friendIds.stream()
                .forEach(friendId -> assertThat(foundFriendIds.contains(friendId)).isTrue());

        for (Long friendId : friendIds) {
            List<Long> foundIds = friendshipService.findFriendIds(friendId);
            assertThat(foundIds).isEqualTo(Arrays.asList(userId));
        }
    }

    @Test
    void 친구_추가_여러번시도() {
        // Arrange
        int userIndex = 0;
        List<Integer> friendIndexes = Arrays.asList(1);
        유저_친구_초기화(userIndex, friendIndexes);

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> 유저_친구_초기화(userIndex, Arrays.asList(1)));
    }

    @Test
    void 친구_추가_이후_삭제() {
        // Arrange
        int userIndex = 0;
        int friendIndex = 1;
        유저_친구_초기화(userIndex, Arrays.asList(friendIndex));

        // Act
        Long userId = savedUserIds.get(userIndex);
        Long friendId = savedUserIds.get(friendIndex);
        friendshipService.breakFriendship(userId, friendId);

        System.out.println(friendshipService.findFriendIds(userId));
        // Assert
        assertThat(friendshipService.findFriendIds(userId).isEmpty()).isTrue();
    }

    @Test
    void 친구여부_확인() {
        // Arrange
        int userIndex = 0;
        int friendIndex = 1;
        유저_친구_초기화(userIndex, Arrays.asList(friendIndex));

        Long userId = savedUserIds.get(userIndex);
        Long friendId = savedUserIds.get(friendIndex);

        // Act & Assert
        assertThat(friendshipService.hasFriendship(userId, friendId)).isTrue();
    }

    @Test
    void 친구여부_확인_서로_친구가_아닌경우() {
        // Arrange
        int userIndex = 0;
        int wrongFriendIndex = 1;

        Long userId = savedUserIds.get(userIndex);
        Long notFriendId = savedUserIds.get(wrongFriendIndex);

        // Act & Assert
        assertThat(friendshipService.hasFriendship(userId, notFriendId)).isFalse();
    }

    // 원래는 통합 테스트에서 해야하지 않을까?
    @Test
    void 유저삭제_친구정보도_삭제() {
        // Arrange
        int userIndex = 0;
        List<Integer> friendIndexes = Arrays.asList(1, 3);
        유저_친구_초기화(userIndex, friendIndexes);

        // Act
        log.debug("userId: {}", userId);
        userService.deleteById(userId);

        // Assert
        assertThat(friendshipService.findFriendIds(userId).isEmpty()).isTrue();
    }

    private void 유저_친구_초기화(int userIndex, List<Integer> friendIndexes) {
        userId = savedUserIds.get(userIndex);
        friendIds = friendIndexes.stream()
                .map(index -> savedUserIds.get(index))
                .collect(Collectors.toList());
        friendIds.stream()
                .forEach(friendId -> friendshipService.makeThemFriends(userId, friendId));
    }

    private <T> void printList(List<T> list) {
        list.stream().forEach(System.out::println);
    }

    private List<User> getUsersByIndexes(List<Integer> indexes) {
        return indexes.stream()
                .map(savedUsers::get)
                .collect(Collectors.toList());
    }

    private List<User> generatesUsers(int numUsers) {
        return Stream.generate(FriendshipServiceTest::newUser)
                .limit(numUsers)
                .collect(Collectors.toList());
    }

    private static User newUser() {
        String anyString = "xxx";

        return new User(
                String.format("email%d@hello.com", newUserId++),
                anyString,
                anyString,
                anyString,
                new UserProfileImage("a", "src/test/resources/static/images/user/profile/default.png"),
                anyString,
                anyString
        );
    }
}