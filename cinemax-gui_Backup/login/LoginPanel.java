

import BookRecommender.Application.DTO.UserDTO;
import bookRecommender.callbacks.LoginCallBack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

LoginPanel extends JPanel implements LoginCallBack {

	Consumer<UserDTO> _loginCallBack;
	Consumer<UserDTO> _logoutCallBack;
	CardLayout cardLayout;
	JLabel labelUser;

	public LoginPanel(Consumer<UserDTO> loginCallBack, Consumer<UserDTO> logoutCallBack) {
		this._loginCallBack = loginCallBack;
		this._logoutCallBack = logoutCallBack;

		//Define layout
		//Creazione pannello generale

		setLayout(cardLayout = new CardLayout());
		setMaximumSize(new Dimension(1000, 100));

		JPanel loginPanel = getLoginPanel();
		JPanel loggedPanel = getLoggedPanel();

		//aggiunta pannelli login e logged al panel principale
		add(loginPanel,"login");
		add(loggedPanel, "logged");

	}

	private JPanel getLoginPanel() {
		JPanel parent = this;

		// Creazione pannello login
		JPanel login = new JPanel();
		login.setLayout(new BoxLayout(login, BoxLayout.X_AXIS));

		//Creazione del bottone registrati
		JButton registratiBotton = new JButton("Registrati");
		registratiBotton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				RegistrationBox popup = new RegistrationBox(login);
				popup.setVisible(true);
				popup.setLocationRelativeTo(null);
			}
		});


		//Creazione del bottone login
		JButton loginBotton = new JButton("Accedi");
		loginBotton.addActionListener(new ActionListener() {

			
			public void actionPerformed(ActionEvent e) {
				LoginBox popup = new LoginBox(login);
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

		return login;
	}

	private JPanel getLoggedPanel() {

		JPanel parent = this;

		//Creazione pannello logged
		JPanel logged = new JPanel();
		logged.setLayout(new BoxLayout(logged, BoxLayout.X_AXIS));

		//Creazione del bottone registrati
		JButton esciButton = new JButton("Esci");
		esciButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(parent, "login");
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

		return logged;

	}


	
	public void onLoginSuccess(UserDTO user) {
		labelUser.setText("Ciao " + user.nome);
		cardLayout.show(this, "logged");
			this._loginCallBack.accept(user);
	}

	
	public void onLoginFailed(String errorMessage) {

	}
}
