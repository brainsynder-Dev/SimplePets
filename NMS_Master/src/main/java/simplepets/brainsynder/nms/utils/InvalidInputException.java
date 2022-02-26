package simplepets.brainsynder.nms.utils;

public class InvalidInputException extends RuntimeException {

    public InvalidInputException (String message) {
        super("[SimplePets Error] "+message);
    }

    public InvalidInputException (Exception exception) {
        super("[SimplePets Error] "+exception.getMessage(), exception);
    }

    public InvalidInputException (String message, Exception exception) {
        super("[SimplePets Error] "+message+" ("+exception.getMessage()+")", exception);
    }

}
