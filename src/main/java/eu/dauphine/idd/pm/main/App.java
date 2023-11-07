package eu.dauphine.idd.pm.main;

import java.sql.Connection;
import java.sql.SQLException;

import eu.dauphine.idd.pm.jdbc.DatabaseConnection;
import eu.dauphine.idd.pm.service.FormationService;
import eu.dauphine.idd.pm.service.ServiceFactory;

public class App {

	public static void main(String[] args) {
		try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
			System.out.println("Connected to DataBase SQLite.");

			FormationService formationService = ServiceFactory.getFormationService();

			// Creation de formations
			System.out.println("Creation de formations: ");
			formationService.createFormation("Informatique", "2023");
			formationService.createFormation("Mathematiques", "2023");
			formationService.createFormation("Physique", "2022");

			// Lister les formations
			System.out.println("\nListe des formations :");
			formationService.listFormations();

			// Supprimer une formation par ID
			System.out.println("\nSuppression de la formation id 2 :");
			formationService.deleteFormationById(2);

			// Lister les formations
			System.out.println("\nListe des formations apres suppression:");
			formationService.listFormations();

			/*
			 * Creation d'etudiants Etudiant etudiant1 = new Etudiant("Nom1", "Prenom1",
			 * formation1); Etudiant etudiant2 = new Etudiant("Nom2", "Prenom2",
			 * formation2); objetsAInserer.add(etudiant1); objetsAInserer.add(etudiant2);
			 */

			/*
			 * Creation de projets Projet projet1 = new Projet("Matiere1", "Sujet1", new
			 * java.util.Date()); Projet projet2 = new Projet("Matiere2", "Sujet2", new
			 * java.util.Date()); objetsAInserer.add(projet1); objetsAInserer.add(projet2);
			 */

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
