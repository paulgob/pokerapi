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
    void thePotIsEmptyAfterGameStart() {
        Game startedGame = createStartedGame();

        assertEquals(0, startedGame.getPot());
    }

    @Test
    @Transactional
    void recomputePotEqualsTheSumOfThePlayersBets() {
        Game game = createStartedGame();
        Player p1 = game.getTable().getPlayers().get(0);
        Player p2 = game.getTable().getPlayers().get(1);

        playerService.callPlayer(p1.getId(), 30);
        playerService.raisePlayer(p2.getId(), 50);

        Game updated = service.recomputePot(game.getId());

        assertEquals(80, updated.getPot());
    }

    @Test
    @Transactional
    void playerCallUpdatesThePot() {
        Game game = createStartedGame();
        Player player = game.getTable().getPlayers().get(0);

        Game updated = service.playerCall(game.getId(), player.getId(), 40);

        assertEquals(40, updated.getPot());
    }

    @Test
    @Transactional
    void playerRaiseAddsToThePot() {
        Game game = createStartedGame();
        Player p1 = game.getTable().getPlayers().get(0);
        Player p2 = game.getTable().getPlayers().get(1);

        service.playerCall(game.getId(), p1.getId(), 20);
        Game updated = service.playerRaise(game.getId(), p2.getId(), 60);

        assertEquals(80, updated.getPot());
    }

    @Test
    @Transactional
    void playerCheckLeavesThePotUnchanged() {
        Game game = createStartedGame();
        Player p1 = game.getTable().getPlayers().get(0);
        Player p2 = game.getTable().getPlayers().get(1);

        service.playerCall(game.getId(), p1.getId(), 25);
        Game updated = service.playerCheck(game.getId(), p2.getId());

        assertEquals(25, updated.getPot());
    }

    @Test
    @Transactional
    void playerFoldLeavesThePotUnchanged() {
        Game game = createStartedGame();
        Player p1 = game.getTable().getPlayers().get(0);
        Player p2 = game.getTable().getPlayers().get(1);

        service.playerCall(game.getId(), p1.getId(), 25);
        Game updated = service.playerFold(game.getId(), p2.getId());

        assertEquals(25, updated.getPot());
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
            site.pgsandbox.pokerapi.model.game.Status.PRE_FLOP,
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
