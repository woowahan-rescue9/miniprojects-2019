package techcourse.fakebook.exception;

public class InvalidFriendshipUserIdException extends RuntimeException {
    public InvalidFriendshipUserIdException() {
        super("[precedentUser.id < user.id] 를 만족해야합니다.");
    }
}
