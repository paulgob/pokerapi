package site.pgsandbox.pokerapi.model.game;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.pgsandbox.pokerapi.model.card.Deck;
import site.pgsandbox.pokerapi.model.table.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Table table;

    @OneToOne
    private Deck deck;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Game(Table table, Deck deck) {
        this.table = table;
        this.deck = deck;
        this.status = Status.NOT_STARTED;
    }
}
