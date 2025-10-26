package serverPackage;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private int clientNumber;

    public ClientHandler(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
    }

    @Override
    public void run() {
        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Message de bienvenue avec numéro d’ordre
            out.println("Bienvenue, vous êtes le client n°" + clientNumber);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Client n°" + clientNumber + " : " + message);
                out.println("Serveur → Message reçu : " + message);
            }

        } catch (IOException e) {
            System.out.println("Client n°" + clientNumber + " déconnecté.");
        }
    }
}
