package eu.dauphine.idd.pm.main;

import eu.dauphine.idd.pm.dao.DAOFactory;
import eu.dauphine.idd.pm.dao.EtudiantDAO;
import eu.dauphine.idd.pm.dao.FormationDAO;
import eu.dauphine.idd.pm.dao.impl.EtudiantDAOImpl;
import eu.dauphine.idd.pm.jdbc.DatabaseConnection;
import eu.dauphine.idd.pm.model.Formation;

public class MainTest {
	public static void main(String[] args) {
		String dbPath = "./src/main/resources/sample.db" ;
		//Si argument en ligne de commande on recupere le chemin de la base de donnees
		if (args.length > 0) {
			dbPath = args[0]; 
	    }
		System.out.println("Data Base Path: " + dbPath);
		
		//Injection du chemin de la BDD pour la connexion
		DatabaseConnection.setDatabasePath(dbPath);
		//FormationDAO s = DAOFactory.getFormationDAO();
		EtudiantDAO s = DAOFactory.getEtudiantDAO();
		System.out.println(s.findAll());
		//System.out.println(s.findById(1).toString());

	}
}
