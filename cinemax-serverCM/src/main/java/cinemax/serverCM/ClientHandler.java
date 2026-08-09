package cinemax.serverCM;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.interfaces.Query;
import cinemax.contracts.requests.*;
import cinemax.contracts.interfaces.*;
import cinemax.serverCM.services.ProjectionService;

public class ClientHandler implements Runnable {
	private Socket clientSocket;
	private String dbHost;
	private String dbUser;
	private String dbPassword;

	public ClientHandler(Socket socket, String dbHost, String dbUser, String dbPassword) {
		this.clientSocket = socket;
		this.dbHost = dbHost;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
	}

	@Override
	public void run() {
		try (
				// Stream per ricevere gli oggetti dal client
				ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
				// Stream (opzionale ma consigliato) per inviare la Response indietro al client
				ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())
				) {
			// 1. Legge l'oggetto inviato dal client e lo casta all'interfaccia comune
			Object received = ois.readObject();

			if (received instanceof Query) {
				Query request = (Query) received;
				System.out.println("Ricevuta richiesta di tipo: " + request.getClass().getSimpleName());

				try (Connection conn = GetConnection()) {
					// 2. Esegui il controllo del tipo per gestire la specifica richiesta
					if (request instanceof ProjectionRequest) {

						ProjectionService service = new ProjectionService(conn);
						Response response = service.Find((ProjectionRequest) request);

						// TODO: Interroga il database usando dbHost, dbUser, dbPassword
						// TODO: Crea la Response corrispondente e inviala al client con oos.writeObject(response);
						oos.writeObject(response);
					}
				} catch (SQLException e) {
					System.err.println("Errore SQL durante la gestione della proiezione: " + e.getMessage());
					e.printStackTrace();
				}


			}

		} catch (Exception e) {
			System.err.println("Errore nella gestione del client: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Connection GetConnection() {
		String url = "jdbc:postgresql://localhost:5432/Cinemax";
		try {
			return DriverManager.getConnection(url, "postgres", "personalSpace");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}