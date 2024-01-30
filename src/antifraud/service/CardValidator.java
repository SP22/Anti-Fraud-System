package antifraud.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardValidator {
    public boolean isValid(String number) {
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
