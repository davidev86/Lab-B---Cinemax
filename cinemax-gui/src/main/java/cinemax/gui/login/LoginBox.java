package cinemax.gui.login;



import javax.swing.*;
import javax.swing.border.EmptyBorder;

import cinemax.application.services.TcpClient;
import cinemax.application.services.UserService;
import cinemax.contracts.dto.UserDetails;
import cinemax.contracts.responses.GetUserByCredentialResponse;
import cinemax.gui.callback.LoginCallBack;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoginBox extends JDialog {
    private JTextField usernameField;
    private JTextField passwordField;
    
    private UserService userService;



    public LoginBox(JPanel parent, TcpClient tcpClient) {
        // Configurazione della finestra di dialogo
        super();
        setSize(300, 200);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);
        
        this.userService = new UserService(tcpClient);
    }

    public void Show(LoginCallBack callBack){
        // Pannello principale
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        // Etichette e campi di testo
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        add(panel, BorderLayout.CENTER);

        // Pannello dei pulsanti
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Annulla");

        // Azione pulsante Login
        loginButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {

                String username = usernameField.getText();
                String pwd = passwordField.getText();

                
                new Thread(() ->{
                
	                GetUserByCredentialResponse user = userService.getUserByCredentials(username, pwd);
	
	                if (user!=null) {
	                            JOptionPane.showMessageDialog(LoginBox.this, "Login riuscito!", "Successo", JOptionPane.INFORMATION_MESSAGE);
	                            dispose();
	                            SwingUtilities.invokeLater(() ->  callBack.onLoginSuccess(user.getUser()));
	                } else {
	                    JOptionPane.showMessageDialog(LoginBox.this, "Credenziali errate!", "Errore", JOptionPane.ERROR_MESSAGE);
	                }
                }).start();
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
//        buttonPanel.setBorder(new EmptyBorder(10, 10, 20, 0));
        add(buttonPanel, BorderLayout.SOUTH);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    }

}
