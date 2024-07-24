class Moto extends Vehicule {
    private int cylindree;

    public Moto(String marque, String modele, String immatriculation, int cylindree) {
        super(marque, modele, immatriculation);
        this.cylindree = cylindree;
    }

    public int getCylindree() {
        return cylindree;
    }

    @Override
    public String toString() {
        return super.toString() + ", Cylindrée : " + cylindree;
    }
}