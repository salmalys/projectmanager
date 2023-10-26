package eu.dauphine.idd.pm.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import eu.dauphine.idd.pm.model.*;

public class SqlLiteSQLJDBC2 {
    private static Connection connection;
    private static final String DB_PATH = SqlLiteSQLJDBC2.class.getClassLoader().getResource("sample.db").getPath();

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'initialisation de la connexion à la base de données SQLite.");
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insert(List<Object> objects) {
        try {
            connection.setAutoCommit(false);

//            for (Object obj : objects) {
//                if (obj instanceof Formation) {
//                    // Adapt the following lines according to your SQLite data model
//                    // FormationSQLiteDAO formationDAO = new FormationSQLiteDAO(connection);
//                    // formationDAO.insert((Formation) obj);
//                } else if (obj instanceof Etudiant) {
//                    // EtudiantSQLiteDAO etudiantDAO = new EtudiantSQLiteDAO(connection);
//                    // etudiantDAO.insert((Etudiant) obj);
//                } else if (obj instanceof Note) {
//                    // NoteSQLiteDAO noteDAO = new NoteSQLiteDAO(connection);
//                    // noteDAO.insert((Note) obj);
//                } else if (obj instanceof Projet) {
//                    // ProjetSQLiteDAO projetDAO = new ProjetSQLiteDAO(connection);
//                    // projetDAO.insert((Projet) obj);
//                } else if (obj instanceof Binome) {
//                    // BinomeSQLiteDAO binomeDAO = new BinomeSQLiteDAO(connection);
//                    // binomeDAO.insert((Binome) obj);
//                } else {
//                    throw new IllegalArgumentException("Type d'objet non pris en charge pour l'insertion.");
//                }
//            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Similar changes can be made for the delete method based on your data model
}
