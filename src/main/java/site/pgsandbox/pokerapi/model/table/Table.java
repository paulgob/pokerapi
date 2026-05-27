package site.pgsandbox.pokerapi.model.table;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.pgsandbox.pokerapi.model.player.Player;

@Entity
@jakarta.persistence.Table(name = "poker_table")
@Getter
@Setter
@NoArgsConstructor
public class Table {

    @Id
    @GeneratedValue
    private Long id;

    private int maxPlayer = 6; // Default value = 6

    @ManyToMany
    private List<Player> players = new ArrayList<>();

    public Table(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }
}
