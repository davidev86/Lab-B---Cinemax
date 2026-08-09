package cinemax.serverCM;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;

public class serverMain {

	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        String dbHost;
        String dbUser;
        String dbPassword;
        int serverPort = 12345; // Porta di default per il server CM

        try {
            System.out.println("=== AVVIO SERVER CINEMAX (serverCM) ===");
            
            // 1. Richiesta dei parametri per il DB (come richiesto dalle specifiche)
            System.out.print("Inserisci l'host del database (es. localhost): ");
            dbHost = reader.readLine();
            
            System.out.print("Inserisci lo username per il database: ");
            dbUser = reader.readLine();
            
            System.out.print("Inserisci la password per il database: ");
            dbPassword = reader.readLine();

            // Opzionale: Test della connessione al database con JDBC prima di aprire le porte
            // DatabaseManager.testConnection(dbHost, dbUser, dbPassword);

            // 2. Apertura del ServerSocket per accettare i clientCM in modo concorrente
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("ServerCM avviato con successo ed in ascolto sulla porta " + serverPort + "...");

            // 3. Ciclo di accettazione delle connessioni (gestione concorrenza)
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuovo client connesso: " + clientSocket.getInetAddress());

                // Avvio di un nuovo thread per gestire la concorrenza con più client
                ClientHandler clientHandler = new ClientHandler(clientSocket, dbHost, dbUser, dbPassword);
                new Thread(clientHandler).start();
            }

        } catch (IOException e) {
            System.err.println("Errore durante l'avvio del server: " + e.getMessage());
            e.printStackTrace();
	}
  }
}

