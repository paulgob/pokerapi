package site.pgsandbox.pokerapi.exception.table;

public class MaxPlayerAmountInvalidException extends ArithmeticException {

    public MaxPlayerAmountInvalidException() {
        super("A table must have between 2 and 6 players max.");
    }
}
