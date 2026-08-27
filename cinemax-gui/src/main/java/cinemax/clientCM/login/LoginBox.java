/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.clientCM.login;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import cinemax.application.services.TcpClient;
import cinemax.application.services.UserService;
import cinemax.clientCM.callback.LoginCallBack;
import cinemax.contracts.responses.GetUserByCredentialResponse;

/**
 * Finestra di dialogo modale per l'autenticazione dell'utente nel sistema Cinemax.
 * <p>
 * Acquisisce le credenziali (username e password), esegue la chiamata asincrona di verifica
 * al server tramite {@link UserService} e notifica il risultato all'applicazione invocante
 * per mezzo del callback {@link LoginCallBack}.
 * </p>
 */
public class LoginBox extends JDialog {

    private static final long serialVersionUID = 1L;

    private static final Font FONT_BASE = new Font("Tahoma", Font.PLAIN, 12);
    private static final Font FONT_BOLD = new Font("Tahoma", Font.BOLD, 12);

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton cancelButton;

    private final UserService userService;
    private LoginCallBack loginCallBack;

    /**
     * Costruisce e configura la finestra modale di autenticazione.
     *
     * @param owner     la finestra genitore (parent window)
     * @param tcpClient il client di rete per l'inoltro delle richieste verso il server
     */
    public LoginBox(Window owner, TcpClient tcpClient) {
        super(owner, "Entra nel tuo Account", ModalityType.APPLICATION_MODAL);
        this.userService = new UserService(tcpClient);

        setSize(340, 210);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // FORM CENTRALE
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(15, 15, 10, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(FONT_BOLD);
        this.usernameField = new JTextField(15);
        this.usernameField.setFont(FONT_BASE);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(FONT_BOLD);
        this.passwordField = new JPasswordField(15);
        this.passwordField.setFont(FONT_BASE);

        // Riga 0: Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(lblUsername, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(usernameField, gbc);

        // Riga 1: Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(lblPassword, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(passwordField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // FOOTER: Pulsanti di Azione
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        this.cancelButton = new JButton("Annulla");
        this.cancelButton.setFont(FONT_BASE);
        this.cancelButton.setPreferredSize(new Dimension(85, 28));
        this.cancelButton.addActionListener(e -> dispose());

        this.loginButton = new JButton("Login");
        this.loginButton.setFont(FONT_BOLD);
        this.loginButton.setPreferredSize(new Dimension(85, 28));
        this.loginButton.addActionListener(e -> eseguiAutenticazione());

        buttonPanel.add(cancelButton);
        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(loginButton);
    }

    /**
     * Associa il callback di autenticazione e rende visibile la finestra di dialogo.
     *
     * @param callBack il callback invocato in caso di login completato con successo
     */
    public void show(LoginCallBack callBack) {
        this.loginCallBack = callBack;
        setVisible(true);
    }

    /**
     * Alias di compatibilità per la visualizzazione della finestra di dialogo.
     *
     * @param callBack il callback invocato in caso di login completato con successo
     */
    public void Show(LoginCallBack callBack) {
        show(callBack);
    }

    /**
     * Valida i campi inseriti e avvia la procedura di autenticazione asincrona tramite {@link SwingWorker}.
     */
    private void eseguiAutenticazione() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Inserire sia lo username che la password per accedere.",
                    "Dati Incompleti",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        setStatoControlli(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<GetUserByCredentialResponse, Void>() {
            @Override
            protected GetUserByCredentialResponse doInBackground() throws Exception {
                return userService.getUserByCredentials(username, password);
            }

            @Override
            protected void done() {
                setStatoControlli(true);
                setCursor(Cursor.getDefaultCursor());

                try {
                    GetUserByCredentialResponse response = get();
                    if (response != null && response.getUser() != null) {
                        JOptionPane.showMessageDialog(
                                LoginBox.this,
                                "Benvenuto " + response.getUser().getNome() + " " + response.getUser().getCognome(),
                                "Accesso Eseguito",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        dispose();
                        if (loginCallBack != null) {
                            loginCallBack.onLoginSuccess(response.getUser());
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                                LoginBox.this,
                                "Credenziali non valide! Riprovare.",
                                "Errore di Autenticazione",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    JOptionPane.showMessageDialog(
                            LoginBox.this,
                            "Errore durante la comunicazione con il server: " + cause.getMessage(),
                            "Errore di Connessione",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }.execute();
    }

    /**
     * Abilita o disabilita interattivamente i componenti del form durante l'elaborazione di rete.
     *
     * @param enabled {@code true} per abilitare i controlli, {@code false} per bloccarli
     */
    private void setStatoControlli(boolean enabled) {
        loginButton.setEnabled(enabled);
        cancelButton.setEnabled(enabled);
        usernameField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
    }
}