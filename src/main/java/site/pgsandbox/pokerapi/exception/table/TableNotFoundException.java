package site.pgsandbox.pokerapi.exception.table;

import java.util.NoSuchElementException;

public class TableNotFoundException extends NoSuchElementException {

    public TableNotFoundException(Long id) {
        super("Table not found: " + id);
    }
}
