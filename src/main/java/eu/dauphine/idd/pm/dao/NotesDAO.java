package eu.dauphine.idd.pm.dao;

import java.util.HashMap;

import eu.dauphine.idd.pm.model.Notes;

public interface NotesDAO extends GenericDAO<Notes> {
	// Ajouter services
	public Notes findByBinomeId(int idBinome);
	
}
