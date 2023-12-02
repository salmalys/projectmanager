package eu.dauphine.idd.pm.dao;

import eu.dauphine.idd.pm.model.BinomeProjet;

public interface BinomeProjetDAO extends GenericDAO<BinomeProjet>{
	//Ajouter services
	public BinomeProjet findByMembersAndIdProjet(int idEtudiant1, int idEtudiant2, int idProjet);
}

