package clientPackage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import calculatorPackage.Operation;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println(in.readObject()); // message de bienvenue

            while (true) {
                System.out.print("Entrez opérande1 : ");
                double op1 = scanner.nextDouble();
                System.out.print("Entrez opérande2 : ");
                double op2 = scanner.nextDouble();
                System.out.print("Entrez opérateur (+, -, *, /) : ");
                String operator = scanner.next();

                Operation operation = new Operation(op1, op2, operator);
                out.writeObject(operation);

                Object response = in.readObject();
                System.out.println(response);

                System.out.print("Nouvelle opération ? (oui/non) : ");
                String cont = scanner.next();
                if (cont.equalsIgnoreCase("non")) break;
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur : connexion au serveur impossible.");
        }
    }
}
