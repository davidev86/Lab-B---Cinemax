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

import cinemax.application.services.ProjectionService;
import cinemax.application.services.TcpClient;
import cinemax.clientCM.dialog.DettaglioProiezioneDialog;
import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.responses.GetProjectionsResponse;

/**
 * Pannello per la visualizzazione e la consultazione dello storico delle proiezioni cinematografiche.
 * <p>
 * Recupera in modo asincrono l'elenco completo delle proiezioni archiviate tramite {@link SwingWorker}
 * all'istanziazione della vista e permette di accedere alla scheda descrittiva dettagliata
 * ({@link DettaglioProiezioneDialog}) tramite doppio clic su una riga selezionata.
 * </p>
 */
public class SearchProjectionHistory extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Font FONT_BASE = new Font("Tahoma", Font.PLAIN, 12);
    private static final Font FONT_TITLE = new Font("Tahoma", Font.BOLD, 14);

    private final ProjectionService projectionService;
    private final DefaultListModel<ProjectionDetails> resultListModel;
    private final JList<ProjectionDetails> listaRisultati;

    /**
     * Costruisce e inizializza il pannello per la consultazione dello storico delle proiezioni.
     *
     * @param tcpClient il client di rete per l'inoltro delle richieste verso il server di backend
     */
    public SearchProjectionHistory(TcpClient tcpClient) {
        this.projectionService = new ProjectionService(tcpClient);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Intestazione / Titolo del pannello
        JLabel labelTitolo = new JLabel("Storico Proiezioni Cinematografiche", SwingConstants.CENTER);
        labelTitolo.setFont(FONT_TITLE);
        labelTitolo.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        add(labelTitolo, BorderLayout.NORTH);

        // Inizializzazione del modello dati e della lista dei risultati
        this.resultListModel = new DefaultListModel<>();
        this.listaRisultati = new JList<>(resultListModel);
        this.listaRisultati.setFont(FONT_BASE);
        this.listaRisultati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaRisultati.setFixedCellHeight(26);
        this.listaRisultati.setCellRenderer(new ProjectionHistoryCellRenderer());

        // Gestione dell'apertura del dettaglio tramite doppio clic
        this.listaRisultati.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    int index = listaRisultati.locationToIndex(e.getPoint());
                    Rectangle cellBounds = listaRisultati.getCellBounds(index, index);

                    if (index >= 0 && cellBounds != null && cellBounds.contains(e.getPoint())) {
                        ProjectionDetails proiezione = resultListModel.getElementAt(index);
                        Window parentWindow = SwingUtilities.getWindowAncestor(SearchProjectionHistory.this);
                        DettaglioProiezioneDialog dialog = new DettaglioProiezioneDialog(parentWindow, proiezione);
                        dialog.setVisible(true);
                    }
                }
            }
        });

        JScrollPane scrollPanel = new JScrollPane(listaRisultati);
        scrollPanel.setPreferredSize(new Dimension(800, 400));
        scrollPanel.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

        add(scrollPanel, BorderLayout.CENTER);

        // Caricamento iniziale dei dati in background
        caricaStoricoInBackground();
    }

    /**
     * Esegue la chiamata al server in background tramite {@link SwingWorker}
     * per preservare la fluidità e la reattività dell'interfaccia utente (EDT).
     */
    public void caricaStoricoInBackground() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<GetProjectionsResponse, Void>() {
            @Override
            protected GetProjectionsResponse doInBackground() throws Exception {
                return projectionService.getHistoricalProjection();
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());

                try {
                    GetProjectionsResponse response = get();
                    popolaListaRisultati(response);
                } catch (Exception ex) {
                    mostraMessaggio(
                            "Errore durante il caricamento dello storico: " + ex.getMessage(),
                            "Errore Server",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }.execute();
    }

    /**
     * Aggiorna il modello della lista con i dati storici restituiti dal server.
     *
     * @param response la risposta contenente la lista delle proiezioni storiche
     */
    public void popolaListaRisultati(GetProjectionsResponse response) {
        resultListModel.clear();

        if (response != null && response.getProjections() != null) {
            List<ProjectionDetails> projections = response.getProjections();

            if (projections.isEmpty()) {
                mostraMessaggio(
                        "Nessuna proiezione presente nello storico.",
                        "Storico Vuoto",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                resultListModel.addAll(projections);
            }
        } else {
            mostraMessaggio(
                    "Risposta nulla o non valida ricevuta dal server.",
                    "Errore Server",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Visualizza un messaggio di dialogo informativo, di avviso o di errore.
     *
     * @param testo  il testo del messaggio
     * @param titolo il titolo della finestra modale
     * @param tipo   la tipologia del messaggio (es. {@link JOptionPane#INFORMATION_MESSAGE})
     */
    private void mostraMessaggio(String testo, String titolo, int tipo) {
        JOptionPane.showMessageDialog(this, testo, titolo, tipo);
    }

    /**
     * Renderer personalizzato per la formattazione grafica delle celle della lista delle proiezioni storiche.
     */
    private static class ProjectionHistoryCellRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof ProjectionDetails) {
                setText(((ProjectionDetails) value).toString());
            }
            setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            return this;
        }
    }
}