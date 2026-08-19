package cinemax.application.services;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import cinemax.contracts.interfaces.Response;

public class TcpClient {
    private final String host;
    private final int port;

    public TcpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    
    // Il parametro è Class<T>, dove T deve estendere Response
    public <T extends Response> T sendRequest(Object requestPayload, Class<T> responseClass) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(requestPayload);
            out.flush();
          
            return responseClass.cast(in.readObject());
        } catch (Exception e) {
            throw new RuntimeException("Errore di comunicazione TCP", e);
        }
    }
}