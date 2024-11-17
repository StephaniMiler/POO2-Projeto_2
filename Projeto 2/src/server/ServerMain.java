package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    public static void main(String[] args) {
        try {
             ServerController serverController = new ServerController();
             ServerGUI serverGUI = new ServerGUI(serverController);
             serverGUI.setVisible(true);
             
            try (ServerSocket serverSocket = new ServerSocket(12345)) {
				System.out.println("Server started...");

				while (true) {
				    Socket clientSocket = serverSocket.accept();
				    System.out.println("New client connected.");
				    ClientHandler clientHandler = new ClientHandler(clientSocket, serverController);
				    new Thread(clientHandler).start();
				}
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
