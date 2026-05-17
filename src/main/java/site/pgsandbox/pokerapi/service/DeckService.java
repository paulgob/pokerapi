package site.pgsandbox.pokerapi.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import site.pgsandbox.pokerapi.exception.card.DeckNotFoundException;
import site.pgsandbox.pokerapi.exception.card.EmptyDeckException;
import site.pgsandbox.pokerapi.model.card.Card;
import site.pgsandbox.pokerapi.model.card.Deck;
import site.pgsandbox.pokerapi.model.card.Rank;
import site.pgsandbox.pokerapi.model.card.Suit;
import site.pgsandbox.pokerapi.repository.DeckRepositery;

@Service
@Transactional
public class DeckService {

    private final DeckRepositery repositery;

    public DeckService(DeckRepositery repositery) {
        this.repositery = repositery;
    }

    /**
     * Create a new deck.
     * The deck is initialized with the suits in this order: Spades, Hearts, Diamonds, Clubs. And the ranks: Ace, King, Queen, ..., Three, Two.
     * @return A new deck.
     */
    public Deck createADeck() {
        List<Card> cards = new ArrayList<>();

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }

        return repositery.save(new Deck(cards));
    }

    /**
     * Shuffle a deck.
     * @param id Id of the deck to shuffle.
     * @return The deck with shuffled cards.
     */
    public Deck shuffle(Long id) {
        Deck deck = repositery
            .findById(id)
            .orElseThrow(() -> new DeckNotFoundException(id));

        List<Card> cards = deck.getCardList();
        Collections.shuffle(cards);
        deck.setCardList(cards);

        return repositery.save(deck);
    }

    /**
     * Deals the first card from the deck. (Remove the card from the deck).
     * @param id Id of the deck to get a card from.
     * @return The first card of the deck.
     */
    public Card getACard(Long id) {
        Deck deck = repositery
            .findById(id)
            .orElseThrow(() -> new DeckNotFoundException(id));

        if (deck.getCardList().isEmpty()) {
            throw new EmptyDeckException(id);
        }

        Card card = deck.getCardList().removeFirst();
        repositery.save(deck);

        return card;
    }
}
