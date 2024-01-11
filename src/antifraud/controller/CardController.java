package antifraud.controller;

import antifraud.entity.Card;
import antifraud.exceptions.InvalidCardFormatException;
import antifraud.service.CardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/antifraud/stolencard")
@Validated
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping(value = {"/", ""})
    public ResponseEntity<Card> addCard(@RequestBody @Valid CardRequest cardRequest) {
        Card card = new Card();
        card.setNumber(cardRequest.number());
        if (!isValid(card.getNumber())) {
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
        if (!isValid(number)) {
            throw new InvalidCardFormatException();
        }
        cardService.delete(number);
        return ResponseEntity.ok(Map.of("status", String.format("Card %s successfully removed!", number)));
    }

    private boolean isValid(String number) {
        List<Integer> digits = new ArrayList<>(number.length());
        for (String digit : number.split("")) {
            digits.add(Integer.parseInt(digit));
        }

        int checksum = digits.get(digits.size() - 1);
        int sum = 0;

        digits.set(digits.size() - 1, 0);
        for (int i = 0; i < digits.size(); i++) {
            if (i % 2 == 0) {
                digits.set(i, digits.get(i) * 2);
            }
            if (digits.get(i) > 9) {
                digits.set(i, digits.get(i) - 9);
            }

            sum += digits.get(i);
        }
        return (sum + checksum) % 10 == 0;
    }
}

record CardRequest(@NotBlank String number) {}
