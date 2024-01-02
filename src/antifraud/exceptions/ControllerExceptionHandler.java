package antifraud.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Void> handleDuplicateUserRegistration() {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFoundException() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnsupportedRoleException.class)
    public ResponseEntity<Void> handleUnsupportedRoleException() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
