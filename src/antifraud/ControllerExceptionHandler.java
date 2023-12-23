package antifraud;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Void> handleNullPointer() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
