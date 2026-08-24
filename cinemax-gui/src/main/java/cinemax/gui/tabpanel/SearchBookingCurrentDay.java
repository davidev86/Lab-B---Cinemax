package cinemax.gui.tabpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.SwingWorker;

import cinemax.application.services.BookingService;
import cinemax.application.services.TcpClient;
import cinemax.contracts.dto.BookingDetails;
import cinemax.contracts.responses.GetBookingResponse;

/**
 * Pannello per la visualizzazione delle prenotazioni della giornata odierna.
 */
public class SearchBookingCurrentDay extends JPanel {

    private static final Font FONT_BASE = new Font("Tahoma", Font.PLAIN, 12);
    private static final Font FONT_TITLE = new Font("Tahoma", Font.BOLD, 14);

    private final BookingService bookingService;
    private final DefaultListModel<BookingDetails> resultListModel;
    private final JList<BookingDetails> listaRisultati;

    public SearchBookingCurrentDay(TcpClient tcpClient) {
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

        this.listaRisultati.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 1) {
                    int index = listaRisultati.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        
                    }
                }
            }
        });

        JScrollPane scrollPanel = new JScrollPane(listaRisultati);
        scrollPanel.setPreferredSize(new Dimension(800, 400));
        scrollPanel.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

        add(scrollPanel, BorderLayout.CENTER);

        // Caricamento automatico all'inizializzazione del pannello
        caricaPrenotazioniOggi();
    }

    private void caricaPrenotazioniOggi() {
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

    private void mostraMessaggio(String testo, String titolo, int tipo) {
        JOptionPane.showMessageDialog(this, testo, titolo, tipo);
    }

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