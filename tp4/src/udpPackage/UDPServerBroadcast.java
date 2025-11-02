package udpPackage;

import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class UDPServerBroadcast {
    private static final int PORT = 1234;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        Set<SocketAddress> clients = new HashSet<>();

        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("Serveur UDP (chat) en écoute sur le port " + PORT + "...");

            byte[] buffer = new byte[BUFFER_SIZE];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                SocketAddress senderAddress = packet.getSocketAddress();

                // Ajouter le client s’il n’est pas déjà connu
                clients.add(senderAddress);

                System.out.println("Reçu de " + senderAddress + " → " + message);

                // Diffuser à tous les autres clients
                for (SocketAddress client : clients) {
                    if (!client.equals(senderAddress)) {
                        byte[] data = message.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(data, data.length, client);
                        socket.send(sendPacket);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
