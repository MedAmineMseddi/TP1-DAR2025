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
                // Attendre un entier du client
                String input = in.readLine();
                if (input == null) break; // client fermé
                int x = Integer.parseInt(input);
                System.out.println("Serveur a reçu : " + x);

                // Si l'utilisateur envoie 0 → fin de communication
                if (x == 0) {
                    System.out.println("Le client a envoyé 0. Fermeture de la connexion...");
                    out.println("Fin de communication. Au revoir !");
                    break;
                }

                // Calcul du résultat
                int result = x * 5;
                System.out.println("Serveur calcule : " + x + " * 5 = " + result);

                // Envoi du résultat
                out.println(result);
            }

            // Fermeture des ressources
            clientSocket.close();
            System.out.println("Connexion fermée.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
