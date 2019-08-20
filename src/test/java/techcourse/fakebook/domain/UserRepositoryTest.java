package techcourse.fakebook.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.domain.user.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

// TODO: 이 어노테이션을 하면 왜 userRepository 를 찾을 수 있는 건지 아직 이해가 안된다
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void check_In_문법이적용되는지_확인한다() {
        assertThat(userRepository).isNotNull();
        int numUsers = 10;
        List<User> users = generatesUsers(numUsers);
        printList(users);

        List<User> savedUsers = users.stream()
                .map(userRepository::save)
                .collect(Collectors.toList());
        printList(savedUsers);


        List<Integer> wantedIndexes = Arrays.asList(0, 5, 6);
        List<Long> wantedIds = wantedIndexes.stream()
                .map(index -> savedUsers.get(index).getId())
                .collect(Collectors.toList());
        printList(wantedIds);

        // 특정 id 로 조회해서 내용이랑 비교
        List<User> foundUsersByWantedIds = userRepository.findByIdIn(wantedIds);
        System.out.println("foundUsersByWantedIds: +++++++++++++");
        printList(foundUsersByWantedIds);

        assertThat(foundUsersByWantedIds.size()).isEqualTo(wantedIds.size());
        for (int index : wantedIndexes) {
            User user = savedUsers.get(index);

            assertThat(foundUsersByWantedIds.contains(user)).isTrue();
        }
    }

    private <T> void printList(List<T> list) {
        list.stream().forEach(System.out::println);
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
                anyString,
                anyString,
                anyString
        );
    }
}
