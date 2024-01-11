package antifraud.repository;

import antifraud.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByNumber(String number);

    boolean existsByNumber(String number);
}
