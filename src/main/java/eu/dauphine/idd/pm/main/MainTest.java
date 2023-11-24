package eu.dauphine.idd.pm.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

import eu.dauphine.idd.pm.dao.DAOFactory;
import eu.dauphine.idd.pm.dao.EtudiantDAO;
import eu.dauphine.idd.pm.dao.FormationDAO;
import eu.dauphine.idd.pm.dao.impl.EtudiantDAOImpl;
import eu.dauphine.idd.pm.jdbc.DatabaseConnection;
import eu.dauphine.idd.pm.model.BinomeProjet;
import eu.dauphine.idd.pm.model.Formation;
import eu.dauphine.idd.pm.service.BinomeProjetService;
import eu.dauphine.idd.pm.service.ProjetService;
import eu.dauphine.idd.pm.service.ServiceFactory;

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
		
		ProjetService p  = ServiceFactory.getProjetService();
		System.out.println(p.listProjets());
		p.createProjet("Programmation C", "Algorithme du PageRank","2024-05-15");
		p.deleteProjetById(3);
		System.out.println(p.listProjets());
		p.updateProjet(1, "POA" , "Gestion de Projets", "2023-12-01");
		
		BinomeProjetService b = ServiceFactory.getBinomeProjetService();
		System.out.println(b.listBinomeProjets());
		
	}
}
