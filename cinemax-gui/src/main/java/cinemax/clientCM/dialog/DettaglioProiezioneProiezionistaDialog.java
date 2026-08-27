/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.clientCM.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.function.Function;

import javax.swing.BorderFactory;
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

import cinemax.contracts.dto.ProjectionDetails;

/**
 * Finestra di dialogo modale per la gestione (inserimento, modifica e cancellazione) 
 * delle proiezioni cinematografiche da parte dell'utente con ruolo Proiezionista.
 * <p>
 * Fornisce campi di input formattati per data/ora e tariffa di ingresso, effettua
 * la validazione locale dei vincoli temporali ed economici e inoltra le richieste
 * al server tramite i rispettivi callback operativi.
 * </p>
 */
public class DettaglioProiezioneProiezionistaDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private static final Font FONT_BASE = new Font("Tahoma", Font.PLAIN, 12);
    private static final Font FONT_BOLD = new Font("Tahoma", Font.BOLD, 12);
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ProjectionDetails proiezioneCorrente;
    private final JFormattedTextField textFieldDataOra;
    private final JFormattedTextField textFieldCostoBiglietto;

    /**
     * Costruisce e visualizza la finestra modale per la gestione della proiezione.
     *
     * @param owner              la finestra proprietaria (parent window)
     * @param proiezione         l'istanza {@link ProjectionDetails} da modificare/cancellare o il modello base per un nuovo inserimento
     * @param onModificaCallback funzione di callback per il salvataggio delle modifiche su una proiezione esistente
     * @param onCancellaCallback funzione di callback per la cancellazione di una proiezione esistente
     * @param onInsertCallback   funzione di callback per l'inserimento a palinsesto di una nuova proiezione
     */
    public DettaglioProiezioneProiezionistaDialog(
            Window owner, 
            ProjectionDetails proiezione,
            Function<ProjectionDetails, Boolean> onModificaCallback,
            Function<ProjectionDetails, Boolean> onCancellaCallback,
            Function<ProjectionDetails, Boolean> onInsertCallback) {

        super(owner, "Gestione Proiezione (Proiezionista)", ModalityType.APPLICATION_MODAL);
        this.proiezioneCorrente = (proiezione != null) ? proiezione : new ProjectionDetails();

        boolean exists = (this.proiezioneCorrente.getId() != null);

        setSize(520, 560);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // SCHEDA DATI PROIEZIONE
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

        // Griglia Campi di input e riepilogo
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

        // BOTTONI DI AZIONE CONDIZIONATI ALLO STATO DELLA PROIEZIONE
        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton btnAnnulla = new JButton("Chiudi");
        btnAnnulla.setFont(FONT_BASE);
        btnAnnulla.addActionListener(e -> dispose());

        if (exists) {
            // Caso Proiezione Esistente (ID Presente): Modifica e Cancellazione
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
                    if (onCancellaCallback != null && Boolean.TRUE.equals(onCancellaCallback.apply(proiezioneCorrente))) {
                        dispose();
                    }
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
            getRootPane().setDefaultButton(btnModifica);

        } else {
            // Caso Nuova Proiezione (ID Nullo): Inserimento a palinsesto
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
            getRootPane().setDefaultButton(btnInserisci);
        }

        add(panelBottoni, BorderLayout.SOUTH);
    }

    /**
     * Valida i vincoli di input lato client (data futura, costo non negativo) e invoca la callback specificata.
     *
     * @param callback la funzione di business logic da eseguire con l'oggetto aggiornato
     * @return {@code true} se la validazione e l'esecuzione del callback hanno avuto esito positivo, {@code false} altrimenti
     */
    private boolean validaEApplicaModifiche(Function<ProjectionDetails, Boolean> callback) {
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
            return Boolean.TRUE.equals(callback.apply(proiezioneCorrente));
        }

        return true;
    }

    /**
     * Inserisce una riga nel contenitore con etichetta e componente formattato.
     *
     * @param panel il pannello con GridBagLayout
     * @param label l'etichetta del campo
     * @param field il componente di input o testo
     * @param gbc   i vincoli di griglia
     * @param riga  l'indice di riga corrente
     */
    private void aggiungiRigaForm(JPanel panel, JLabel label, JComponent field, GridBagConstraints gbc, int riga) {
        gbc.gridx = 0; 
        gbc.gridy = riga; 
        gbc.weightx = 0.0;
        label.setFont(FONT_BOLD);
        label.setForeground(Color.DARK_GRAY);
        panel.add(label, gbc);

        gbc.gridx = 1; 
        gbc.gridy = riga; 
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    /**
     * Inizializza un campo formattato con maschera numerica per data e ora (gg/mm/aaaa hh:mm).
     *
     * @param valoreIniziale la stringa iniziale da visualizzare nel campo
     * @return il componente {@link JFormattedTextField} configurato
     */
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
        } catch (ParseException ignored) {
        }
        return field;
    }

    /**
     * Inizializza un campo formattato per la gestione monetaria in valuta locale (Euro).
     *
     * @return il componente {@link JFormattedTextField} configurato per importi decimali
     */
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

    /**
     * Esegue il parsing del testo del campo nella corrispondente istanza {@link LocalDateTime}.
     *
     * @param field il campo contenente la data/ora formattata
     * @return l'oggetto {@link LocalDateTime}, oppure {@code null} se il formato è errato o incompleto
     */
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

    /**
     * Estrae in modo sicuro un valore {@link BigDecimal} dal campo formattato di valuta.
     *
     * @param field il campo da cui estrarre il valore monetario
     * @return il valore convertito in {@link BigDecimal}, o {@link BigDecimal#ZERO} se nullo o non valido
     */
    private BigDecimal getBigDecimalFromField(JFormattedTextField field) {
        try {
            field.commitEdit();
        } catch (Exception ignored) {
        }

        Object value = field.getValue();
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        return BigDecimal.ZERO;
    }

    /**
     * Restituisce il valore fornito se non nullo o vuoto, altrimenti una stringa di fallback ("N/D").
     *
     * @param val la stringa da verificare
     * @return la stringa originale oppure "N/D"
     */
    private String valoreODefault(String val) {
        return (val != null && !val.trim().isEmpty()) ? val : "N/D";
    }
}