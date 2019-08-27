package techcourse.fakebook.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import techcourse.fakebook.domain.friendship.Friendship;
import techcourse.fakebook.domain.friendship.FriendshipRepository;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.domain.user.UserProfileImage;
import techcourse.fakebook.domain.user.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    private List<User> savedUsers;
    private List<Long> savedUserIds;

    @BeforeEach
    void 유저_10명_추가() {
        assertThat(userRepository).isNotNull();
        int numUsers = 10;
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
    void check_In_문법이적용되는지_확인한다() {
        List<Integer> wantedIndexes = Arrays.asList(0, 5, 6);
        List<Long> wantedIds = wantedIndexes.stream()
                .map(index -> savedUsers.get(index).getId())
                .collect(Collectors.toList());
        printList(wantedIds);

        // 특정 id 로 조회해서 내용이랑 비교
        List<User> foundUsersByWantedIds = userRepository.findByIdIn(wantedIds);
        printList(foundUsersByWantedIds);

        assertThat(foundUsersByWantedIds.size()).isEqualTo(wantedIds.size());
        for (int index : wantedIndexes) {
            User user = savedUsers.get(index);

            assertThat(foundUsersByWantedIds.contains(user)).isTrue();
        }
    }

    @Test
    void friendshipRepository_findByPrecedentUserIdOrUserId_잘동작하는지확인() {
        List<Integer> wantedIndexes = Arrays.asList(1, 2, 3, 4);
        List<User> usersSortedById = wantedIndexes.stream()
                .map(savedUsers::get)
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());

        User overlappedUser = usersSortedById.get(1);
        List<Friendship> expectedFriendships = new ArrayList<>();
        expectedFriendships.add(friendshipRepository.save(Friendship.from(usersSortedById.get(0), overlappedUser)));
        expectedFriendships.add(friendshipRepository.save(Friendship.from(overlappedUser, usersSortedById.get(2))));
        expectedFriendships.add(friendshipRepository.save(Friendship.from(overlappedUser, usersSortedById.get(3))));


        // Assert
        List<Friendship> foundFriendships = friendshipRepository.findByPrecedentUserIdOrUserId(overlappedUser.getId(), overlappedUser.getId());
        assertThat(foundFriendships.size()).isEqualTo(expectedFriendships.size());
        for (Friendship friendship : expectedFriendships) {
            assertThat(foundFriendships.contains(friendship)).isTrue();
        }
        printList(foundFriendships);
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
        return IntStream.range(0, numUsers)
                .mapToObj(UserRepositoryTest::newUser)
                .collect(Collectors.toList());
    }

    private static User newUser(int number) {
        String anyString = "xxx";

        return new User(
                String.format("email%d@hello.com", number),
                anyString,
                anyString,
                anyString,
                new UserProfileImage("a", "src/test/resources/static/images/user/profile/default.png"),
                anyString,
                anyString
        );
    }
}
