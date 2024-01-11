package antifraud.service;

import antifraud.entity.Card;
import antifraud.exceptions.DuplicateEntityException;
import antifraud.exceptions.EntityNotFoundException;
import antifraud.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;
    public Card create(Card card) {
        if (null == cardRepository.findByNumber(card.getNumber())) {
            return cardRepository.save(card);
        }
        throw new DuplicateEntityException("Card already exists");
    }

    public List<Card> getCardList() {
        return cardRepository.findAll();
    }

    public void delete(String number) {
        Card entity = cardRepository.findByNumber(number);
        if (null == entity) {
            throw new EntityNotFoundException();
        }
        cardRepository.delete(entity);
    }

    public boolean existsByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }
}
