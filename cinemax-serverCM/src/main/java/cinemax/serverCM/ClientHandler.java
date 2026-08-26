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

import cinemax.contracts.interfaces.BookingRequest;
import cinemax.contracts.interfaces.Command;
import cinemax.contracts.interfaces.FilmRequest;
import cinemax.contracts.interfaces.ProjectionRequest;
import cinemax.contracts.interfaces.Query;
import cinemax.contracts.interfaces.Response;
import cinemax.contracts.interfaces.UserRequest;
import cinemax.serverCM.dao.BookingDao;
import cinemax.serverCM.dao.Dao;
import cinemax.serverCM.dao.FilmDao;
import cinemax.serverCM.dao.ProjectionDao;
import cinemax.serverCM.dao.UserDao;


/**
 * Gestore per singola connessione client lato serverCM.
 * Riceve oggetti Query/Command dal client, seleziona il DAO appropriato
 * ed esegue l'operazione ritornando la Response corrispondente.
 */
public class ClientHandler implements Runnable {
	private Socket clientSocket;
	private String dbHost;
	private int dbPort;
	private String dbUser;
	private String dbPassword;

	public ClientHandler(Socket socket, String dbHost,int dbPort, String dbUser, String dbPassword) {
		this.clientSocket = socket;
		this.dbHost = dbHost;
		this.dbPort = dbPort;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
	}

	@Override
	public void run() {
		try (
				ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())
		) {
			oos.flush(); // Invia l'header al client

			while (!clientSocket.isClosed()) {
				Object received;
				try {
					received = ois.readObject();
				} catch (EOFException | SocketException e) {
					// Il client ha chiuso la connessione (EOFException o SocketException ricevuta)
					System.out.println("Client disconnesso.");
					break;
				}

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
		if (req instanceof FilmRequest) return new FilmDao(conn);	
		return null;
	}
	
	private Connection getConnection() throws SQLException {
	    String url = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/Cinemax";
	    return DriverManager.getConnection(url, dbUser, dbPassword);
	}
	
	
	/*
	private Connection getConnection() throws SQLException {

		//FOR TESTING PURPOSE
		String url = "jdbc:postgresql://127.0.0.1:5432/Cinemax";
		return DriverManager.getConnection(url, "postgres", "carlotta");
		
		//String url = "jdbc:postgresql://" + dbHost + "/Cinemax";
		//return DriverManager.getConnection(url, dbUser, dbPassword);
	}
	*/
}
