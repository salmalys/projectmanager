package eu.dauphine.idd.projectmanager.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eu.dauphine.idd.projectmanager.dao.EtudiantDAO;
import eu.dauphine.idd.projectmanager.dao.FormationDAO;
import eu.dauphine.idd.projectmanager.jdbc.SqlLiteSQLJDBC2;
import eu.dauphine.idd.projectmanager.model.*;

public class EnseignantApp {
	public static void main(String[] args) {
		try (Connection connection = SqlLiteSQLJDBC2.getConnection()) {

			System.out.println("Connexion bien établie avec BD");

			// Création d'une liste d'objets à ajouter
			List<Object> objetsAInserer = new ArrayList<>();
		EtudiantDAO x=	new EtudiantDAO(connection);
		x.createTable();
		FormationDAO s=new FormationDAO(connection);
		s.createTable();
 
			// Création de formations
			Formation formation1 = new Formation("ID", "Initiale");
			Formation formation2 = new Formation("MIAGE-IF", "Formation Continue");
			objetsAInserer.add(formation1);
			objetsAInserer.add(formation2);

			// Création d'étudiants
			Etudiant etudiant1 = new Etudiant("Nom1", "Prenom1", formation1);
			Etudiant etudiant2 = new Etudiant("Nom2", "Prenom2", formation2);
			objetsAInserer.add(etudiant1);
			objetsAInserer.add(etudiant2);

			// Création de projets
			Projet projet1 = new Projet("Matiere1", "Sujet1", new java.util.Date());
			Projet projet2 = new Projet("Matiere2", "Sujet2", new java.util.Date());
			objetsAInserer.add(projet1);
			objetsAInserer.add(projet2);

			// SqlLiteSQLJDBC2.delete(new Etudiant(), 1);
			// SqlLiteSQLJDBC2.delete(new Projet(), 2);

			// Appel à la fonction insert de SQLiteDatabaseConnection pour insérer tous les
			// objets
			SqlLiteSQLJDBC2.insert(objetsAInserer);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SqlLiteSQLJDBC2.closeConnection();
		}
	}
}
