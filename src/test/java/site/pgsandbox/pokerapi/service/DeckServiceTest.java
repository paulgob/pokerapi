package site.pgsandbox.pokerapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pgsandbox.pokerapi.model.card.Card;
import site.pgsandbox.pokerapi.model.card.Deck;

@SpringBootTest
class DeckServiceTest {

    @Autowired
    private DeckService service;

    @Test
    void ItShouldHaveFiftyTwoUniquesCards() {
        Deck deck = service.createADeck();

        assertEquals(52, deck.getCardList().size());
    }

    @Test
    void NoCardIsDealtTwice() {
        Deck deck = service.createADeck();

        Set<Card> dealtCards = new HashSet<>();

        for (int i = 0; i < 52; i++) {
            Card card = service.getACard(deck.getId());
            dealtCards.add(card);
        }

        assertEquals(52, dealtCards.size());
    }
}
