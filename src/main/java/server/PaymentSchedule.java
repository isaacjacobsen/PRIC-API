package server;

import java.time.LocalDateTime;

public class PaymentSchedule {

    private final LocalDateTime startDate;
    private final int monthlyPayment;

    public PaymentSchedule(LocalDateTime startDate, int monthlyPayment) {
        this.startDate = startDate;
        this.monthlyPayment = monthlyPayment;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public int getMonthlyPayment() {
        return monthlyPayment;
    }
}
