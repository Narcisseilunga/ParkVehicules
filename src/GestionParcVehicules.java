import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class ParcVehicules {
    private Map<String, Vehicule> vehicules;
    private Connection connection;

    public ParcVehicules() {
        vehicules = new HashMap<>();
        try {
            // Connexion à la base de données
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ParcVehicules", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ajouterVehicule(Vehicule vehicule) {
        vehicules.put(vehicule.getImmatriculation(), vehicule);

        try {
            // Ajout du véhicule dans la base de données
            PreparedStatement statement = connection.prepareStatement("INSERT INTO vehicule (immatriculation, marque, modele) VALUES (?, ?, ?)");
            statement.setString(1, vehicule.getImmatriculation());
            statement.setString(2, vehicule.getMarque());
            statement.setString(3, vehicule.getModele());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimerVehicule(String immatriculation) {
        vehicules.remove(immatriculation);

        try {
            // Suppression du véhicule dans la base de données
            PreparedStatement statement = connection.prepareStatement("DELETE FROM vehicule WHERE immatriculation = ?");
            statement.setString(1, immatriculation);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifierVehicule(String immatriculation, Vehicule nouveauVehicule) {
        vehicules.put(immatriculation, nouveauVehicule);

        try {
            // Mise à jour du véhicule dans la base de données
            PreparedStatement statement = connection.prepareStatement("UPDATE vehicule SET marque = ?, modele = ? WHERE immatriculation = ?");
            statement.setString(1, nouveauVehicule.getMarque());
            statement.setString(2, nouveauVehicule.getModele());
            statement.setString(3, immatriculation);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Vehicule rechercherVehicule(String nom) {
        Vehicule vehicule = vehicules.values().stream()
                .filter(v -> v.getMarque().equalsIgnoreCase(nom) || v.getModele().equalsIgnoreCase(nom))
                .findFirst()
                .orElse(null);

        if (vehicule == null) {
            try {
                // Recherche du véhicule dans la base de données
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM Vehicule WHERE marque = ? OR modele = ?");
                statement.setString(1, nom);
                statement.setString(2, nom);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String immatriculation = resultSet.getString("immatriculation");
                    String marque = resultSet.getString("marque");
                    String modele = resultSet.getString("modele");
                    vehicule = new Vehicule(immatriculation, marque, modele);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return vehicule;
    }

    public Map<String, Vehicule> listerVehicules(char lettre) {
        Map<String, Vehicule> vehiculesMap = vehicules.values().stream()
                .filter(v -> v.getMarque().toUpperCase().startsWith(String.valueOf(lettre).toUpperCase()))
                .collect(Collectors.toMap(Vehicule::getImmatriculation, v -> v));

        if (vehiculesMap.isEmpty()) {
            try {
                // Récupération des véhicules dans la base de données
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM Vehicule WHERE marque LIKE ?");
                statement.setString(1, String.valueOf(lettre).toUpperCase() + "%");
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String immatriculation = resultSet.getString("immatriculation");
                    String marque = resultSet.getString("marque");
                    String modele = resultSet.getString("modele");
                    Vehicule vehicule = new Vehicule(immatriculation, marque, modele);
                    vehiculesMap.put(immatriculation, vehicule);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return vehiculesMap;
    }

    public int afficherNombreVehicules() {
        try {
            // Récupération du nombre de véhicules dans la base de données
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS total FROM Vehicule");
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void afficherStatistiques() {
        int nbVoitures = 0, nbCamions = 0, nbMotos = 0;
        for (Vehicule v : vehicules.values()) {
            if (v instanceof Voiture) {
                nbVoitures++;
            } else if (v instanceof Camion) {
                nbCamions++;
            } else if (v instanceof Moto) {
                nbMotos++;
            }
        }
        System.out.printf("Nombre de voitures : %d\nNombre de camions : %d\nNombre de motos : %d\n",
                nbVoitures, nbCamions, nbMotos);
    }

    public void afficherVehiculesPlusRecents() {
        vehicules.values().stream()
                .sorted((v1, v2) -> v2.getImmatriculation().compareTo(v1.getImmatriculation()))
                .limit(5)
                .forEach(System.out::println);
    }
}
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