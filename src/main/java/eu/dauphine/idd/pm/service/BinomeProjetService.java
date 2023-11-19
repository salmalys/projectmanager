package eu.dauphine.idd.pm.service;

import java.sql.Date;

import eu.dauphine.idd.pm.model.BinomeProjet;
import javafx.collections.ObservableList;

public interface BinomeProjetService {
    int createBinomeProjet(int idEtudiant1, int idEtudiant2, int idProjet, Date dateRemiseEffective);
    void deleteBinomeProjetById(int idBinome);
    ObservableList<BinomeProjet> listBinomeProjets();
    void updateBinomeProjet(int idBinome, int idEtudiant1, int idEtudiant2, int idProjet, Date dateRemiseEffective);
}
