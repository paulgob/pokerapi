package site.pgsandbox.pokerapi.service;

import java.util.List;
import org.springframework.stereotype.Service;
import site.pgsandbox.pokerapi.exception.player.BetIsHigherThanChipsException;
import site.pgsandbox.pokerapi.exception.player.DuplicateUsernameException;
import site.pgsandbox.pokerapi.exception.player.PlayerNotFoundException;
import site.pgsandbox.pokerapi.model.player.Actions;
import site.pgsandbox.pokerapi.model.player.Player;
import site.pgsandbox.pokerapi.repository.PlayerRepository;

@Service
public class PlayerService {

    private final PlayerRepository repository;

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    /**
     * Create a new player.
     * @param username His username (must be unique).
     * @param chips The amount of chips he has.
     * @return the newly created player.
     */
    public Player createAPlayer(String username, int chips) {
        if (repository.findByUsername(username).isPresent()) {
            throw new DuplicateUsernameException(username);
        }
        return repository.save(new Player(username, chips));
    }

    /**
     * Find a player by his ID.
     * @param id The player ID.
     * @return The player with the given ID.
     */
    public Player getPlayerById(Long id) {
        return repository
            .findById(id)
            .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    /**
     * List all the players.
     * @return All the players.
     */
    public List<Player> getAllPlayer() {
        return repository.findAll();
    }

    /**
     * Update the player with the given ID.
     * @param id The player ID.
     * @param username His new username.
     * @param chips His new amount of chips.
     * @return The updated player.
     */
    public Player updatePlayer(Long id, String username, int chips) {
        Player player = repository
            .findById(id)
            .orElseThrow(() -> new PlayerNotFoundException(id));

        player.setUsername(username);
        player.setChips(chips);

        return repository.save(player);
    }

    /**
     * Delete a player with the given ID.
     * @param id The player ID.
     */
    public void deletePlayer(Long id) {
        repository.deleteById(id);
    }

    /**
     * Handle the fold actions of a player.
     * @param id The player ID.
     * @return The player who does the action.
     */
    public Player foldPlayer(Long id) {
        Player player = getPlayerById(id);

        player.setActionChoosen(Actions.FOLD);

        return repository.save(player);
    }

    /**
     * Handle the check actions of a player.
     * @param id The player ID.
     * @return The player who does the action.
     */
    public Player checkPlayer(Long id) {
        Player player = getPlayerById(id);

        player.setActionChoosen(Actions.CHECK);

        return repository.save(player);
    }

    /**
     * Handle the call actions of a player.
     * @param id The player ID.
     * @param bet The amount of chips bet.
     * @return The player who does the action.
     */
    public Player callPlayer(Long id, int bet) {
        Player player = getPlayerById(id);
        int chips = player.getChips();

        if (chips < bet) {
            throw new BetIsHigherThanChipsException(chips, bet);
        } else {
            player.setChips(chips - bet);
        }

        player.setActionChoosen(Actions.CALL);
        player.setBet(bet);

        return repository.save(player);
    }

    /**
     * Handle the raise actions of a player.
     * @param id The player ID.
     * @param bet The amount of chips bet.
     * @return The player who does the action.
     */
    public Player raisePlayer(Long id, int bet) {
        Player player = getPlayerById(id);
        int chips = player.getChips();

        if (chips < bet) {
            throw new BetIsHigherThanChipsException(chips, bet);
        } else {
            player.setChips(chips - bet);
        }

        player.setActionChoosen(Actions.RAISE);
        player.setBet(bet);

        return repository.save(player);
    }
}
