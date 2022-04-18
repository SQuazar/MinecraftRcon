package net.flawe.rcon.exception;

public class RconLoginException extends RuntimeException {

    public RconLoginException(String message) {
        super(message);
    }

    public RconLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public RconLoginException(Throwable cause) {
        super(cause);
    }
}
