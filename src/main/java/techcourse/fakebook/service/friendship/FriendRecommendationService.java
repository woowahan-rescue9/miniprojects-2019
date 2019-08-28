package techcourse.fakebook.service.friendship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import techcourse.fakebook.domain.friendship.FriendCandidate;
import techcourse.fakebook.service.friendship.dto.FriendRecommendation;
import techcourse.fakebook.service.user.UserService;
import techcourse.fakebook.service.user.dto.UserOutline;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        List<Long> rankedIndirectFriendIds = rankedFriendCandidates.stream()
                .mapToLong(FriendCandidate::getFriendId)
                .boxed()
                .collect(Collectors.toList());
        List<UserOutline> userOutlines = userService.findFriends(rankedIndirectFriendIds);

        return IntStream.range(0, userOutlines.size())
                .mapToObj(i -> newFriendRecommendation(rankedFriendCandidates, userOutlines, i))
                .collect(Collectors.toList());
    }

    private FriendRecommendation newFriendRecommendation(List<FriendCandidate> rankedFriendCandidates, List<UserOutline> userOutlines, int idx) {
        UserOutline userOutline = userOutlines.get(idx);
        FriendCandidate friendCandidate  = rankedFriendCandidates.get(idx);
        return new FriendRecommendation(userOutline, friendCandidate.getMutualFriendIds());
    }
}
