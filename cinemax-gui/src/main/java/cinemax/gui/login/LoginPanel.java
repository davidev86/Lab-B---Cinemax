package cinemax.gui.login;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cinemax.application.services.TcpClient;
import cinemax.contracts.dto.UserMinInfo;
import cinemax.gui.RegistratiBox;
import cinemax.gui.callback.LoginCallBack;

public class LoginPanel extends JPanel implements LoginCallBack {

	Consumer<UserMinInfo> loginCallBack;
	Runnable logoutCallBack;
	CardLayout cardLayout;
	JPanel panel;
	JLabel labelUser;
	TcpClient tcpClient;
	RegistratiBox popup;

	public LoginPanel(TcpClient tcpClient, Consumer<UserMinInfo> loginCallBack, Runnable logoutCallBack) {
		this.loginCallBack = loginCallBack;
		this.logoutCallBack = logoutCallBack;
		this.tcpClient = tcpClient;
	}

	public JPanel build() {

		//Creazione pannello generale
		panel = new JPanel(cardLayout = new CardLayout());
		panel.setMaximumSize(new Dimension(1000, 100));

		// Creazione pannello login
		JPanel login = new JPanel();
		login.setLayout(new BoxLayout(login, BoxLayout.X_AXIS));

		//Creazione del bottone registrati
		JButton registratiBotton = new JButton("Registrati");
		registratiBotton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				RegistratiBox popup = new RegistratiBox(login, tcpClient);
				popup.setVisible(true);
				popup.setLocationRelativeTo(null);
			}
		});

		//Creazione del bottone login
		JButton loginBotton = new JButton("Accedi");
		loginBotton.addActionListener(new ActionListener() {

			
			public void actionPerformed(ActionEvent e) {
				LoginBox popup = new LoginBox(login, tcpClient); 
				popup.Show(LoginPanel.this);
				popup.setVisible(true);
				popup.setLocationRelativeTo(null);
			}
		});

		//composizione pannello login
		login.add(Box.createHorizontalGlue());
		login.add(registratiBotton);
		login.add(Box.createRigidArea(new Dimension(5, 10)));
		login.add(loginBotton);
		login.add(Box.createRigidArea(new Dimension(5, 10)));
		login.setVisible(true);

		//Creazione pannello logged
		JPanel logged = new JPanel();
		logged.setLayout(new BoxLayout(logged, BoxLayout.X_AXIS));

		//Creazione del bottone registrati
		JButton esciButton = new JButton("Esci");
		esciButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
			
				if (logoutCallBack != null) {
					logoutCallBack.run(); // Avvisa il listener di logout
				}				
				
				cardLayout.show(panel, "login");
			}
			
			
		});

		//Creazione della label di benvenuto
		labelUser = new JLabel();
		labelUser.setFont(new Font("Monospaced", Font.PLAIN, 20));
		labelUser.setForeground(Color.BLUE);
		logged.add(Box.createHorizontalGlue());
		logged.add(labelUser);
		logged.add(Box.createRigidArea(new Dimension(5, 10)));
		logged.add(esciButton);
		logged.add(Box.createRigidArea(new Dimension(5, 10)));

		//aggiunta pannelli login e logged al panel principale
		panel.add(login,"login");
		panel.add(logged, "logged");

		return panel;
	}

	
	public void onLoginSuccess(UserMinInfo user) {
		labelUser.setText("Ciao " + user.getNome());
		cardLayout.show(panel, "logged");
			this.loginCallBack.accept(user);
	}

	
	public void onLoginFailed(String errorMessage) {

	}
}
