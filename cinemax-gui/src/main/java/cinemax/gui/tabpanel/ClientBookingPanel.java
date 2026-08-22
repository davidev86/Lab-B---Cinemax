package cinemax.gui.tabpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import cinemax.application.services.BookingService;
import cinemax.application.services.TcpClient;
import cinemax.contracts.dto.BookingDetails;
import cinemax.contracts.dto.UserMinInfo;
import cinemax.contracts.responses.GetBookingResponse;
import cinemax.gui.callback.SelezioneBookingCallBack;

/**
 * Pannello per la visualizzazione e gestione (modifica / cancellazione) delle prenotazioni utente.
 */
public class ClientBookingPanel extends JPanel {

    private static final Font FONT_BASE = new Font("Tahoma", Font.PLAIN, 12);
    private static final Font FONT_TITLE = new Font("Tahoma", Font.BOLD, 14);

    private final BookingService bookingService;
    private final UserMinInfo user;
    private final SelezioneBookingCallBack callBack;

    private final DefaultListModel<BookingDetails> resultListModel;
    private final JList<BookingDetails> listaRisultati;
    private final JButton btnModifica;
    private final JButton btnCancella;

    public ClientBookingPanel(UserMinInfo user, SelezioneBookingCallBack callBack, TcpClient tcpClient) {
        this.user = user;
        this.callBack = callBack;
        this.bookingService = new BookingService(tcpClient);
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        this.btnModifica = new JButton("Modifica Prenotazione");
        this.btnModifica.setFont(FONT_BASE);
        this.btnModifica.setEnabled(false);
        this.btnModifica.addActionListener(e -> gestisciModifica());

        this.btnCancella = new JButton("Cancella Prenotazione");
        this.btnCancella.setFont(FONT_BASE);
        this.btnCancella.setEnabled(false);
        this.btnCancella.addActionListener(e -> gestisciCancellazione());
        

        // 1. Modello e Lista con rendering ottimizzato
        this.resultListModel = new DefaultListModel<>();
        this.listaRisultati = new JList<>(resultListModel);
        this.listaRisultati.setFont(FONT_BASE);
        this.listaRisultati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaRisultati.setFixedCellHeight(26);
        this.listaRisultati.setCellRenderer(new BookingCellRenderer());

        // Abilita/disabilita i pulsanti d'azione in base alla selezione attiva
        this.listaRisultati.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = !listaRisultati.isSelectionEmpty();
                btnModifica.setEnabled(hasSelection);
                btnCancella.setEnabled(hasSelection);
            }
        });

        this.listaRisultati.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    gestisciModifica();
                }
            }
        });

        // 2. Viewport & Scroll
        JScrollPane scrollPanel = new JScrollPane(listaRisultati);
        scrollPanel.setPreferredSize(new Dimension(800, 400));
        scrollPanel.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

        // 3. Barra superiore con Titolo e Pulsanti Azione
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitolo = new JLabel("Le Tue Prenotazioni Effettuate");
        lblTitolo.setFont(FONT_TITLE);

        JPanel panelAzioni = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        panelAzioni.add(this.btnModifica);
        panelAzioni.add(this.btnCancella);

        topPanel.add(lblTitolo, BorderLayout.WEST);
        topPanel.add(panelAzioni, BorderLayout.EAST);

        // 4. Assemblaggio layout
        add(topPanel, BorderLayout.NORTH);
        add(scrollPanel, BorderLayout.CENTER);

        // 5. Caricamento iniziale
        visualizzaBooking();
    }

    // =========================================================================
    // LOGICA AZIONI (MODIFICA E CANCELLAZIONE)
    // =========================================================================

    private void gestisciModifica() {
        BookingDetails selected = listaRisultati.getSelectedValue();
        if (selected != null && callBack != null) {
            callBack.onSelezione(selected);
        }
    }

    private void gestisciCancellazione() {
        BookingDetails selected = listaRisultati.getSelectedValue();
        if (selected == null) return;

        int conferma = JOptionPane.showConfirmDialog(
            this,
            "Sei sicuro di voler cancellare la prenotazione selezionata?",
            "Conferma Cancellazione",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (conferma == JOptionPane.YES_OPTION) {
            eseguiCancellazioneAsync(selected);
        }
    }

    private void eseguiCancellazioneAsync(BookingDetails booking) {
        setBottoniAbilitati(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return bookingService.deleteBooking(booking.getId());
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                try {
                    boolean success = get();
                    if (success) {
                        mostraMessaggio("Prenotazione cancellata con successo.", "Operazione Riuscita", JOptionPane.INFORMATION_MESSAGE);
                        visualizzaBooking();
                    } else {
                        mostraMessaggio("Impossibile cancellare la prenotazione.", "Errore", JOptionPane.ERROR_MESSAGE);
                        setBottoniAbilitati(!listaRisultati.isSelectionEmpty());
                    }
                } catch (Exception ex) {
                    mostraMessaggio("Errore durante la cancellazione: " + ex.getMessage(), "Errore Server", JOptionPane.ERROR_MESSAGE);
                    setBottoniAbilitati(!listaRisultati.isSelectionEmpty());
                }
            }
        }.execute();
    }

    // =========================================================================
    // CARICAMENTO DATI ASINCRONO
    // =========================================================================

    public void visualizzaBooking() {
        if (this.user == null) return;

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
                        if (bookings.isEmpty()) {
                            mostraMessaggio("Non hai ancora effettuato nessuna prenotazione.", "Nessun Risultato", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            resultListModel.addAll(bookings);
                        }
                    } else {
                        mostraMessaggio("Risposta non valida dal server.", "Errore Server", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    mostraMessaggio("Errore di comunicazione con il server: " + ex.getMessage(), "Errore di Rete", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void setBottoniAbilitati(boolean stato) {
        btnModifica.setEnabled(stato);
        btnCancella.setEnabled(stato);
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