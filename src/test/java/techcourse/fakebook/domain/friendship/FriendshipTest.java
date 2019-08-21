package techcourse.fakebook.domain.friendship;

import org.junit.jupiter.api.Test;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.exception.FriendshipNotRelatedUserIdException;
import techcourse.fakebook.exception.InvalidFriendshipUserIdException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class FriendshipTest {

    @Test
    void 생성자_두_유저가_id가_동일한경우() {
        Long sameId = 1L;
        User precedentUser = mock(User.class);
        given(precedentUser.getId()).willReturn(sameId);

        User user = mock(User.class);
        given(user.getId()).willReturn(sameId);

        assertThrows(InvalidFriendshipUserIdException.class, () -> new Friendship(precedentUser, user));
    }

    @Test
    void 생성자_앞선유저가_id가_더_큰경우() {
        Long precedentUserId = 10L;
        Long userId = 1L;
        User precedentUser = mock(User.class);
        given(precedentUser.getId()).willReturn(precedentUserId);

        User user = mock(User.class);
        given(user.getId()).willReturn(userId);

        assertThrows(InvalidFriendshipUserIdException.class, () -> new Friendship(precedentUser, user));
    }

    @Test
    void from_앞선유저가_id가_더_작은경우() {
        // Arrange
        Long precedentUserId = 1L;
        Long userId = 10L;
        User precedentUser = mock(User.class);
        given(precedentUser.getId()).willReturn(precedentUserId);

        User user = mock(User.class);
        given(user.getId()).willReturn(userId);

        Friendship expectedFriendship = new Friendship(precedentUser, user);
        // Act & Assert
        assertThat(Friendship.from(precedentUser, user)).isEqualTo(expectedFriendship);
    }

    @Test
    void from_앞선유저가_id가_더_큰경우() {
        // Arrange
        Long precedentUserId = 10L;
        Long userId = 1L;
        User precedentUser = mock(User.class);
        given(precedentUser.getId()).willReturn(precedentUserId);

        User user = mock(User.class);
        given(user.getId()).willReturn(userId);

        Friendship expectedFriendship = new Friendship(user, precedentUser);
        // Act & Assert
        assertThat(Friendship.from(precedentUser, user)).isEqualTo(expectedFriendship);
    }

    @Test
    void getFriendId_해당Friendship에_내id가_없을경우() {
        // Arrange
        Long precedentUserId = 1L;
        Long userId = 2L;
        User precedentUser = mock(User.class);
        given(precedentUser.getId()).willReturn(precedentUserId);

        User user = mock(User.class);
        given(user.getId()).willReturn(userId);

        Friendship friendship = Friendship.from(precedentUser, user);

        // Act & Assert
        Long notRelatedUserId = 0L;
        assertThrows(FriendshipNotRelatedUserIdException.class, () -> friendship.getFriendId(notRelatedUserId));
    }

    @Test
    void getFriendId_상대방id_잘가져오는지() {
        // Arrange
        User me = mock(User.class);
        Long myId = 1L;
        given(me.getId()).willReturn(myId);

        User friend = mock(User.class);
        Long friendId = 2L;
        given(friend.getId()).willReturn(friendId);

        Friendship friendship = Friendship.from(me, friend);

        // Act & Assert
        assertThat(friendship.getFriendId(me.getId())).isEqualTo(friend.getId());
    }
}