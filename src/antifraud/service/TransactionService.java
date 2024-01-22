package antifraud.service;

import antifraud.entity.Transaction;
import antifraud.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
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
}
