package site.pgsandbox.pokerapi.exception.card;

public class DeckNotFoundException extends RuntimeException {

    public DeckNotFoundException(Long id) {
        super("Deck not found: " + id);
    }
}
