package eu.dauphine.idd.pm.model;

import java.util.Date;

public class Notes {
	private int idNotes;
	private Binome binome;
	private double NoteRapport;
	private double NoteSoutenanceMembre1;
	private double NoteSoutenanceMembre2;

	public Notes(int id, Binome b, double NotesR, double NotesS1, double NotesS2, Date date) {
		this.binome = b;
		this.NoteRapport = NotesR;
		this.NoteSoutenanceMembre1 = NotesS1;
		this.NoteSoutenanceMembre2 = NotesS2;
		this.idNotes = id;
	}

	public Notes(Binome b, Etudiant e, double NotesR, double NotesS1, double NotesS2, Date date) {
		this.binome = b;
		this.NoteRapport = NotesR;
		this.NoteSoutenanceMembre1 = NotesS1;
		this.NoteSoutenanceMembre2 = NotesS2;
	}

	public Notes() {
	}

	public Binome getBinome() {
		return this.binome;
	}

	public void setBinome(Binome binome) {
		this.binome = binome;
	}

	public double getNoteRapport() {
		return this.NoteRapport;
	}

	public void setNoteRapport(double NotesRapport) {
		this.NoteRapport = NotesRapport;
	}

	public double getNoteSoutenanceMembre1() {
		return this.NoteSoutenanceMembre1;
	}

	public void setNoteSoutenanceMembre1(double NotesSoutenance) {
		this.NoteSoutenanceMembre1 = NotesSoutenance;
	}
	
	public double getNoteSoutenanceMembre2() {
		return this.NoteSoutenanceMembre2;
	}

	public void setNoteSoutenanceMembre2(double NotesSoutenance) {
		this.NoteSoutenanceMembre2 = NotesSoutenance;
	}

	public int getId() {
		return this.idNotes;
	}

	public void setId(int id) {
		this.idNotes = id;
	}

	@Override
	public String toString() {
		return "Notes [binome = " + this.binome.toString() + ", NotesRapport = " + this.NoteRapport + ", NotesRapport = " + this.NoteSoutenanceMembre1 + ", NotesSoutenanceMembre2 = " + this.NoteSoutenanceMembre2 + "]";
	}

}
