package antifraud.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionResult {
    ALLOWED, MANUAL_PROCESSING, PROHIBITED
}
