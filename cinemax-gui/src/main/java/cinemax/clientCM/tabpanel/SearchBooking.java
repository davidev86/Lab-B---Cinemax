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
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import cinemax.clientCM.callback.SelezioneBookingCallBack;
import cinemax.clientCM.dialog.DettaglioPrenotazioneBigliettaioDialog;
import cinemax.contracts.dto.BookingDetails;
import cinemax.contracts.dto.ui.ProjectionDetailsView;
import cinemax.contracts.responses.GetBookingResponse;
import cinemax.contracts.responses.GetProjectionResponse;

/**
 * Pannello di ricerca per le prenotazioni con rendering ottimizzato e query asincrone.
 */
public class SearchBooking extends JPanel {

    private static final Font FONT_BASE = new Font("Tahoma", Font.PLAIN, 12);
    private static final Font FONT_BOLD = new Font("Tahoma", Font.BOLD, 12);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final BookingService bookingService;
    private final DefaultListModel<BookingDetails> resultListModel;
    private final JList<BookingDetails> listaRisultati;
    private final JButton btnCerca;
    
    // Controlli Form
    private final JFormattedTextField textFieldCodicePrenotazione;
    private final JTextField textFieldNome;
    private final JTextField textFieldCognome;
    private final JTextField textFieldTitoloFilm;
    private final JFormattedTextField dataInizio;
    private final JFormattedTextField dataFine;

    /**
     * Costruisce il pannello di ricerca delle prenotazioni.
     * Inizializza i controlli UI, il servizio di accesso remoto e la lista dei risultati.
     * @param tcpClient client TCP usato dal {@link BookingService} per comunicare con il server
     */
    public SearchBooking(TcpClient tcpClient) {
        this.bookingService = new BookingService(tcpClient);
        setLayout(new BorderLayout(10, 10)); 
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Filtri di Ricerca Prenotazioni"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        integerFormat.setGroupingUsed(false);

        // Campo codice prenotazione (inizialmente vuoto/null per non forzare il filtro)
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


        //LISTA RISULTATI
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
                    
                    // Verifica che il click sia avvenuto effettivamente su una riga esistente
                    if (index >= 0 && cellBounds != null && cellBounds.contains(e.getPoint())) {
                        BookingDetails prenotazioneSelezionata = resultListModel.getElementAt(index);
                        Integer idProiezioneSel = prenotazioneSelezionata.getIdProiezione();
                        ProjectionService projService = new ProjectionService(tcpClient);
                        cinemax.contracts.responses.ui.GetProjectionResponse res = projService.getProjectionById(idProiezioneSel);
                        
                        Window parentWindow = SwingUtilities.getWindowAncestor(SearchBooking.this);
                        ProjectionDetailsView proiezione = res.getProjection();
                        DettaglioPrenotazioneBigliettaioDialog dialog = 
                                new DettaglioPrenotazioneBigliettaioDialog(parentWindow, proiezione, prenotazioneSelezionata);
                        dialog.setVisible(true);
                   
                }
                }
            }
        });

        JScrollPane scrollPanel = new JScrollPane(listaRisultati);
        scrollPanel.setPreferredSize(new Dimension(800, 300));
        scrollPanel.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);


        //ASSEMBLAGGIO GENERALE
        add(topContainer, BorderLayout.NORTH);
        add(scrollPanel, BorderLayout.CENTER);
    }

    // LOGICA DI RICERCA ASINCRONA
    /**
     * Avvia una ricerca asincrona delle prenotazioni usando i filtri correnti presenti nel form.
     * Disabilita il bottone di ricerca e mostra il cursore di attesa durante l'operazione.
     * I risultati aggiornano la lista dei risultati o mostrano messaggi all'utente in caso di errori.
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
     * Ripristina i campi del form allo stato iniziale (valori vuoti/null).
     * Questo metodo è usato dopo il termine di una ricerca per preparare il form ad una nuova ricerca.
     */
    private void pulisciCampi() {
        // Reset JTextField standard
        textFieldTitoloFilm.setText("");
        textFieldNome.setText("");
        textFieldCognome.setText("");

        // Reset JFormattedTextField (date e numeri)
        dataInizio.setValue(null);
        dataFine.setValue(null);
        
        textFieldCodicePrenotazione.setValue(null);
    }

    // METODI AUSILIARI E PARSER
    /**
     * Aggiunge una riga al pannello form usando GridBagLayout: prima colonna label, seconda il field.
     * @param panel pannello che contiene il form
     * @param label etichetta da posizionare nella prima colonna
     * @param field componente di input da posizionare nella seconda colonna
     * @param gbc constraints condivisi per il layout
     * @param riga indice di riga (gridy) nel GridBagLayout
     */
    private void aggiungiRigaForm(JPanel panel, JLabel label, JComponent field, GridBagConstraints gbc, int riga) {
        gbc.gridx = 0; gbc.gridy = riga; gbc.weightx = 0.0;
        label.setFont(FONT_BASE);
        panel.add(label, gbc);

        gbc.gridx = 1; gbc.gridy = riga; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    /**
     * Restituisce il codice prenotazione inserito nel campo corrispondente.
     * Se il campo è vuoto o contiene un valore non positivo ritorna {@code null}.
     * @return codice prenotazione valido (>0) oppure {@code null} se non presente
     */
    public Integer getCodicePrenotazione() {
        try {
            textFieldCodicePrenotazione.commitEdit();
        } catch (Exception ignored) {}

        Object value = textFieldCodicePrenotazione.getValue();
        if (value instanceof Number) {
            int intVal = ((Number) value).intValue();
            return intVal > 0 ? intVal : null;
        }
        return null;
    }

    /**
     * Crea e restituisce un JFormattedTextField configurato per l'inserimento di date nel formato dd/MM/yyyy.
     * Il campo utilizza una MaskFormatter per mostrare il placeholder e gestire l'input.
     * @return campo formattato per date
     */
    private JFormattedTextField creaCampoData() {
        JFormattedTextField field = new JFormattedTextField();
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            field.setFormatterFactory(new DefaultFormatterFactory(mask));
            field.setColumns(10);
            field.setFont(FONT_BASE);
        } catch (ParseException ignored) {}
        return field;
    }

    /**
     * Parsea il testo presente in un JFormattedTextField e tenta di convertirlo in {@link LocalDate}
     * usando il formato definito da {@code DATE_FORMATTER} (dd/MM/yyyy).
     * Se il campo è incompleto (contiene placeholder) o la data non è valida ritorna {@code null}.
     * @param field campo contenente la data in formato testuale
     * @return LocalDate parsata oppure {@code null} in caso di input invalido
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
     * Mostra una finestra di dialogo informativa all'utente.
     * @param testo testo del messaggio
     * @param titolo titolo della finestra
     * @param tipo tipo di messaggio (es. JOptionPane.INFORMATION_MESSAGE)
     */
    private void mostraMessaggio(String testo, String titolo, int tipo) {
        JOptionPane.showMessageDialog(this, testo, titolo, tipo);
    }

    // CELL RENDERER
    /**
     * Renderer semplice per visualizzare gli elementi {@link BookingDetails} nella JList.
     * Mostra la rappresentazione testuale ottenuta tramite {@code BookingDetails.toString()}.
     */
    private static class BookingCellRenderer extends DefaultListCellRenderer {
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
