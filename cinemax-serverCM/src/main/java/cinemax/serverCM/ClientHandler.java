package cinemax.serverCM;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import cinemax.contracts.interfaces.*;
import cinemax.serverCM.dao.*;


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
		// 1. Inizializza PRIMA ObjectOutputStream e poi ObjectInputStream per evitare deadlock sull'header
		try (
				ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())
		) {
			oos.flush(); // Invia l'header al client

			// 2. Ciclo continuo per gestire più richieste consecutive sulla stessa socket
			while (!clientSocket.isClosed()) {
				Object received;
				try {
					received = ois.readObject();
				} catch (EOFException | SocketException e) {
					// Il client ha chiuso la connessione normalmente
					System.out.println("Client disconnesso.");
					break;
				}

				// --- GESTIONE QUERY ---
				if (received instanceof Query) {
					Query request = (Query) received;
					System.out.println("Ricevuta richiesta (Query) di tipo: " + received.getClass().getSimpleName());

					try (Connection conn = getConnection()) {
						
						Dao service = getEntityDao(received, conn);
						Response response = service.find(request);
						
						oos.writeObject(response);
						oos.flush();
						
					} catch (SQLException e) { 
						System.err.println("Errore SQL durante la gestione della Query: " + e.getMessage());
						e.printStackTrace();
					}
				} 
				// --- GESTIONE COMMAND ---
				else if (received instanceof Command) {
					Command command = (Command) received;
					System.out.println("Ricevuto comando (Command) di tipo: " + command.getClass().getSimpleName());

					try (Connection conn = getConnection()) {						
						Dao service = getEntityDao(received, conn);
						Response response = service.execute(command);

						oos.writeObject(response);
						oos.flush();						
					} catch (SQLException e) {
						System.err.println("Errore SQL durante la gestione del Command: " + e.getMessage());
						e.printStackTrace();
					}
				}
			}

		} catch (Exception e) {
			System.err.println("Errore nella gestione della socket del client: " + e.getMessage());
		} finally {
			try {
				if (clientSocket != null && !clientSocket.isClosed()) {
					clientSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Dao getEntityDao(Object req , Connection conn ) {
		if (req instanceof ProjectionRequest) return new ProjectionDao(conn);
		if (req instanceof UserRequest) return new UserDao(conn);
		if (req instanceof BookingRequest) return new BookingDao(conn);
		
		return null;
	}
	
	

	private Connection getConnection() throws SQLException {

		//TODO:DA CAMBIARE PRIMA DI CONSEGNARE!!!
		String url = "jdbc:postgresql://192.168.1.61:5432/Cinemax";
		return DriverManager.getConnection(url, "postgres", "personalSpace");
		
		//String url = "jdbc:postgresql://" + dbHost + "/Cinemax";
		//return DriverManager.getConnection(url, dbUser, dbPassword);
	}
}