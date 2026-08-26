package cinemax.clientCM.login;



import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import cinemax.application.services.TcpClient;
import cinemax.application.services.UserService;
import cinemax.clientCM.callback.LoginCallBack;
import cinemax.contracts.dto.UserMinInfo;
import cinemax.contracts.responses.GetUserByCredentialResponse;


public class LoginBox extends JDialog {
    private JTextField usernameField;
    private JTextField passwordField;
    
    private UserService userService;



    public LoginBox(Window owner, TcpClient tcpClient) {
        // Configurazione della finestra di dialogo
    	super(owner, "Entra nel tuo Account", ModalityType.APPLICATION_MODAL);
        setSize(300, 200);
        setLayout(new BorderLayout());
        setLocationRelativeTo(owner);
        
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
                
	                GetUserByCredentialResponse responseGetUser = userService.getUserByCredentials(username, pwd);
	                if (responseGetUser!=null) {
	                            JOptionPane.showMessageDialog(LoginBox.this, "Benvenuto " + responseGetUser.getUser().getNome(), "Successo", JOptionPane.INFORMATION_MESSAGE);
	                            dispose();
	                            SwingUtilities.invokeLater(() ->  callBack.onLoginSuccess(responseGetUser.getUser()));
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

        buttonPanel.add(cancelButton);
        buttonPanel.add(loginButton);
        
//        buttonPanel.setBorder(new EmptyBorder(10, 10, 20, 0));
        add(buttonPanel, BorderLayout.SOUTH);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    }

}
