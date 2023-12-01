package eu.dauphine.idd.pm.dao;

import eu.dauphine.idd.pm.model.Notes;

public interface NotesDAO extends GenericDAO<Notes> {
	public Notes findByBinomeId(int idBinome);
	public void deleteAll();
}
