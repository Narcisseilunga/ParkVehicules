import java.util.Scanner;

public class Utilitaire {
    private static final Scanner scanner = new Scanner(System.in);

    public static int lireEntier(String message) {
        System.out.print(message);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrée invalide. Veuillez saisir un nombre entier : ");
            }
        }
    }

    public static String lireString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public static char lireChar(String message) {
        String input = lireString(message);
        if (input.length() > 0) {
            return input.charAt(0);
        } else {
            return '\0'; // Retourne un caractère nul si l'entrée est vide
        }
    }

    // Autres méthodes de la classe Utilitaire...
}