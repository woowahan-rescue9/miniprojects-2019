package techcourse.fakebook.service.friendship;

import org.springframework.stereotype.Service;
import techcourse.fakebook.domain.friendship.FriendCandidate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IndirectFriendService {
    private final FriendshipService friendshipService;

    public IndirectFriendService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    public List<FriendCandidate> retrieve(Long rootUserId) {
        List<Long> directFriendIds = friendshipService.findFriendIds(rootUserId);

        Map<Long, Set<Long>> mutualFriendsCounter = new HashMap<>(); // <id, num of mutual friend with rootUser>
        for (Long directFriendId : directFriendIds) {
            List<Long> indirectFriendIds = friendshipService.findFriendIds(directFriendId);

            for (Long indirectFriendId : indirectFriendIds) {
                if (indirectFriendId.equals(rootUserId)) {
                    continue;
                }
                if (directFriendIds.contains(indirectFriendId)) {
                    continue;
                }

                Set<Long> friendIds = mutualFriendsCounter.getOrDefault(indirectFriendId, new HashSet<>());
                friendIds.add(directFriendId);
                mutualFriendsCounter.put(indirectFriendId, friendIds);
            }
        }

        return mutualFriendsCounter.keySet().stream()
                .map(id -> new FriendCandidate(rootUserId, id, mutualFriendsCounter.get(id)))
                .collect(Collectors.toList());
    }
}
