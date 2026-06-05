package site.pgsandbox.pokerapi.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import site.pgsandbox.pokerapi.model.card.Card;
import site.pgsandbox.pokerapi.model.card.HandRank;
import site.pgsandbox.pokerapi.model.card.Rank;
import site.pgsandbox.pokerapi.model.card.Suit;
import site.pgsandbox.pokerapi.model.player.Player;

@Service
public class HandEvaluatorService {

    /**
     * Determines the winning player at showdown. Each player's two hole cards are
     * combined with the shared community cards, and the resulting seven-card hand
     * is evaluated; the player with the highest {@link HandRank} wins. When several
     * players share the same rank the first one encountered is kept as the winner.
     *
     * @param players        the players still in the hand
     * @param communityCards the shared cards on the table
     * @return the winning player, or null if no players were provided
     */
    public Player findWinner(List<Player> players, List<Card> communityCards) {
        Player winner = null;
        HandRank bestRank = null;

        for (Player player : players) {
            List<Card> cards = new ArrayList<>(player.getHand());
            cards.addAll(communityCards);

            HandRank rank = evaluateHand(cards);
            if (bestRank == null || rank.ordinal() > bestRank.ordinal()) {
                bestRank = rank;
                winner = player;
            }
        }
        return winner;
    }

    /**
     * Evaluates a single hand and returns its best {@link HandRank}.
     */
    public HandRank evaluateHand(List<Card> hand) {
        // Count how many cards share each rank and each suit.
        Map<Rank, Integer> rankCounts = new EnumMap<>(Rank.class);
        Map<Suit, Integer> suitCounts = new EnumMap<>(Suit.class);
        Set<Integer> values = new HashSet<>();

        for (Card card : hand) {
            rankCounts.merge(card.getRank(), 1, Integer::sum);
            suitCounts.merge(card.getSuit(), 1, Integer::sum);
            values.add(value(card.getRank()));
        }

        boolean isFlush = suitCounts
            .values()
            .stream()
            .anyMatch(count -> count >= 5);
        boolean isStraight = isStraight(values);

        // Ordered list of how many duplicates each rank has, e.g. [3, 2] for a full house.
        List<Integer> groups = new ArrayList<>(rankCounts.values());
        groups.sort(Collections.reverseOrder());

        boolean fourOfAKind = groups.get(0) == 4;
        boolean threeOfAKind = groups.get(0) == 3;
        boolean pair = groups.get(0) == 2;
        boolean twoPair =
            groups.size() >= 2 && groups.get(0) == 2 && groups.get(1) == 2;
        boolean fullHouse =
            groups.get(0) == 3 && groups.size() >= 2 && groups.get(1) >= 2;

        if (isStraight && isFlush) {
            boolean isRoyal = values.containsAll(List.of(10, 11, 12, 13, 14));
            return isRoyal ? HandRank.ROYAL_FLUSH : HandRank.STRAIGHT_FLUSH;
        }
        if (fourOfAKind) {
            return HandRank.FOUR_OF_A_KIND;
        }
        if (fullHouse) {
            return HandRank.FULL_HOUSE;
        }
        if (isFlush) {
            return HandRank.FLUSH;
        }
        if (isStraight) {
            return HandRank.STRAIGHT;
        }
        if (threeOfAKind) {
            return HandRank.THREE_OF_A_KIND;
        }
        if (twoPair) {
            return HandRank.TWO_PAIR;
        }
        if (pair) {
            return HandRank.PAIR;
        }
        return HandRank.HIGH_CARD;
    }

    /**
     * Maps a {@link Rank} to its numeric value (Two = 2 ... King = 13, Ace = 14).
     */
    private int value(Rank rank) {
        return 14 - rank.ordinal();
    }

    /**
     * Returns true if the given card values contain at least five consecutive ranks.
     * The Ace can be used both high (14) and low (1) to form the wheel A-2-3-4-5.
     */
    private boolean isStraight(Set<Integer> values) {
        List<Integer> sorted = new ArrayList<>(values);
        if (sorted.contains(14)) {
            sorted.add(1); // Ace can also be the low end of a straight.
        }
        Collections.sort(sorted);

        int consecutive = 1;
        for (int i = 1; i < sorted.size(); i++) {
            if (sorted.get(i) == sorted.get(i - 1) + 1) {
                consecutive++;
                if (consecutive >= 5) {
                    return true;
                }
            } else {
                consecutive = 1;
            }
        }
        return false;
    }
}
