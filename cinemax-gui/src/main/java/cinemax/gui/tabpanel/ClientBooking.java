package cinemax.gui.tabpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;  
import javax.swing.JDialog;
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
import cinemax.contracts.dto.BookingDetails;
import cinemax.contracts.dto.UserMinInfo;
import cinemax.contracts.responses.DeleteBookingResponse;
import cinemax.contracts.responses.GetBookingResponse;
import cinemax.contracts.responses.ui.GetProjectionResponse;
import cinemax.gui.callback.SelezioneBookingCallBack;
import cinemax.gui.dialog.DettaglioProiezioneClienteDialog;

/**
 * Pannello per la visualizzazione e gestione (modifica / cancellazione) delle prenotazioni utente.
 */
public class ClientBooking extends JPanel {

    private static final Font FONT_BASE = new Font("Tahoma", Font.PLAIN, 12);
    private static final Font FONT_TITLE = new Font("Tahoma", Font.BOLD, 14);

    private final BookingService bookingService;
    private final UserMinInfo user;
    private final SelezioneBookingCallBack callBack;
    private final TcpClient tcpClient;

    private final DefaultListModel<BookingDetails> resultListModel;
    private final JList<BookingDetails> listaRisultati;
    private final JButton btnModifica;
    private final JButton btnCancella;

    public ClientBooking(UserMinInfo user, SelezioneBookingCallBack callBack, TcpClient tcpClient) {
        this.user = user;
        this.tcpClient = tcpClient;
        this.callBack = callBack;
        this.bookingService = new BookingService(tcpClient);
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Inizializzazione preliminare dei pulsanti (risolve l'errore di inizializzazione nel listener)
        this.btnModifica = new JButton("Modifica Prenotazione");
        this.btnModifica.setFont(FONT_BASE);
        this.btnModifica.setEnabled(false);
        this.btnModifica.addActionListener(e -> gestisciModifica());

        this.btnCancella = new JButton("Cancella Prenotazione");
        this.btnCancella.setFont(FONT_BASE);
        this.btnCancella.setEnabled(false);
        this.btnCancella.addActionListener(e -> gestisciCancellazione());

        // 2. Modello e Lista con rendering ottimizzato
        this.resultListModel = new DefaultListModel<>();
        this.listaRisultati = new JList<>(resultListModel);
        this.listaRisultati.setFont(FONT_BASE);
        this.listaRisultati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaRisultati.setFixedCellHeight(26);
        this.listaRisultati.setCellRenderer(new BookingCellRenderer());

        // Abilita i pulsanti solo se una riga è effettivamente selezionata
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

        // 3. Viewport & Scroll
        JScrollPane scrollPanel = new JScrollPane(listaRisultati);
        scrollPanel.setPreferredSize(new Dimension(800, 400));
        scrollPanel.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

        // 4. Barra superiore con Titolo e Pulsanti Azione
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitolo = new JLabel("Le Tue Prenotazioni Effettuate");
        lblTitolo.setFont(FONT_TITLE);

        JPanel panelAzioni = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        panelAzioni.add(this.btnModifica);
        panelAzioni.add(this.btnCancella);

        topPanel.add(lblTitolo, BorderLayout.WEST);
        topPanel.add(panelAzioni, BorderLayout.EAST);

        // 5. Assemblaggio layout
        add(topPanel, BorderLayout.NORTH);
        add(scrollPanel, BorderLayout.CENTER);

        // 6. Caricamento iniziale
        visualizzaBooking();
    }

    // GESTIONE AZIONI SULLA PRENOTAZIONE SELEZIONATA
     private void gestisciModifica() {
        BookingDetails selected = listaRisultati.getSelectedValue();
        Window parentWindow = SwingUtilities.getWindowAncestor(ClientBooking.this);
        JDialog dialog = null;
        BookingService bkgService = new BookingService(tcpClient);
        
        if (selected != null && callBack != null) {
            //callBack.onSelezione(selected, selected.getIdPrenotazione());
            ProjectionService projectionService = new ProjectionService(this.tcpClient);
            	GetProjectionResponse projection = projectionService.getProjectionById(selected.getIdProiezione()) ;
            
            	dialog = new DettaglioProiezioneClienteDialog(
                    parentWindow, 
                    projection.getProjection(),
                    (Integer seats) -> {
                        bkgService.updateBooking(selected.getIdPrenotazione(), user.getId(), selected.getIdProiezione(), seats);      
                    }); 
            	
            	dialog.setVisible(true);
        }
    }

    private void gestisciCancellazione() {
        BookingDetails selected = listaRisultati.getSelectedValue();
        if (selected == null) return;

        // Estrazione dinamica dell'ID dalla prenotazione selezionata
        var idPrenotazione = selected.getIdPrenotazione();

		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		String dataFormattata = (selected.getDataOraProiezione() != null) ? selected.getDataOraProiezione().format(formatter) : "Data non disponibile";
        
        int conferma = JOptionPane.showConfirmDialog(
            this,
            "Sei sicuro di voler cancellare la prenotazione in data: " + dataFormattata + "?",
            "Conferma Cancellazione",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (conferma == JOptionPane.YES_OPTION) {
            eseguiCancellazioneAsync(selected);
        }
    }

    private void eseguiCancellazioneAsync(BookingDetails booking) {
        setBottoniAbilitati(true);
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
                    if (response.isSuccess()) {
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
                        if (!bookings.isEmpty()) {
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