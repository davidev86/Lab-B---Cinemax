

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


RegistratiBox extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public RegistratiBox(JPanel parent) {
        // Configurazione della finestra di dialogo
        super();
        setSize(400, 200);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);


        // Pannello principale
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Etichette e campi di testo
        JLabel usernameLabel = new JLabel("Inserisci nuova Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Inserisci nuova Password:");
        passwordField = new JPasswordField();

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);

        add(panel, BorderLayout.CENTER);

        // Pannello dei pulsanti
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Registrati");
        JButton cancelButton = new JButton("Annulla");

        // Azione pulsante Login
        loginButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

//                if (validateCredentials(username, password)) {
//                    JOptionPane.showMessageDialog(RegistratiBox.this, "Login riuscito!", "Successo", JOptionPane.INFORMATION_MESSAGE);
//                    dispose();
//                } else {
//                    JOptionPane.showMessageDialog(RegistratiBox.this, "Credenziali errate!", "Errore", JOptionPane.ERROR_MESSAGE);
//                }
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

    // Metodo per validare le credenziali (esempio semplice)
    private boolean validateCredentials(String username, String password) {
        return "admin".equals(username) && "password".equals(password); // Puoi personalizzarlo
    }
}
