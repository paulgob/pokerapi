package site.pgsandbox.pokerapi.exception.game;

import java.util.NoSuchElementException;

public class GameNotFoundException extends NoSuchElementException {

    public GameNotFoundException(Long id) {
        super("Game not found: " + id);
    }
}
