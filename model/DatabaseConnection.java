package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/inventory_management";
     private static final String USER = "root"; 
     private static final String PASSWORD = "sct221-0275/2022";

     private static Connection connection;

     public static Connection getConnection() throws SQLException{
        if(connection == null || connection.isClosed() ){
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
     }
      
     public static void closedConnection() throws SQLException{
        if(connection != null && !connection.isClosed()){
            connection.close();
        }
     }
    }
