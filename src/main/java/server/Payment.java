package server;

import java.time.LocalDateTime;

public class Payment {
    private final LocalDateTime paymentDate;
    private final int amount;

    public Payment(LocalDateTime paymentDate, int amount) {
        this.paymentDate = paymentDate;
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public int getAmount() {
        return amount;
    }
}