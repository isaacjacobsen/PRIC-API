package server;

import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

public class FeeBreakdown {

    private final LocalDateTime dueDate;
    private final LocalDateTime datePaid;
    private final int daysLate;
    private final int amountPaid;
    private final int amountExpected;
    private final int amountOutstanding;

    public FeeBreakdown(LocalDateTime dueDate, LocalDateTime datePaid, int daysLate, int amountPaid, int amountExpected, int amountOutstanding) {
        this.dueDate = dueDate;
        this.datePaid = datePaid;
        this.daysLate = daysLate;
        this.amountPaid = amountPaid;
        this.amountExpected = amountExpected;
        this.amountOutstanding = amountOutstanding;
    }

    protected LocalDateTime getDueDateAsDate() {
        return dueDate;
    }

    public String getDueDate() {
        return dueDate.toString();
    }

    protected LocalDateTime getDatePaidAsDate() {
        return datePaid;
    }

    public String getDatePaid() {
        return datePaid.toString();
    }

    public int getDaysLate() {
        return daysLate;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public int getAmountExpected() {
        return amountExpected;
    }

    public int getAmountOutstanding() {
        return amountOutstanding;
    }
}
