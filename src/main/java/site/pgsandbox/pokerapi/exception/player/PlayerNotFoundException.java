package site.pgsandbox.pokerapi.exception.player;

import java.util.NoSuchElementException;

public class PlayerNotFoundException extends NoSuchElementException {

    public PlayerNotFoundException(Long id) {
        super("Player not found: " + id);
    }
}
