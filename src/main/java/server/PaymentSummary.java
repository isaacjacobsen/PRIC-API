package server;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("TotalContributed")
    public int getTotalContributed() {
        return totalContributed;
    }

    @JsonProperty("DateLastContributed")
    public String getDateLastContributed() {
        return dateLastContributed.toString();
    }

    @JsonProperty("MonthlyPayment")
    public int getMonthlyPayment() {
        return monthlyPayment;
    }

    @JsonProperty("AmountOutstanding")
    public int getAmountOutstanding() {
        return amountOutstanding;
    }

    @JsonProperty("NextPaymentDate")
    public String getNextPaymentDate() {
        return nextPaymentDate.toString();
    }
}