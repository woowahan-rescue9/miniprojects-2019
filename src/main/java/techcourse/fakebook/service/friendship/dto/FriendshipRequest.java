package techcourse.fakebook.service.friendship.dto;

public class FriendshipRequest {
    private Long friendId;

    public FriendshipRequest() {
    }

    public FriendshipRequest(Long friendId) {
        this.friendId = friendId;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    @Override
    public String toString() {
        return "FriendshipRequest{" +
                "friendId=" + friendId +
                '}';
    }
}
