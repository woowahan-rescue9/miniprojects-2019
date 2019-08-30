package techcourse.fakebook.service.friendship.dto;

import techcourse.fakebook.service.user.dto.UserOutline;

import java.util.Set;

public class FriendRecommendation {
    private UserOutline friendOutline;
    private Set<Long> mutualFriendIds;

    public FriendRecommendation() {
    }

    public FriendRecommendation(UserOutline friendOutline, Set<Long> mutualFriendIds) {
        this.friendOutline = friendOutline;
        this.mutualFriendIds = mutualFriendIds;
    }

    public UserOutline getFriendOutline() {
        return friendOutline;
    }

    public void setFriendOutline(UserOutline friendOutline) {
        this.friendOutline = friendOutline;
    }

    public Set<Long> getMutualFriendIds() {
        return mutualFriendIds;
    }

    public void setMutualFriendIds(Set<Long> mutualFriendIds) {
        this.mutualFriendIds = mutualFriendIds;
    }
}
