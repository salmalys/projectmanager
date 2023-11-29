package eu.dauphine.idd.pm.main;

import eu.dauphine.idd.pm.dao.BinomeProjetDAO;
import eu.dauphine.idd.pm.dao.DAOFactory;
import eu.dauphine.idd.pm.dao.EtudiantDAO;
import eu.dauphine.idd.pm.dao.FormationDAO;
import eu.dauphine.idd.pm.dao.NotesDAO;
import eu.dauphine.idd.pm.dao.ProjetDAO;
import eu.dauphine.idd.pm.dao.impl.NotesDAOImpl;
import eu.dauphine.idd.pm.jdbc.DatabaseConnection;
import eu.dauphine.idd.pm.model.BinomeProjet;
import eu.dauphine.idd.pm.model.Etudiant;
import eu.dauphine.idd.pm.model.Formation;
import eu.dauphine.idd.pm.model.Notes;
import eu.dauphine.idd.pm.service.BinomeProjetService;
import eu.dauphine.idd.pm.service.EtudiantService;
import eu.dauphine.idd.pm.service.FormationService;
import eu.dauphine.idd.pm.service.ProjetService;
import eu.dauphine.idd.pm.service.NotesService;
import eu.dauphine.idd.pm.service.ServiceFactory;

public class MainTest {
	public static void main(String[] args) {
		String dbPath = "./src/main/resources/DataBaseGestionProjet.db" ;
		//Si argument en ligne de commande on recupere le chemin de la base de donnees
		if (args.length > 0) {
			dbPath = args[0]; 
	    }
		System.out.println("Data Base Path: " + dbPath);
		
		//Injection du chemin de la BDD pour la connexion
		DatabaseConnection.setDatabasePath(dbPath);
		
		
		FormationService fS = ServiceFactory.getFormationService();
		FormationDAO f = DAOFactory.getFormationDAO();
		/*
		System.out.println(f.listFormations());
		f.createFormation("CAP", "Initiale");
		f.deleteFormationById(7);
		System.out.println(f.listFormations());
		*/
		
		EtudiantDAO e = DAOFactory.getEtudiantDAO();
		EtudiantService eS = ServiceFactory.getEtudiantService();
		//e.findByName("yani", "lhaj");
		//System.out.println(e.findByName("SAIS", "Ilyes"));
		//System.out.println(e.findAll());
		//Formation formation = f.findById(9);
		//System.out.println(formation);
		//eS.createEtudiant("KHAROUF", "Saber", 9);
		//Etudiant saber = e.findByName("KHAROUF", "Saber");
		//System.out.println(saber);
		//eS.deleteEtudiantById(saber.getIdEtudiant());
		//eS.createEtudiant("KHAROUF", "Saber", 9);
		//System.out.println(e.findById(2));
		
		
		
		ProjetService pS  = ServiceFactory.getProjetService();
		ProjetDAO p = DAOFactory.getProjetDAO();
		/*
		System.out.println(p.listProjets());
		p.createProjet("Programmation C", "Algorithme du PageRank","2024-05-15");
		p.deleteProjetById(3);
		System.out.println(p.listProjets());
		p.updateProjet(1, "POA" , "Gestion de Projets", "2023-12-01");
		*/
		//System.out.println(pS.listProjets());
		//System.out.println(p.findById(2));
		
		
		
		BinomeProjetService bS = ServiceFactory.getBinomeProjetService();
		BinomeProjetDAO b = DAOFactory.getBinomeProjetDAO();
		//Etudiant saber = e.findByName("KHAROUF", "Saber");
		//Etudiant saber = e.findById(4);
		//Etudiant youssef = e.findByName("LAHRACH", "Youssef");
		//System.out.println(saber);
		//System.out.println(youssef);
		//System.out.println(e.findAll());
		//Binome binome = new Binome()
		//b.create();
		//System.out.println(bS.listBinomeProjets());
		//System.out.println(b.findById(1));
		
		NotesService nS = ServiceFactory.getNotesService();
		NotesDAO n = DAOFactory.getNotesDAO();
		
		//System.out.println(b.findById(1));
		BinomeProjet binomeSI = b.findById(1);
		Notes notes = n.findById(1);
		
		System.out.println(notes.getId());
		System.out.println(notes);
		double[] notesfinales = nS.calculNoteFinale(notes.getId());
		System.out.println((notesfinales[0]));
		System.out.println((notesfinales[1]));
		
		
		//System.out.println(notes);
		//System.out.println(nS.calculNoteFinale(notes.getId()));
		//System.out.println(notes);
		//n.delete(notes);
		//System.out.println(notes.getId());
		//System.out.println(n.findAll());
		//System.out.println(n.findById(1));
		//n.deleteById(1);
		//System.out.println(n.findAll());
		//n.findById(1);
		//n.create(notes);
		//System.out.println(n.findAll());
		
		//n.createNotes(1, 16, 14, 15);
	}
}
