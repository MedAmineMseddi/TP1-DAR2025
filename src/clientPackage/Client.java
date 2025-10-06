package clientPackage; // or clientPackage

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        final String SERVER_IP = "localhost"; // your server IP
        final int SERVER_PORT = 1234;         // must match server port

        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connecté au serveur : " + SERVER_IP + ":" + SERVER_PORT);

            // Lire le message du serveur
            String message = in.readLine();
            System.out.println("Message du serveur : " + message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
