import java.sql.Connection;
import java.sql.DriverManager;
// import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sqlcon 
{
static final String db_url="Jdbc:Mysql://localhost:3306/bbsr_usbm";
static final String use="root";
static final String pass="Priyanka@2000";

    public static void main(String[] args) {
        try(Connection con=DriverManager.getConnection(db_url, use, pass); Statement stm=con.createStatement();)
        {
           
        //     String sql="CREATE DATABASE bbsr_usbm";
        //    stm.executeUpdate(sql);
        // String createTableSql = "CREATE TABLE MCA1 (" +
        //             "std_id INT(11) NOT NULL AUTO_INCREMENT," +
        //             "std_name VARCHAR(50) NOT NULL," +
        //             "std_marks INT(11) NOT NULL," +
        //             "std_email VARCHAR(50) NOT NULL," +
        //             "PRIMARY KEY (std_id)" +
        //             ")";
        //             stm.executeUpdate(createTableSql);      
            
            String insertDataSql = "INSERT INTO MCA1 (std_name,std_marks,std_email) VALUES " +
            "('John ','200','john@example.com')," +
            "('Jane ','201','jane@example.com')," +
            "('Jane ','202','jane@example.com')," +
            "('Jane ','203','jane@example.com')," +
            "('Jane ','204','jane@example.com')";
            stm.executeUpdate(insertDataSql);
            System.out.println("Db ready");


        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        
    }
}