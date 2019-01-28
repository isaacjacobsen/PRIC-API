package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class DbAccessor {

    private static final String connectionURL = "jdbc:postgresql://localhost:5432/PricDB";
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DbAccessor() {

    }

    public static User getUser(String username, String password) {
        int userId = -1;
        String email = null;
        String name = null;

        String query = "" +
                "SELECT " +
                "      userid," +
                "      username," +
                "      email," +
                "      name" +
                "    FROM Users" +
                "    WHERE" +
                "      username='" + username + "'" +
                "      AND password='" + password + "';";

        Connection c;
        Statement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(connectionURL, "postgres", "");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);


            if (rs.next()) {
                userId = rs.getInt("userid");
                email = rs.getString("email");
                name = rs.getString("name");
            }

            if (rs.next()) {
                System.err.println("More than 1 user!");
                System.exit(0);
            }

            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");

        return new User(userId, username, name, email);
    }

    public static List<Payment> getPayments(int userId) {
        List<Payment> payments = new ArrayList<>();

        String query = "" +
                "   SELECT" +
                "       amount," +
                "       paymentdate" +
                "   FROM Payments" +
                "   WHERE" +
                "       userid=" + userId +
                "   ORDER BY" +
                "       paymentdate ASC;";

        Connection c;
        Statement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(connectionURL, "postgres", "");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                LocalDateTime paymentDate = LocalDateTime.parse(rs.getString("paymentdate"), dateFormatter);
                int amount = rs.getInt("amount");
                payments.add(new Payment(paymentDate, amount));

                System.out.println("Payment date = " + paymentDate + "\tAmount = $" + amount);
            }


            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");

        return payments;
    }

    public static List<PaymentSchedule> getPaymentSchedules(int userId) {
        List<PaymentSchedule> schedules = new ArrayList<>();

        String query = "" +
                "SELECT" +
                "      startdate," +
                "      monthlypayment" +
                "    FROM PaymentSchedule" +
                "    WHERE" +
                "      userid=" + userId +
                "    ORDER BY\n" +
                "      startdate ASC;";

        Connection c;
        Statement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(connectionURL, "postgres", "");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                LocalDateTime startDate = LocalDateTime.parse(rs.getString("startdate"), dateFormatter);
                int monthlyPayment = rs.getInt("monthlyPayment");
                schedules.add(new PaymentSchedule(startDate, monthlyPayment));

                System.out.println("Start date = " + startDate);
                System.out.println("Monthly Payment = $" + monthlyPayment);
            }


            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");

        return schedules;
    }

}
