package eu.dauphine.idd.pm.service;

import eu.dauphine.idd.pm.model.Notes;
import javafx.collections.ObservableList;

public interface NotesService {
    int createNotes(int idBinome, double noteRapport, double noteSoutenanceMembre1, double noteSoutenanceMembre2);
    void deleteNotesById(int idNotes);
    ObservableList<Notes> listNotes();
    void updateNotes(int idNotes, int idBinome, double noteRapport, double noteSoutenanceMembre1, double noteSoutenanceMembre2);
    double[] calculNoteFinale(int idNotes);
    Notes findNoteForBinome(int idBinome);
    public void deleteAll();
}