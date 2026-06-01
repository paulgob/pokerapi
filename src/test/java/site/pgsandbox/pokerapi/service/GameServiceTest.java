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
        Game startedGame = createStartedGame();

        for (Player player : startedGame.getTable().getPlayers()) {
            assertEquals(player.getHand().size(), 2);
        }
    }

    @Test
    @Transactional
    void theGamePotEqualsAllThePlayersChips() {
        Game startedGame = createStartedGame();

        int expectedPot = 0;
        for (Player p : startedGame.getTable().getPlayers()) {
            expectedPot += p.getChips();
        }

        assertEquals(expectedPot, startedGame.getPot());
    }

    @Test
    @Transactional
    void flopStageDealsThreeCommunityCards() {
        Game game = createStartedGame();
        game.setStatus(site.pgsandbox.pokerapi.model.game.Status.FLOP);

        Game afterFlop = service.nextStage(game.getId());

        assertEquals(3, afterFlop.getCommunityCards().size());
        assertEquals(
            site.pgsandbox.pokerapi.model.game.Status.TURN,
            afterFlop.getStatus()
        );
    }

    @Test
    @Transactional
    void turnStageDealsOneCommunityCard() {
        Game game = createStartedGame();
        game.setStatus(site.pgsandbox.pokerapi.model.game.Status.FLOP);
        service.nextStage(game.getId());

        Game afterTurn = service.nextStage(game.getId());

        assertEquals(4, afterTurn.getCommunityCards().size());
        assertEquals(
            site.pgsandbox.pokerapi.model.game.Status.RIVER,
            afterTurn.getStatus()
        );
    }

    @Test
    @Transactional
    void riverStageDealsOneCommunityCard() {
        Game game = createStartedGame();
        game.setStatus(site.pgsandbox.pokerapi.model.game.Status.FLOP);
        service.nextStage(game.getId());
        service.nextStage(game.getId());

        Game afterRiver = service.nextStage(game.getId());

        assertEquals(5, afterRiver.getCommunityCards().size());
        assertEquals(
            site.pgsandbox.pokerapi.model.game.Status.SHOWDOWN,
            afterRiver.getStatus()
        );
    }

    @Test
    @Transactional
    void showdownStageDealsNoCommunityCards() {
        Game game = createStartedGame();
        game.setStatus(site.pgsandbox.pokerapi.model.game.Status.FLOP);
        service.nextStage(game.getId());
        service.nextStage(game.getId());
        service.nextStage(game.getId());

        Game afterShowdown = service.nextStage(game.getId());

        assertEquals(5, afterShowdown.getCommunityCards().size());
        assertEquals(
            site.pgsandbox.pokerapi.model.game.Status.SHOWDOWN,
            afterShowdown.getStatus()
        );
    }

    private Game createStartedGame() {
        Table table = tableService.createATable(2);
        Player p1 = playerService.createAPlayer(
            "StageTest_1_" + System.nanoTime(),
            100
        );
        Player p2 = playerService.createAPlayer(
            "StageTest_2_" + System.nanoTime(),
            100
        );
        tableService.addAPlayer(table.getId(), p1.getId());
        tableService.addAPlayer(table.getId(), p2.getId());

        long deckId = deckService.createADeck().getId();
        Game game = service.createAGame(table.getId(), deckId);
        return service.startGame(game.getId());
    }
}
