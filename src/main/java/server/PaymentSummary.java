package server;

import java.time.LocalDateTime;

public class PaymentSummary {
    private final int totalContributed;
    private final LocalDateTime dateLastContributed;
    private final int monthlyPayment;
    private final int amountOutstanding;
    private final LocalDateTime nextPaymentDate;

    public PaymentSummary(int totalContributed, LocalDateTime dateLastContributed, int monthlyPayment, int amountOutstanding, LocalDateTime nextPaymentDate) {
        this.totalContributed = totalContributed;
        this.dateLastContributed = dateLastContributed;
        this.monthlyPayment = monthlyPayment;
        this.amountOutstanding = amountOutstanding;
        this.nextPaymentDate = nextPaymentDate;
    }

    public int getTotalContributed() {
        return totalContributed;
    }

    public String getDateLastContributed() {
        return dateLastContributed.toString();
    }

    public int getMonthlyPayment() {
        return monthlyPayment;
    }

    public int getAmountOutstanding() {
        return amountOutstanding;
    }

    public String getNextPaymentDate() {
        return nextPaymentDate.toString();
    }
}