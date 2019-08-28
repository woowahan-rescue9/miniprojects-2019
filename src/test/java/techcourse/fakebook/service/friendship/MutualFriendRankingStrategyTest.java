package techcourse.fakebook.service.friendship;

import org.junit.jupiter.api.Test;
import techcourse.fakebook.domain.friendship.FriendCandidate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class MutualFriendRankingStrategyTest {
    private static final Long ROOT_USER_ID = 0L;
    private MutualFriendRankingStrategy rankingStrategy = new MutualFriendRankingStrategy();

    @Test
    void rank_빈_indirectFriends() {
        assertThat(rankingStrategy.rank(Collections.emptyList())).isEmpty();
    }

    @Test
    void rank_indirectFriends_2명_mutualFriends_적은_순() {
        List<FriendCandidate> friendCandidates = Arrays.asList(
                new FriendCandidate(ROOT_USER_ID, 1L, newSet(Arrays.asList(11L))),
                new FriendCandidate(ROOT_USER_ID, 2L, newSet(Arrays.asList(11L, 12L)))
        );

        List<FriendCandidate> rankedFriendCandidates = rankingStrategy.rank(friendCandidates);

        assertThat(rankedFriendCandidates.size()).isEqualTo(friendCandidates.size());
        assertThat(rankedFriendCandidates.get(0)).isEqualTo(friendCandidates.get(1));
        assertThat(rankedFriendCandidates.get(1)).isEqualTo(friendCandidates.get(0));
    }

    @Test
    void rank_indirectFriends_2명_mutualFriends_많은_순() {
        List<FriendCandidate> friendCandidates = Arrays.asList(
                new FriendCandidate(ROOT_USER_ID, 1L, newSet(Arrays.asList(11L, 12L))),
                new FriendCandidate(ROOT_USER_ID, 2L, newSet(Arrays.asList(11L)))
        );

        List<FriendCandidate> rankedFriendCandidates = rankingStrategy.rank(friendCandidates);

        assertThat(rankedFriendCandidates.size()).isEqualTo(friendCandidates.size());
        assertThat(rankedFriendCandidates.get(0)).isEqualTo(friendCandidates.get(0));
        assertThat(rankedFriendCandidates.get(1)).isEqualTo(friendCandidates.get(1));
    }

    // TODO: List<FriendCandidate> 를 일급콜렉션으로 만들면 자연스럽게 중복이 제거될 듯
    private Set<Long> newSet(List<Long> friendIds) {
        return friendIds.stream()
                .collect(Collectors.toSet());
    }
}