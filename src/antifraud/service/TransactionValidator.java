package antifraud.service;

import antifraud.entity.Transaction;
import antifraud.entity.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TransactionValidator {

    @Autowired
    private IpService ipService;

    @Autowired
    private CardService cardService;

    @Autowired
    private TransactionService transactionService;

    public TransactionResponse validate(Transaction transaction) {
        long amount = transaction.getAmount();
        List<String> reasons = new ArrayList<>(3);
        String result = "";

        if (ipService.existsByIp(transaction.getIp())) {
            reasons.add("ip");
            result = "PROHIBITED";
        }

        if (cardService.existsByNumber(transaction.getNumber())) {
            reasons.add("card-number");
            result = "PROHIBITED";
        }

        if (transactionService.countIps(transaction) > 2) {
            result = "PROHIBITED";
            reasons.add("ip-correlation");
        } else if (result.isEmpty() && transactionService.countIps(transaction) == 2) {
            result = "MANUAL_PROCESSING";
            reasons.add("ip-correlation");
        }

        if (transactionService.countRegions(transaction) > 2) {
            result = "PROHIBITED";
            reasons.add("region-correlation");
        } else if ((result.isEmpty() || result.equals("MANUAL_PROCESSING")) && transactionService.countRegions(transaction) == 2) {
            result = "MANUAL_PROCESSING";
            reasons.add("region-correlation");
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

        return new TransactionResponse(result, String.join(", ", reasons));
    }
}
