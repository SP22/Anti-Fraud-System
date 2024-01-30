package antifraud.service;

import antifraud.entity.Setting;
import antifraud.entity.Transaction;
import antifraud.entity.TransactionResponse;
import antifraud.entity.TransactionResult;
import antifraud.exceptions.FeedbackException;
import antifraud.exceptions.UnprocessableEntityException;
import antifraud.repository.SettingRepository;
import jakarta.annotation.PostConstruct;
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
    private SettingRepository settingRepository;

    private double manualThreshold = 200;
    private double prohibitedThreshold = 1500;

    public TransactionResponse validateTransaction(Transaction transaction, int ipCount, int regionCount) {
        long amount = transaction.getAmount();
        List<String> reasons = new ArrayList<>(3);
        TransactionResult result = null;

        if (ipService.existsByIp(transaction.getIp())) {
            reasons.add("ip");
            result = TransactionResult.PROHIBITED;
        }

        if (cardService.existsByNumber(transaction.getNumber())) {
            reasons.add("card-number");
            result = TransactionResult.PROHIBITED;
        }

        if (ipCount > 2) {
            result = TransactionResult.PROHIBITED;
            reasons.add("ip-correlation");
        } else if (result == null && ipCount == 2) {
            result = TransactionResult.MANUAL_PROCESSING;
            reasons.add("ip-correlation");
        }

        if (regionCount > 2) {
            result = TransactionResult.PROHIBITED;
            reasons.add("region-correlation");
        } else if ((result == null || result.equals(TransactionResult.MANUAL_PROCESSING)) && regionCount == 2) {
            result = TransactionResult.MANUAL_PROCESSING;
            reasons.add("region-correlation");
        }

        TransactionResponse response;
        if (amount <= manualThreshold && reasons.isEmpty()) {
            result = TransactionResult.ALLOWED;
        } else if (amount <= prohibitedThreshold && reasons.isEmpty()) {
            reasons.add("amount");
            result = TransactionResult.MANUAL_PROCESSING;
        } else if (amount > prohibitedThreshold) {
            reasons.add("amount");
            result = TransactionResult.PROHIBITED;
        }

        if (reasons.isEmpty()) {
            reasons.add("none");
        }

        Collections.sort(reasons);

        return new TransactionResponse(result, String.join(", ", reasons));
    }

    public Transaction validateFeedback(Transaction transaction, TransactionResult feedback) {
        if (transaction.getResult().equals(feedback)) {
            throw new UnprocessableEntityException();
        }
        if (!transaction.getFeedback().isEmpty()) {
            throw new FeedbackException();
        }
        switch (feedback) {
            case ALLOWED -> {
                changeManualThreshold(transaction.getAmount(), 1);
                if (transaction.getResult().equals(TransactionResult.PROHIBITED)) {
                    changeProhibitedThreshold(transaction.getAmount(), 1);
                }
            }
            case MANUAL_PROCESSING -> {
                if (transaction.getResult().equals(TransactionResult.ALLOWED)) {
                    changeManualThreshold(transaction.getAmount(), -1);
                } else {
                    changeProhibitedThreshold(transaction.getAmount(), 1);
                }
            }
            case PROHIBITED -> {
                changeProhibitedThreshold(transaction.getAmount(), -1);
                if (transaction.getResult().equals(TransactionResult.ALLOWED)) {
                    changeManualThreshold(transaction.getAmount(), -1);
                }
            }
        }
        transaction.setFeedback(feedback.toString());
        return transaction;
    }

    private void changeManualThreshold(long amount, int signum) {
        manualThreshold = correlateThreshold(manualThreshold, amount, signum);
        settingRepository.save(new Setting("manualThreshold", Double.toString(manualThreshold)));
    }

    private void changeProhibitedThreshold(long amount, int signum) {
        prohibitedThreshold = correlateThreshold(prohibitedThreshold, amount, signum);
        settingRepository.save(new Setting("prohibitedThreshold", Double.toString(prohibitedThreshold)));
    }

    private double correlateThreshold(double limit, long amount, int signum) {
        return Math.ceil(0.8 * limit + 0.2 * signum * amount);
    }

    @PostConstruct
    private void getLimits() {
        manualThreshold = Double.parseDouble(settingRepository.findById("manualThreshold").orElse(new Setting("manualThreshold", "200")).getSettingValue());
        prohibitedThreshold = Double.parseDouble(settingRepository.findById("prohibitedThreshold").orElse(new Setting("prohibitedThreshold", "1500")).getSettingValue());
    }
}
