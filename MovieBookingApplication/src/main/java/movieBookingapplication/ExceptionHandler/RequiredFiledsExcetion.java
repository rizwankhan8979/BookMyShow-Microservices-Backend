package movieBookingapplication.ExceptionHandler;

public class RequiredFiledsExcetion extends RuntimeException {
    public RequiredFiledsExcetion(String message) {
        super(message);
    }
}
