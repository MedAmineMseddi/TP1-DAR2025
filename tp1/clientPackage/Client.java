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

            while (true) {
                // Étape a : Demander un entier
                System.out.print("Entrez un entier (0 pour quitter) : ");
                String number = userInput.readLine();

                // Envoyer l'entier au serveur
                out.println(number);

                // Si l'utilisateur saisit 0 → on sort de la boucle
                if (number.equals("0")) {
                    String farewell = in.readLine();
                    System.out.println("Serveur : " + farewell);
                    break;
                }

                // Recevoir le résultat
                String response = in.readLine();
                System.out.println("Résultat reçu du serveur : " + response);
            }

            System.out.println("Fermeture du client...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
