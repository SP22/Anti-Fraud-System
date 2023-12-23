package antifraud.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/antifraud")
@Validated
public class Controller {
    @PostMapping("/transaction")
    public ResponseEntity<TransactionResponse> processTransaction(@RequestBody @Valid TransactionRequest request) {
        long amount = request.amount();
        String status;
        if (amount <= 200) {
            status = "ALLOWED";
        } else if (amount <= 1500) {
            status = "MANUAL_PROCESSING";
        } else {
            status = "PROHIBITED";
        }
        return new ResponseEntity<>(new TransactionResponse(status), HttpStatus.OK);
    }
}

record TransactionRequest(@Positive long amount) {}

record TransactionResponse(String result) {}