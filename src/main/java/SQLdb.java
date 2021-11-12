import com.mysql.cj.jdbc.Driver;

import java.sql.*;
import java.util.ArrayList;

public class SQLdb {
    public static String name = "";
    public static String val = "";
    private final static String url = "jdbc:mysql://localhost:3306/currencydb";
    private final static String user = "root";
    public static void updateDB(String date) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(url, user, "root")) {
                Statement statement = connection.createStatement();
                System.out.println("Connection succesfull...");
                ArrayList<Valute> bigArray = API.getAllCurrencyTest(date);
                statement.executeUpdate("CREATE TABLE `" + date + "` (`id` SMALLINT NOT NULL AUTO_INCREMENT, " +
                        "`valute_id` VARCHAR(45) NULL, `num_code` VARCHAR(45) NULL, `char_code` VARCHAR(45) NULL, " +
                        "`nominal` VARCHAR(45) NULL, `name` VARCHAR(45) NULL, `value` FLOAT NULL, PRIMARY KEY (`id`));");
                for (int i = 0; i < bigArray.size(); i++) {
                    Valute val =  bigArray.get(i);
                    statement.executeUpdate("INSERT INTO `" + date + "`(valute_id, num_code, char_code, nominal, " +
                            "name, value) VALUES ('"+val.id+"', '" + val.numcode + "', '" + val.charcode + "', '" +
                            val.nominal + "', '" + val.name + "', " + "0.5" + ");");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Connection failed...");
            System.out.println(e);
        }
    }
    public static void check(String date, String currency) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(url, user, "root")) {

                Statement statement = connection.createStatement();
                String dateForDB = date.replace("-", "/");
                String query = "SELECT name, nominal, value FROM `"+dateForDB+"` WHERE char_code = '"+currency+"';";
                ResultSet rs = statement.executeQuery(query);
                rs.next();
                System.out.println(rs.getString("name"));
                System.out.println(rs.getString("nominal") + rs.getString("name") + " = " + rs.getString("value") + " Российских рубля");
                System.out.println("Ошибки не было");
            } catch (Exception e) {
                System.out.println("В бд нет, выполняется запрос...");
                API.getResponse(date, currency);
        }
        } catch (Exception e) {
            System.out.println("Connection failed...");
            System.out.println(e);
        }
    }
}
