package site.pgsandbox.pokerapi.service;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;
import org.junit.jupiter.api.Test;
import site.pgsandbox.pokerapi.model.card.Card;
import site.pgsandbox.pokerapi.model.card.Rank;
import site.pgsandbox.pokerapi.model.card.Suit;
import site.pgsandbox.pokerapi.model.player.Player;

public class HandEvaluatorServiceTest {

    private final HandEvaluatorService service = new HandEvaluatorService();

    @Test
    void higherRankedHandWinsTheShowdown() {
        // Community Card: nothing connects for either player.
        List<Card> community = List.of(
            new Card(Suit.Clubs, Rank.Two),
            new Card(Suit.Diamonds, Rank.Five),
            new Card(Suit.Hearts, Rank.Nine),
            new Card(Suit.Spades, Rank.Jack),
            new Card(Suit.Clubs, Rank.Queen)
        );

        // Pair of aces.
        Player withPair = playerWithHand(
            new Card(Suit.Spades, Rank.Ace),
            new Card(Suit.Hearts, Rank.Ace)
        );
        // Ace-high, no pair.
        Player withHighCard = playerWithHand(
            new Card(Suit.Diamonds, Rank.Ace),
            new Card(Suit.Clubs, Rank.Three)
        );

        Player winner = service.findWinner(
            List.of(withHighCard, withPair),
            community
        );

        assertSame(withPair, winner);
    }

    @Test
    void winningHandIsBuiltFromTheCommunityCards() {
        // Four hearts on the community cards: a player holding one more heart makes a flush.
        List<Card> community = List.of(
            new Card(Suit.Hearts, Rank.Two),
            new Card(Suit.Hearts, Rank.Five),
            new Card(Suit.Hearts, Rank.Nine),
            new Card(Suit.Hearts, Rank.Jack),
            new Card(Suit.Spades, Rank.Queen)
        );

        Player flushPlayer = playerWithHand(
            new Card(Suit.Hearts, Rank.King),
            new Card(Suit.Clubs, Rank.Three)
        );
        // A pair of queens loses to the flush.
        Player pairPlayer = playerWithHand(
            new Card(Suit.Diamonds, Rank.Queen),
            new Card(Suit.Clubs, Rank.Seven)
        );

        Player winner = service.findWinner(
            List.of(pairPlayer, flushPlayer),
            community
        );

        assertSame(flushPlayer, winner);
    }

    @Test
    void noPlayersYieldsNoWinner() {
        assertNull(service.findWinner(List.of(), List.of()));
    }

    private Player playerWithHand(Card first, Card second) {
        Player player = new Player("Test_" + System.nanoTime(), 100);
        player.setHand(List.of(first, second));
        return player;
    }
}
