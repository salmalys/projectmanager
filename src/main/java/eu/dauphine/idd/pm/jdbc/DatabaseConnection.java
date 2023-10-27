package eu.dauphine.idd.pm.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    //private static final String DB_PATH = DatabaseConnection.class.getClassLoader().getResource("sample.db").getPath();
    private static final String DB_PATH = "./src/main/resources/sample.db" ;

    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            //connection.setAutoCommit(true);
         
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error : Connexion to DataBase SQLite failed.");
        }
    }
        
    public Connection getConnection() {
        return connection;
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public static void closeConnection() throws SQLException {
        try {
            if (instance != null && !instance.connection.isClosed()) {
                instance.connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
