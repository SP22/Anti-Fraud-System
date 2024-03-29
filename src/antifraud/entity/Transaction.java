package antifraud.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionId;

    @Positive
    private long amount;

    @NotBlank
    private String ip;

    @NotBlank
    private String number;

    private Region region;

    private LocalDateTime date;

    @Enumerated(value = EnumType.STRING)
    private TransactionResult result;

    @Nullable
    private String feedback = "";
}
