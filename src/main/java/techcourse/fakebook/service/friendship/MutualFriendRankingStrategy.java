package techcourse.fakebook.service.friendship;

import org.springframework.stereotype.Component;
import techcourse.fakebook.domain.friendship.FriendCandidate;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MutualFriendRankingStrategy {
    public List<FriendCandidate> rank(List<FriendCandidate> friendCandidates) {
        return friendCandidates.stream()
                .sorted(Comparator.comparing(FriendCandidate::countMutualFriends).reversed())
                .collect(Collectors.toList());
    }
}
