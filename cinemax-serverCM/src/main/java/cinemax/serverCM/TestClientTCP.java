package cinemax.serverCM;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import cinemax.contracts.requests.GetProjections;
import cinemax.contracts.responses.GetProjectionResponse;

public class TestClientTCP {
    public static void main(String[] args) {
        String serverIP = "127.0.0.1"; // localhost
        int serverPort = 12345;

        System.out.println("Tentativo di connessione al server " + serverIP + ":" + serverPort + "...");

        try (Socket socket = new Socket(serverIP, serverPort);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Connessione stabilita con successo!");

            // 1. Creazione dell'oggetto richiesta
            GetProjections request = new GetProjections();
            request.setGenere("Fantasy");
            System.out.println("Invio della richiesta al server: GetProjections");

            // 2. Invio dell'oggetto tramite socket
            oos.writeObject(request);
            oos.flush(); // Forza l'invio dei dati

            // 3. Attesa e lettura della risposta (se il server risponde)
            GetProjectionResponse response = (GetProjectionResponse) ois.readObject();
             System.out.println("Risposta ricevuta dal server!");

        } catch (Exception e) {
            System.err.println("Errore durante la simulazione della chiamata TCP:");
            e.printStackTrace();
        }
    }
}