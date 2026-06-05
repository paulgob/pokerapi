package site.pgsandbox.pokerapi.model.game;

public enum Status {
    NOT_STARTED,
    PRE_FLOP,
    FLOP,
    TURN,
    RIVER,
    SHOWDOWN;

    private static final Status[] vals = values();

    public Status next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }
}
