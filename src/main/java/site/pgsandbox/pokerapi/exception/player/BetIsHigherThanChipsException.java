package site.pgsandbox.pokerapi.exception.player;

public class BetIsHigherThanChipsException extends ArithmeticException {

    public BetIsHigherThanChipsException(int chips, int bet) {
        super(
            "The player's bet (= " +
                bet +
                ") is higher than the player's chips (= " +
                chips +
                ")."
        );
    }
}
