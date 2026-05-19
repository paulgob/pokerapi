package site.pgsandbox.pokerapi.exception.card;

import java.util.NoSuchElementException;

public class DeckNotFoundException extends NoSuchElementException {

    public DeckNotFoundException(Long id) {
        super("Deck not found: " + id);
    }
}
