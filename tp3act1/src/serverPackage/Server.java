package serverPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private static final int PORT = 1234;
    private static AtomicInteger clientCount = new AtomicInteger(0);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur démarré sur le port " + PORT + " ...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                int clientNumber = clientCount.incrementAndGet();

                System.out.println("Nouveau client connecté : " +
                        clientSocket.getRemoteSocketAddress() +
                        " | Client n°" + clientNumber);

                // Créer un thread pour gérer ce client
                ClientHandler handler = new ClientHandler(clientSocket, clientNumber);
                new Thread(handler).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
