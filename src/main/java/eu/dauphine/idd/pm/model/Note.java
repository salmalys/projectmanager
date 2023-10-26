package eu.dauphine.idd.pm.model;

import java.util.Date;

public class Note {
	private int id;

	private Binome binome;
	private Etudiant etudiant;
	private double NoteRapport;
	private double NoteSoutenance;
	private Date date_remise_effective;

	public Note(int id, Binome b, Etudiant e, double noteR, double noteS, Date date_remise_effective) {
		this.binome = b;
		this.etudiant = e;
		this.NoteRapport = noteR;
		this.NoteSoutenance = noteS;
		this.id = id;
		this.date_remise_effective = date_remise_effective;

	}

	public Note(Binome b, Etudiant e, double noteR, double noteS, Date date_remise_effective) {
		this.binome = b;
		this.etudiant = e;
		this.NoteRapport = noteR;
		this.NoteSoutenance = noteS;

		this.date_remise_effective = date_remise_effective;

	}

	public Note() {
		// TODO Auto-generated constructor stub
	}

	public Date getDate_remise_effective() {
		return date_remise_effective;
	}

	public void setDate_remise_effective(Date date_remise_effective) {
		this.date_remise_effective = date_remise_effective;
	}

	public Binome getBinome() {
		return binome;

	}

	public void setBinome(Binome binome) {
		this.binome = binome;
	}

	public Etudiant getEtudiant() {
		return etudiant;
	}

	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}

	public double getNoteRapport() {
		return NoteRapport;
	}

	public void setNoteRapport(double noteRapport) {
		NoteRapport = noteRapport;
	}

	public double getNoteSoutenance() {
		return NoteSoutenance;
	}

	public void setNoteSoutenance(double noteSoutenance) {
		NoteSoutenance = noteSoutenance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Note [binome=" + binome + ", etudiant=" + etudiant + ", NoteRapport=" + NoteRapport
				+ ", NoteSoutenance=" + NoteSoutenance + ", date_remise_effective=" + date_remise_effective + "]";
	}

}
