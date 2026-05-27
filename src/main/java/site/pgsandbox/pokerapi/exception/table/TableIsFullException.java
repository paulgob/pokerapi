package site.pgsandbox.pokerapi.exception.table;

public class TableIsFullException extends ArithmeticException {

    public TableIsFullException() {
        super("This table is full.");
    }
}
