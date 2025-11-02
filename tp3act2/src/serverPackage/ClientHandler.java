package serverPackage;

import java.io.*;
import java.net.Socket;
import calculatorPackage.Operation;

public class ClientHandler implements Runnable {
    private Socket socket;
    private static int operationCount = 0;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            out.writeObject("Bienvenue sur le serveur de calculatrice !");
            
            Object obj;
            while ((obj = in.readObject()) != null) {
                if (obj instanceof Operation) {
                    Operation op = (Operation) obj;
                    double result = calculate(op);

                    // Synchronisation manuelle
                    int count;
                    synchronized (ClientHandler.class) {
                        operationCount++;
                        count = operationCount;
                    }

                    System.out.println("Opération traitée #" + count + 
                                       " | Client : " + socket.getInetAddress().getHostAddress());

                    out.writeObject("Résultat : " + result);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client déconnecté : " + socket.getInetAddress().getHostAddress());
        }
    }

    private double calculate(Operation op) {
        switch (op.getOperator()) {
            case "+" : return op.getOperand1() + op.getOperand2();
            case "-" : return op.getOperand1() - op.getOperand2();
            case "*" : return op.getOperand1() * op.getOperand2();
            case "/" : return op.getOperand1() / op.getOperand2();
            default : throw new IllegalArgumentException("Opérateur inconnu : " + op.getOperator());
        }
    }
}
