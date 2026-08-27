/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.clientCM.tabpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import cinemax.application.services.BookingService;
import cinemax.application.services.ProjectionService;
import cinemax.application.services.TcpClient;
import cinemax.clientCM.dialog.DettaglioPrenotazioneBigliettaioDialog;
import cinemax.contracts.dto.BookingDetails;
import cinemax.contracts.dto.ui.ProjectionDetailsView;
import cinemax.contracts.responses.GetBookingResponse;
import cinemax.contracts.responses.ui.GetProjectionResponse;

/**
 * Pannello di ricerca avanzata delle prenotazioni per l'operatore di biglietteria.
 * <p>
 * Consente il filtraggio multi-parametro (codice prenotazione, anagrafica cliente, titolo film e intervallo temporale),
 * l'esecuzione di query asincrone verso il backend e la consultazione dettagliata della prenotazione
 * selezionata tramite {@link DettaglioPrenotazioneBigliettaioDialog}.
 * </p>
 */
public class SearchBooking extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Font FONT_BASE = new Font("Tahoma", Font.PLAIN, 12);
    private static final Font FONT_BOLD = new Font("Tahoma", Font.BOLD, 12);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final BookingService bookingService;
    private final TcpClient tcpClient;

    private final DefaultListModel<BookingDetails> resultListModel;
    private final JList<BookingDetails> listaRisultati;
    private final JButton btnCerca;

    // Controlli Form di Ricerca
    private final JFormattedTextField textFieldCodicePrenotazione;
    private final JTextField textFieldNome;
    private final JTextField textFieldCognome;
    private final JTextField textFieldTitoloFilm;
    private final JFormattedTextField dataInizio;
    private final JFormattedTextField dataFine;

    /**
     * Costruisce il pannello di ricerca delle prenotazioni.
     * <p>
     * Inizializza i controlli UI, i filtri di input e la lista dei risultati con supporto al doppio clic.
     * </p>
     *
     * @param tcpClient il client TCP utilizzato per la comunicazione con il server di backend
     */
    public SearchBooking(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
        this.bookingService = new BookingService(tcpClient);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // PANNELLO FORM FILTRI
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Filtri di Ricerca Prenotazioni"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        integerFormat.setGroupingUsed(false);

        this.textFieldCodicePrenotazione = new JFormattedTextField(integerFormat);
        this.textFieldCodicePrenotazione.setValue(null);
        this.textFieldCodicePrenotazione.setColumns(15);
        this.textFieldCodicePrenotazione.setFont(FONT_BASE);

        this.textFieldNome = new JTextField(15);
        this.textFieldNome.setFont(FONT_BASE);

        this.textFieldCognome = new JTextField(15);
        this.textFieldCognome.setFont(FONT_BASE);

        this.textFieldTitoloFilm = new JTextField(15);
        this.textFieldTitoloFilm.setFont(FONT_BASE);

        this.dataInizio = creaCampoData();
        this.dataFine = creaCampoData();

        int riga = 0;
        aggiungiRigaForm(panelForm, new JLabel("Codice Prenotazione:"), this.textFieldCodicePrenotazione, gbc, riga++);
        aggiungiRigaForm(panelForm, new JLabel("Nome Cliente:"), this.textFieldNome, gbc, riga++);
        aggiungiRigaForm(panelForm, new JLabel("Cognome Cliente:"), this.textFieldCognome, gbc, riga++);
        aggiungiRigaForm(panelForm, new JLabel("Titolo Film:"), this.textFieldTitoloFilm, gbc, riga++);
        aggiungiRigaForm(panelForm, new JLabel("Data Inizio (gg/mm/aaaa):"), this.dataInizio, gbc, riga++);
        aggiungiRigaForm(panelForm, new JLabel("Data Fine (gg/mm/aaaa):"), this.dataFine, gbc, riga++);

        this.btnCerca = new JButton("Avvia Ricerca");
        this.btnCerca.setFont(FONT_BOLD);
        this.btnCerca.addActionListener(e -> eseguiRicerca());

        JPanel panelBottone = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBottone.add(this.btnCerca);

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.add(panelForm);
        topContainer.add(panelBottone);

        // LISTA DEI RISULTATI
        this.resultListModel = new DefaultListModel<>();
        this.listaRisultati = new JList<>(resultListModel);
        this.listaRisultati.setFont(FONT_BASE);
        this.listaRisultati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaRisultati.setFixedCellHeight(26);
        this.listaRisultati.setCellRenderer(new BookingCellRenderer());

        this.listaRisultati.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    int index = listaRisultati.locationToIndex(e.getPoint());
                    Rectangle cellBounds = listaRisultati.getCellBounds(index, index);
                    
                    if (index >= 0 && cellBounds != null && cellBounds.contains(e.getPoint())) {
                        BookingDetails prenotazioneSelezionata = resultListModel.getElementAt(index);
                        apriDettaglioPrenotazione(prenotazioneSelezionata);
                    }
                }
            }
        });

        JScrollPane scrollPanel = new JScrollPane(listaRisultati);
        scrollPanel.setPreferredSize(new Dimension(800, 300));
        scrollPanel.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

        add(topContainer, BorderLayout.NORTH);
        add(scrollPanel, BorderLayout.CENTER);
    }

    /**
     * Esegue la ricerca asincrona delle prenotazioni filtrando in base ai valori inseriti nel form.
     */
    private void eseguiRicerca() {
        Integer codicePrenotazione = getCodicePrenotazione();
        String nome = textFieldNome.getText().trim();
        String cognome = textFieldCognome.getText().trim();
        String titoloFilm = textFieldTitoloFilm.getText().trim();

        LocalDate dInizio = parseLocalDate(this.dataInizio);
        LocalDate dFine = parseLocalDate(this.dataFine);

        if (dInizio != null && dFine != null && dInizio.isAfter(dFine)) {
            mostraMessaggio("La data di inizio non può essere successiva alla data di fine.", "Errore Date", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnCerca.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<GetBookingResponse, Void>() {
            @Override
            protected GetBookingResponse doInBackground() throws Exception {
                return bookingService.getBookings(
                        codicePrenotazione,
                        nome.isEmpty() ? null : nome,
                        cognome.isEmpty() ? null : cognome,
                        titoloFilm.isEmpty() ? null : titoloFilm,
                        dInizio,
                        dFine
                );
            }

            @Override
            protected void done() {
                btnCerca.setEnabled(true);
                setCursor(Cursor.getDefaultCursor());
                pulisciCampi();

                try {
                    GetBookingResponse response = get();
                    resultListModel.clear();

                    if (response != null && response.getBookings() != null) {
                        List<BookingDetails> bookings = response.getBookings();
                        if (bookings.isEmpty()) {
                            mostraMessaggio("Nessuna prenotazione trovata per i criteri specificati.", "Nessun Risultato", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            resultListModel.addAll(bookings);
                        }
                    } else {
                        mostraMessaggio("Risposta non valida ricevuta dal server.", "Errore Server", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    mostraMessaggio("Errore di comunicazione con il server: " + ex.getMessage(), "Errore di Rete", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    /**
     * Recupera in modo asincrono i dettagli della proiezione e visualizza la scheda di riepilogo della prenotazione.
     *
     * @param prenotazione i dettagli della prenotazione selezionata
     */
    private void apriDettaglioPrenotazione(BookingDetails prenotazione) {
        if (prenotazione == null || prenotazione.getIdProiezione() == null) {
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<GetProjectionResponse, Void>() {
            @Override
            protected GetProjectionResponse doInBackground() throws Exception {
                ProjectionService projService = new ProjectionService(tcpClient);
                return projService.getProjectionById(prenotazione.getIdProiezione());
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                try {
                    GetProjectionResponse response = get();
                    if (response != null && response.getProjection() != null) {
                        Window parentWindow = SwingUtilities.getWindowAncestor(SearchBooking.this);
                        ProjectionDetailsView proiezione = response.getProjection();
                        DettaglioPrenotazioneBigliettaioDialog dialog = 
                                new DettaglioPrenotazioneBigliettaioDialog(parentWindow, proiezione, prenotazione);
                        dialog.setVisible(true);
                    } else {
                        mostraMessaggio("Impossibile caricare le informazioni della proiezione associata.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    mostraMessaggio("Errore durante il recupero dei dettagli: " + ex.getMessage(), "Errore di Rete", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    /**
     * Ripristina i campi del form allo stato iniziale per preparare una nuova interrogazione.
     */
    private void pulisciCampi() {
        textFieldTitoloFilm.setText("");
        textFieldNome.setText("");
        textFieldCognome.setText("");
        dataInizio.setValue(null);
        dataFine.setValue(null);
        textFieldCodicePrenotazione.setValue(null);
    }

    /**
     * Inserisce una riga nel contenitore del form associando etichetta e campo di input.
     *
     * @param panel il pannello con GridBagLayout
     * @param label l'etichetta del campo
     * @param field il componente di input
     * @param gbc   i vincoli di griglia
     * @param riga  l'indice di riga corrente
     */
    private void aggiungiRigaForm(JPanel panel, JLabel label, JComponent field, GridBagConstraints gbc, int riga) {
        gbc.gridx = 0; 
        gbc.gridy = riga; 
        gbc.weightx = 0.0;
        label.setFont(FONT_BASE);
        panel.add(label, gbc);

        gbc.gridx = 1; 
        gbc.gridy = riga; 
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    /**
     * Restituisce l'identificativo numerico della prenotazione inserito nel campo formattato.
     *
     * @return il codice prenotazione valido (> 0), oppure {@code null} se non valorizzato
     */
    public Integer getCodicePrenotazione() {
        try {
            textFieldCodicePrenotazione.commitEdit();
        } catch (Exception ignored) {
        }

        Object value = textFieldCodicePrenotazione.getValue();
        if (value instanceof Number) {
            int intVal = ((Number) value).intValue();
            return intVal > 0 ? intVal : null;
        }
        return null;
    }

    /**
     * Crea un campo formattato per l'inserimento delle date con maschera gg/mm/aaaa.
     *
     * @return l'istanza configurata di {@link JFormattedTextField}
     */
    private JFormattedTextField creaCampoData() {
        JFormattedTextField field = new JFormattedTextField();
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            field.setFormatterFactory(new DefaultFormatterFactory(mask));
            field.setColumns(10);
            field.setFont(FONT_BASE);
        } catch (ParseException ignored) {
        }
        return field;
    }

    /**
     * Converte il valore testuale del campo data nell'istanza {@link LocalDate} corrispondente.
     *
     * @param field il campo contenente la data formattata
     * @return la data parsata, oppure {@code null} se il campo è incompleto o non valido
     */
    private LocalDate parseLocalDate(JFormattedTextField field) {
        String text = field.getText().trim();
        if (text.contains("_") || text.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(text, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Visualizza una finestra di messaggio standard.
     *
     * @param testo  il testo descrittivo del messaggio
     * @param titolo il titolo della finestra
     * @param tipo   la tipologia del messaggio (es. {@link JOptionPane#INFORMATION_MESSAGE})
     */
    private void mostraMessaggio(String testo, String titolo, int tipo) {
        JOptionPane.showMessageDialog(this, testo, titolo, tipo);
    }

    /**
     * Renderer personalizzato per gli elementi {@link BookingDetails} all'interno della {@link JList}.
     */
    private static class BookingCellRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof BookingDetails) {
                setText(((BookingDetails) value).toString());
            }
            setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            return this;
        }
    }
}