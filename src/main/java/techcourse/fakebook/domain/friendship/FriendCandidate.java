package techcourse.fakebook.domain.friendship;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FriendCandidate {
    private final Long userId;
    private final Long friendId;
    private final Set<Long> mutualFriendIds;

    public FriendCandidate(Long userId, Long friendId, Set<Long> mutualFriendIds) {
        this.userId = userId;
        this.friendId = friendId;
        this.mutualFriendIds = mutualFriendIds;
    }

    public static FriendCandidate withNoMutualFriends(Long userId, Long friendId) {
        return new FriendCandidate(userId, friendId, new HashSet<>());
    }

    public int countMutualFriends() {
        return mutualFriendIds.size();
    }

    public Long getFriendId() {
        return friendId;
    }

    public Set<Long> getMutualFriendIds() {
        return mutualFriendIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendCandidate that = (FriendCandidate) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(friendId, that.friendId) &&
                Objects.equals(mutualFriendIds, that.mutualFriendIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, friendId, mutualFriendIds);
    }

    @Override
    public String toString() {
        return "FriendCandidate{" +
                "userId=" + userId +
                ", friendId=" + friendId +
                ", mutualFriendIds=" + mutualFriendIds +
                '}';
    }
}
