package udpPackage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class UDPServer {
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(null)) {
            socket.bind(new InetSocketAddress(PORT));
            System.out.println("Serveur UDP en écoute sur le port " + PORT + "...");

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Message reçu de " +
                        packet.getAddress().getHostAddress() + ":" + packet.getPort() +
                        " → " + message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
