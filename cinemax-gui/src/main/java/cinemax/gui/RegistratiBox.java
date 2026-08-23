/*
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */

package cinemax.gui;

import cinemax.application.services.TcpClient;
import cinemax.application.services.UserService;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/*
 * Dialog modale di registrazione utenti con layout GridBagLayout strutturato
 * gestione asincrona delle chiamate.
 */

public class RegistratiBox extends JDialog {

    private static final Font FONT_BASE = new Font("Tahoma", Font.PLAIN, 12);
    private static final Font FONT_BOLD = new Font("Tahoma", Font.BOLD, 12);
    private static final Font FONT_TITLE = new Font("Tahoma", Font.BOLD, 15);
    private static final Font FONT_HINT = new Font("Tahoma", Font.ITALIC, 11);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JTextField nomeField;
    private final JTextField cognomeField;
    private final JFormattedTextField dataNascitaField;
    private final JTextField domicilioField;

    private final JButton btnRegistrati;
    private final JButton btnAnnulla;
    private final UserService userService;

    public RegistratiBox(JPanel parent, TcpClient tcpClient) {
        super(SwingUtilities.getWindowAncestor(parent), "Registrazione Account", ModalityType.APPLICATION_MODAL);
        this.userService = new UserService(tcpClient);

        setLayout(new BorderLayout(0, 0));
        setResizable(false);

        // HEADER: Titolo e Sottotitolo
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(245, 246, 248));
        headerPanel.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(220, 224, 230)),
                new EmptyBorder(14, 18, 14, 18)
        ));

        JLabel titleLabel = new JLabel("Crea un nuovo account");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Compila i campi sottostanti per registrarti al servizio.");
        subtitleLabel.setFont(FONT_HINT);
        subtitleLabel.setForeground(Color.DARK_GRAY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        headerPanel.add(subtitleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // FORM CENTRALE: GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(14, 18, 10, 18));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 4, 5, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        this.usernameField = new JTextField(18);
        this.passwordField = new JPasswordField(18);
        this.nomeField = new JTextField(18);
        this.cognomeField = new JTextField(18);
        this.dataNascitaField = creaCampoData();
        this.domicilioField = new JTextField(18);

        usernameField.setFont(FONT_BASE);
        passwordField.setFont(FONT_BASE);
        nomeField.setFont(FONT_BASE);
        cognomeField.setFont(FONT_BASE);
        domicilioField.setFont(FONT_BASE);

        int riga = 0;
        aggiungiRigaForm(formPanel, "Username *:", usernameField, gbc, riga++);
        aggiungiRigaForm(formPanel, "Password *:", passwordField, gbc, riga++);
        aggiungiRigaForm(formPanel, "Nome: *", nomeField, gbc, riga++);
        aggiungiRigaForm(formPanel, "Cognome: *", cognomeField, gbc, riga++);
        aggiungiRigaForm(formPanel, "Data di Nascita:", dataNascitaField, gbc, riga++);
        aggiungiRigaForm(formPanel, "Domicilio: *", domicilioField, gbc, riga++);

        // Nota sui campi obbligatori
        gbc.gridx = 1;
        gbc.gridy = riga;
        gbc.weightx = 1.0;
        JLabel lblObbligatorio = new JLabel("* Campi obbligatori");
        lblObbligatorio.setFont(FONT_HINT);
        lblObbligatorio.setForeground(Color.GRAY);
        formPanel.add(lblObbligatorio, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Pulsanti Azione
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footerPanel.setBorder(new MatteBorder(1, 0, 0, 0, new Color(230, 232, 235)));

        this.btnAnnulla = new JButton("Annulla");
        this.btnAnnulla.setFont(FONT_BASE);
        this.btnAnnulla.setPreferredSize(new Dimension(90, 28));
        this.btnAnnulla.addActionListener(e -> dispose());

        this.btnRegistrati = new JButton("Registrati");
        this.btnRegistrati.setFont(FONT_BOLD);
        this.btnRegistrati.setPreferredSize(new Dimension(105, 28));
        this.btnRegistrati.addActionListener(e -> eseguiRegistrazione());

        footerPanel.add(btnAnnulla);
        footerPanel.add(btnRegistrati);
        add(footerPanel, BorderLayout.SOUTH);

        // Associa il tasto Invio (Enter) al pulsante Registrati
        getRootPane().setDefaultButton(btnRegistrati);

        pack();
        setLocationRelativeTo(parent);
    }

    // =========================================================================
    // LOGICA DI REGISTRAZIONE ASINCRONA
    // =========================================================================

    private void eseguiRegistrazione() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String nome = nomeField.getText().trim();
        String cognome = cognomeField.getText().trim();
        String domicilio = domicilioField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || nome.isEmpty() ||cognome.isEmpty() || domicilio.isEmpty()) {
            mostraMessaggio("Completa i campi obbligatori per completare la registrazione.", 
                    "Campi Mancanti", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate dataNascita = parseDataNascita();
        if (dataNascita == null && !isDataFieldEmpty()) {
            mostraMessaggio("Formato data non valido! Inserire gg/mm/aaaa o lasciare vuoto.", 
                    "Errore Data", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setStatoControlli(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                userService.insertUser(username, password, nome, cognome, dataNascita, domicilio);
                return null;
            }

            @Override
            protected void done() {
                setStatoControlli(true);
                setCursor(Cursor.getDefaultCursor());

                try {
                    get();
                    mostraMessaggio("Registrazione completata con successo!", 
                            "Operazione Riuscita", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (Exception ex) {
                    mostraMessaggio("Errore durante la registrazione: " + ex.getMessage(), 
                            "Errore Server", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    // =========================================================================
    // UTILITY DI LAYOUT E PARSING
    // =========================================================================

    private void aggiungiRigaForm(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc, int riga) {
        gbc.gridy = riga;

        // Label a larghezza naturale, allineata a destra verso il campo
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel label = new JLabel(labelText);
        label.setFont(FONT_BASE);
        panel.add(label, gbc);

        // Campo di input espanso orizzontalmente
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(field, gbc);
    }

    private JFormattedTextField creaCampoData() {
        JFormattedTextField field = new JFormattedTextField();
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            field.setFormatterFactory(new DefaultFormatterFactory(mask));
            field.setFont(FONT_BASE);
            field.setColumns(18);
        } catch (ParseException ignored) {}
        return field;
    }

    private boolean isDataFieldEmpty() {
        String text = dataNascitaField.getText().replace("_", "").replace("/", "").trim();
        return text.isEmpty();
    }

    private LocalDate parseDataNascita() {
        if (isDataFieldEmpty()) return null;
        try {
            return LocalDate.parse(dataNascitaField.getText().trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private void setStatoControlli(boolean enabled) {
        btnRegistrati.setEnabled(enabled);
        btnAnnulla.setEnabled(enabled);
        usernameField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        nomeField.setEnabled(enabled);
        cognomeField.setEnabled(enabled);
        dataNascitaField.setEnabled(enabled);
        domicilioField.setEnabled(enabled);
    }

    private void mostraMessaggio(String testo, String titolo, int tipo) {
        JOptionPane.showMessageDialog(this, testo, titolo, tipo);
    }
}