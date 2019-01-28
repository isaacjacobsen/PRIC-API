package server;

import java.sql.ResultSet;
import java.sql.Statement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;

@RestController
public class ApiController {

    @RequestMapping("/user")
    public User user(@RequestParam(value="username", defaultValue="") String username, @RequestParam(value="password", defaultValue="") String password) {

        int userId = -1;
        String email = null;
        String name = null;

        Connection c;
        Statement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/PricDB",
                            "postgres", "");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "" +
                            "SELECT " +
                            "      userid," +
                            "      username," +
                            "      email," +
                            "      name" +
                            "    FROM Users" +
                            "    WHERE" +
                            "      username='" + username + "'" +
                            "      AND password='" + password + "';"
            );


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
}