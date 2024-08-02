import java.util.Map;

public class GestionParcVehicules {

    public static void main(String[] args) {
        ParcVehicules parc = new ParcVehicules();
        boolean continuer = true;

        while (continuer) {
            System.out.println("Bienvenue dans la gestion du parc de véhicules !");
            System.out.println("Que voulez-vous faire ?");
            System.out.println("1. Ajouter un véhicule");
            System.out.println("2. Supprimer un véhicule");
            System.out.println("3. Modifier un véhicule");
            System.out.println("4. Rechercher un véhicule");
            System.out.println("5. Lister les véhicules par marque");
            System.out.println("6. Afficher le nombre de véhicules");
            System.out.println("7. Afficher les statistiques du parc");
            System.out.println("8. Afficher les véhicules les plus récents");
            System.out.println("9. Quitter");

            int choix = Utilitaire.lireEntier("Entrez votre choix : ");

            switch (choix) {
                case 1:
                    Vehicule demo = new Vehicule("Auto","high","tower");
                    parc.ajouterVehicule(demo);
                    break;
                case 2:
                    parc.supprimerVehicule(Utilitaire.lireString("Entrez l'immatriculation du véhicule à supprimer : "));
                    break;
                case 3:
                    String immatriculation = Utilitaire.lireString("Entrez l'immatriculation du véhicule à modifier : ");
                    Vehicule nouveauVehicule = new Vehicule("ALPA","78045",immatriculation);
                    parc.modifierVehicule(immatriculation, nouveauVehicule);
                    break;
                case 4:
                    String nom = Utilitaire.lireString("Entrez le nom du véhicule à rechercher : ");
                    Vehicule vehicule = parc.rechercherVehicule(nom);
                    if (vehicule != null) {
                        System.out.println("Véhicule trouvé : " + vehicule);
                    } else {
                        System.out.println("Aucun véhicule correspondant n'a été trouvé.");
                    }
                    break;
                case 5:
                    char lettre = Utilitaire.lireChar("Entrez la première lettre de la marque à lister : ");
                    Map<String, Vehicule> vehiculesParMarque = parc.listerVehicules(lettre);
                    vehiculesParMarque.forEach((immat, v) -> System.out.println(v));
                    break;
                case 6:
                    int nbVehicules = parc.afficherNombreVehicules();
                    System.out.println("Nombre total de véhicules : " + nbVehicules);
                    break;
                case 7:
                    parc.afficherStatistiques();
                    break;
                case 8:
                    parc.afficherVehiculesPlusRecents();
                    break;
                case 9:
                    System.out.println("Au revoir !");
                    continuer = false;
                    break;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }
}