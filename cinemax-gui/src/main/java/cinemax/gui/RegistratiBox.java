
import cinemax.application.services.TcpClient;
import cinemax.application.services.UserService;
import cinemax.contracts.dto.UserMinInfos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class RegistratiBox extends JDialog {
        JTextField usernameField;
        JTextField passwordField;
        JTextField nomeField;
        JTextField cognomeField;
        JTextField dataNascitaField;
        JTextField indirizzoField;

    public RegistratiBox(JPanel parent, TcpClient tcpClient) {
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
        JLabel password = new JLabel("Password:");
        passwordField = new JPasswordField();
        JLabel nome = new JLabel("Nome:");
        nomeField = new JTextField();
        JLabel cognome = new JLabel("Cognome:");
        cognomeField = new JTextField();
        JLabel dataNascita = new JLabel("data di nascita:");
        dataNascitaField = new JTextField();
        JLabel indirizzo = new JLabel("Indirizzo:");
        indirizzoField = new JTextField();
        
       

        panel.add(username);
        panel.add(usernameField);
        panel.add(password);
        panel.add(passwordField);
        panel.add(nome);
        panel.add(nomeField);
        panel.add(cognome);
        panel.add(cognomeField);
        panel.add(dataNascita);
        panel.add(dataNascitaField);
        panel.add(indirizzo);
        panel.add(indirizzoField);
       
      

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
                String pwd = passwordField.getText();
                String indirizzo = indirizzoField.getText();
                String dataNascita = dataNascitaField.getText();
                

             UserService userService = new UserService(tcpClient);
           
            
             try {
				userService.StoreUserResponse(username, password,   nome,  cognome,	  dataNascita,	  domicilio);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				
				//Uno dei parametri non é valid
				
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
