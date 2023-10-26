package eu.dauphine.idd.pm.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;
   // private String url = "jdbc:postgresql://localhost:5432/yourDatabase"; // Modifiez selon votre base de données
   // private String username = "yourUsername";
   // private String password = "yourPassword";
    //private static final String DB_PATH = DatabaseConnection.class.getClassLoader().getResource("sample.db").getPath();
    private static final String DB_PATH = "./src/main/resources/sample.db" ;

    private DatabaseConnection() throws SQLException {

        
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
         

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'initialisation de la connexion à la base de données SQLite.");
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
}
