package techcourse.fakebook.service.friendship;

import org.springframework.stereotype.Component;
import techcourse.fakebook.domain.friendship.FriendCandidate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FriendCandidateFactory {
    private final IndirectFriendService indirectFriendService;
    private final VisitorTrackingService visitorTrackingService;
    private final FriendshipService friendshipService;

    public FriendCandidateFactory(IndirectFriendService indirectFriendService, VisitorTrackingService visitorTrackingService, FriendshipService friendshipService) {
        this.indirectFriendService = indirectFriendService;
        this.visitorTrackingService = visitorTrackingService;
        this.friendshipService = friendshipService;
    }

    // TODO: 이름을 좀 더 명확하게
    public List<FriendCandidate> createCandidates(Long userId) {
        Set<FriendCandidate> candidates = new HashSet<>(indirectFriendService.retrieve(userId));
        Set<Long> candidateIds = candidates.stream()
                .map(FriendCandidate::getFriendId)
                .collect(Collectors.toSet());
        Set<Long> friendIds = new HashSet<>(friendshipService.findFriendIds(userId));

        List<Long> candidateIdsForAdd = visitorTrackingService.currentCounter(userId).keySet().stream()
                .filter(visitorId -> !visitorId.equals(userId))
                .filter(visitorId -> !friendIds.contains(visitorId))
                .filter(visitorId -> !candidateIds.contains(visitorId))
                .collect(Collectors.toList());
        for (Long candidateIdForAdd : candidateIdsForAdd) {
            candidates.add(FriendCandidate.withNoMutualFriends(userId, candidateIdForAdd));
        }

        return new ArrayList<>(candidates);
    }
}
