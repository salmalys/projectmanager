package eu.dauphine.idd.pm.model;

public class Notes {
	private int idNotes;
	private BinomeProjet binomeProjet;
	private double NoteRapport;
	private double NoteSoutenanceMembre1;
	private double NoteSoutenanceMembre2;

	public Notes(int id, BinomeProjet b, double NotesR, double NotesS1, double NotesS2) {
		this.binomeProjet = b;
		this.NoteRapport = NotesR;
		this.NoteSoutenanceMembre1 = NotesS1;
		this.NoteSoutenanceMembre2 = NotesS2;
		this.idNotes = id;
	}

	public Notes(BinomeProjet b, double NotesR, double NotesS1, double NotesS2) {
		this.binomeProjet = b;
		this.NoteRapport = NotesR;
		this.NoteSoutenanceMembre1 = NotesS1;
		this.NoteSoutenanceMembre2 = NotesS2;
	}

	public Notes() {
	}

	public BinomeProjet getBinomeProjet() {
		return this.binomeProjet;
	}

	public void setBinomeProjet(BinomeProjet binome) {
		this.binomeProjet = binome;
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
		return "Notes [binome = " + this.binomeProjet.toString() + ", NotesRapport = " + this.NoteRapport + ", NotesSoutenanceMembre1 = " + this.NoteSoutenanceMembre1 + ", NotesSoutenanceMembre2 = " + this.NoteSoutenanceMembre2 + "]";
	}

}
