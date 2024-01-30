package antifraud.controller;

import antifraud.entity.FeedbackRequest;
import antifraud.entity.Transaction;
import antifraud.entity.TransactionResponse;
import antifraud.exceptions.InvalidCardFormatException;
import antifraud.service.CardService;
import antifraud.service.CardValidator;
import antifraud.service.IpService;
import antifraud.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud")
@Validated
public class TransactionController {

    @Autowired
    private CardService cardService;

    @Autowired
    private IpService ipService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CardValidator cardValidator;

    @PostMapping(value = {"/transaction", "/transaction/"})
    public ResponseEntity<TransactionResponse> processTransaction(@RequestBody @Valid Transaction transaction) {
        TransactionResponse result = transactionService.save(transaction);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = {"/transaction","/transaction/"})
    public Transaction processFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        return transactionService.addFeedback(feedbackRequest);
    }

    @GetMapping(value = {"/history", "/history/"})
    public List<Transaction> getTransactionHistory() {
        return transactionService.getHistory();
    }

    @GetMapping(value = "/history/{number}")
    public List<Transaction> getNumberHistory(@PathVariable String number) {
        if (!cardValidator.isValid(number)) {
            throw new InvalidCardFormatException();
        }
        return transactionService.getNumberHistory(number);
    }
}
