package clientPackage;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import sharedPackage.Operation;

public class Client {
    public static void main(String[] args) {
        final String SERVER_IP = "localhost";
        final int SERVER_PORT = 1234;
        
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {
            
            System.out.println("Connecté au serveur calculatrice : " + SERVER_IP + ":" + SERVER_PORT);
            displayWelcomeMessage();
            
            while (true) {
                // Étape 1: Demander l'opération à l'utilisateur
                System.out.print("\nEntrez une opération > ");
                String input = scanner.nextLine().trim();
                
                // Vérifier si l'utilisateur veut quitter
                if (input.equalsIgnoreCase("quit")) {
                    Operation quitOperation = new Operation(0, "quit", 0);
                    objectOut.writeObject(quitOperation);
                    System.out.println("Déconnexion...");
                    break;
                }
                
                // Étape 2: Parser et valider l'opération
                Operation operation = parseOperation(input);
                if (operation == null) {
                    System.out.println("Format invalide. Utilisez: nombre opérateur nombre");
                    System.out.println("   Exemples: 5 + 3, 10.5 * 2, 15 / 4");
                    continue;
                }
                
                // Étape 3: Envoyer l'objet Operation au serveur
                objectOut.writeObject(operation);
                objectOut.flush();
                System.out.println("Opération envoyée au serveur...");
                
                // Étape 4: Recevoir le résultat du serveur
                Operation resultOperation = (Operation) objectIn.readObject();
                
                // Étape 5: Afficher le résultat
                displayResult(resultOperation);
            }
            
            System.out.println("Fermeture du client...");
            
        } catch (ConnectException e) {
            System.err.println("Impossible de se connecter au serveur. Vérifiez qu'il est démarré.");
        } catch (IOException e) {
            System.err.println("Erreur de communication : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : format de données incorrect");
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
        }
    }
    
    /**
     * Affiche le message de bienvenue
     */
    private static void displayWelcomeMessage() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("CALCULATRICE RÉSEAU AVANCÉE");
        System.out.println("=".repeat(50));
        System.out.println("Format: nombre opérateur nombre");
        System.out.println("Opérateurs: +  -  *  /");
        System.out.println("Tapez 'quit' pour quitter");
        System.out.println("=".repeat(50));
    }
    
    /**
     * Parse l'entrée utilisateur et crée un objet Operation
     */
    private static Operation parseOperation(String input) {
        try {
            String[] elements = input.split(" ");
            
            // Vérifier le format
            if (elements.length != 3) {
                return null;
            }
            
            // Convertir les opérandes
            double operand1 = Double.parseDouble(elements[0]);
            String operator = elements[1];
            double operand2 = Double.parseDouble(elements[2]);
            
            // Valider l'opérateur
            if (!operator.matches("[+\\-*/]")) {
                return null;
            }
            
            // Créer et retourner l'objet Operation
            return new Operation(operand1, operator, operand2);
            
        } catch (NumberFormatException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Affiche le résultat de manière élégante
     */
    private static void displayResult(Operation operation) {
        System.out.println("\n" + "─".repeat(40));
        
        if (operation.isSuccess()) {
            System.out.println(operation.getOperationString() + " = " + 
                             String.format("%.2f", operation.getResult()));
        } else {
            System.out.println(operation.getOperationString());
            System.out.println("   Message: " + operation.getErrorMessage());
        }
        
        System.out.println("─".repeat(40));
    }
}