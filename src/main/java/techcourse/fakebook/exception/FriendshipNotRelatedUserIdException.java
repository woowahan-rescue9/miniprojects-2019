package techcourse.fakebook.exception;

public class FriendshipNotRelatedUserIdException extends RuntimeException {
    public FriendshipNotRelatedUserIdException() {
        super("해당 Friendship 과 관련이 없는 유저의 id 입니다.");
    }
}
