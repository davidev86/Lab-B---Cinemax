package cinemax.serverCM;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.time.LocalDateTime;

import cinemax.contracts.commands.StoreProjection;
import cinemax.contracts.queries.GetProjections;
import cinemax.contracts.responses.GetProjectionResponse;
import cinemax.contracts.responses.StoreProjectionResponse;

public class TestClientTCP {
    public static void main(String[] args) {
        String serverIP = "127.0.0.1"; // localhost
        int serverPort = 12345;

        System.out.println("Tentativo di connessione al server " + serverIP + ":" + serverPort + "...");

        try (Socket socket = new Socket(serverIP, serverPort);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Connessione stabilita con successo!\n");

            // ==========================================
            // 1. TEST QUERY: GetProjections
            // ==========================================
            GetProjections request = new GetProjections();
            request.setGenere("Fantasy");
            
            System.out.println("[CLIENT] Invio della richiesta: GetProjections (Genere: Fantasy)");
            oos.writeObject(request);
            oos.flush();

            // Attesa risposta
            GetProjectionResponse response = (GetProjectionResponse) ois.readObject();
            System.out.println("[CLIENT] Risposta ricevuta dal server per GetProjections!");
            if (response != null && response.getProjections() != null) {
                System.out.println("  -> Elementi trovati: " + response.getProjections().size());
            }
            System.out.println("--------------------------------------------------\n");


            // ==========================================
            // 2. TEST COMMAND: StoreProjection
            // ==========================================
            StoreProjection requestStore = new StoreProjection();
            requestStore.setDataOraProiezione(LocalDateTime.now());
            requestStore.setIdFilm(2);
            // Uso di BigDecimal e un valore valido per NUMERIC(4,2) (max 99.99)
            requestStore.setPrezzoBiglietto(new BigDecimal("10.50")); 

            System.out.println("[CLIENT] Invio della richiesta: StoreProjection");
            
            // CORRETTO: Invia l'oggetto requestStore
            oos.writeObject(requestStore); 
            oos.flush();

            // Attesa risposta
            StoreProjectionResponse responseStore = (StoreProjectionResponse) ois.readObject();
            System.out.println("[CLIENT] Risposta ricevuta dal server per StoreProjection!");
            if (responseStore != null) {
                System.out.println("  -> Esito inserimento / ID generato: " + responseStore.getId());
            }

        } catch (Exception e) {
            System.err.println("Errore durante la simulazione della chiamata TCP:");
            e.printStackTrace();
        }
    }
}