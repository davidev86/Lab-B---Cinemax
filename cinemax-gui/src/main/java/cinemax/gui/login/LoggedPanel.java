package cinemax.gui.login;


import cinemax.application.services.TcpClient;
import cinemax.application.services.UserService;
import cinemax.contracts.dto.UserDetails;
import cinemax.contracts.responses.GetUserByCredentialResponse;
import cinemax.gui.callback.LoginCallBack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoggedPanel extends JPanel {

    private final JLabel lblUtente;
    private final JButton btnEsci;

    public LoggedPanel(LoginCallBack onLogoutCallback) {
    	
    	this.userService = new UserService(tcpClient);
    	
        // Layout direttamente sul pannello principale
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        this.lblUtente = new JLabel("Ciao, Utente");
        this.lblUtente.setFont(new Font("Tahoma", Font.PLAIN, 12));

        this.btnEsci = new JButton("Esci");
        this.btnEsci.setFont(new Font("Tahoma", Font.BOLD, 12));

        // Azione al click su "Esci"
        this.btnEsci.addActionListener(e -> {
            // Usa il pulsante stesso come riferimento genitore per la dialog
            Component source = (Component) e.getSource();
            JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(source),
                "Logout effettuato con successo.",
                "Logout",
                JOptionPane.INFORMATION_MESSAGE
            );

            // Notifica l'evento di logout (es. per disattivare i tab in TabPanel / Home)
            if (onLogoutCallback != null) {
            	dispose();
            	SwingUtilities.invokeLater(() ->  callBack.onLogoutCallBack(user.getUser()));
            }
        });

        // Composizione degli elementi
        add(Box.createHorizontalGlue());
        add(lblUtente);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(btnEsci);
        add(Box.createRigidArea(new Dimension(10, 0)));
    }

    /**
     * Metodo per aggiornare il nome utente visualizzato dopo il login
     */
    public void setUsername(String username) {
        if (username != null && !username.trim().isEmpty()) {
            this.lblUtente.setText("Ciao, " + username);
        } else {
            this.lblUtente.setText("Ciao, Utente");
        }
    }
} 