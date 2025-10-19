package serverPackage;

import java.io.*;
import java.net.*;
import sharedPackage.Operation;

public class Server {
    public static void main(String[] args) {
        final int PORT = 1234;
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur calculatrice avancé démarré sur le port " + PORT);
            System.out.println("En attente de connexions clients...");
            
            while (true) {
                // Accepter une nouvelle connexion client
                Socket clientSocket = serverSocket.accept();
                System.out.println("\nClient connecté : " + clientSocket.getInetAddress());
                
                // Créer un nouveau thread pour gérer ce client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
            
        } catch (IOException e) {
            System.err.println("Erreur du serveur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}

/**
 * Classe pour gérer chaque client dans un thread séparé
 */
class ClientHandler implements Runnable {
    private Socket clientSocket;
    
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }
    
    @Override
    public void run() {
        try (ObjectInputStream objectIn = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream objectOut = new ObjectOutputStream(clientSocket.getOutputStream())) {
            
            System.out.println("🔄 Thread démarré pour client : " + clientSocket.getInetAddress());
            
            while (true) {
                // Étape 1: Recevoir l'objet Operation du client
                Operation operation = (Operation) objectIn.readObject();
                System.out.println("Opération reçue : " + operation.getOperationString());
                
                // Étape 2: Vérifier si c'est une demande de déconnexion
                if (operation.getOperator().equalsIgnoreCase("quit")) {
                    System.out.println("Client " + clientSocket.getInetAddress() + " a demandé à quitter");
                    break;
                }
                
                // Étape 3: Effectuer le calcul
                Operation resultOperation = calculateOperation(operation);
                
                // Étape 4: Envoyer le résultat au client
                objectOut.writeObject(resultOperation);
                objectOut.flush();
                System.out.println("Résultat envoyé : " + resultOperation);
            }
            
        } catch (EOFException e) {
            System.out.println("🔌 Client " + clientSocket.getInetAddress() + " déconnecté");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur avec client " + clientSocket.getInetAddress() + " : " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Connexion fermée avec : " + clientSocket.getInetAddress());
            } catch (IOException e) {
                System.err.println("Erreur fermeture socket : " + e.getMessage());
            }
        }
    }
    
    /**
     * Calcule l'opération et retourne un objet Operation avec le résultat
     */
    private Operation calculateOperation(Operation operation) {
        double operand1 = operation.getOperand1();
        String operator = operation.getOperator();
        double operand2 = operation.getOperand2();
        
        try {
            double result;
            switch (operator) {
                case "+":
                    result = operand1 + operand2;
                    operation.setResult(result);
                    break;
                    
                case "-":
                    result = operand1 - operand2;
                    operation.setResult(result);
                    break;
                    
                case "*":
                    result = operand1 * operand2;
                    operation.setResult(result);
                    break;
                    
                case "/":
                    if (operand2 == 0) {
                        operation.setErrorMessage("Division par zéro impossible");
                    } else {
                        result = operand1 / operand2;
                        operation.setResult(result);
                    }
                    break;
                    
                default:
                    operation.setErrorMessage("Opérateur non supporté: " + operator);
            }
            
        } catch (Exception e) {
            operation.setErrorMessage("Erreur lors du calcul: " + e.getMessage());
        }
        
        return operation;
    }
}