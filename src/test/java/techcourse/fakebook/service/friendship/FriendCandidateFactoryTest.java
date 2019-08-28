package techcourse.fakebook.service.friendship;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.fakebook.domain.friendship.FriendCandidate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class FriendCandidateFactoryTest {
    private final Long userId = 0L;
    private static final List<FriendCandidate> EMPTY_FRIEND_CANDIDATES = Collections.emptyList();
    private static final List<Long> EMPTY_ID_LIST = Collections.emptyList();

    @InjectMocks
    private FriendCandidateFactory friendCandidateFactory;

    @Mock
    private IndirectFriendService indirectFriendService;

    @Mock
    private VisitorTrackingService visitorTrackingService;

    @Mock
    private FriendshipService friendshipService;

    @Test
    void indirectFriend만_존재할_경우() {
        // Arrange
        Long FriendId = 10L;
        List<FriendCandidate> indirectFriends = Arrays.asList(FriendCandidate.withNoMutualFriends(userId, FriendId));
        given(indirectFriendService.retrieve(userId)).willReturn(indirectFriends);
        given(visitorTrackingService.currentCounter(userId)).willReturn(Counter.newInstance());
        given(friendshipService.findFriendIds(userId)).willReturn(EMPTY_ID_LIST);


        // Act
        List<FriendCandidate> candidates = friendCandidateFactory.createCandidates(userId);


        // Assert
        assertThat(candidates.get(0)).isEqualTo(indirectFriends.get(0));
    }

    @Test
    void 방문자만_존재할_경우() {
        // Arrange
        Long visitorId = 10L;
        List<FriendCandidate> indirectFriends = EMPTY_FRIEND_CANDIDATES;
        given(indirectFriendService.retrieve(userId)).willReturn(indirectFriends);
        given(visitorTrackingService.currentCounter(userId)).willReturn(counterWithVisitor(visitorId));
        given(friendshipService.findFriendIds(userId)).willReturn(EMPTY_ID_LIST);


        // Act
        List<FriendCandidate> candidates = friendCandidateFactory.createCandidates(userId);


        // Assert
        assertThat(candidates.get(0)).isEqualTo(FriendCandidate.withNoMutualFriends(userId, visitorId));
    }

    @Test
    void 방문자이면서_자기자신() {
        // Arrange
        Long visitorId = 10L;
        List<FriendCandidate> indirectFriends = EMPTY_FRIEND_CANDIDATES;
        given(indirectFriendService.retrieve(userId)).willReturn(indirectFriends);
        given(visitorTrackingService.currentCounter(userId)).willReturn(counterWithVisitor(userId));
        given(friendshipService.findFriendIds(userId)).willReturn(EMPTY_ID_LIST);


        // Act
        List<FriendCandidate> candidates = friendCandidateFactory.createCandidates(userId);


        // Assert
        assertThat(candidates).isEmpty();
    }

    @Test
    void 방문자이면서_indirectFriend() {
        // Arrange
        Long indirectFriendVisitorId = 10L;
        List<FriendCandidate> indirectFriends = Arrays.asList(FriendCandidate.withNoMutualFriends(userId, indirectFriendVisitorId));
        given(indirectFriendService.retrieve(userId)).willReturn(indirectFriends);
        given(visitorTrackingService.currentCounter(userId)).willReturn(counterWithVisitor(indirectFriendVisitorId));
        given(friendshipService.findFriendIds(userId)).willReturn(EMPTY_ID_LIST);


        // Act
        List<FriendCandidate> candidates = friendCandidateFactory.createCandidates(userId);


        // Assert
        assertThat(candidates.get(0)).isEqualTo(FriendCandidate.withNoMutualFriends(userId, indirectFriendVisitorId));
    }

    @Test
    void 방문자이면서_Friend() {
        // Arrange
        Long FriendVisitorId = 10L;
        List<FriendCandidate> indirectFriends = EMPTY_FRIEND_CANDIDATES;
        given(indirectFriendService.retrieve(userId)).willReturn(indirectFriends);
        given(visitorTrackingService.currentCounter(userId)).willReturn(counterWithVisitor(FriendVisitorId));
        given(friendshipService.findFriendIds(userId)).willReturn(Arrays.asList(FriendVisitorId));


        // Act
        List<FriendCandidate> candidates = friendCandidateFactory.createCandidates(userId);


        // Assert
        assertThat(candidates).isEmpty();
    }

    @Test
    void 방문자1명_IndirectFriend1명() {
        // Arrange
        Long indirectFriendId = 10L;
        List<FriendCandidate> indirectFriends = Arrays.asList(FriendCandidate.withNoMutualFriends(userId, indirectFriendId));
        given(indirectFriendService.retrieve(userId)).willReturn(indirectFriends);
        Long visitorId = 11L;
        given(visitorTrackingService.currentCounter(userId)).willReturn(counterWithVisitor(visitorId));
        given(friendshipService.findFriendIds(userId)).willReturn(EMPTY_ID_LIST);
        List<FriendCandidate> expectedCandidates = Arrays.asList(
                FriendCandidate.withNoMutualFriends(userId, indirectFriendId),
                FriendCandidate.withNoMutualFriends(userId, visitorId)
        );


        // Act
        List<FriendCandidate> candidates = friendCandidateFactory.createCandidates(userId);


        // Assert
        assertThat(candidates.size()).isEqualTo(expectedCandidates.size());
        candidates.stream()
                .forEach(candidate -> assertThat(expectedCandidates.contains(candidate)).isTrue());
    }

    private Counter<Long> counterWithVisitor(Long visitorId) {
        Counter<Long> counter = Counter.newInstance();
        counter.increase(visitorId);

        return counter;
    }
}