package net.flawe.rcon.exception;

public class RconConnectionException extends RuntimeException {

    public RconConnectionException() {
    }

    public RconConnectionException(String message) {
        super(message);
    }

    public RconConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public RconConnectionException(Throwable cause) {
        super(cause);
    }
}
