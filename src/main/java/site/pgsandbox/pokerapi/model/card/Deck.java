package site.pgsandbox.pokerapi.model.card;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Deck {

    @Id
    @GeneratedValue
    private Long id;

    private Card[] cardList;

    public Deck(Card[] aCardList) {
        this.cardList = aCardList;
    }
}
