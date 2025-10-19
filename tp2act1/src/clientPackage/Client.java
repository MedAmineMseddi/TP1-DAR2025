package clientPackage;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        final String SERVER_IP = "localhost";
        final int SERVER_PORT = 1234;
        
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {
            
            System.out.println("Connecté au serveur : " + SERVER_IP + ":" + SERVER_PORT);
            System.out.println("Calculatrice réseau - Tapez 'quit' pour quitter");
            System.out.println("Format: nombre opérateur nombre (ex: 5 + 3)");
            System.out.println("Opérateurs supportés: +, -, *, /");
            System.out.println("----------------------------------------");

            while (true) {
                // Demander une opération à l'utilisateur
                System.out.print("Entrez une opération > ");
                String operation = userInput.readLine().trim();

                // Vérifier si l'utilisateur veut quitter
                if (operation.equalsIgnoreCase("quit")) {
                    out.println("quit");
                    String farewell = in.readLine();
                    System.out.println("Serveur : " + farewell);
                    break;
                }

                // Valider la syntaxe avant envoi
                if (isValidOperation(operation)) {
                    // Envoyer l'opération au serveur
                    out.println(operation);
                    
                    // Recevoir le résultat
                    String response = in.readLine();
                    System.out.println("Résultat : " + response);
                } else {
                    System.out.println("Erreur: Format d'opération invalide.");
                    System.out.println("Utilisez: nombre opérateur nombre (ex: 15.5 * 3)");
                    System.out.println("Opérateurs valides: +, -, *, /");
                }
                
                System.out.println(); // Ligne vide pour la lisibilité
            }
            
            System.out.println("Fermeture du client...");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Valide la syntaxe d'une opération mathématique
     * @param operation l'opération à valider
     * @return true si l'opération est valide, false sinon
     */
    private static boolean isValidOperation(String operation) {
        // Vérifier que l'opération n'est pas vide
        if (operation == null || operation.trim().isEmpty()) {
            return false;
        }
        
        // Séparer les éléments
        String[] elements = operation.split(" ");
        
        // Vérifier qu'on a exactement 3 éléments
        if (elements.length != 3) {
            return false;
        }
        
        // Vérifier que les premier et troisième éléments sont des nombres
        try {
            Double.parseDouble(elements[0]);
            Double.parseDouble(elements[2]);
        } catch (NumberFormatException e) {
            return false;
        }
        
        // Vérifier que le deuxième élément est un opérateur valide
        String operator = elements[1];
        return operator.equals("+") || operator.equals("-") || 
               operator.equals("*") || operator.equals("/");
    }
}