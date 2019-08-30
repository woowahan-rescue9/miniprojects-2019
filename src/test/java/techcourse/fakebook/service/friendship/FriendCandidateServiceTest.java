package techcourse.fakebook.service.friendship;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.fakebook.domain.friendship.FriendCandidate;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(SpringExtension.class)
class FriendCandidateServiceTest {
    @InjectMocks
    private IndirectFriendService indirectFriendService;

    @Mock
    private FriendshipService friendshipService;

    @Test
    void retrieve_directFriend_없을때() {
        // Arrange
        Long rootUserId = 0L;
        given(friendshipService.findFriendIds(rootUserId)).willReturn(Collections.emptyList());


        // Act
        List<FriendCandidate> friendCandidates = indirectFriendService.retrieve(rootUserId);


        // Assert
        assertThat(friendCandidates).isEmpty();
    }

    @Test
    void retrieve_directFriend_만_존재할때() {
        // Arrange
        Long rootUserId = 0L;
        List<Long> directFriendIds = Arrays.asList(1L, 2L);
        FriendshipServiceBuilder builder = new FriendshipServiceBuilder(friendshipService)
                .makeFriend(rootUserId, directFriendIds.get(0))
                .makeFriend(rootUserId, directFriendIds.get(1));
        friendshipService = builder.build();


        // Act
        List<FriendCandidate> friendCandidates = indirectFriendService.retrieve(rootUserId);


        // Assert
        assertThat(friendCandidates).isEmpty();
    }

    @Test
    void retrieve_directFriend_1명_indirectFriend_2명() {
        // Arrange
        Long rootUserId = 0L;
        Long directFriendId = 1L;
        List<Long> indirectFriendIds = Arrays.asList(2L, 3L);
        FriendshipServiceBuilder builder = new FriendshipServiceBuilder(friendshipService)
                .makeFriend(rootUserId, directFriendId)
                .makeFriend(directFriendId, indirectFriendIds.get(0))
                .makeFriend(directFriendId, indirectFriendIds.get(1));
        friendshipService = builder.build();


        // Act
        List<FriendCandidate> friendCandidates = indirectFriendService.retrieve(rootUserId);


        // Assert
        List<FriendCandidate> wantedFriendCandidates = Arrays.asList(
                new FriendCandidate(rootUserId, indirectFriendIds.get(0), newSet(Arrays.asList(directFriendId))),
                new FriendCandidate(rootUserId, indirectFriendIds.get(1), newSet(Arrays.asList(directFriendId)))
        );
        for (FriendCandidate friendCandidate : friendCandidates) {
            assertThat(wantedFriendCandidates.contains(friendCandidate)).isTrue();
        }
    }

    @Test
    void retrieve_directFriend_2명_indirectFriend_1명() {
        // Arrange
        Long rootUserId = 0L;
        List<Long> directFriendIds = Arrays.asList(1L, 2L);
        Long indirectFriendId = 3L;

        FriendshipServiceBuilder builder = new FriendshipServiceBuilder(friendshipService)
                .makeFriend(rootUserId, directFriendIds.get(0))
                .makeFriend(rootUserId, directFriendIds.get(1))
                .makeFriend(directFriendIds.get(0), indirectFriendId)
                .makeFriend(directFriendIds.get(1), indirectFriendId);
        friendshipService = builder.build();


        // Act
        List<FriendCandidate> friendCandidates = indirectFriendService.retrieve(rootUserId);


        // Assert
        List<FriendCandidate> wantedFriendCandidates = Arrays.asList(
                new FriendCandidate(rootUserId, indirectFriendId, newSet(directFriendIds))
        );
        for (FriendCandidate friendCandidate : friendCandidates) {
            assertThat(wantedFriendCandidates.contains(friendCandidate)).isTrue();
        }
    }

    @Test
    void retrieve_좀_더_복잡한_경우() {
        // Arrange
        Long rootUserId = 0L;
        List<Long> directFriendIds = Arrays.asList(2L, 3L, 4L);
        List<Long> indirectFriendIds = Arrays.asList(5L, 6L);

        FriendshipServiceBuilder builder = new FriendshipServiceBuilder(friendshipService)
                .makeFriend(rootUserId, directFriendIds.get(0))
                .makeFriend(rootUserId, directFriendIds.get(1))
                .makeFriend(rootUserId, directFriendIds.get(2))
                .makeFriend(directFriendIds.get(0), indirectFriendIds.get(0))
                .makeFriend(directFriendIds.get(1), directFriendIds.get(2))
                .makeFriend(directFriendIds.get(2), indirectFriendIds.get(0));
        friendshipService = builder.build();


        // Act
        List<FriendCandidate> friendCandidates = indirectFriendService.retrieve(rootUserId);


        // Assert
        List<FriendCandidate> wantedFriendCandidates = Arrays.asList(
                new FriendCandidate(rootUserId, indirectFriendIds.get(0), newSet(Arrays.asList(directFriendIds.get(0), directFriendIds.get(2))))
        );
        for (FriendCandidate friendCandidate : friendCandidates) {
            assertThat(wantedFriendCandidates.contains(friendCandidate)).isTrue();
        }
    }

    private Set<Long> newSet(List<Long> ids) {
        return ids.stream()
                .collect(Collectors.toSet());
    }

    class FriendshipServiceBuilder {
        private final FriendshipService friendshipService;
        private Map<Long, Set<Long>> friendIds;

        public FriendshipServiceBuilder(FriendshipService friendshipService) {
            this.friendshipService = friendshipService;
            friendIds = new HashMap<>();
        }

        public FriendshipServiceBuilder(FriendshipService friendshipService, Map<Long, Set<Long>> friendIds) {
            this.friendshipService = friendshipService;
            this.friendIds = friendIds;
        }

        public FriendshipServiceBuilder makeFriend(Long userId, Long otherUserId) {
            addFriend(userId, otherUserId);
            addFriend(otherUserId, userId);
            return new FriendshipServiceBuilder(friendshipService, friendIds);
        }

        private void addFriend(Long userId, Long friendId) {
            Set<Long> friendIdsOfUser = friendIds.getOrDefault(userId, new TreeSet<>());
            friendIdsOfUser.add(friendId);

            friendIds.put(userId, friendIdsOfUser);
        }

        public FriendshipService build() {
            for (Long userId : friendIds.keySet()) {
                given(friendshipService.findFriendIds(userId)).willReturn(new ArrayList<>(friendIds.get(userId)));
            }

            return friendshipService;
        }
    }
}