package cinemax.serverCM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class serverMain {

    public static void main(String[] args) {
        String dbHost = "";
        String dbUser = "";
        String dbPassword = "";
        int serverPort = 12345;

        System.out.println("=== AVVIO SERVER CINEMAX (serverCM) ===");

        // --- 1° TRY-CATCH: Lettura e validazione dei parametri di input ---
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Inserisci l'host del database (es. localhost): ");
            dbHost = reader.readLine();

            System.out.print("Inserisci lo username per il database: ");
            dbUser = reader.readLine();

            System.out.print("Inserisci la password per il database: ");
            dbPassword = reader.readLine();

            // Validazione minima dei dati inseriti
            if (dbHost == null || dbHost.isBlank() || dbUser == null || dbUser.isBlank()) {
                System.err.println("❌ Errore: Host o Username non possono essere vuoti. Avvio annullato.");
                return; // Interrompe l'avvio del server se i dati non sono validi
            }

        } catch (IOException e) {
            System.err.println("❌ Errore durante la lettura dei parametri da tastiera: " + e.getMessage());
            return; // Interrompe l'esecuzione in caso di errore di I/O da tastiera
        }

        // --- 2° TRY-CATCH (Try-with-Resources): Gestione del ServerSocket e dei Client ---
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("✅ ServerCM avviato con successo ed in ascolto sulla porta " + serverPort + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuovo client connesso: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, dbHost, dbUser, dbPassword);
                new Thread(clientHandler).start();
            }

        } catch (IOException e) {
            System.err.println("❌ Errore di rete / ServerSocket sulla porta " + serverPort + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}