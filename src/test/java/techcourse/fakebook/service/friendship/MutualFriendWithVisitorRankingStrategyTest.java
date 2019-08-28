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
class MutualFriendWithVisitorRankingStrategyTest {
    @InjectMocks
    private MutualFriendWithVisitorRankingStrategy rankingStrategy;

    @Mock
    private VisitorTrackingService visitorTrackingService;

    @Test
    void rank_10번_방문_함께아는친구1명이_같은_가중치_일_경우() {
        // Arrange
        Long userId = 0L;
        // (함께아는 친구, 방문수 고려한다고 했을 때)
        // 아무것도 없는 경우 -> 7등
        // 함께아는 친구 1명 -> 5등
        // 함께아는 친구 2명 -> 4등
        // 방문수 1개 -> 6등
        // 방문수 25개 -> 3등
        // 함께아는 친구 2명 방문수 20개 -> 2등
        // 함께아는 친구 1명 방문수 100개 -> 1등
        Long mutualFriend0_visit0_id = 10L;
        Long mutualFriend1_visit0_id = 11L;
        Long mutualFriend2_visit0_id = 12L;
        Long mutualFriend0_visit1_id = 13L;
        Long mutualFriend0_visit25_id = 14L;
        Long mutualFriend2_visit20_id = 15L;
        Long mutualFriend1_visit100_id = 16L;
        List<FriendCandidate> friendCandidates = Arrays.asList(
                new FriendCandidate(userId, mutualFriend0_visit0_id, newSet(Collections.emptyList())),
                new FriendCandidate(userId, mutualFriend1_visit0_id, newSet(Arrays.asList(1L))),
                new FriendCandidate(userId, mutualFriend2_visit0_id, newSet(Arrays.asList(1L, 2L))),
                new FriendCandidate(userId, mutualFriend0_visit1_id, newSet(Collections.emptyList())),
                new FriendCandidate(userId, mutualFriend0_visit25_id, newSet(Collections.emptyList())),
                new FriendCandidate(userId, mutualFriend2_visit20_id, newSet(Arrays.asList(1L, 2L))),
                new FriendCandidate(userId, mutualFriend1_visit100_id, newSet(Arrays.asList(1L)))
        );
        given(visitorTrackingService.currentCounter(userId)).willReturn(Counter.from(
                new HashMap<Long, Long>() {{
                    put(mutualFriend0_visit1_id, 1L);
                    put(mutualFriend0_visit25_id, 25L);
                    put(mutualFriend2_visit20_id, 20L);
                    put(mutualFriend1_visit100_id, 100L);
                }}
        ));
        List<Integer> wantedIdxes = Arrays.asList(6, 5, 4, 2, 1, 3, 0);


        // Act
        List<FriendCandidate> rankedFriendCandidates = rankingStrategy.rank(userId, friendCandidates);


        // Assert
        assertThat(rankedFriendCandidates.size()).isEqualTo(friendCandidates.size());
        for (int i = 0; i < friendCandidates.size(); i++) {
            int wantedIdx = wantedIdxes.get(i);
            FriendCandidate wanted = friendCandidates.get(wantedIdx);
            assertThat(rankedFriendCandidates.get(i)).isEqualTo(wanted);
        }
    }

    private Set<Long> newSet(List<Long> friendIds) {
        return friendIds.stream()
                .collect(Collectors.toSet());
    }
}