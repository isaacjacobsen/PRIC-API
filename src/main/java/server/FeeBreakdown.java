package server;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("DueDate")
    public String getDueDate() {
        return dueDate.toString();
    }

    protected LocalDateTime getDatePaidAsDate() {
        return datePaid;
    }

    @JsonProperty("DatePaid")
    public String getDatePaid() {
        if (datePaid == null) {
            return "null";
        }
        return datePaid.toString();
    }

    @JsonProperty("DaysLate")
    public int getDaysLate() {
        return daysLate;
    }

    @JsonProperty("AmountPaid")
    public int getAmountPaid() {
        return amountPaid;
    }

    @JsonProperty("AmountExpected")
    public int getAmountExpected() {
        return amountExpected;
    }

    @JsonProperty("AmountOutstanding")
    public int getAmountOutstanding() {
        return amountOutstanding;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fee Breakdown:\n");
        sb.append("  Due Date = " + dueDate.toString() + "\n");
        sb.append("  Date Paid = " + (datePaid != null ? datePaid.toString() : "Unpaid") + "\n");
        sb.append("  Days Late = " + daysLate + "\n");
        sb.append("  Amount Paid = " + amountPaid + "\n");
        sb.append("  Amount Expected = " + amountExpected + "\n");
        sb.append("  Amount Outstanding = " + amountOutstanding + "\n");

        return new String(sb);
    }
}
