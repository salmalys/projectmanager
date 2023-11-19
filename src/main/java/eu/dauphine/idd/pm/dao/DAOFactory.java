package eu.dauphine.idd.pm.dao;

import eu.dauphine.idd.pm.dao.impl.*;

public class DAOFactory {
	private static FormationDAO formationDAO;
	private static EtudiantDAO etudiantDAO;
	private static NotesDAO noteDAO;
	private static BinomeProjetDAO binomeDAO;
	private static ProjetDAO projetDAO;

	public static FormationDAO getFormationDAO() {
		if (formationDAO == null) {
			formationDAO = new FormationDAOImpl();
		}
		return formationDAO;
	}

	public static EtudiantDAO getEtudiantDAO() {
		if (etudiantDAO == null) {
			etudiantDAO = new EtudiantDAOImpl();
		}
		return etudiantDAO;
	}
	
	public static ProjetDAO getProjetDAO() {
		if (projetDAO == null) {
			projetDAO = new ProjetDAOImpl();
		}
		return projetDAO;
	}
	
	public static BinomeProjetDAO getBinomeProjetDAO() {
		if (binomeDAO == null) {
			binomeDAO = new BinomeProjetDAOImpl();
		}
		return binomeDAO;
	}

	public static NotesDAO getNotesDAO() {
		if (noteDAO == null) {
			noteDAO = new NotesDAOImpl();
		}
		return noteDAO;
	}
}
