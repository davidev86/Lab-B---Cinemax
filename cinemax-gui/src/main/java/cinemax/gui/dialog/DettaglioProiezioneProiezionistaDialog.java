package cinemax.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.time.LocalDate;
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
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import cinemax.contracts.dto.ProjectionDetails;

public class DettaglioProiezioneProiezionistaDialog extends JDialog {

    private final ProjectionDetails proiezioneCorrente;
//	private final BookingService bookingService;

    // Campi modificabili
    private final JLabel lblTitoloFilm;
    private final JFormattedTextField textFieldDataOra;
    private final JFormattedTextField textFieldCostoBiglietto;

    public DettaglioProiezioneProiezionistaDialog(
            Window owner, 
            ProjectionDetails proiezione,
            Consumer<ProjectionDetails> onModificaCallback,
            Consumer<ProjectionDetails> onCancellaCallback) {

        super(owner, "Gestione Proiezione (Proiezionista)", ModalityType.APPLICATION_MODAL);
        this.proiezioneCorrente = proiezione;

        setSize(520, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        Font fontBase = new Font("Tahoma", Font.PLAIN, 12); 
        Font fontBold = new Font("Tahoma", Font.BOLD, 12);

        // --- VERIFICA PRENOTAZIONI ATTIVE ---
 //       boolean haPrenotazioni = verificaPresenzaPrenotazioni(proiezione);
//        boolean isModificabile = !haPrenotazioni;

        // --- 1. CONTENITORE PRINCIPALE ---
        JPanel mainCenterPanel = new JPanel();
        mainCenterPanel.setLayout(new BoxLayout(mainCenterPanel, BoxLayout.Y_AXIS));
        mainCenterPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
/*
        // Banner informativo se la modifica è bloccata
        if (haPrenotazioni) {
            JPanel bannerAvviso = new JPanel(new FlowLayout(FlowLayout.CENTER));
            bannerAvviso.setBackground(new Color(255, 235, 235));
            bannerAvviso.setBorder(BorderFactory.createLineBorder(new Color(220, 53, 69)));
            JLabel lblAvviso = new JLabel("<html><b>Attenzione:</b> Esistono prenotazioni per questo spettacolo. Modifica e cancellazione bloccate.</html>");
            lblAvviso.setForeground(new Color(150, 0, 0));
            bannerAvviso.add(lblAvviso);
            mainCenterPanel.add(bannerAvviso);
            mainCenterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
*/
        // --- 2. SCHEDA DETTAGLI PROIEZIONE ---
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new CompoundBorder(
                new EmptyBorder(5, 5, 5, 5),
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        " Modifica Parametri Spettacolo ",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        fontBold
                )
        ));
 
        this.lblTitoloFilm = new JLabel(proiezione.getTitoloFilm() != null ? proiezione.getTitoloFilm() : "");
        this.lblTitoloFilm.setFont(new Font("Tahoma", Font.BOLD, 14));
    //    this.textFieldTitoloFilm.setEditable(isModificabile);

        JPanel panelTitolo = new JPanel(new BorderLayout(5, 5));
        panelTitolo.setBorder(new EmptyBorder(5, 10, 10, 10));
        panelTitolo.add(new JLabel("Titolo Film:"), BorderLayout.NORTH);
        panelTitolo.add(lblTitoloFilm, BorderLayout.CENTER);
        cardPanel.add(panelTitolo);

