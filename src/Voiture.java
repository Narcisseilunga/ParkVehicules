class Voiture extends Vehicule {
    private int nbPortes;

    public Voiture(String marque, String modele, String immatriculation, int nbPortes) {
        super(marque, modele, immatriculation);
        this.nbPortes = nbPortes;
    }

    public int getNbPortes() {
        return nbPortes;
    }

    @Override
    public String toString() {
        return super.toString() + ", Nombre de portes : " + nbPortes;
    }
}