package projectmanager.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqlLiteSQLJDBC2 {

    public static void main(String[] args) {
        Connection c = null;
        Statement stmt = null;
        final String DBPath = SqlLiteSQLJDBC2.class.getClassLoader().getResource("sample.db").getPath();

        try {
            // Charge le driver JDBC de PostgreSQL
            Class.forName("org.sqlite.JDBC");
            
            // Établit la connexion à la base de données
            // Salma
            c = DriverManager
               .getConnection("jdbc:sqlite:" + DBPath);
            c.setAutoCommit(false);
            System.out.println("Connexion réussie à la base de données!");

            // Crée une instruction et exécute une requête
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Etudiant;");
            
            while (rs.next()) {
                int id = rs.getInt("ID_Etudiant");
                String nom = rs.getString("Nom");
                System.out.println("ID = " + id);
                System.out.println("NOM = " + nom);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opération réussie");
    }
}
