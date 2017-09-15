package simplepets.brainsynder.errors;

public class SimplePetsException extends RuntimeException {

    public SimplePetsException() {
    }

    public SimplePetsException(String message) {
        super("[SimplePets] Error: " + message);
    }

    public SimplePetsException(Throwable message) {
        super(message);
    }

    public SimplePetsException(String message, Throwable cause) {
        super("[SimplePets] Error: " + message, cause);
    }

    public SimplePetsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super("[SimplePets] Error: " + message, cause, enableSuppression, writableStackTrace);
    }
}