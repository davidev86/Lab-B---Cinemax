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
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import cinemax.application.services.BookingService;
import cinemax.application.services.ProjectionService;
import cinemax.application.services.TcpClient;
import cinemax.clientCM.dialog.DettaglioProiezioneClienteDialog;
import cinemax.contracts.dto.BookingDetails;
import cinemax.contracts.dto.UserMinInfo;
import cinemax.contracts.responses.DeleteBookingResponse;
import cinemax.contracts.responses.GetBookingResponse;
import cinemax.contracts.responses.ui.GetProjectionResponse;

/**
 * Pannello per la consultazione e la gestione (modifica e cancellazione) delle prenotazioni effettuate dal cliente.
 * <p>
 * Recupera in modo asincrono lo storico delle prenotazioni associate all'utente autenticato tramite {@link SwingWorker},
 * consentendo l'aggiornamento dei posti riservati mediante {@link DettaglioProiezioneClienteDialog} e la cancellazione
 * previa conferma dell'utente con aggiornamento reattivo dell'interfaccia.
 * </p>
 */
public class ClientBooking extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Font FONT_BASE = new Font("Tahoma", Font.PLAIN, 12);
    private static final Font FONT_TITLE = new Font("Tahoma", Font.BOLD, 14);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final BookingService bookingService;
    private final UserMinInfo user;
    private final TcpClient tcpClient;

    private final DefaultListModel<BookingDetails> resultListModel;
    private final JList<BookingDetails> listaRisultati;
    private final JButton btnModifica;
    private final JButton btnCancella;

    /**
     * Costruisce e inizializza il pannello per la consultazione delle prenotazioni del cliente.
     *
     * @param user      le informazioni sintetiche dell'utente autenticato
     * @param tcpClient il client di rete per l'inoltro delle richieste verso il backend
     */
    public ClientBooking(UserMinInfo user, TcpClient tcpClient) { 
        this.user = user;
        this.tcpClient = tcpClient;
        this.bookingService = new BookingService(tcpClient);
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Inizializzazione pulsanti di controllo
        this.btnModifica = new JButton("Modifica Prenotazione");
        this.btnModifica.setFont(FONT_BASE);
        this.btnModifica.setEnabled(false);
        this.btnModifica.addActionListener(e -> gestisciModifica());

        this.btnCancella = new JButton("Cancella Prenotazione");
        this.btnCancella.setFont(FONT_BASE);
        this.btnCancella.setEnabled(false);
        this.btnCancella.addActionListener(e -> gestisciCancellazione());

        // 2. Configurazione modello e lista risultati
        this.resultListModel = new DefaultListModel<>();
        this.listaRisultati = new JList<>(resultListModel);
        this.listaRisultati.setFont(FONT_BASE);
        this.listaRisultati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaRisultati.setFixedCellHeight(26);
        this.listaRisultati.setCellRenderer(new BookingCellRenderer());

        // Abilitazione dei controlli condizionata alla presenza di una selezione attiva
        this.listaRisultati.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = !listaRisultati.isSelectionEmpty();
                btnModifica.setEnabled(hasSelection);
                btnCancella.setEnabled(hasSelection);
            }
        });

        // Doppio clic per modifica rapida
        this.listaRisultati.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    gestisciModifica();
                }
            }
        });

        // 3. Pannello di scorrimento
        JScrollPane scrollPanel = new JScrollPane(listaRisultati);
        scrollPanel.setPreferredSize(new Dimension(800, 400));
        scrollPanel.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

        // 4. Intestazione superiore con titolo e bottoni di azione
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitolo = new JLabel("Le Tue Prenotazioni Effettuate");
        lblTitolo.setFont(FONT_TITLE);

        JPanel panelAzioni = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        panelAzioni.add(this.btnModifica);
        panelAzioni.add(this.btnCancella);

        topPanel.add(lblTitolo, BorderLayout.WEST);
        topPanel.add(panelAzioni, BorderLayout.EAST);

        // 5. Assemblaggio componenti nel pannello
        add(topPanel, BorderLayout.NORTH);
        add(scrollPanel, BorderLayout.CENTER);

        // 6. Caricamento iniziale dei dati
        visualizzaBooking();
    }

    /**
     * Recupera in modo asincrono i dettagli della proiezione e apre la finestra modale per la modifica dei posti prenotati.
     */
    private void gestisciModifica() {
        BookingDetails selected = listaRisultati.getSelectedValue();
        if (selected == null) {
            return;
        }

        setBottoniAbilitati(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<GetProjectionResponse, Void>() {
            @Override
            protected GetProjectionResponse doInBackground() throws Exception {
                ProjectionService projectionService = new ProjectionService(tcpClient);
                return projectionService.getProjectionById(selected.getIdProiezione());
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                setBottoniAbilitati(!listaRisultati.isSelectionEmpty());

                try {
                    GetProjectionResponse response = get();
                    if (response != null && response.getProjection() != null) {
                        Window parentWindow = SwingUtilities.getWindowAncestor(ClientBooking.this);
                        DettaglioProiezioneClienteDialog dialog = new DettaglioProiezioneClienteDialog(
                                parentWindow,
                                response.getProjection(),
                                (Integer seats) -> eseguiAggiornamentoAsync(selected, seats)
                        );
                        dialog.setVisible(true);
                    } else {
                        mostraMessaggio("Impossibile recuperare i dettagli della proiezione.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    mostraMessaggio("Errore durante il recupero della proiezione: " + ex.getMessage(), "Errore di Rete", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    /**
     * Invia la richiesta asincrona di aggiornamento dei posti per la prenotazione selezionata.
     *
     * @param booking  la prenotazione da modificare
     * @param newSeats il nuovo quantitativo di posti richiesto
     */
    private void eseguiAggiornamentoAsync(BookingDetails booking, int newSeats) {
        setBottoniAbilitati(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                bookingService.updateBooking(booking.getIdPrenotazione(), user.getId(), booking.getIdProiezione(), newSeats);
                return null;
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                try {
                    get();
                    mostraMessaggio("Prenotazione aggiornata con successo.\nCodice prenotazione: " + booking.getIdPrenotazione(), "Operazione Riuscita", JOptionPane.INFORMATION_MESSAGE);
                    visualizzaBooking();
                } catch (Exception ex) {
                    mostraMessaggio("Errore durante l'aggiornamento della prenotazione: " + ex.getMessage(), "Errore Server", JOptionPane.ERROR_MESSAGE);
                    setBottoniAbilitati(!listaRisultati.isSelectionEmpty());
                }
            }
        }.execute();
    }

    /**
     * Richiede conferma all'utente e avvia la procedura asincrona di cancellazione della prenotazione selezionata.
     */
    private void gestisciCancellazione() {
        BookingDetails selected = listaRisultati.getSelectedValue();
        if (selected == null) {
            return;
        }

        String dataFormattata = (selected.getDataOraProiezione() != null) 
                ? selected.getDataOraProiezione().format(DATE_TIME_FORMATTER) 
                : "Data non disponibile";
        
        int conferma = JOptionPane.showConfirmDialog(
            this,
            "Sei sicuro di voler cancellare la prenotazione per la proiezione del " + dataFormattata + "?",
            "Conferma Cancellazione",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (conferma == JOptionPane.YES_OPTION) {
            eseguiCancellazioneAsync(selected);
        }
    }

    /**
     * Esegue la cancellazione asincrona della prenotazione tramite {@link SwingWorker}.
     *
     * @param booking la prenotazione da eliminare
     */
    private void eseguiCancellazioneAsync(BookingDetails booking) {
        setBottoniAbilitati(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<DeleteBookingResponse, Void>() {
            @Override
            protected DeleteBookingResponse doInBackground() throws Exception {
                return bookingService.deleteBooking(booking.getIdPrenotazione());
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                try {
                    DeleteBookingResponse response = get();
                    if (response != null && response.isSuccess()) {
                        mostraMessaggio("Prenotazione cancellata con successo.", "Operazione Riuscita", JOptionPane.INFORMATION_MESSAGE);
                        visualizzaBooking();
                    } else {
                        mostraMessaggio("Impossibile cancellare la prenotazione selezionata.", "Errore", JOptionPane.ERROR_MESSAGE);
                        setBottoniAbilitati(!listaRisultati.isSelectionEmpty());
                    }
                } catch (Exception ex) {
                    mostraMessaggio("Errore durante la cancellazione: " + ex.getMessage(), "Errore Server", JOptionPane.ERROR_MESSAGE);
                    setBottoniAbilitati(!listaRisultati.isSelectionEmpty());
                }
            }
        }.execute();
    }

    /**
     * Ricarica in modo asincrono l'elenco delle prenotazioni relative all'utente corrente.
     */
    public void visualizzaBooking() {
        if (this.user == null) {
            return;
        }

        setBottoniAbilitati(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<GetBookingResponse, Void>() {
            @Override
            protected GetBookingResponse doInBackground() throws Exception {
                return bookingService.getBookingsByUserId(user.getId());
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                try {
                    GetBookingResponse response = get();
                    resultListModel.clear();

                    if (response != null && response.getBookings() != null) {
                        List<BookingDetails> bookings = response.getBookings();
                        if (!bookings.isEmpty()) {
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
     * Abilita o disabilita contemporaneamente i pulsanti di azione.
     *
     * @param stato {@code true} per abilitare i pulsanti, {@code false} altrimenti
     */
    private void setBottoniAbilitati(boolean stato) {
        btnModifica.setEnabled(stato);
        btnCancella.setEnabled(stato);
    }

    /**
     * Mostra una finestra di dialogo standard per messaggi di notifica, avviso o errore.
     *
     * @param testo  il messaggio da visualizzare
     * @param titolo il titolo della finestra di dialogo
     * @param tipo   la tipologia del messaggio (es. {@link JOptionPane#INFORMATION_MESSAGE})
     */
    private void mostraMessaggio(String testo, String titolo, int tipo) {
        JOptionPane.showMessageDialog(this, testo, titolo, tipo);
    }

    /**
     * Renderer personalizzato per le celle della lista delle prenotazioni.
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