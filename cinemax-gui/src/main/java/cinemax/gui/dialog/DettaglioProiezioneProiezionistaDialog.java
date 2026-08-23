package cinemax.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import cinemax.contracts.dto.FilmDetails;
import cinemax.contracts.dto.ProjectionDetails;

public class DettaglioProiezioneProiezionistaDialog extends JDialog {

    private static final Font FONT_BASE = new Font("Tahoma", Font.PLAIN, 12);
    private static final Font FONT_BOLD = new Font("Tahoma", Font.BOLD, 12);
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ProjectionDetails proiezioneCorrente;
    private final JFormattedTextField textFieldDataOra;
    private final JFormattedTextField textFieldCostoBiglietto;

    public DettaglioProiezioneProiezionistaDialog(
            Window owner, 
            ProjectionDetails proiezione,
            Consumer<ProjectionDetails> onModificaCallback,
            Consumer<ProjectionDetails> onCancellaCallback,
            Consumer<ProjectionDetails> onInsertCallback) {

        super(owner, "Gestione Proiezione (Proiezionista)", ModalityType.APPLICATION_MODAL);
        this.proiezioneCorrente = (proiezione != null) ? proiezione : new ProjectionDetails();

        boolean exists = (this.proiezioneCorrente.getId() != null);

        setSize(520, 560);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // =====================================================================
        // 1. SCHEDA DATI PROIEZIONE
        // =====================================================================
        JPanel mainCenterPanel = new JPanel();
        mainCenterPanel.setLayout(new BoxLayout(mainCenterPanel, BoxLayout.Y_AXIS));
        mainCenterPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new CompoundBorder(
                new EmptyBorder(5, 5, 5, 5),
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        exists ? " Modifica Proiezione " : " Inserimento Nuova Proiezione ",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        FONT_BOLD
                )
        ));

        JLabel lblTitoloFilm = new JLabel(this.proiezioneCorrente.getTitoloFilm() != null ? this.proiezioneCorrente.getTitoloFilm() : "-");
        lblTitoloFilm.setFont(new Font("Tahoma", Font.BOLD, 14));

        JPanel panelTitolo = new JPanel(new BorderLayout(5, 5));
        panelTitolo.setBorder(new EmptyBorder(5, 10, 10, 10));
        JLabel lblTitoloHeader = new JLabel("Titolo Film:");
        lblTitoloHeader.setFont(FONT_BOLD);
        panelTitolo.add(lblTitoloHeader, BorderLayout.NORTH);
        panelTitolo.add(lblTitoloFilm, BorderLayout.CENTER);
        cardPanel.add(panelTitolo);

        // Griglia Campi
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setBorder(new EmptyBorder(5, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String dataOraIniziale = (this.proiezioneCorrente.getDataOraProiezione() != null) 
                ? this.proiezioneCorrente.getDataOraProiezione().format(DATETIME_FORMATTER) 
                : "";
        this.textFieldDataOra = creaCampoDataOra(dataOraIniziale);

        this.textFieldCostoBiglietto = creaCampoValuta();
        this.textFieldCostoBiglietto.setValue(this.proiezioneCorrente.getCosto() != null ? this.proiezioneCorrente.getCosto() : BigDecimal.ZERO);

        int riga = 0;
        aggiungiRigaForm(formGrid, new JLabel("Data & Ora (gg/mm/aaaa hh:mm):"), textFieldDataOra, gbc, riga++);
        aggiungiRigaForm(formGrid, new JLabel("Prezzo Biglietto (€):"), textFieldCostoBiglietto, gbc, riga++);
        aggiungiRigaForm(formGrid, new JLabel("Regista:"), new JLabel(valoreODefault(this.proiezioneCorrente.getRegista())), gbc, riga++);
        aggiungiRigaForm(formGrid, new JLabel("Genere:"), new JLabel(valoreODefault(this.proiezioneCorrente.getGenere())), gbc, riga++);
        aggiungiRigaForm(formGrid, new JLabel("Anno Uscita:"), new JLabel(this.proiezioneCorrente.getAnno() != null ? String.valueOf(this.proiezioneCorrente.getAnno()) : "-"), gbc, riga++);
        aggiungiRigaForm(formGrid, new JLabel("Durata:"), new JLabel((this.proiezioneCorrente.getDurataMinuti() != null ? this.proiezioneCorrente.getDurataMinuti() : 0) + " min"), gbc, riga++);
        aggiungiRigaForm(formGrid, new JLabel("Età Minima:"), new JLabel((this.proiezioneCorrente.getEtaMinima() != null ? this.proiezioneCorrente.getEtaMinima() : 0) + " anni"), gbc, riga++);

        cardPanel.add(formGrid);
        mainCenterPanel.add(cardPanel);
        add(mainCenterPanel, BorderLayout.CENTER);

        // =====================================================================
        // 2. BOTTONI DI AZIONE CONDIZIONATI ALL'ID
        // =====================================================================
        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton btnAnnulla = new JButton("Chiudi");
        btnAnnulla.setFont(FONT_BASE);
        btnAnnulla.addActionListener(e -> dispose());

        if (exists) {
            // Caso Proiezione Esistente (ID Presente): Modifica e Cancellazione attivi
            JButton btnCancella = new JButton("Annulla Proiezione");
            btnCancella.setFont(FONT_BASE);
            btnCancella.setForeground(new Color(180, 0, 0));
            btnCancella.addActionListener(e -> {
                int conferma = JOptionPane.showConfirmDialog(
                        this,
                        "Sei sicuro di voler annullare definitivamente questa proiezione?",
                        "Conferma Cancellazione",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (conferma == JOptionPane.YES_OPTION) {
                    if (onCancellaCallback != null) {
                        onCancellaCallback.accept(proiezioneCorrente);
                    }
                    dispose();
                }
            });

            JButton btnModifica = new JButton("Salva Modifiche");
            btnModifica.setFont(FONT_BOLD);
            btnModifica.addActionListener(e -> {
                int conferma = JOptionPane.showConfirmDialog(
                        this,
                        "Sei sicuro di voler salvare le modifiche a questa proiezione?",
                        "Conferma Modifica",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (conferma == JOptionPane.YES_OPTION) {
                    if (validaEApplicaModifiche(onModificaCallback)) {
                        dispose();
                    }
                }
            });

            panelBottoni.add(btnCancella);
            panelBottoni.add(btnAnnulla);
            panelBottoni.add(btnModifica);

        } else {
            // Caso Nuova Proiezione (ID Nullo): Inserimento attivo
            JButton btnInserisci = new JButton("Inserisci nuova Proiezione");
            btnInserisci.setFont(FONT_BOLD);
            btnInserisci.addActionListener(e -> {
                int conferma = JOptionPane.showConfirmDialog(
                        this,
                        "Sei sicuro di voler inserire questa nuova proiezione a palinsesto?",
                        "Conferma Inserimento",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (conferma == JOptionPane.YES_OPTION) {
                    if (validaEApplicaModifiche(onInsertCallback)) {
                        dispose();
                    }
                }
            });

            panelBottoni.add(btnAnnulla);
            panelBottoni.add(btnInserisci);
        }

        add(panelBottoni, BorderLayout.SOUTH);
    }

    // =========================================================================
    // VALIDAZIONE E SALVATAGGIO
    // =========================================================================

    private boolean validaEApplicaModifiche(Consumer<ProjectionDetails> callback) {
        LocalDateTime nuovaDataOra = parseLocalDateTime(textFieldDataOra);
        if (nuovaDataOra == null) {
            JOptionPane.showMessageDialog(this, "Inserire data e ora nel formato gg/mm/aaaa hh:mm.", "Errore Data", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (nuovaDataOra.isBefore(LocalDateTime.now())) {
            JOptionPane.showMessageDialog(this, "La data di proiezione non può essere nel passato.", "Errore Data", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        BigDecimal nuovoCosto = getBigDecimalFromField(textFieldCostoBiglietto);
        if (nuovoCosto.compareTo(BigDecimal.ZERO) < 0) {
            JOptionPane.showMessageDialog(this, "Il prezzo non può essere negativo.", "Errore Prezzo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        proiezioneCorrente.setDataOraProiezione(nuovaDataOra);
        proiezioneCorrente.setCosto(nuovoCosto);

        if (callback != null) {
            callback.accept(proiezioneCorrente);
        }

        return true;
    }

    // =========================================================================
    // METODI AUSILIARI DI RENDERING E PARSING
    // =========================================================================

    private void aggiungiRigaForm(JPanel panel, JLabel label, JComponent field, GridBagConstraints gbc, int riga) {
        gbc.gridx = 0; gbc.gridy = riga; gbc.weightx = 0.0;
        label.setFont(FONT_BOLD);
        label.setForeground(Color.DARK_GRAY);
        panel.add(label, gbc);

        gbc.gridx = 1; gbc.gridy = riga; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private JFormattedTextField creaCampoDataOra(String valoreIniziale) {
        JFormattedTextField field = new JFormattedTextField();
        try {
            MaskFormatter mask = new MaskFormatter("##/##/#### ##:##");
            mask.setPlaceholderCharacter('_');
            field.setFormatterFactory(new DefaultFormatterFactory(mask));
            field.setColumns(14);
            field.setFont(FONT_BASE);
            if (valoreIniziale != null && !valoreIniziale.isEmpty()) {
                field.setText(valoreIniziale);
            }
        } catch (ParseException ignored) {}
        return field;
    }

    private JFormattedTextField creaCampoValuta() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);

        if (currencyFormat instanceof java.text.DecimalFormat) {
            ((java.text.DecimalFormat) currencyFormat).setParseBigDecimal(true);
        }

        JFormattedTextField field = new JFormattedTextField(currencyFormat);
        field.setColumns(10);
        field.setFont(FONT_BASE);
        return field;
    }

    private LocalDateTime parseLocalDateTime(JFormattedTextField field) {
        String text = field.getText().trim();
        if (text.contains("_") || text.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(text, DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private BigDecimal getBigDecimalFromField(JFormattedTextField field) {
        try {
            field.commitEdit();
        } catch (Exception ignored) {}

        Object value = field.getValue();
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        return BigDecimal.ZERO;
    }

    private String valoreODefault(String val) {
        return (val != null && !val.trim().isEmpty()) ? val : "N/D";
    }
}