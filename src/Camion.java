class Camion extends Vehicule {
    private int tonnage;

    public Camion(String marque, String modele, String immatriculation, int tonnage) {
        super(marque, modele, immatriculation);
        this.tonnage = tonnage;
    }

    public int getTonnage() {
        return tonnage;
    }

    @Override
    public String toString() {
        return super.toString() + ", Tonnage : " + tonnage;
    }
}