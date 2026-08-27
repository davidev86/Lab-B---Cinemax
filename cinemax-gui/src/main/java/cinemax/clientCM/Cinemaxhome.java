/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.clientCM;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import cinemax.application.services.TcpClient;
import cinemax.clientCM.callback.SelezioneProjectionCallBack;
import cinemax.clientCM.login.LoginPanel;
import cinemax.clientCM.tabpanel.SearchProjection;
import cinemax.clientCM.tabpanel.TabPanel;
import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.dto.UserMinInfo;

/**
 * Classe di ingresso per l'interfaccia grafica (GUI) dell'applicazione client Cinemax.
 * <p>
 * Inizializza la connessione di rete con il server tramite {@link TcpClient}, assembla la finestra
 * principale ({@link JFrame}), istanzia i componenti di navigazione (login e tab panel) e gestisce
 * lo stato globale della sessione utente e della selezione corrente delle proiezioni.
 * </p>
 */
public class Cinemaxhome {

	/**
	 * Indirizzo IP predefinito del server Cinemax.
	 */
	private static final String DEFAULT_SERVER_IP = "127.0.0.1";

	/**
	 * Porta TCP predefinita del server Cinemax.
	 */
	private static final int DEFAULT_SERVER_PORT = 12345;

	/**
	 * Informazioni minime dell'utente attualmente autenticato nella sessione client.
	 */
	public static UserMinInfo loggedUser = null;

	/**
	 * Dettagli della proiezione cinematografica correntemente selezionata nella vista.
	 */
	public static ProjectionDetails onSelezione = null;

	/**
	 * Dettagli della proiezione selezionata per l'operazione di prenotazione.
	 */
	public static ProjectionDetails onSelezioneLibro = null;

	/**
	 * Callback per la notifica della selezione di una proiezione tra i diversi pannelli.
	 */
	public static SelezioneProjectionCallBack selezioneProjectionCallBack;

	/**
	 * Riferimento al contenitore a schede (TabPanel) per la commutazione delle viste.
	 */
	public static TabPanel tabPanel;

	/**
	 * Costruttore privato per prevenire l'istanziazione diretta della classe di bootstrap.
	 */
	private Cinemaxhome() {
	}

	/**
	 * Punto di ingresso principale dell'applicazione client.
	 * <p>
	 * Configura il thread dell'Event Dispatch Thread (EDT) di Swing e avvia l'interfaccia utente.
	 * </p>
	 *
	 * @param args argomenti passati da riga di comando (non utilizzati)
	 */
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(() -> initAndShowGUI(DEFAULT_SERVER_IP, DEFAULT_SERVER_PORT));
	}

	/**
	 * Inizializza i componenti grafici, la connessione socket TCP e visualizza il frame principale.
	 *
	 * @param serverIp   l'indirizzo IP del server di backend
	 * @param serverPort la porta di ascolto TCP del server
	 */
	private static void initAndShowGUI(String serverIp, int serverPort) {
		// Inizializzazione del client TCP per la comunicazione con il server
		TcpClient tcpClient = new TcpClient(serverIp, serverPort);

		// Configurazione del frame principale della finestra
		JFrame frameHome = new JFrame("Cinemax");
		frameHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameHome.setSize(1000, 800);
		frameHome.setLocationRelativeTo(null);
		frameHome.setResizable(false);

		// Creazione del pannello contenitore principale con layout verticale
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		// Istanziazione del gestore dei tab
		tabPanel = new TabPanel(tcpClient);

		// Configurazione del pannello di autenticazione e binding dei callback di login/logout
		LoginPanel loginPanel = new LoginPanel(
				tcpClient,
				(UserMinInfo user) -> {
					loggedUser = user;
					tabPanel.setPanelforUSerLogged(user);
					tabPanel.revalidate();
					tabPanel.repaint();
				},
				() -> {
					loggedUser = null;
					tabPanel.setPanelforUserUnlogged();
					tabPanel.revalidate();
					tabPanel.repaint();
				}
		);

		try {
			// Assemblaggio e aggiunta dei componenti al contenitore principale
			mainPanel.add(loginPanel.build());
			mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			mainPanel.add(tabPanel.build());
		} catch (Exception e) {
			System.err.println("Errore durante il caricamento dei pannelli grafici: " + e.getMessage());
		}

		// Aggiunta del pannello al content pane e visualizzazione
		frameHome.getContentPane().add(mainPanel);
		frameHome.setVisible(true);
	}

	/**
	 * Aggiorna lo stato della sessione a seguito di un'operazione di login.
	 *
	 * @param userMinInfo le informazioni sintetiche dell'utente autenticato
	 */
	public static void login(UserMinInfo userMinInfo) {
		loggedUser = userMinInfo;
	}
}