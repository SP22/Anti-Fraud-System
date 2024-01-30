package antifraud.controller;

import antifraud.entity.Card;
import antifraud.exceptions.InvalidCardFormatException;
import antifraud.service.CardService;
import antifraud.service.CardValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/antifraud/stolencard")
@Validated
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private CardValidator cardValidator;

    @PostMapping(value = {"/", ""})
    public ResponseEntity<Card> addCard(@RequestBody @Valid CardRequest cardRequest) {
        Card card = new Card();
        card.setNumber(cardRequest.number());
        if (!cardValidator.isValid(card.getNumber())) {
            throw new InvalidCardFormatException();
        }
        card = cardService.create(card);
        return ResponseEntity.ok(card);
    }

    @GetMapping(value = {"", "/"})
    public List<Card> getCards() {
        return cardService.getCardList();
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<Map<String, String>> deleteCard(@PathVariable("number") String number) {
        if (!cardValidator.isValid(number)) {
            throw new InvalidCardFormatException();
        }
        cardService.delete(number);
        return ResponseEntity.ok(Map.of("status", String.format("Card %s successfully removed!", number)));
    }
}

record CardRequest(@NotBlank String number) {}
