package cinemax.gui.tabpanel;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import cinemax.application.services.BookingService;
import cinemax.application.services.TcpClient;
import cinemax.contracts.dto.BookingDetails;
import cinemax.contracts.responses.GetBookingResponse;
import cinemax.gui.callback.SelezioneBookingCallBack;

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

    public SearchBooking(SelezioneBookingCallBack selezioneBookingCallBack, TcpClient tcpClient) {
        this.bookingService = new BookingService(tcpClient);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // =====================================================================
        // 1. FORM DI RICERCA (GridBagLayout)
        // =====================================================================
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Filtri di Ricerca Prenotazioni"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        integerFormat.setGroupingUsed(false);

        this.textFieldCodicePrenotazione = new JFormattedTextField(integerFormat);
        this.textFieldCodicePrenotazione.setValue(0);
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

        // =====================================================================
        // 2. LISTA RISULTATI (Rendering O(1) e Scroll Ottimizzato)
        // =====================================================================
        this.resultListModel = new DefaultListModel<>();
        this.listaRisultati = new JList<>(resultListModel);
        this.listaRisultati.setFont(FONT_BASE);
        this.listaRisultati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaRisultati.setFixedCellHeight(26);
        this.listaRisultati.setCellRenderer(new BookingCellRenderer());

        this.listaRisultati.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 1) {
                    int index = listaRisultati.locationToIndex(e.getPoint());
                    if (index >= 0 && selezioneBookingCallBack != null) {
                        selezioneBookingCallBack.onSelezione(resultListModel.getElementAt(index),null);
                    }
                }
            }
        });

        JScrollPane scrollPanel = new JScrollPane(listaRisultati);
        scrollPanel.setPreferredSize(new Dimension(800, 300));
        scrollPanel.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

        // =====================================================================
        // 3. ASSEMBLAGGIO GENERALE
        // =====================================================================
        add(topContainer, BorderLayout.NORTH);
        add(scrollPanel, BorderLayout.CENTER);
    }

    // =========================================================================
    // LOGICA DI RICERCA ASINCRONA
    // =========================================================================

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
                return bookingService.getBookings(codicePrenotazione, nome, cognome, titoloFilm, dInizio, dFine);
            }

            @Override
            protected void done() {
                btnCerca.setEnabled(true);
                setCursor(Cursor.getDefaultCursor());

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

    // =========================================================================
    // METODI AUSILIARI E PARSER
    // =========================================================================

    private void aggiungiRigaForm(JPanel panel, JLabel label, JComponent field, GridBagConstraints gbc, int riga) {
        gbc.gridx = 0; gbc.gridy = riga; gbc.weightx = 0.0;
        label.setFont(FONT_BASE);
        panel.add(label, gbc);

        gbc.gridx = 1; gbc.gridy = riga; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    public Integer getCodicePrenotazione() {
        try {
            textFieldCodicePrenotazione.commitEdit();
        } catch (Exception ignored) {}

        Object value = textFieldCodicePrenotazione.getValue();
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }

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

    private void mostraMessaggio(String testo, String titolo, int tipo) {
        JOptionPane.showMessageDialog(this, testo, titolo, tipo);
    }

    // =========================================================================
    // CELL RENDERER
    // =========================================================================

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