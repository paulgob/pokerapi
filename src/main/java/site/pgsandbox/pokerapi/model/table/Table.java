package site.pgsandbox.pokerapi.model.table;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.pgsandbox.pokerapi.model.player.Player;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Table {

    @Id
    @GeneratedValue
    private Long id;

    private int maxPlayer = 6; // Default value = 6
    private Player[] players = new Player[maxPlayer];

    public Table(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }
}
