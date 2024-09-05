import java.sql.Connection;
import java.sql.DriverManager;
// import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sqlcon 
{
static final String db_url="Jdbc:Mysql://localhost:3306/MYDB";
static final String use="root";
static final String pass="Priyanka@2000";

    public static void main(String[] args) {
        try(Connection con=DriverManager.getConnection(db_url, use, pass); Statement stm=con.createStatement();)
        {
           
        //     String sql="CREATE DATABASE MYDB";
        //    stm.executeUpdate(sql);
        String createTableSql = "CREATE TABLE users (" +
                    "id INT(11) NOT NULL AUTO_INCREMENT," +
                    "name VARCHAR(50) NOT NULL," +
                    "email VARCHAR(50) NOT NULL," +
                    "PRIMARY KEY (id)" +
                    ")";
                    stm.executeUpdate(createTableSql);      
            System.out.println("Db ready");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        
    }
}