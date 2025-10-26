package clientPackage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connecté au serveur : " + socket.getRemoteSocketAddress());
            System.out.println(in.readLine()); // message de bienvenue du serveur

            String message;
            while (true) {
                System.out.print("Vous : ");
                message = scanner.nextLine();

                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Déconnexion...");
                    break;
                }

                out.println(message);
                System.out.println(in.readLine());
            }

        } catch (IOException e) {
            System.out.println("Erreur : impossible de se connecter au serveur.");
        }
    }
}
