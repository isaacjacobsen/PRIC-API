package server;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Payment {
    private final LocalDateTime paymentDate;
    private int amount;

    public Payment(LocalDateTime paymentDate, int amount) {
        this.paymentDate = paymentDate;
        this.amount = amount;
    }

    protected LocalDateTime getPaymentDateAsDate() {
        return paymentDate;
    }

    @JsonProperty("paymentDate")
    public String getPaymentDate() {
        return paymentDate.toString();
    }

    @JsonProperty("amount")
    public int getAmount() {
        return amount;
    }

    protected void setAmount(int amount) {
        this.amount = amount;
    }
}