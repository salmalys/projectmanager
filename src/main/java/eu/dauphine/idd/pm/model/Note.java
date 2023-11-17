package eu.dauphine.idd.pm.model;

import java.util.Date;

public class Note {
	private int id;
	private Binome binome;
	private Etudiant etudiant;
	private double noteRapport;
	private double noteSoutenance;

	public Note(int id, Binome b, Etudiant e, double noteR, double noteS, Date date) {
		this.binome = b;
		this.etudiant = e;
		this.noteRapport = noteR;
		this.noteSoutenance = noteS;
		this.id = id;
	}

	public Note(Binome b, Etudiant e, double noteR, double noteS, Date date) {
		this.binome = b;
		this.etudiant = e;
		this.noteRapport = noteR;
		this.noteSoutenance = noteS;
	}

	public Note() {
	}

	public Binome getBinome() {
		return this.binome;
	}

	public void setBinome(Binome binome) {
		this.binome = binome;
	}

	public Etudiant getEtudiant() {
		return this.etudiant;
	}

	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}

	public double getNoteRapport() {
		return this.noteRapport;
	}

	public void setNoteRapport(double noteRapport) {
		this.noteRapport = noteRapport;
	}

	public double getNoteSoutenance() {
		return this.noteSoutenance;
	}

	public void setNoteSoutenance(double noteSoutenance) {
		this.noteSoutenance = noteSoutenance;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Note [binome = " + this.binome.toString() + ", etudiant = " + this.etudiant.toString()
				+ ", NoteRapport = " + this.noteRapport + ", NoteSoutenance = " + this.noteSoutenance + "]";
	}

}
