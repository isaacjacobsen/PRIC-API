package server;

import java.time.LocalDateTime;

public class Payment {
    private final LocalDateTime paymentDate;
    private final int amount;

    public Payment(LocalDateTime paymentDate, int amount) {
        this.paymentDate = paymentDate;
        this.amount = amount;
    }

    protected LocalDateTime getPaymentDateAsDate() {
        return paymentDate;
    }

    public String getPaymentDate() {
        return paymentDate.toString();
    }

    public int getAmount() {
        return amount;
    }
}