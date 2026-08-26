/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.serverCM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Entry point per il componente serverCM. Si occupa di leggere i parametri di
 * connessione al database da stdin e avviare il ServerSocket per accettare
 * connessioni client.
 */
public class ServerMain {

    public static void main(String[] args) {
        String dbHost = "";
        String dbUser = "";
        String dbPassword = "";
        int dbPort = 5432;
        int serverPort = 12345;

        System.out.println("=== AVVIO SERVER CINEMAX (serverCM) ===");

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Inserisci l'host del database (es. localhost): ");
            dbHost = reader.readLine();
            
            System.out.print("Inserisci la porta del database (default 5432): ");
            String dbPortStr = reader.readLine();
            dbPort = dbPortStr.isBlank() ? dbPort : Integer.parseInt(dbPortStr);

            System.out.print("Inserisci lo username per il database: ");
            dbUser = reader.readLine();

            System.out.print("Inserisci la password per il database: ");
            dbPassword = reader.readLine();

            // Validazione dei dati inseriti
            if (dbHost == null || dbHost.isBlank()) {
                System.err.println("Errore: Host del database non può essere vuoto. Avvio annullato.");
                return;
            }
            if (dbUser == null || dbUser.isBlank()) {
                System.err.println("Errore: Username del database non può essere vuoto. Avvio annullato.");
                return;
            }
            if (dbPassword == null || dbPassword.isBlank()) {
                System.err.println("Errore: Password del database non può essere vuota. Avvio annullato.");
                return;
            }

        } catch (IOException e) {
            System.err.println("Errore durante la lettura dei parametri da tastiera: " + e.getMessage());
            return; // Interrompe l'esecuzione in caso di errore di I/O da tastiera
        }

        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("ServerCM avviato con successo ed in ascolto sulla porta " + serverPort + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuovo client connesso: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, dbHost, dbPort, dbUser, dbPassword);
                new Thread(clientHandler).start();
            }

        } catch (IOException e) {
            System.err.println("Errore di rete / ServerSocket sulla porta " + serverPort + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}

