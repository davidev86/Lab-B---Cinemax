/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */


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
 * Gestore della sessione e del canale di comunicazione per una singola connessione client TCP.
 * <p>
 * Implementa {@link Runnable} per consentire l'esecuzione concorrente multi-thread sul server.
 * Riceve dal socket gli oggetti serializzati di tipo {@link Query} o {@link Command},
 * individua il Data Access Object (DAO) di competenza ({@link ProjectionDao}, {@link UserDao},
 * {@link BookingDao}, {@link FilmDao}), ne delega l'elaborazione su database PostgreSQL e
 * trasmette la {@link Response} risultante verso il client.
 * </p>
 */
public class ClientHandler implements Runnable {

	private final Socket clientSocket;
	private final String dbHost;
	private final int dbPort;
	private final String dbUser;
	private final String dbPassword;

	/**
	 * Costruisce un gestore per la connessione del client con i parametri di rete e di accesso al database.
	 *
	 * @param socket     il socket TCP associato alla connessione del client
	 * @param dbHost     l'host o indirizzo IP del server PostgreSQL
	 * @param dbPort     la porta di ascolto del database PostgreSQL (es. 5432)
	 * @param dbUser     lo username per l'autenticazione al database
	 * @param dbPassword la password associata all'utente del database
	 */
	public ClientHandler(Socket socket, String dbHost, int dbPort, String dbUser, String dbPassword) {
		this.clientSocket = socket;
		this.dbHost = dbHost;
		this.dbPort = dbPort;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
	}

	/**
	 * Ciclo di vita principale del thread di gestione client.
	 * <p>
	 * Inizializza i flussi di input/output a oggetti ({@link ObjectOutputStream} e {@link ObjectInputStream}),
	 * rimane in ascolto delle richieste fino alla disconnessione o chiusura del socket ed esegue
	 * il dispatching delle query/comandi.
	 * </p>
	 */
	@Override
	public void run() {
		try (
				ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())
		) {
			// Invio preventivo dell'header dello stream per evitare deadlock di handshake
			oos.flush();

			while (!clientSocket.isClosed()) {
				Object received;
				try {
					received = ois.readObject();
				} catch (EOFException | SocketException e) {
					System.out.println("Client disconnesso (" + clientSocket.getRemoteSocketAddress() + ").");
					break;
				}

				if (received instanceof Query) {
					Query request = (Query) received;
					System.out.println("Ricevuta richiesta (Query) di tipo: " + request.getClass().getSimpleName());

					try (Connection conn = getConnection()) {
						Dao dao = getEntityDao(request, conn);
						if (dao != null) {
							Response response = dao.find(request);
							oos.writeObject(response);
							oos.flush();
						} else {
							System.err.println("Nessun DAO registrato per gestire la Query: " + request.getClass().getName());
						}
					} catch (SQLException e) {
						System.err.println("Errore SQL durante l'esecuzione della Query: " + e.getMessage());
						e.printStackTrace();
					}
				} else if (received instanceof Command) {
					Command command = (Command) received;
					System.out.println("Ricevuto comando (Command) di tipo: " + command.getClass().getSimpleName());

					try (Connection conn = getConnection()) {
						Dao dao = getEntityDao(command, conn);
						if (dao != null) {
							Response response = dao.execute(command);
							oos.writeObject(response);
							oos.flush();
						} else {
							System.err.println("Nessun DAO registrato per gestire il Command: " + command.getClass().getName());
						}
					} catch (SQLException e) {
						System.err.println("Errore SQL durante l'esecuzione del Command: " + e.getMessage());
						e.printStackTrace();
					}
				}
			}

		} catch (Exception e) {
			System.err.println("Errore imprevisto nella gestione del socket client: " + e.getMessage());
		} finally {
			closeSocket();
		}
	}

	/**
	 * Risolve l'istanza corretta del Data Access Object associata all'entità richiesta.
	 *
	 * @param request    l'oggetto richiesta ricevuto dal socket
	 * @param connection la connessione JDBC attiva
	 * @return l'istanza specifica del DAO corrispondente, oppure {@code null} se il tipo non è riconosciuto
	 */
	private Dao getEntityDao(Object request, Connection connection) {
		if (request instanceof ProjectionRequest) {
			return new ProjectionDao(connection);
		}
		if (request instanceof UserRequest) {
			return new UserDao(connection);
		}
		if (request instanceof BookingRequest) {
			return new BookingDao(connection);
		}
		if (request instanceof FilmRequest) {
			return new FilmDao(connection);
		}
		return null;
	}

	/**
	 * Crea e restituisce una nuova connessione JDBC verso il database PostgreSQL del cinema.
	 *
	 * @return l'istanza {@link Connection} attiva
	 * @throws SQLException in caso di credenziali errate o mancata raggiungibilità del DBMS
	 */
	private Connection getConnection() throws SQLException {
		String url = String.format("jdbc:postgresql://%s:%d/Cinemax", dbHost, dbPort);
		return DriverManager.getConnection(url, dbUser, dbPassword);
	}

	/**
	 * Chiude in sicurezza il socket del client se aperto.
	 */
	private void closeSocket() {
		try {
			if (clientSocket != null && !clientSocket.isClosed()) {
				clientSocket.close();
			}
		} catch (IOException e) {
			System.err.println("Errore durante la chiusura del socket: " + e.getMessage());
		}
	}
}