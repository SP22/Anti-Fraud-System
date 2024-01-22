package antifraud.controller;

import antifraud.entity.Transaction;
import antifraud.entity.TransactionResponse;
import antifraud.service.CardService;
import antifraud.service.IpService;
import antifraud.service.TransactionService;
import antifraud.service.TransactionValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionValidator transactionValidator;

    @PostMapping(value = {"", "/"})
    public ResponseEntity<TransactionResponse> processTransaction(@RequestBody @Valid Transaction transaction) {
        TransactionResponse result = transactionValidator.validate(transaction);
        transactionService.save(transaction);

        return new ResponseEntity<>(new TransactionResponse(result.result(), result.info()), HttpStatus.OK);
    }

    @PutMapping(value= {"", "/"})
    public ResponseEntity<> addFeedback()
}
