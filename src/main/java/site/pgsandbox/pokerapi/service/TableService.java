package site.pgsandbox.pokerapi.service;

import java.util.List;
import org.springframework.stereotype.Service;
import site.pgsandbox.pokerapi.exception.table.MaxPlayerAmountInvalidException;
import site.pgsandbox.pokerapi.exception.table.TableNotFoundException;
import site.pgsandbox.pokerapi.model.table.Table;
import site.pgsandbox.pokerapi.repository.TableRepository;

@Service
public class TableService {

    private final TableRepository repository;

    public TableService(TableRepository repository) {
        this.repository = repository;
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
}
