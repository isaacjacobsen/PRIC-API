package server;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.time.*;
import org.springframework.web.bind.annotation.*;


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
    @GetMapping("/users/{userId}/paymentschedule")
    @ResponseBody
    public PaymentSchedule[] getUserPaymentSchedules(
            @PathVariable int userId) {

        return DbAccessor.getPaymentSchedules(userId).toArray(new PaymentSchedule[0]);
    }

    @CrossOrigin
    @GetMapping("/users/{userId}/payments")
    @ResponseBody
    public PaymentDetail getUserPaymentDetail(
            @PathVariable int userId) {

        List<PaymentSchedule> schedules = new ArrayList<>(DbAccessor.getPaymentSchedules(userId));
        List<Payment> payments = new ArrayList<>(DbAccessor.getPayments(userId));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginningOfThisMonth = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);

        // Total Contribution
        int totalContribution = 0;
        for (Payment p : payments) {
            totalContribution += p.getAmount();
        }


        LocalDateTime dateLastContributed = payments.get(payments.size() - 1).getPaymentDateAsDate();
        LocalDateTime nextPaymentDate = beginningOfThisMonth.plus(1, ChronoUnit.MONTHS);
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

        return new PaymentDetail(totalContribution, dateLastContributed, monthlyPayment, (expectedContribution - totalContribution), nextPaymentDate, schedules.toArray(new PaymentSchedule[0]), payments.toArray(new Payment[0]));
    }
}