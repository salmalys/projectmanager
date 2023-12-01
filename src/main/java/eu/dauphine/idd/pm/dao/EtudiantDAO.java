package eu.dauphine.idd.pm.dao;

import eu.dauphine.idd.pm.model.Etudiant;

public interface EtudiantDAO extends GenericDAO<Etudiant>{
	public Etudiant findByName(String nom, String prenom);
}
