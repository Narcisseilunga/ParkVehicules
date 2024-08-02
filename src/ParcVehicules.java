import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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