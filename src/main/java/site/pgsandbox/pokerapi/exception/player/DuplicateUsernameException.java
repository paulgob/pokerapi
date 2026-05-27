package site.pgsandbox.pokerapi.exception.player;

public class DuplicateUsernameException extends IllegalArgumentException {

    public DuplicateUsernameException(String username) {
        super("Username already exists: " + username);
    }
}
