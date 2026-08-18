

import BookRecommender.Application.DTO.UserDTO;
import BookRecommender.Application.Services.UserService;
import bookRecommender.callbacks.LoginCallBack;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


LoginBox extends JDialog {
    private JTextField usernameField;
    private JTextField passwordField;
    UserService userService;

    public LoginBox(JPanel parent) {
        // Configurazione della finestra di dialogo
        super();
        setSize(300, 200);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);
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
                String filePath = "C:/sources/";

                userService = new UserService(filePath);
                UserDTO user = userService.login(username, pwd);

                if (user!=null) {
                            JOptionPane.showMessageDialog(LoginBox.this, "Login riuscito!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                            callBack.onLoginSuccess(user);
                } else {
                    JOptionPane.showMessageDialog(LoginBox.this, "Credenziali errate!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
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
