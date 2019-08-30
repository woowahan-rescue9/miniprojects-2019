package techcourse.fakebook.service.friendship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import techcourse.fakebook.domain.friendship.FriendCandidate;
import techcourse.fakebook.service.friendship.dto.FriendRecommendation;
import techcourse.fakebook.service.user.UserService;
import techcourse.fakebook.service.user.dto.UserOutline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FriendRecommendationService {
    private static final Logger log = LoggerFactory.getLogger(FriendRecommendationService.class);

    private final FriendCandidateFactory friendCandidateFactory;
    private final MutualFriendWithVisitorRankingStrategy friendRankingStrategy;
    private final UserService userService;

    public FriendRecommendationService(FriendCandidateFactory friendCandidateFactory, MutualFriendWithVisitorRankingStrategy friendRankingStrategy, UserService userService) {
        this.friendCandidateFactory = friendCandidateFactory;
        this.friendRankingStrategy = friendRankingStrategy;
        this.userService = userService;
    }

    public List<FriendRecommendation> recommendate(Long userId) {
        log.debug("begin");

        log.debug("userId: {}", userId);

        List<FriendCandidate> friendCandidates = friendCandidateFactory.createCandidates(userId);

        log.debug("candidates: {}", friendCandidates);

        List<FriendCandidate> rankedFriendCandidates = friendRankingStrategy.rank(userId, friendCandidates);

        log.debug("ranked candidates: {}", rankedFriendCandidates);

        Map<Long, UserOutline> userOutlines = initUserOutlines(rankedFriendCandidates);

        return rankedFriendCandidates.stream()
                .filter(candidate -> userOutlines.containsKey(candidate.getFriendId()))
                .map(candidate -> new FriendRecommendation(userOutlines.get(candidate.getFriendId()), candidate.getMutualFriendIds()))
                .collect(Collectors.toList());
    }

    private Map<Long, UserOutline> initUserOutlines(List<FriendCandidate> rankedFriendCandidates) {
        List<Long> candidateIds = rankedFriendCandidates.stream()
                .map(candidate -> candidate.getFriendId())
                .collect(Collectors.toList());

        Map<Long, UserOutline> userOutlines = new HashMap<>();
        for (UserOutline outline : userService.findFriends(candidateIds)) {
            userOutlines.put(outline.getId(), outline);
        }

        return userOutlines;
    }
}
