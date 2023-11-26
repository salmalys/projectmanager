package eu.dauphine.idd.pm.main;

import eu.dauphine.idd.pm.jdbc.DatabaseConnection;
import eu.dauphine.idd.pm.model.BinomeProjet;
import eu.dauphine.idd.pm.model.Formation;
import eu.dauphine.idd.pm.service.BinomeProjetService;
import eu.dauphine.idd.pm.service.FormationService;
import eu.dauphine.idd.pm.service.ProjetService;
import eu.dauphine.idd.pm.service.NotesService;
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
		
		FormationService f = ServiceFactory.getFormationService();
		System.out.println(f.listFormations());
		f.createFormation("CAP", "Initiale");
		f.deleteFormationById(7);
		System.out.println(f.listFormations());
		
		/*
		ProjetService p  = ServiceFactory.getProjetService();
		System.out.println(p.listProjets());
		p.createProjet("Programmation C", "Algorithme du PageRank","2024-05-15");
		p.deleteProjetById(3);
		System.out.println(p.listProjets());
		p.updateProjet(1, "POA" , "Gestion de Projets", "2023-12-01");
		*/
		
		
		BinomeProjetService b = ServiceFactory.getBinomeProjetService();
		//b.createBinomeProjet(1, 1, 1, null);
		System.out.println(b.listBinomeProjets());
		
		NotesService n = ServiceFactory.getNotesService();
		n.createNotes(1, 16, 14, 15);
	}
}
