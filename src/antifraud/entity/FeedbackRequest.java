package antifraud.entity;

public record FeedbackRequest(Long transactionId, TransactionResult feedback) {}
