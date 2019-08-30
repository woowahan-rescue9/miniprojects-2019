package techcourse.fakebook.service.friendship;

import org.springframework.stereotype.Component;
import techcourse.fakebook.domain.friendship.FriendCandidate;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MutualFriendWithVisitorRankingStrategy {
    private static final Long SCORE_PER_MUTUAL_FRIEND = 10L;
    private static final Long SCORE_PER_VISIT = 1L;

    private final VisitorTrackingService visitorTrackingService;

    public MutualFriendWithVisitorRankingStrategy(VisitorTrackingService visitorTrackingService) {
        this.visitorTrackingService = visitorTrackingService;
    }

    // 입력 받은 친구후보들로만 랭킹을 매길 것인지?
    // 그렇다면 후보자를 만들어 내는 로직도 어딘가에 있어야 할 듯
    public List<FriendCandidate> rank(Long userId, List<FriendCandidate> friendCandidates) {
        Counter<Long> visitorCounter = visitorTrackingService.currentCounter(userId);
        Counter<Long> mutualFriendCounter = newMutualFriendCounter(friendCandidates);

        return friendCandidates.stream()
                .sorted(Comparator.comparing((FriendCandidate candidate) -> getScore(candidate.getFriendId(), visitorCounter, mutualFriendCounter)).reversed())
                .collect(Collectors.toList());
    }

    private Counter<Long> newMutualFriendCounter(List<FriendCandidate> friendCandidates) {
        Map<Long, Long> counter = new HashMap<>();

        for (FriendCandidate candidate : friendCandidates) {
            Long friendId = candidate.getFriendId();
            counter.put(friendId, (long) candidate.countMutualFriends());
        }

        return Counter.from(counter);
    }

    private Long getScore(Long id, Counter<Long> visitorCounter, Counter<Long> mutualFriendCounter) {
        return visitorCounter.count(id) * SCORE_PER_VISIT
                + mutualFriendCounter.count(id) * SCORE_PER_MUTUAL_FRIEND;
    }
}
