package cinemax.gui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import cinemax.application.services.TcpClient;
import cinemax.application.services.UserService;


public class RegistratiBox extends JDialog {
        JTextField usernameField;
        JTextField passwordField;
        JTextField nomeField;
        JTextField cognomeField;
        JTextField dataNascitaField;
        JTextField domicilioField;

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
        dataNascitaField = new JTextField(10);
        JLabel domicilio = new JLabel("Domicilio:");
        domicilioField = new JTextField();
        
       

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
        panel.add(domicilio);
        panel.add(domicilioField);
       
      

        add(panel, BorderLayout.CENTER);

        // Pannello dei pulsanti
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Registrati");
        JButton cancelButton = new JButton("Annulla");

        // Azione pulsante Login
        loginButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {

                String username = usernameField.getText();
                String nome = nomeField.getText();
                String cognome = cognomeField.getText();
                String password = passwordField.getText();
                String domicilio = domicilioField.getText();
                LocalDate dataNascita = getLocalDateFromField(dataNascitaField.getText());
                

             UserService userService = new UserService(tcpClient);
           
             
             try {
				userService.insertUser(username, password, nome, cognome, dataNascita, domicilio);
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
    
    
    public LocalDate getLocalDateFromField(String inputField) {
        String input = inputField.trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate date = LocalDate.parse(input, formatter);
            return date;
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(null, 
                "Formato data non valido! Usa il formato gg/mm/aaaa.", 
                "Errore Data", 
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
    
    
    }
    
    
}
