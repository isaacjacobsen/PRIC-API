package server;

import java.time.LocalDateTime;

public class PaymentSchedule {

    private final LocalDateTime startDate;
    private final int monthlyPayment;

    public PaymentSchedule(LocalDateTime startDate, int monthlyPayment) {
        this.startDate = startDate;
        this.monthlyPayment = monthlyPayment;
    }

    protected LocalDateTime getStartDateAsDate() {
        return startDate;
    }

    public String getStartDate() {
        return startDate.toString();
    }

    public int getMonthlyPayment() {
        return monthlyPayment;
    }
}
