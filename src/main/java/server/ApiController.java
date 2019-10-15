package server;

import java.io.IOException;
import java.io.InputStream;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.time.*;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
public class ApiController {

    private final String connectionURL = "jdbc:postgresql://localhost:5432/PricDB";

    @CrossOrigin
    @GetMapping("/users")
    @ResponseBody
    public User getUserByUsernamePassword(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        return DbAccessor.getUser(username, password);
    }

    @CrossOrigin
    @GetMapping("/users/{userId}/profilepicture")
    @ResponseBody
    public byte[] getImageAsByteArray(
            @PathVariable int userId) throws IOException {
        InputStream in = ApiController.class.getResourceAsStream("/images/" + userId + "_profile_picture.jpg");
        if (in == null) {
            in = ApiController.class.getResourceAsStream("/images/no_profile_pic.jpg");
        }
        return IOUtils.toByteArray(in);
    }

    @CrossOrigin
    @GetMapping("/users/{userId}/fees/paymentschedules")
    @ResponseBody
    public PaymentSchedule[] getUserPaymentSchedules(
            @PathVariable int userId) {

        return DbAccessor.getPaymentSchedules(userId).toArray(new PaymentSchedule[0]);
    }

    @CrossOrigin
    @GetMapping("/users/{userId}/fees/payments")
    @ResponseBody
    public Payment[] getUserPayments(
            @PathVariable int userId) {

        return DbAccessor.getPayments(userId).toArray(new Payment[0]);
    }

    @CrossOrigin
    @GetMapping("/users/{userId}/fees/paymentsummary")
    @ResponseBody
    public PaymentSummary getUserPaymentSummary(
            @PathVariable int userId) {

        List<PaymentSchedule> schedules = new ArrayList<>(DbAccessor.getPaymentSchedules(userId));
        List<Payment> payments = new ArrayList<>(DbAccessor.getPayments(userId));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thisMonthBegin = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);

        // Total Contribution
        int totalContribution = payments.stream().mapToInt(p -> p.getAmount()).sum();

        LocalDateTime dateLastContributed = payments.get(payments.size() - 1).getPaymentDateAsDate();
        LocalDateTime nextPaymentDate = thisMonthBegin.plus(1, ChronoUnit.MONTHS);
        int monthlyPayment = schedules.get(schedules.size() - 1).getMonthlyPayment();

        // Expected Contribution
        LocalDateTime startDate = schedules.get(schedules.size() - 1).getStartDateAsDate();
        LocalDateTime endDate = nextPaymentDate;
        int monthDiff = 0;
        while (startDate.isBefore(endDate)) {
            monthDiff++;
            startDate = startDate.plus(1, ChronoUnit.MONTHS);
        }

        int expectedContribution = (monthDiff * schedules.get(schedules.size() - 1).getMonthlyPayment());

        for (int i = 0; i < schedules.size() - 1; i++) {
            startDate = schedules.get(i).getStartDateAsDate();
            endDate = schedules.get(i+1).getStartDateAsDate();
            monthDiff = 0;
            while (startDate.isBefore(endDate)) {
                monthDiff++;
                startDate = startDate.plus(1, ChronoUnit.MONTHS);
            }

            expectedContribution += (monthDiff * schedules.get(i).getMonthlyPayment());
        }

        return new PaymentSummary(totalContribution, dateLastContributed, monthlyPayment, (expectedContribution - totalContribution), nextPaymentDate);
    }

    @CrossOrigin
    @GetMapping("/users/{userId}/fees/monthlybreakdown")
    @ResponseBody
    public FeeBreakdown[] getUserMonthlyBreakdown(
            @PathVariable int userId) {

        // Get the payment schedule for the user
        List<PaymentSchedule> schedules = new ArrayList<>(DbAccessor.getPaymentSchedules(userId));

        // Get the payments that have been made by the user
        List<Payment> payments = new ArrayList<>(DbAccessor.getPayments(userId));

        List<FeeBreakdown> monthlyFees = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginningOfThisMonth = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);

        int schedIndex = 0;
        int numSchedules = schedules.size();

        int payIndex = 0;
        int numPayments = payments.size();

        LocalDateTime xDate = schedules.get(0).getStartDateAsDate();

        // Keep getting monthly fees until we're past this month
        while (xDate.isBefore(beginningOfThisMonth) || xDate.isEqual(beginningOfThisMonth)) {

            // If we are in the last schedule, or we're still working with the current schedule (hasn't gone to the next schedule)
            if (((schedIndex + 1) >= numSchedules) || (xDate.isBefore(schedules.get(schedIndex + 1).getStartDateAsDate()))) {

                int thisMonthPaymentDue = schedules.get(schedIndex).getMonthlyPayment();
                int thisMonthPaid = 0;
                LocalDateTime datePaid = null;

                // If there is still monthly payments due and you haven't exhausted all the payments
                while ((thisMonthPaymentDue > 0) && payIndex < numPayments) {
                    if (datePaid == null) {
                        datePaid = payments.get(payIndex).getPaymentDateAsDate();
                    }

                    // Figure out which payment covers this month's payment
                    if (payments.get(payIndex).getAmount() == thisMonthPaymentDue) {
                        thisMonthPaid += thisMonthPaymentDue;
                        thisMonthPaymentDue = 0;
                        payments.get(payIndex).setAmount(0);
                        payIndex++;
                    } else if (payments.get(payIndex).getAmount() > thisMonthPaymentDue) {
                        thisMonthPaid += thisMonthPaymentDue;
                        payments.get(payIndex).setAmount(payments.get(payIndex).getAmount() - thisMonthPaymentDue);
                        thisMonthPaymentDue = 0;
                    } else {
                        thisMonthPaid += payments.get(payIndex).getAmount();
                        thisMonthPaymentDue -= payments.get(payIndex).getAmount();
                        payments.get(payIndex).setAmount(0);
                        payIndex++;
                    }
                }

                int daysLate = Math.max((int)ChronoUnit.DAYS.between(xDate, (datePaid != null ? datePaid : now)), 0);

                int thisMonthOutstanding = Math.max(thisMonthPaymentDue - thisMonthPaid, 0);

                monthlyFees.add(new FeeBreakdown(xDate, datePaid, daysLate, thisMonthPaid, thisMonthPaymentDue, thisMonthOutstanding));
                System.out.println(monthlyFees.get(monthlyFees.size()-1).toString());

                xDate = xDate.plus(1, ChronoUnit.MONTHS);
            } else {
                schedIndex++;
            }
        }

        return monthlyFees.toArray(new FeeBreakdown[0]);
    }
}