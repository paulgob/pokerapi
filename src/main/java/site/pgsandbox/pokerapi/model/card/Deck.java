package site.pgsandbox.pokerapi.model.card;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
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

    @ElementCollection
    private List<Card> cardList = new ArrayList<>();

    public Deck(List<Card> aCardList) {
        this.cardList = aCardList;
    }
}
