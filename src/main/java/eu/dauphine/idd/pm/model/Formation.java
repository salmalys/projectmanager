package eu.dauphine.idd.pm.model;

public class Formation {

    private int idFormation;
    private String nom;
    private String promotion;

    // Constructeur complet
    public Formation(int idFormation, String nom, String promotion) {
        this.idFormation = idFormation;
        this.nom = nom;
        this.promotion = promotion;
    }

    // Constructeur sans ID (utile pour les nouvelles insertions)
    public Formation(String nom, String promotion) {
        this.nom = nom;
        this.promotion = promotion;
    }

    // Constructeur par défaut
    public Formation() {
    }

    // Getters et setters
    public int getIdFormation() {
        return idFormation;
    }

    public void setIdFormation(int idFormation) {
        this.idFormation = idFormation;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    @Override
    public String toString() {
        return "Formation [idFormation=" + idFormation + ", nom=" + nom + ", promotion=" + promotion + "]";
    }
}
