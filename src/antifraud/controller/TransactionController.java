package antifraud.controller;

import antifraud.service.CardService;
import antifraud.service.IpService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/antifraud/transaction")
@Validated
public class TransactionController {

    @Autowired
    private CardService cardService;
    @Autowired
    private IpService ipService;

    @PostMapping(value = {"", "/"})
    public ResponseEntity<TransactionResponse> processTransaction(@RequestBody @Valid TransactionRequest request) {
        long amount = request.amount();
        List<String> reasons = new ArrayList<>(3);
        String result = "";

        if (ipService.existsByIp(request.ip())) {
            reasons.add("ip");
            result = "PROHIBITED";
        }

        if (cardService.existsByNumber(request.number())) {
            reasons.add("card-number");
            result = "PROHIBITED";
        }

        TransactionResponse response;
        if (amount <= 200 && reasons.isEmpty()) {
            result = "ALLOWED";
        } else if (amount <= 1500 && reasons.isEmpty()) {
            reasons.add("amount");
            result = "MANUAL_PROCESSING";
        } else if (amount > 1500) {
            reasons.add("amount");
            result = "PROHIBITED";
        }

        if (reasons.isEmpty()) {
            reasons.add("none");
        }

        Collections.sort(reasons);
        return new ResponseEntity<>(new TransactionResponse(result, String.join(", ", reasons)), HttpStatus.OK);
    }
}

record TransactionRequest(@Positive long amount, @NotBlank String ip, @NotBlank String number) {}

record TransactionResponse(String result, String info) {}
