package eu.dauphine.idd.pm.dao;

import eu.dauphine.idd.pm.model.Note;
import java.util.List;

public interface NoteDAO {
    void create(Note note);
    void update(Note note);
    void deleteById(int id);
    Note findById(int id);
    List<Note> findAll();
}
