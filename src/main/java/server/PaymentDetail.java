package server;

import java.time.LocalDateTime;

public class PaymentDetail {
    private final int totalContributed;
    private final LocalDateTime dateLastContributed;
    private final int monthlyPayment;
    private final int amountOutstanding;
    private final LocalDateTime nextPaymentDate;
    private final PaymentSchedule[] paymentSchedules;
    private final Payment[] payments;

    public PaymentDetail(int totalContributed, LocalDateTime dateLastContributed, int monthlyPayment, int amountOutstanding, LocalDateTime nextPaymentDate, PaymentSchedule[] paymentSchedules, Payment[] payments) {
        this.totalContributed = totalContributed;
        this.dateLastContributed = dateLastContributed;
        this.monthlyPayment = monthlyPayment;
        this.amountOutstanding = amountOutstanding;
        this.nextPaymentDate = nextPaymentDate;
        this.paymentSchedules = paymentSchedules;
        this.payments = payments;
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

    public PaymentSchedule[] getPaymentSchedules() {
        return paymentSchedules;
    }

    public Payment[] getPayments() {
        return payments;
    }
}