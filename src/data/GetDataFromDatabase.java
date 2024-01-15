package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GetDataFromDatabase {
    List<User> data = new ArrayList<>();

    public GetDataFromDatabase() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        // Step 1: Loading or registering Oracle JDBC driver class
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        }
        catch(ClassNotFoundException cnfex) {
            System.out.println("Problem in loading or registering MS Access JDBC driver");
            cnfex.printStackTrace();
        }
        // Step 2: Opening database connection
        try {
            String msAccDB = "resources/databases/users.accdb";
            String dbURL = "jdbc:ucanaccess://" + msAccDB;
            // Step 2.A: Create and get connection using DriverManager class
            connection = DriverManager.getConnection(dbURL);
            // Step 2.B: Creating JDBC Statement
            statement = connection.createStatement();
            // Step 2.C: Executing SQL & retrieve data into ResultSet
            resultSet = statement.executeQuery("SELECT user.ID, user.FirstName, user.Surname, user.Password, permission.ViewDeaths, permission.ViewCases, permission.ViewPrediction, permission.addUser FROM user INNER JOIN permission ON user.ID = permission.ID;");
            // processing returned data
            while(resultSet.next()) {
                User newUser = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getBoolean(5), resultSet.getBoolean(6), resultSet.getBoolean(7), resultSet.getBoolean(8));
                data.add(newUser);
            }
        }
        catch(SQLException sqlex){
            sqlex.printStackTrace();
        }
        finally {
            // Step 3: Closing database connection
            try {
                if(null != connection) {
                    // cleanup resources, once after processing
                    assert resultSet != null;
                    resultSet.close();
                    statement.close();
                    // and then finally close connection
                    connection.close();
                }
            }
            catch (SQLException sqlex) {
                sqlex.printStackTrace();
            }
        }
    }

    public List<User> getData() {
        return data;
    }

    public void inputDataToDatabase(List<User> data){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet;
        int lastID = 0;
        // Step 1: Loading or registering Oracle JDBC driver class
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        }
        catch(ClassNotFoundException cnfex) {
            System.out.println("Problem in loading or registering MS Access JDBC driver");
            cnfex.printStackTrace();
        }
        // Step 2: Opening database connection
        try {
            String msAccDB = "resources/databases/users.accdb";
            String dbURL = "jdbc:ucanaccess://" + msAccDB;
            // Step 2.A: Create and get connection using DriverManager class
            connection = DriverManager.getConnection(dbURL);
            // Step 2.B: Creating JDBC Statement
            statement = connection.createStatement();
            // Step 2.C: Executing SQL & retrieve data into ResultSet
            resultSet = statement.executeQuery("SELECT user.ID, user.FirstName, user.Surname, user.Password, permission.ViewDeaths, permission.ViewCases, permission.ViewPrediction, permission.addUser FROM user INNER JOIN permission ON user.ID = permission.ID;");
            while(resultSet.next()) {
                lastID = resultSet.getInt(1);
            }
            for (User user : data){
                statement.executeUpdate("INSERT INTO user (ID, FirstName, Surname, Password) VALUES ('" + lastID++ + "', '" + user.getFirstName() + "', '" + user.getSurname() + "', '" + user.getPassword() + "');");
                statement.executeUpdate("INSERT INTO permission (ID, ViewDeaths, ViewCases, ViewPrediction, AddUser) VALUES ('" + lastID + "', '" + user.isViewDeaths() + "', '" + user.isViewCases() + "', '" + user.isViewPredictions() + "', '" + user.isAddUser() + "');");
            }
        }
        catch(SQLException sqlex){
            sqlex.printStackTrace();
        }
        finally {
            // Step 3: Closing database connection
            try {
                if(null != connection) {
                    // cleanup resources, once after processing
                    assert statement != null;
                    statement.close();
                    // and then finally close connection
                    connection.close();
                }
            }
            catch (SQLException sqlex) {
                sqlex.printStackTrace();
            }
        }
    }
}