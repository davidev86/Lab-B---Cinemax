

import javax.swing.*;

import BookRecommender.Application.Services.UserService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

LoginPanel extends JPanel {

	public JPanel build() {

		//Creazione pannello
		JPanel login = new JPanel();
		login.setLayout(new BoxLayout(login, BoxLayout.X_AXIS));

		//Creazione del bottone registrati
		JButton registratiBotton = new JButton("Registrati");
		registratiBotton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				RegistratiBox popup = new RegistratiBox(login);
				popup.setVisible(true);
				popup.setLocationRelativeTo(null);
			}
		});

		//Creazione del bottone login
		JButton loginBotton = new JButton("Accedi");
		loginBotton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				LoginBox popup = new LoginBox(login);
				popup.setVisible(true);
				popup.setLocationRelativeTo(null);

				//UserService userService = new UserService(filePath);
				//userService.login("",popup.usernameField.getText(), popup.passwordField.getText());

			}
		});

		login.add(Box.createHorizontalGlue());
		login.add(registratiBotton);
		login.add(Box.createRigidArea(new Dimension(5, 10)));
		login.add(loginBotton);
		login.add(Box.createRigidArea(new Dimension(5, 10)));
		login.setVisible(true);

		return login;
	}


}
