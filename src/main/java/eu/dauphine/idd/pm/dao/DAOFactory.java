package eu.dauphine.idd.pm.dao;

import eu.dauphine.idd.pm.dao.impl.*;

public class DAOFactory {
	private static FormationDAO formationDAO;
	private static EtudiantDAO etudiantDAO;
	private static NoteDAO noteDAO;

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

	public static NoteDAO getNoteDAO() {
		if (noteDAO == null) {
			noteDAO = new NoteDAOImpl();
		}
		return noteDAO;
	}
}
