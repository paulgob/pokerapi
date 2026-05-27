package site.pgsandbox.pokerapi.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.pgsandbox.pokerapi.exception.player.PlayerNotFoundException;
import site.pgsandbox.pokerapi.exception.table.MaxPlayerAmountInvalidException;
import site.pgsandbox.pokerapi.exception.table.TableIsFullException;
import site.pgsandbox.pokerapi.exception.table.TableNotFoundException;
import site.pgsandbox.pokerapi.model.player.Player;
import site.pgsandbox.pokerapi.model.table.Table;
import site.pgsandbox.pokerapi.repository.PlayerRepository;
import site.pgsandbox.pokerapi.repository.TableRepository;

@Service
public class TableService {

    private final TableRepository repository;
    private final PlayerRepository playerRepository;

    public TableService(
        TableRepository repository,
        PlayerRepository playerRepository
    ) {
        this.repository = repository;
        this.playerRepository = playerRepository;
    }

    /**
     * Create a new table.
     * @param maxPlayer The maximum amount of player a table can have.
     * @return the newly created table.
     */
    public Table createATable(int maxPlayer) {
        if (maxPlayer > 6 || maxPlayer < 2) {
            throw new MaxPlayerAmountInvalidException();
        }

        return repository.save(new Table(maxPlayer));
    }

    /**
     * Find a table by his ID.
     * @param id The table ID.
     * @return The table with the given ID.
     */
    public Table getTableById(Long id) {
        return repository
            .findById(id)
            .orElseThrow(() -> new TableNotFoundException(id));
    }

    /**
     * List all the tables.
     * @return All the tables.
     */
    public List<Table> getAllTables() {
        return repository.findAll();
    }

    /**
     * Update the table with the given ID.
     * @param id The table ID.
     * @param maxPlayer The new maximum amount of player a table can have.
     * @return The updated table.
     */
    public Table updateTable(Long id, int maxPlayer) {
        Table table = repository
            .findById(id)
            .orElseThrow(() -> new TableNotFoundException(id));

        table.setMaxPlayer(maxPlayer);

        return repository.save(table);
    }

    /**
     * Delete a table with the given ID.
     * @param id The table ID.
     */
    public void deleteTable(Long id) {
        repository.deleteById(id);
    }

    /**
     * Add a player to a Table.
     * @param tableId The table where the player will be added.
     * @param playerId The player that will be added.
     * @return The newly added player.
     */
    @Transactional
    public Player addAPlayer(Long tableId, Long playerId) {
        Player player = playerRepository
            .findById(playerId)
            .orElseThrow(() -> new PlayerNotFoundException(playerId));
        Table table = repository
            .findById(tableId)
            .orElseThrow(() -> new TableNotFoundException(tableId));

        List<Player> players = table.getPlayers();

        if (players.size() >= table.getMaxPlayer()) {
            throw new TableIsFullException();
        } else {
            players.add(player);
            repository.save(table);
            return player;
        }
    }
}
