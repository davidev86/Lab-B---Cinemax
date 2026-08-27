/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.clientCM.tabpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import cinemax.application.services.BookingService;
import cinemax.application.services.ProjectionService;
import cinemax.application.services.TcpClient;
import cinemax.clientCM.dialog.DettaglioPrenotazioneBigliettaioDialog;
import cinemax.contracts.dto.BookingDetails;
import cinemax.contracts.dto.ui.ProjectionDetailsView;
import cinemax.contracts.responses.GetBookingResponse;
import cinemax.contracts.responses.ui.GetProjectionResponse;

/**
 * Pannello per la consultazione rapida e la visualizzazione delle prenotazioni della giornata odierna.
 * <p>
 * Carica in modo asincrono l'elenco degli spettacoli e dei posti prenotati per la data corrente
 * a beneficio degli operatori di cassa/biglietteria, consentendo l'apertura della scheda di dettaglio
 * {@link DettaglioPrenotazioneBigliettaioDialog} tramite doppio clic sulla voce selezionata.
 * </p>
 */
public class SearchBookingCurrentDay extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Font FONT_BASE = new Font("Tahoma", Font.PLAIN, 12);
    private static final Font FONT_TITLE = new Font("Tahoma", Font.BOLD, 14);

    private final BookingService bookingService;
    private final TcpClient tcpClient;
    private final DefaultListModel<BookingDetails> resultListModel;
    private final JList<BookingDetails> listaRisultati;

    /**
     * Costruisce e inizializza il pannello per il riepilogo delle prenotazioni odierne.
     *
     * @param tcpClient il client di rete per l'inoltro delle richieste verso il backend
     */
    public SearchBookingCurrentDay(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
        this.bookingService = new BookingService(tcpClient);

        setLayout(new BorderLayout(10, 10)); 
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Intestazione
        JLabel lblTitolo = new JLabel("Prenotazioni di Oggi", SwingConstants.CENTER);
        lblTitolo.setFont(FONT_TITLE);
        lblTitolo.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        add(lblTitolo, BorderLayout.NORTH);

        // Lista Risultati
        this.resultListModel = new DefaultListModel<>();
        this.listaRisultati = new JList<>(resultListModel);
        this.listaRisultati.setFont(FONT_BASE);
        this.listaRisultati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaRisultati.setFixedCellHeight(26);
        this.listaRisultati.setCellRenderer(new BookingCellRenderer());

        // Apertura dettaglio su doppio clic
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
        scrollPanel.setPreferredSize(new Dimension(800, 400));
        scrollPanel.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

        add(scrollPanel, BorderLayout.CENTER);

        // Caricamento automatico delle prenotazioni odierne
        caricaPrenotazioniOggi();
    }

    /**
     * Esegue il recupero asincrono delle prenotazioni registrate per la data corrente.
     */
    public void caricaPrenotazioniOggi() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<GetBookingResponse, Void>() {
            @Override
            protected GetBookingResponse doInBackground() throws Exception {
                return bookingService.getBookingsCurrentDay();
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());

                try {
                    GetBookingResponse response = get();
                    resultListModel.clear();

                    if (response != null && response.getBookings() != null) {
                        List<BookingDetails> bookings = response.getBookings();
                        if (bookings.isEmpty()) {
                            mostraMessaggio("Nessuna prenotazione presente per la giornata di oggi.", "Nessun Risultato", JOptionPane.INFORMATION_MESSAGE);
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
     * Recupera i dettagli della proiezione e apre la finestra modale con le informazioni complete della prenotazione.
     *
     * @param prenotazione la prenotazione selezionata
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
                        Window parentWindow = SwingUtilities.getWindowAncestor(SearchBookingCurrentDay.this);
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
     * Visualizza una finestra di messaggio informativo o di errore.
     *
     * @param testo  il testo del messaggio
     * @param titolo il titolo della finestra
     * @param tipo   la tipologia di icona del messaggio
     */
    private void mostraMessaggio(String testo, String titolo, int tipo) {
        JOptionPane.showMessageDialog(this, testo, titolo, tipo);
    }

    /**
     * Renderer personalizzato per la visualizzazione formattata degli elementi {@link BookingDetails}.
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