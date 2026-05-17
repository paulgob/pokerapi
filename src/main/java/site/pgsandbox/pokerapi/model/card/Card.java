package site.pgsandbox.pokerapi.model.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Card {

    private Suit suit;
    private Rank rank;
}
