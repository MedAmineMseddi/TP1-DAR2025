package udpPackage;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UDPClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Entrez votre nom d'utilisateur : ");
            String username = scanner.nextLine();

            InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);
            System.out.println("Vous pouvez maintenant envoyer des messages...");

            while (true) {
                System.out.print("> ");
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("quit")) break;

                String fullMessage = "[" + username + "] : " + message;
                byte[] data = fullMessage.getBytes();

                DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, SERVER_PORT);
                socket.send(packet);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
