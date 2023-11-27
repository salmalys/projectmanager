package eu.dauphine.idd.pm.service.impl;

import java.sql.Date;

import eu.dauphine.idd.pm.dao.BinomeProjetDAO;
import eu.dauphine.idd.pm.dao.DAOFactory;
import eu.dauphine.idd.pm.dao.EtudiantDAO;
import eu.dauphine.idd.pm.dao.ProjetDAO;
import eu.dauphine.idd.pm.model.BinomeProjet;
import eu.dauphine.idd.pm.model.Etudiant;
import eu.dauphine.idd.pm.model.Projet;
import eu.dauphine.idd.pm.service.BinomeProjetService;
import javafx.collections.ObservableList;

public class BinomeProjetServiceImpl implements BinomeProjetService {

    private BinomeProjetDAO binomeProjetDAO;
    private EtudiantDAO etudiantDAO;
    private ProjetDAO projetDAO;

    public BinomeProjetServiceImpl() {
        this.binomeProjetDAO = DAOFactory.getBinomeProjetDAO();
        this.etudiantDAO = DAOFactory.getEtudiantDAO();
        this.projetDAO = DAOFactory.getProjetDAO();
    }

    @Override
    public int createBinomeProjet(int idEtudiant1, int idEtudiant2, int idProjet, Date dateRemiseEffective) {
        Etudiant membre1 = etudiantDAO.findById(idEtudiant1);
        Etudiant membre2 = etudiantDAO.findById(idEtudiant2);
        Projet projet = projetDAO.findById(idProjet);

        if (membre1 == null || projet == null) {
            System.out.println("Invalid member or project ID");
            return 1;
        }

        // Always set dateRemiseEffective to null
        binomeProjetDAO.create(new BinomeProjet(membre1, membre2, projet, null));
        System.out.println("BinomeProjet created successfully.");
        return 0;
    }

    @Override
    public void deleteBinomeProjetById(int idBinome) {
        binomeProjetDAO.deleteById(idBinome);
        System.out.println("BinomeProjet with ID " + idBinome + " successfully removed.");
    }

    @Override
    public ObservableList<BinomeProjet> listBinomeProjets() {
        return binomeProjetDAO.findAll();
    }

    @Override
    public void updateBinomeProjet(int idBinome, int idEtudiant1, int idEtudiant2, int idProjet, Date dateRemiseEffective) {
        Etudiant membre1 = etudiantDAO.findById(idEtudiant1);
        Etudiant membre2 = etudiantDAO.findById(idEtudiant2);
        Projet projet = projetDAO.findById(idProjet);

        if (membre1 == null || projet == null) {
            System.out.println("Invalid member or project ID");
            return;
        }

        BinomeProjet binomeProjet = new BinomeProjet(idBinome, membre1, membre2, projet, dateRemiseEffective);
        binomeProjetDAO.update(binomeProjet);
        System.out.println("BinomeProjet with ID " + idBinome + " successfully updated.");
        System.out.println(binomeProjet.toString());
    }

	@Override
	public void updateDateRemise(Integer valueOf, Date sqlDate) {
		
		 BinomeProjet binomeProjet = binomeProjetDAO.findById(valueOf);

		    if (binomeProjet == null) {
		        System.out.println("Invalid BinomeProjet ID");
		        return;
		    }

		    binomeProjet.setDateRemiseEffective(sqlDate);
		    binomeProjetDAO.update(binomeProjet);

		    System.out.println("Date of Remise for BinomeProjet with ID " + valueOf + " successfully updated.");
		    System.out.println(binomeProjet.toString());
		
	}
	
	public static void main(String[] args) {
		BinomeProjetServiceImpl l=new BinomeProjetServiceImpl();
		System.out.println(l.createBinomeProjet(3, 1, 2, null));
		l.updateBinomeProjet(2, 1, 3, 2, null);
	}
    
    //Count
}
