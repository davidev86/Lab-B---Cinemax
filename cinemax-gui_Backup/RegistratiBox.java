import BookRecommender.Application.Services.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


RegistratiBox extends JDialog {
        JTextField usernameField;
        JTextField nomeField;
        JTextField cognomeField;
        JTextField codiceFiscaleField;
        JTextField emailField;
    JTextField passwordField;

    public RegistratiBox(JPanel parent) {
        // Configurazione della finestra di dialogo
        super();
        setSize(400, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);

        JLabel title = new JLabel("Per registrarti inserisci i tuoi dati:");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.PLAIN, 14));
        title.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // Pannello principale
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Etichette e campi di testo
        JLabel username = new JLabel("Username:");
        usernameField = new JTextField();
        username.setPreferredSize(new Dimension(250, 25));
        JLabel nome = new JLabel("Nome:");
        nomeField = new JTextField();
        JLabel cognome = new JLabel("Cognome:");
        cognomeField = new JTextField();
        JLabel codiceFiscale = new JLabel("Codice Fiscale:");
        codiceFiscaleField = new JTextField();
        JLabel email = new JLabel("email:");
        emailField = new JTextField();
        JLabel password = new JLabel("Password:");
        passwordField = new JPasswordField();

        panel.add(username);
        panel.add(usernameField);
        panel.add(nome);
        panel.add(nomeField);
        panel.add(cognome);
        panel.add(cognomeField);
        panel.add(codiceFiscale);
        panel.add(codiceFiscaleField);
        panel.add(email);
        panel.add(emailField);
        panel.add(password);
        panel.add(passwordField);

        add(panel, BorderLayout.CENTER);

        // Pannello dei pulsanti
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Registrati");
        JButton cancelButton = new JButton("Annulla");

        // Azione pulsante Login
        loginButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {

                String user = usernameField.getText();
                String name = nomeField.getText();
                String surname = cognomeField.getText();
                String codfisc = codiceFiscaleField.getText();
                String mail = emailField.getText();
                String pwd = passwordField.getText();

             UserService userService = new UserService("C:/sources/");
           
            
             try {
				userService.store(user, name, surname, codfisc, mail, pwd);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				
				//Uno dei parametri non é valido
				
				e1.printStackTrace();
			}
                dispose();
            }
        });

        // Azione pulsante Annulla
        cancelButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                dispose(); // Chiude il pop-up
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
