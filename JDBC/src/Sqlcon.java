import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sqlcon 
{
static final String db_url="Jdbc:Mysql://localhost:3306";
static final String use="root";
static final String pass="Priyanka@2000";

    public static void main(String[] args) {
        try(Connection con=DriverManager.getConnection(db_url, use, pass); Statement stm=con.createStatement();)
        {
            ResultSet rs=null;
            //String sql="CREATE DATABASE MYDB";
           /*  String createTableSql = "CREATE TABLE users (" +
                    "id INT(11) NOT NULL AUTO_INCREMENT," +
                    "name VARCHAR(50) NOT NULL," +
                    "email VARCHAR(50) NOT NULL," +
                    "PRIMARY KEY (id)" +
                    ")";
            //stm.executeUpdate(sql);
            stm.executeUpdate(createTableSql);*/
            System.out.println("Table created ");
            String insertDataSql = "INSERT INTO users (name, email) VALUES " +
                    "('John roe', 'john@example.com')," +
                    "('Jane Smithwa', 'jane@example.com')";
                    stm.executeUpdate(insertDataSql);
                    // SQL query to retrieve data from the table
            String selectQuery = "SELECT * FROM users";

            // Execute the query
            rs = stm.executeQuery(selectQuery);

            // Print the table contents
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");

                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Email: " + email);
                System.out.println("-------------------");
            }
                    System.out.println("inserted table");


            System.out.println("Db ready");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        
    }
}