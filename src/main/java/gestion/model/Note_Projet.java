package gestion.model;
public class Note_Projet {

	private Binome binome;
	private Etudiant etudiant;
	private double NoteRapport;
	private double NoteSoutenance;

	public Note_Projet(Binome b, Etudiant e, double noteR, double noteS) {
		this.binome = b;
		this.etudiant = e;
		this.NoteRapport = noteR;
		this.NoteSoutenance = noteS;

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

	@Override
	public String toString() {
		return "Note_Projet [binome=" + binome + ", etudiant=" + etudiant + ", NoteRapport=" + NoteRapport
				+ ", NoteSoutenance=" + NoteSoutenance + "]";
	}

}
