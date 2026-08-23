package cinemax.gui.tabpanel;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
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

import cinemax.contracts.dto.ui.ProjectionDetailsView;
import cinemax.contracts.responses.ui.GetProjectionsResponse;
import cinemax.gui.callback.SelezioneProjectionCallBack;

/**
 * Pannello per la visualizzazione dello storico delle proiezioni.
 */
public class SearchProjectionHistory implements LoginCallBack, extends JPanel  {

    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final DefaultListModel<ProjectionDetailsView> resultListModel;
    private final JList<ProjectionDetailsView> listaRisultati;

    public SearchProjectionHistory(SelezioneProjectionCallBack selezioneProjectionCallBack) {
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

        this.listaRisultati.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = listaRisultati.locationToIndex(e.getPoint());
                    if (index >= 0 && selezioneProjectionCallBack != null) {
                        selezioneProjectionCallBack.onSelezione(resultListModel.getElementAt(index));
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
    }

    /**
     * Aggiorna il modello della lista con i dati restituiti dal server.
     */
    private void popolaListaRisultati(GetProjectionsResponse response) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> popolaListaRisultati(response));
            return;
        }

        resultListModel.clear();

        if (response != null && response.getProjections() != null) {
            List<ProjectionDetailsView> projections = response.getProjections();

            if (projections.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Nessuna proiezione presente nello storico.",
                    "Storico Vuoto",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (ProjectionDetailsView projection : projections) {
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