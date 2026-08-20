package movieBookingapplication.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class ExceptionsHandler {

    // Movie Not Found
    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMovieNotFoundException(
            MovieNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                new Date(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Resource Already Present
    @ExceptionHandler(ResourceAlreadyPresentException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyPresentException(
            ResourceAlreadyPresentException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                new Date(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                new Date(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Screen Not Found
    @ExceptionHandler(ScreenNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleScreenNotFoundException(
            ScreenNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                new Date(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }



    // Required Fields / OTP Validation Error
    @ExceptionHandler(RequiredFiledsExcetion.class)
    public ResponseEntity<ErrorResponse> handleRequiredFieldsException(
            RequiredFiledsExcetion ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                new Date(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}