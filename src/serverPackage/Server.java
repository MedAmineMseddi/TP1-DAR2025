package serverPackage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
    public static void main(String[] args) {
        
        try(ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Je suis un serveur en attente de la connexion d'un client sur le port " + "1234" + "...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Un client est connecté depuis : " + clientSocket.getInetAddress());

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("Bienvenue, vous êtes connecté au serveur !");

            clientSocket.close();
            System.out.println("Connexion fermée avec le client.");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}