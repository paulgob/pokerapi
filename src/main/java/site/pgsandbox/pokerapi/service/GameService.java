package site.pgsandbox.pokerapi.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import site.pgsandbox.pokerapi.exception.card.DeckNotFoundException;
import site.pgsandbox.pokerapi.exception.game.GameNotFoundException;
import site.pgsandbox.pokerapi.exception.table.TableNotFoundException;
import site.pgsandbox.pokerapi.model.card.Card;
import site.pgsandbox.pokerapi.model.card.Deck;
import site.pgsandbox.pokerapi.model.game.Game;
import site.pgsandbox.pokerapi.model.game.Status;
import site.pgsandbox.pokerapi.model.player.Player;
import site.pgsandbox.pokerapi.model.table.Table;
import site.pgsandbox.pokerapi.repository.DeckRepository;
import site.pgsandbox.pokerapi.repository.GameRepository;
import site.pgsandbox.pokerapi.repository.TableRepository;

@Service
public class GameService {

    private final GameRepository repository;
    private final TableRepository tableRepository;
    private final DeckRepository deckRepository;
    private final DeckService deckService;
    private final PlayerService playerService;

    public GameService(
        GameRepository repository,
        TableRepository tableRepository,
        DeckRepository deckRepository,
        DeckService deckService,
        PlayerService playerService
    ) {
        this.repository = repository;
        this.tableRepository = tableRepository;
        this.deckRepository = deckRepository;
        this.deckService = deckService;
        this.playerService = playerService;
    }

    /**
     * Create a new game for the given table.
     * @param tableId The ID of the table the game is played on.
     * @return The newly created game.
     */
    public Game createAGame(Long tableId, Long deckId) {
        Table table = tableRepository
            .findById(tableId)
            .orElseThrow(() -> new TableNotFoundException(tableId));

        Deck deck = deckRepository
            .findById(deckId)
            .orElseThrow(() -> new DeckNotFoundException(deckId));

        return repository.save(new Game(table, deck));
    }

    /**
     * Find a game by its ID.
     * @param id The game ID.
     * @return The game with the given ID.
     */
    public Game getGameById(Long id) {
        return repository
            .findById(id)
            .orElseThrow(() -> new GameNotFoundException(id));
    }

    /**
     * List all games.
     * @return All games.
     */
    public List<Game> getAllGames() {
        return repository.findAll();
    }

    /**
     * Update the status of a game.
     * @param id The game ID.
     * @param status The new status.
     * @return The updated game.
     */
    public Game updateGame(Long id, Status status) {
        Game game = repository
            .findById(id)
            .orElseThrow(() -> new GameNotFoundException(id));

        game.setStatus(status);

        return repository.save(game);
    }

    /**
     * Delete a game by its ID.
     * @param id The game ID.
     */
    public void deleteGame(Long id) {
        repository.deleteById(id);
    }

    /**
     * Start a game with the choosen id.
     * Shuffle the deck, deal two cards to each players and set the status to PRE_FLOP.
     * @param id The game ID.
     * @return The game started.
     */
    public Game startGame(Long id) {
        Game game = getGameById(id);
        Table table = game.getTable();
        Deck deck = game.getDeck();
        List<Player> players = table.getPlayers();

        deckService.shuffle(deck.getId());

        // Deals two cards to each players
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < players.size(); j++) {
                List<Card> hand = players.get(j).getHand();
                hand.add(deckService.getACard(deck.getId()));
                players.get(j).setHand(hand);
            }
        }

        // No bets placed yet at the start of a hand
        game.setPot(0);

        game.setStatus(Status.PRE_FLOP);

        return repository.save(game);
    }

    /**
     * Advance the game to the next stage. Manage the stage's behavior.
     * @param id The game ID.
     * @return The game entity.
     */
    public Game nextStage(Long id) {
        Game game = getGameById(id);
        Deck deck = game.getDeck();
        List<Card> cards = new ArrayList<Card>();

        switch (game.getStatus()) {
            case FLOP:
                for (int i = 0; i < 3; i++) {
                    cards.add(deckService.getACard(deck.getId()));
                }
                game.setStatus(Status.TURN);
                break;
            case TURN:
                cards.add(deckService.getACard(deck.getId()));
                game.setStatus(Status.RIVER);
                break;
            case RIVER:
                cards.add(deckService.getACard(deck.getId()));
                game.setStatus(Status.SHOWDOWN);
                break;
            case SHOWDOWN:
                break;
            default:
                break;
        }

        game.getCommunityCards().addAll(cards);

        return repository.save(game);
    }

    /**
     * Recompute the pot as the sum of every player's bet on the game's table.
     * @param id The game ID.
     * @return The updated game.
     */
    public Game recomputePot(Long id) {
        Game game = getGameById(id);

        int pot = game
            .getTable()
            .getPlayers()
            .stream()
            .mapToInt(Player::getBet)
            .sum();

        game.setPot(pot);

        return repository.save(game);
    }

    /**
     * Handle a player's fold action and refresh the pot.
     * @param gameId The game ID.
     * @param playerId The player ID.
     * @return The updated game.
     */
    public Game playerFold(Long gameId, Long playerId) {
        playerService.foldPlayer(playerId);
        return recomputePot(gameId);
    }

    /**
     * Handle a player's check action and refresh the pot.
     * @param gameId The game ID.
     * @param playerId The player ID.
     * @return The updated game.
     */
    public Game playerCheck(Long gameId, Long playerId) {
        playerService.checkPlayer(playerId);
        return recomputePot(gameId);
    }

    /**
     * Handle a player's call action and refresh the pot.
     * @param gameId The game ID.
     * @param playerId The player ID.
     * @param bet The amount of chips bet.
     * @return The updated game.
     */
    public Game playerCall(Long gameId, Long playerId, int bet) {
        playerService.callPlayer(playerId, bet);
        return recomputePot(gameId);
    }

    /**
     * Handle a player's raise action and refresh the pot.
     * @param gameId The game ID.
     * @param playerId The player ID.
     * @param bet The amount of chips bet.
     * @return The updated game.
     */
    public Game playerRaise(Long gameId, Long playerId, int bet) {
        playerService.raisePlayer(playerId, bet);
        return recomputePot(gameId);
    }
}
