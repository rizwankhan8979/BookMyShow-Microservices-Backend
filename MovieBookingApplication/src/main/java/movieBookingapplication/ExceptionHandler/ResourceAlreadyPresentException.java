package movieBookingapplication.ExceptionHandler;

public class ResourceAlreadyPresentException extends RuntimeException {
    public ResourceAlreadyPresentException(String message) {
        super(message);
    }
}
