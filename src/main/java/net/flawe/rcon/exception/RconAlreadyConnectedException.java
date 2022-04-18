package net.flawe.rcon.exception;

public class RconAlreadyConnectedException extends RuntimeException {

    public RconAlreadyConnectedException(String message) {
        super(message);
    }

    public RconAlreadyConnectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
