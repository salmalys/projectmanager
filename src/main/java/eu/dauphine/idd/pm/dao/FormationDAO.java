package eu.dauphine.idd.pm.dao;

import eu.dauphine.idd.pm.model.Formation;

public interface FormationDAO extends GenericDAO<Formation> {	
	public Formation findByNameAndPromotion(String nom, String promotion);
}
