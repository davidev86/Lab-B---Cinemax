package cinemax.clientCM.tabpanel;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import cinemax.application.services.ProjectionService;
import cinemax.application.services.TcpClient;
import cinemax.clientCM.dialog.DettaglioProiezioneDialog;
import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.responses.GetProjectionsResponse;

/**
 * Pannello per la visualizzazione dello storico delle proiezioni.
 * Carica automaticamente lo storico dei dati all'istanziazione via SwingWorker.
 */
public class SearchProjectionHistory extends JPanel {

    private final ProjectionService projectionService;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final DefaultListModel<ProjectionDetails> resultListModel;
    private final JList<ProjectionDetails> listaRisultati;

    public SearchProjectionHistory(TcpClient tcpClient) {
        this.projectionService = new ProjectionService(tcpClient);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Font fontBase = new Font("Tahoma", Font.PLAIN, 12);

        // Header / Titolo del pannello
        JLabel labelTitolo = new JLabel("Storico proiezioni");
        labelTitolo.setFont(new Font("Tahoma", Font.BOLD, 16));
        labelTitolo.setAlignmentX(CENTER_ALIGNMENT);

        // Inizializzazione della lista e del modello dati
        this.resultListModel = new DefaultListModel<>();
        this.listaRisultati = new JList<>(resultListModel);
        this.listaRisultati.setFont(fontBase);

        
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        this.listaRisultati.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = listaRisultati.locationToIndex(e.getPoint());
                    if (index >= 0) {                    	 
                    	var dialog =  new DettaglioProiezioneDialog(parentWindow, resultListModel.getElementAt(index));
                    	dialog.setVisible(true);
                    } 
                }
            }
        });

        JScrollPane scrollPanel = new JScrollPane(listaRisultati);
        scrollPanel.setPreferredSize(new Dimension(800, 300));

        this.cardLayout = new CardLayout();
        this.cardPanel = new JPanel(cardLayout);
        this.cardPanel.add(scrollPanel, "scrollPanel");

        // Assemblaggio componenti grafici
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(labelTitolo);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(cardPanel);

        // Avvio del caricamento automatico in background
        caricaStoricoInBackground();
    }

    /**
     * Esegue la chiamata al server in un thread separato (SwingWorker)
     * per non bloccare l'interfaccia grafica durante il recupero dei dati.
     */
    private void caricaStoricoInBackground() {
        SwingWorker<GetProjectionsResponse, Void> worker = new SwingWorker<>() {
            @Override
            protected GetProjectionsResponse doInBackground() throws Exception {
                // Chiamata di rete eseguita in background (sostituisci i parametri se necessario)
                return projectionService.getHistoricalProjection(); 
            }

            @Override
            protected void done() {
                try {
                    GetProjectionsResponse response = get();
                    popolaListaRisultati(response);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SearchProjectionHistory.this,
                        "Errore durante il caricamento dello storico: " + ex.getMessage(),
                        "Errore Server",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    /**
     * Aggiorna il modello della lista con i dati restituiti dal server.
     */
    public void popolaListaRisultati(GetProjectionsResponse response) {
        resultListModel.clear();

        if (response != null && response.getProjections() != null) {
            List<ProjectionDetails> projections = response.getProjections();

            if (projections.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Nessuna proiezione presente nello storico.",
                    "Storico Vuoto",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (ProjectionDetails projection : projections) {
                    resultListModel.addElement(projection);
                }
                cardLayout.show(cardPanel, "scrollPanel");
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Risposta nulla o non valida ricevuta dal server.", 
                "Errore Server", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}