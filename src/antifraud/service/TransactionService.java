package antifraud.service;

import antifraud.entity.FeedbackRequest;
import antifraud.entity.Transaction;
import antifraud.entity.TransactionResponse;
import antifraud.exceptions.EntityNotFoundException;
import antifraud.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionValidator transactionValidator;

    public TransactionResponse save(Transaction transaction) {
        TransactionResponse result = transactionValidator.validateTransaction(transaction, countIps(transaction), countRegions(transaction));
        transaction.setResult(result.result());
        transactionRepository.save(transaction);
        return result;
    }

    public int countIps(Transaction transaction) {
        return transactionRepository.countIps(
                transaction.getNumber(),
                transaction.getIp(),
                transaction.getDate().minusHours(1),
                transaction.getDate()
        );
    }

    public int countRegions(Transaction transaction) {
        return transactionRepository.countRegions(
                transaction.getNumber(),
                transaction.getRegion(),
                transaction.getDate().minusHours(1),
                transaction.getDate()
        );
    }

    public Transaction provideFeedback(FeedbackRequest feedbackRequest) {
        return null;
    }

    public List<Transaction> getHistory() {
        return transactionRepository.findAll();
    }


    public List<Transaction> getNumberHistory(String number) {
        List<Transaction> transactions = transactionRepository.findAllByNumber(number);
        if (transactions.isEmpty()) {
            throw new EntityNotFoundException();
        } else {
            return transactions;
        }
    }

    public Transaction addFeedback(FeedbackRequest feedbackRequest) {
        Transaction transaction = transactionRepository.findById(feedbackRequest.transactionId()).orElseThrow(EntityNotFoundException::new);
        transaction = transactionValidator.validateFeedback(transaction, feedbackRequest.feedback());
        return transactionRepository.save(transaction);
    }
}
