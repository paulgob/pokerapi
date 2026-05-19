package site.pgsandbox.pokerapi.model.player;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.pgsandbox.pokerapi.model.card.Card;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    @ElementCollection
    private List<Card> hand = new ArrayList<>();

    private int chips;

    public Player(String username, int chips) {
        this.username = username;
        this.chips = chips;
    }
}
