package serverPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        final int PORT = 1234;
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur en attente de connexion sur le port " + PORT + "...");
            
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connecté depuis : " + clientSocket.getInetAddress());

            // Création des flux
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            while (true) {
                // Recevoir l'opération du client
                String operation = in.readLine();
                if (operation == null) break; // client fermé
                
                System.out.println("Serveur a reçu : " + operation);

                // Si l'utilisateur envoie "quit" → fin de communication
                if (operation.equalsIgnoreCase("quit")) {
                    System.out.println("Le client a demandé à quitter. Fermeture de la connexion...");
                    out.println("Fin de communication. Au revoir !");
                    break;
                }

                // Traiter l'opération et calculer le résultat
                String result = calculateOperation(operation);
                
                // Envoyer le résultat au client
                out.println(result);
                System.out.println("Serveur envoie : " + result);
            }

            // Fermeture des ressources
            clientSocket.close();
            System.out.println("Connexion fermée.");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calcule le résultat d'une opération mathématique
     * @param operation l'opération sous forme de chaîne (ex: "5 + 3")
     * @return le résultat ou un message d'erreur
     */
    private static String calculateOperation(String operation) {
        try {
            // Séparer les éléments de l'opération
            String[] elements = operation.split(" ");
            
            // Vérifier qu'on a exactement 3 éléments
            if (elements.length != 3) {
                return "ERREUR: Format incorrect. Utilisez: nombre opérateur nombre (ex: 5 + 3)";
            }
            
            // Extraire les opérandes et l'opérateur
            double operand1 = Double.parseDouble(elements[0]);
            String operator = elements[1];
            double operand2 = Double.parseDouble(elements[2]);
            
            // Effectuer le calcul selon l'opérateur
            double result;
            switch (operator) {
                case "+":
                    result = operand1 + operand2;
                    break;
                case "-":
                    result = operand1 - operand2;
                    break;
                case "*":
                    result = operand1 * operand2;
                    break;
                case "/":
                    if (operand2 == 0) {
                        return "ERREUR: Division par zéro impossible";
                    }
                    result = operand1 / operand2;
                    break;
                default:
                    return "ERREUR: Opérateur non supporté. Utilisez +, -, * ou /";
            }
            
            return String.valueOf(result);
            
        } catch (NumberFormatException e) {
            return "ERREUR: Les opérandes doivent être des nombres valides";
        } catch (Exception e) {
            return "ERREUR: Problème lors du calcul";
        }
    }
}