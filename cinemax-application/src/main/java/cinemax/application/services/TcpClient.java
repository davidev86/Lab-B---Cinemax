/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */

package cinemax.application.services;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import cinemax.contracts.interfaces.Response;

/**
 * Client di rete per la comunicazione point-to-point su socket TCP.
 * <p>
 * Gestisce l'apertura sincrona della connessione, la serializzazione dell'oggetto
 * di richiesta, la ricezione dello stream di risposta e il relativo casting
 * a un tipo conforme all'interfaccia {@link Response}.
 */
public class TcpClient {

    /** Indirizzo IP o hostname del server di destinazione. */
    private final String host;

    /** Porta di ascolto del servizio TCP sul server. */
    private final int port;

    /**
     * Inizializza il client TCP con i parametri di connessione di rete.
     *
     * @param host L'indirizzo di rete o nome host del server.
     * @param port Il numero di porta su cui stabilire la connessione.
     */
    public TcpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Invia un payload di richiesta al server e restituisce la risposta tipizzata.
     * <p>
     * Utilizza un blocco {@code try-with-resources} per garantire la corretta
     * chiusura del socket e dei flussi I/O al termine della transazione.
     *
     * @param <T>            Il tipo del messaggio di risposta, vincolato all'interfaccia {@link Response}.
     * @param requestPayload L'oggetto (DTO/Command/Query) serializzabile da inviare al server.
     * @param responseClass  La classe del tipo atteso, utilizzata per effettuare il casting del risultato.
     * @return L'oggetto risposta deserializzato e convertito nel tipo {@code T}.
     * @throws RuntimeException In caso di errori di connessione, anomalie di I/O o fallimento nella deserializzazione.
     */
    public <T extends Response> T sendRequest(Object requestPayload, Class<T> responseClass) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Serializzazione e invio immediato del payload
            out.writeObject(requestPayload);
            out.flush();

            // Lettura della risposta, deserializzazione e casting dinamico
            return responseClass.cast(in.readObject());
        } catch (Exception e) {
            throw new RuntimeException("Errore di comunicazione TCP", e);
        }
    }
}
