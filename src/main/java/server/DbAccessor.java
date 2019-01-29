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
        String phone = null;
        String profilePicPath = null;
        String aboutMe = null;
        ArrayList<String> positionNames = new ArrayList<>();
        ArrayList<String> positionNamesShort = new ArrayList<>();
        boolean isOnBoard = false;

        String query = "" +
                "   SELECT" +
                "       agg.*," +
                "       UP.profilepicturepath," +
                "       UP.aboutme" +
                "   FROM" +
                "       (select" +
                "           U.userid," +
                "           U.username," +
                "           U.email," +
                "           U.name," +
                "           U.phone," +
                "           string_agg(PT.positionname, ';') AS positionnames," +
                "           string_agg(PT.positionnameshort, ';') AS positionnamesshort," +
                "           MAX(PT.isboardposition) AS isonboard" +
                "       FROM Users U" +
                "           LEFT JOIN Positions P on P.userid = U.userid" +
                "           LEFT JOIN PositionTypes PT on PT.positiontypeid = P.positiontypeid" +
                "       WHERE" +
                "           U.username = '" + username + "'" +
                "           AND U.password = '" + password + "'" +
                "           AND P.enddate IS NULL" +
                "           AND PT.positionname != 'Partner'" +
                "       GROUP BY" +
                "           U.userid) agg" +
                "   LEFT JOIN UserProfiles UP on UP.userid = agg.userid;";

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
                phone = rs.getString("phone");
                profilePicPath = rs.getString("profilepicturepath");
                aboutMe = rs.getString("aboutMe");
                String[] pos = rs.getString("positionnames").split(";");
                String[] posShort = rs.getString("positionnamesshort").split(";");

                for (int i = 0; i < pos.length; i++) {
                    positionNames.add(pos[i]);
                    positionNamesShort.add(posShort[i]);
                }

                isOnBoard = rs.getBoolean("isOnBoard");
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

        return new User(userId, username, name, email, phone, profilePicPath, aboutMe, positionNames, positionNamesShort, isOnBoard);
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
                "    FROM PaymentSchedules" +
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
