package antifraud.repository;

import antifraud.entity.Region;
import antifraud.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT count(distinct u.ip) FROM Transaction u WHERE u.number = ?1 and u.ip <> ?2 and u.date > ?3 and u.date <= ?4")
    Integer countIps(String number, String ip, LocalDateTime date, LocalDateTime transactionDate);

    @Query("SELECT count(distinct u.region) FROM Transaction u WHERE u.number = ?1 and u.region <> ?2 and u.date > ?3 and u.date <= ?4")
    Integer countRegions(String number, Region region, LocalDateTime date, LocalDateTime transactionDate);

    List<Transaction> findAllByNumber(String number);
}
