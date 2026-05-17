package site.pgsandbox.pokerapi.exception.card;

public class EmptyDeckException extends RuntimeException {

    public EmptyDeckException(Long id) {
        super("Deck has no more cards: " + id);
    }
}
