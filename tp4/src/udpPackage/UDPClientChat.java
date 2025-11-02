package udpPackage;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UDPClientChat {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress serverAddr = InetAddress.getByName(SERVER_ADDRESS);
            Scanner scanner = new Scanner(System.in);

            System.out.print("Nom d'utilisateur : ");
            String username = scanner.nextLine();

            // Thread pour écouter les messages entrants
            Thread listener = new Thread(() -> {
                byte[] buffer = new byte[1024];
                while (true) {
                    try {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        String received = new String(packet.getData(), 0, packet.getLength());
                        System.out.println("\n" + received);
                        System.out.print("> ");
                    } catch (IOException e) {
                        break;
                    }
                }
            });
            listener.start();

            // Boucle d’envoi
            System.out.println("Vous pouvez maintenant discuter (tapez 'quit' pour quitter).");
            while (true) {
                System.out.print("> ");
                String msg = scanner.nextLine();
                if (msg.equalsIgnoreCase("quit")) break;

                String fullMsg = "[" + username + "] : " + msg;
                byte[] data = fullMsg.getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length, serverAddr, SERVER_PORT);
                socket.send(packet);
            }

            socket.close();
            listener.interrupt();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
