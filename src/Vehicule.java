public class Vehicule {
    protected String marque;
    protected String modele;
    protected String immatriculation;

    public Vehicule(String marque, String modele, String immatriculation) {
        this.marque = marque;
        this.modele = modele;
        this.immatriculation = immatriculation;
    }

    public String getMarque() {
        return marque;
    }

    public String getModele() {
        return modele;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    @Override
    public String toString() {
        return "Marque : " + marque + ", Modèle : " + modele + ", Immatriculation : " + immatriculation;
    }
}