        // Form con griglia
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setBorder(new EmptyBorder(5, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataOraIniziale = (proiezione.getDataOraProiezione() != null) 
                ? proiezione.getDataOraProiezione().format(dtf) 
                : "";
        this.textFieldDataOra = creaCampoDataOra(dataOraIniziale);
 //       this.textFieldDataOra.setEditable(isModificabile);

        this.textFieldCostoBiglietto = creaCampoValuta(fontBase);
        this.textFieldCostoBiglietto.setValue(proiezione.getCosto() != null ? proiezione.getCosto() : BigDecimal.ZERO);
 //       this.textFieldCostoBiglietto.setEditable(isModificabile);

        int riga = 0;
        aggiungiRigaForm(formGrid, new JLabel("Data & Ora (gg/mm/aaaa hh:mm):"), textFieldDataOra, gbc, riga++);
        aggiungiRigaForm(formGrid, new JLabel("Prezzo Biglietto (€):"), textFieldCostoBiglietto, gbc, riga++);
        aggiungiRigaForm(formGrid, new JLabel("Regista:"), new JLabel(valoreODefault(proiezione.getRegista())), gbc, riga++);
        aggiungiRigaForm(formGrid, new JLabel("Genere:"), new JLabel(valoreODefault(proiezione.getGenere())), gbc, riga++);
        aggiungiRigaForm(formGrid, new JLabel("Anno Uscita:"), new JLabel(proiezione.getAnno() != null ? String.valueOf(proiezione.getAnno()) : "-"), gbc, riga++);
        aggiungiRigaForm(formGrid, new JLabel("Durata:"), new JLabel((proiezione.getDurataMinuti() != null ? proiezione.getDurataMinuti() : 0) + " min"), gbc, riga++);
        aggiungiRigaForm(formGrid, new JLabel("Età Minima:"), new JLabel((proiezione.getEtaMinima() != null ? proiezione.getEtaMinima() : 0) + " anni"), gbc, riga++);

        cardPanel.add(formGrid);
        mainCenterPanel.add(cardPanel);
        add(mainCenterPanel, BorderLayout.CENTER);

        // --- 3. BOTTONI DI AZIONE ---
        JButton btnAnnulla = new JButton("Chiudi");
        JButton btnModifica = new JButton("Salva Modifiche");
        JButton btnCancella = new JButton("Annulla Proiezione");

        btnCancella.setForeground(new Color(180, 0, 0));
 //       btnModifica.setEnabled(isModificabile);
 //       btnCancella.setEnabled(isModificabile);

        btnModifica.addActionListener(e -> {
        	int conferma = JOptionPane.showConfirmDialog(
                    this,
                    "Sei sicuro di voler moodificare questa proiezione?",
                    "Modifica salvata",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );        	
        	
        	if (validaESalvaModifiche(onModificaCallback)) {
                dispose();
            
            } 
        });

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

        btnAnnulla.addActionListener(e -> dispose());

        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBottoni.add(btnCancella);
        panelBottoni.add(btnAnnulla);
        panelBottoni.add(btnModifica);
        add(panelBottoni, BorderLayout.SOUTH);
    }

    // =========================================================================
    // LOGICA DI CONTROLLO E VALIDAZIONE
    // =========================================================================

 /*   private boolean verificaPresenzaPrenotazioni(ProjectionDetails proiezione) {
        if (proiezione == null || proiezione.getNumeroPosti() == null) {
            return false;
        }
        // Se numeroPosti indica i posti già prenotati (> 0 blocca la modifica)
        return proiezione.getNumeroPosti() > 0;
    }
*/
    private boolean validaESalvaModifiche(Consumer<ProjectionDetails> onModificaCallback) {
     
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

        if (onModificaCallback != null) {
            onModificaCallback.accept(proiezioneCorrente);
        }

        return true;
    }

    // =========================================================================
    // HELPERS GRAFICI E PARSING
    // =========================================================================

    private void aggiungiRigaForm(JPanel panel, JLabel label, JComponent field, GridBagConstraints gbc, int riga) {
        gbc.gridx = 0; gbc.gridy = riga; gbc.weightx = 0.0;
        label.setFont(new Font("Tahoma", Font.BOLD, 12));
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
            field.setFont(new Font("Tahoma", Font.PLAIN, 12));
            if (valoreIniziale != null && !valoreIniziale.isEmpty()) {
                field.setText(valoreIniziale);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return field;
    }

    private JFormattedTextField creaCampoValuta(Font font) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);

        if (currencyFormat instanceof java.text.DecimalFormat) {
            ((java.text.DecimalFormat) currencyFormat).setParseBigDecimal(true);
        }

        JFormattedTextField field = new JFormattedTextField(currencyFormat);
        field.setColumns(10);
        field.setFont(font);
        return field;
    }

    private LocalDateTime parseLocalDateTime(JFormattedTextField field) {
        String text = field.getText().trim();
        if (text.contains("_") || text.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try {
            return LocalDate.parse(text.substring(0, 10), DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            .atTime(Integer.parseInt(text.substring(11, 13)), Integer.parseInt(text.substring(14, 16)));
        } catch (Exception e) {
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
