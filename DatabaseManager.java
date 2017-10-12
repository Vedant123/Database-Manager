import java.security.spec.ECField;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Vedant on 9/29/17.
 */
public class DatabaseManager {
    private ArrayList<ArrayList<Object>> databaseMatrix;    //2 dimensional ArrayList to store all elements of the table

    private String databaseName;    //keeps track of the Database's name

    public DatabaseManager() {
        this("");
    }

    //an alternative constructor to the class that takes in the Database's name directly as a parameter s
    public DatabaseManager(String databaseName) {
        databaseMatrix = new ArrayList<>();

        this.databaseName = databaseName;

        try {
            Class.forName("com.mysql.jdbc.Driver"); //loads, if not already, the MYSQL Driver class
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    //setters and getters for the 'databaseName' variable

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    //function that reads in from the desired Database and loads it onto the 'databaseMatrix' variable
    public ArrayList<ArrayList<Object>> getDatabaseMatrix(String tableName) {
        try {
            //establishes a connection to the Database
            Connection con;
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, "root", "root");    //connects and authenticates into the desired Database

            Statement stmt;
            stmt = con.createStatement();

            ResultSet rs;
            rs = stmt.executeQuery("SELECT * from " + tableName);   //picks up all values in the specified table

            //keeps track of the ResultSet's metadata so that we can determine the size of the table
            ResultSetMetaData rsmd;
            rsmd = rs.getMetaData();

            while(rs.next()) {  //iterates through each row of the specified table
                ArrayList<Object> currentRow;
                currentRow = new ArrayList<>();

                for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                    currentRow.add(rs.getObject(i));    //casts the table elements into Objects, to then be stored in the 'currentRow' ArrayList
                }

                databaseMatrix.add(currentRow); //appends the current row into the databaseMatrix for later reference by the user
            }
        }catch (Exception e) {  //handles any Exceptions caught during the program
            e.printStackTrace();
        }

        return databaseMatrix;  //returns the matrix of the desired table back to the user
    }

    //function that calls the local function 'runSQL' to add a row to a specified table
    public void addToDatabase(ArrayList<Object> rowList, String tableName) {
        runSQL(generateInsertSQL(rowList, tableName));
    }

    //fuction that again calls the 'runSQL' function to update the values of a specified row within a table
    public void updateDatabaseRow(ArrayList<Object> rowList, String tableName, int id) {
        runSQL(generateUpdateSQL(rowList, tableName, id));
    }

    //function that runs an SQL statement inputted as a String
    public void runSQL(String sqlStatement) {
        try {
            //establishes a connection to the desired Database, and authenticates the root admin
            Connection connection;
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, "root", "root");

            //initializes the 'statement' variable that will be used to run the desired SQL statement
            Statement statement;
            statement = connection.createStatement();

            statement.executeUpdate(sqlStatement);
        }catch (Exception e) {  //handles any Exceptions thrown during the program
            e.printStackTrace();
        }
    }

    //formats a SQL statement to add a row to a desired table
    public String generateInsertSQL(ArrayList<Object> rowList, String tableName) {  //takes in the two parameters
        String retSQL;
        retSQL = "INSERT INTO `" + databaseName + "`.`" + tableName + "` VALUES (";

        for(Object itemVal: rowList) {  //iterates through each Object in the 'rowList'
            //if the Object is an instance of a String, it will need to be wrapped with quotation marks
            if(itemVal instanceof String) {
                retSQL += "'" + itemVal + "', ";
            }else {
                retSQL += itemVal + ", ";
            }
        }

        retSQL = retSQL.substring(0, retSQL.lastIndexOf(", ")); //removes the excess ', ' from the loop

        retSQL += ")";

        return retSQL;  //returns the formatted SQL code
    }

    //formats a SQL statement to update any desired row within a table
    public String generateUpdateSQL(ArrayList<Object> rowList, ArrayList<String> fieldsList, String tableName, int id) {
        String retSQL;
        retSQL = "UPDATE `" + databaseName + "`.`" + tableName + "` SET ";

        int index;
        index = 0;

        ////formats each Object in the 'rowList' ArrayList into an SQL statement to update the desired row

        for(String name: fieldsList) {
            retSQL += "`" + name + "=`'" + rowList.get(index) + "', ";

            index++;
        }

        retSQL = retSQL.substring(0, retSQL.lastIndexOf(", "));

        return retSQL;  //relays the formatted SQL statement back to the user
    }

}
