package site.pgsandbox.pokerapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pgsandbox.pokerapi.exception.table.TableNotFoundException;
import site.pgsandbox.pokerapi.model.game.Game;
import site.pgsandbox.pokerapi.model.player.Player;
import site.pgsandbox.pokerapi.model.table.Table;

@SpringBootTest
public class GameServiceTest {

    @Autowired
    private GameService service;

    @Autowired
    private DeckService deckService;

    @Autowired
    private TableService tableService;

    @Autowired
    private PlayerService playerService;

    @Test
    void createGameWithNonExistentTableThrowsTableNotFoundException() {
        long nonExistentTableId = -1;
        long deckId = deckService.createADeck().getId();

        assertThrows(TableNotFoundException.class, () ->
            service.createAGame(nonExistentTableId, deckId)
        );
    }

    @Test
    @Transactional
    void eachPlayerHasTwoCardsAfterGameStart() {
        Table table = tableService.createATable(2);
        Player p1 = playerService.createAPlayer("GameTest_1", 100);
        Player p2 = playerService.createAPlayer("GameTest_2", 100);
        tableService.addAPlayer(table.getId(), p1.getId());
        tableService.addAPlayer(table.getId(), p2.getId());

        long deckId = deckService.createADeck().getId();
        Game game = service.createAGame(table.getId(), deckId);
        Game startedGame = service.startGame(game.getId());

        for (Player player : startedGame.getTable().getPlayers()) {
            assertEquals(player.getHand().size(), 2);
        }
    }

    @Test
    @Transactional
    void theGamePotEqualsAllThePlayersChips() {
        Table table = tableService.createATable(2);
        Player p1 = playerService.createAPlayer("GameTest_1", 100);
        Player p2 = playerService.createAPlayer("GameTest_2", 100);
        tableService.addAPlayer(table.getId(), p1.getId());
        tableService.addAPlayer(table.getId(), p2.getId());

        long deckId = deckService.createADeck().getId();
        Game game = service.createAGame(table.getId(), deckId);
        Game startedGame = service.startGame(game.getId());

        int expectedPot = 0;
        for (Player p : startedGame.getTable().getPlayers()) {
            expectedPot += p.getChips();
        }

        assertEquals(expectedPot, startedGame.getPot());
    }
}
