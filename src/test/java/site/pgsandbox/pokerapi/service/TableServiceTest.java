package site.pgsandbox.pokerapi.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pgsandbox.pokerapi.exception.table.TableIsFullException;
import site.pgsandbox.pokerapi.model.player.Player;
import site.pgsandbox.pokerapi.model.table.Table;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService service;

    @Autowired
    private PlayerService playerService;

    /**
     * If the table has reached its maximum number of players, it is impossible to add any more.
     */
    @Test
    void cantAddPlayerIfTableFull() {
        Table table = service.createATable(2);

        for (int i = 0; i < 2; i++) {
            Player player = playerService.createAPlayer(
                "Test_" + Integer.toString(i),
                100
            );

            service.addAPlayer(table.getId(), player.getId());
        }

        Player extraPlayer = playerService.createAPlayer("Test_extra", 100);

        assertThrows(TableIsFullException.class, () ->
            service.addAPlayer(table.getId(), extraPlayer.getId())
        );
    }
}
