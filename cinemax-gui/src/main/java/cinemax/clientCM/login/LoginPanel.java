/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.clientCM.login;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import cinemax.application.services.TcpClient;
import cinemax.clientCM.RegistratiBox;
import cinemax.clientCM.callback.LoginCallBack;
import cinemax.contracts.dto.UserMinInfo;

/**
 * Pannello UI che gestisce l'interfaccia di autenticazione e sessione dell'utente.
 * <p>
 * Implementa {@link LoginCallBack} e utilizza un layout a schede ({@link CardLayout}) per commutare dinamicamente
 * la vista tra lo stato disconnesso (pulsanti "Registrati" e "Accedi") e lo stato autenticato
 * (messaggio di benvenuto personalizzato e pulsante "Esci").
 * </p>
 */
public class LoginPanel extends JPanel implements LoginCallBack {

	private static final long serialVersionUID = 1L;

	private static final String CARD_LOGIN = "login";
	private static final String CARD_LOGGED = "logged";
	private static final Font FONT_WELCOME = new Font("Monospaced", Font.PLAIN, 20);

	private final TcpClient tcpClient;
	private final Consumer<UserMinInfo> loginCallBack;
	private final Runnable logoutCallBack;

	private CardLayout cardLayout;
	private JPanel containerPanel;
	private JLabel labelUser;

	/**
	 * Costruisce il pannello di gestione dell'autenticazione.
	 *
	 * @param tcpClient      il client TCP per l'inoltro delle richieste di autenticazione e registrazione
	 * @param loginCallBack  callback invocato al completamento con successo del login
	 * @param logoutCallBack callback invocato alla disconnessione dell'utente
	 */
	public LoginPanel(TcpClient tcpClient, Consumer<UserMinInfo> loginCallBack, Runnable logoutCallBack) {
		this.tcpClient = tcpClient;
		this.loginCallBack = loginCallBack;
		this.logoutCallBack = logoutCallBack;
	}

	/**
	 * Costruisce e assembla la gerarchia dei componenti grafici del pannello di login.
	 *
	 * @return il componente {@link JPanel} configurato con il layout a schede
	 */
	public JPanel build() {
		cardLayout = new CardLayout();
		containerPanel = new JPanel(cardLayout);
		containerPanel.setMaximumSize(new Dimension(1000, 100));

		// Pannello stato: Utente Non Autenticato (Login)
		JPanel unloggedPanel = new JPanel();
		unloggedPanel.setLayout(new BoxLayout(unloggedPanel, BoxLayout.X_AXIS));

		JButton btnRegistrati = new JButton("Registrati");
		btnRegistrati.addActionListener(e -> {
			Window parentWindow = SwingUtilities.getWindowAncestor(containerPanel);
			RegistratiBox popup = new RegistratiBox(parentWindow, tcpClient);
			popup.setVisible(true);
		});

		JButton btnLogin = new JButton("Accedi");
		btnLogin.addActionListener(e -> {
			Window parentWindow = SwingUtilities.getWindowAncestor(containerPanel);
			LoginBox popup = new LoginBox(parentWindow, tcpClient);
			popup.show(LoginPanel.this);
		});

		unloggedPanel.add(Box.createHorizontalGlue());
		unloggedPanel.add(btnRegistrati);
		unloggedPanel.add(Box.createRigidArea(new Dimension(5, 10)));
		unloggedPanel.add(btnLogin);
		unloggedPanel.add(Box.createRigidArea(new Dimension(5, 10)));

		// Pannello stato: Utente Autenticato (Logged)
		JPanel loggedPanel = new JPanel();
		loggedPanel.setLayout(new BoxLayout(loggedPanel, BoxLayout.X_AXIS));

		labelUser = new JLabel();
		labelUser.setFont(FONT_WELCOME);
		labelUser.setForeground(Color.BLUE);

		JButton btnEsci = new JButton("Esci");
		btnEsci.addActionListener(e -> {
			if (logoutCallBack != null) {
				logoutCallBack.run();
			}
			cardLayout.show(containerPanel, CARD_LOGIN);
		});

		loggedPanel.add(Box.createHorizontalGlue());
		loggedPanel.add(labelUser);
		loggedPanel.add(Box.createRigidArea(new Dimension(5, 10)));
		loggedPanel.add(btnEsci);
		loggedPanel.add(Box.createRigidArea(new Dimension(5, 10)));

		containerPanel.add(unloggedPanel, CARD_LOGIN);
		containerPanel.add(loggedPanel, CARD_LOGGED);

		return containerPanel;
	}

	/**
	 * Notifica l'avvenuto login con successo aggiornando l'interfaccia utente e invocando il listener associato.
	 *
	 * @param user l'istanza {@link UserMinInfo} contenente le informazioni minime dell'utente autenticato
	 */
	@Override
	public void onLoginSuccess(UserMinInfo user) {
		if (user != null) {
			labelUser.setText("Ciao " + (user.getNome() != null ? user.getNome() : ""));
		}
		cardLayout.show(containerPanel, CARD_LOGGED);

		if (loginCallBack != null) {
			loginCallBack.accept(user);
		}
	}

	/**
	 * Notifica il fallimento dell'autenticazione con il relativo messaggio di errore.
	 *
	 * @param errorMessage il messaggio descrittivo dell'errore
	 */
	@Override
	public void onLoginFailed(String errorMessage) {
		// Eventuale gestione centralizzata errori di autenticazione
	}
}

