package cinemax.clientCM.tabpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
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

import cinemax.application.services.FilmService;
import cinemax.application.services.ProjectionService;
import cinemax.application.services.TcpClient;
import cinemax.clientCM.dialog.DettaglioProiezioneProiezionistaDialog;
import cinemax.contracts.dto.FilmDetails;
import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.responses.GetFilmsResponse;

/**
 * Pannello per la ricerca dei film e la creazione/gestione delle proiezioni da parte del proiezionista.
 */
public class ProiezionistaChangeProjection extends JPanel {

    private static final Font FONT_BASE = new Font("Tahoma", Font.PLAIN, 12);
    private static final Font FONT_BOLD = new Font("Tahoma", Font.BOLD, 12);
    private static final Font FONT_SMALL_ITALIC = new Font("Tahoma", Font.ITALIC, 11);

    private final FilmService filmService;
    private final ProjectionService projectionService;

    private final DefaultListModel<FilmDetails> resultListModel;
    private final JList<FilmDetails> listaRisultati;
    private final JTextField textFieldTitoloSemplice;
    private final JButton btnCerca;
    
    
    public ProiezionistaChangeProjection(TcpClient tcpClient) {
        this.filmService = new FilmService(tcpClient);
        this.projectionService = new ProjectionService(tcpClient);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // =====================================================================
        // 1. FORM RICERCA FILM
        // =====================================================================
        this.textFieldTitoloSemplice = new JTextField(20);
        this.textFieldTitoloSemplice.setFont(FONT_BASE);

        JPanel panelFormTitolo = new JPanel(new GridBagLayout());
        panelFormTitolo.setBorder(BorderFactory.createTitledBorder("Ricerca Film a Catalogo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        // Riga 0: Campo Titolo
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel lblTitolo = new JLabel("Titolo Film:");
        lblTitolo.setFont(FONT_BASE);
        panelFormTitolo.add(lblTitolo, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panelFormTitolo.add(this.textFieldTitoloSemplice, gbc);

        // Riga 1: Info di supporto
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        JLabel labelInfo = new JLabel("* Doppio clic su un film per pianificare o gestire una proiezione.");
        labelInfo.setFont(FONT_SMALL_ITALIC);
        labelInfo.setForeground(Color.DARK_GRAY);
        panelFormTitolo.add(labelInfo, gbc);

        // =====================================================================
        // 2. PULSANTE AZIONE
        // =====================================================================
        this.btnCerca = new JButton("Cerca Film");
        this.btnCerca.setFont(FONT_BOLD);
        this.btnCerca.addActionListener(e -> eseguiRicerca());

        JPanel panelBottone = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBottone.add(this.btnCerca);

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.add(panelFormTitolo);
        topContainer.add(panelBottone);

        // =====================================================================
        // 3. LISTA RISULTATI (Rendering O(1) e Scroll Ottimizzato)
        // =====================================================================
        this.resultListModel = new DefaultListModel<>();
        this.listaRisultati = new JList<>(resultListModel);
        this.listaRisultati.setFont(FONT_BASE);
        this.listaRisultati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaRisultati.setFixedCellHeight(26);
        this.listaRisultati.setCellRenderer(new FilmCellRenderer());

        this.listaRisultati.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    int index = listaRisultati.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        FilmDetails filmSelezionato = resultListModel.getElementAt(index);
                        apriDialogProiezione(filmSelezionato);
                    }
                }
            }
        });

        JScrollPane scrollPanel = new JScrollPane(listaRisultati);
        scrollPanel.setPreferredSize(new Dimension(800, 300));
        scrollPanel.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

        // =====================================================================
        // 4. ASSEMBLAGGIO GENERALE
        // =====================================================================
        add(topContainer, BorderLayout.NORTH);
        add(scrollPanel, BorderLayout.CENTER);
    }

    // =========================================================================
    // APERTURA DIALOG CON CALLBACK INTEGRATE
    // =========================================================================

    private void apriDialogProiezione(FilmDetails film) {
        if (film == null) return;

        // Creazione del template ProjectionDetails associato al film (con ID nullo per attivare l'inserimento)
        ProjectionDetails nuovaProiezione = new ProjectionDetails();
        nuovaProiezione.setTitoloFilm(film.getTitoloFilm());
        nuovaProiezione.setRegista(film.getRegista());
        nuovaProiezione.setGenere(film.getGenere());
        nuovaProiezione.setAnno(film.getAnno());
        nuovaProiezione.setDurataMinuti(film.getDurataMinuti());
        nuovaProiezione.setEtaMinima(film.getEtaMinima());
        nuovaProiezione.setIdFilm(film.getId());

        Window parentWindow = SwingUtilities.getWindowAncestor(this);

        DettaglioProiezioneProiezionistaDialog dialog = new DettaglioProiezioneProiezionistaDialog(
                parentWindow,
                nuovaProiezione,
                null,
                null,
                this::eseguiInserimentoProiezione
        );

        dialog.setVisible(true);
    }

    private Boolean eseguiInserimentoProiezione(ProjectionDetails proiezione) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            // Chiamata diretta (il dialog modale si occupa già dell'attesa UI)
            projectionService.insertProjection(
                proiezione.getIdFilm(), 
                proiezione.getDataOraProiezione(), 
                proiezione.getCosto()
            );

            JOptionPane.showMessageDialog(
                this, 
                "Proiezione inserita con successo nel palinsesto!", 
                "Operazione Riuscita", 
                JOptionPane.INFORMATION_MESSAGE
            );
            return true; // Esito positivo

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, 
                "Errore durante l'inserimento: " + ex.getMessage(), 
                "Errore Server / Validazione", 
                JOptionPane.ERROR_MESSAGE
            );
            return false; // Esito negativo: impedisce il dispose()
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    // =========================================================================
    // LOGICA DI RICERCA ASINCRONA (SwingWorker)
    // =========================================================================

    private void eseguiRicerca() {
        String titoloFilm = textFieldTitoloSemplice.getText().trim();

        if (titoloFilm.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Inserire il titolo del film da cercare.",
                "Titolo Mancante",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        btnCerca.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<GetFilmsResponse, Void>() {
            @Override
            protected GetFilmsResponse doInBackground() throws Exception {
                return filmService.getFilmsByTitle(titoloFilm);
            }

            @Override
            protected void done() {
                btnCerca.setEnabled(true);
                setCursor(Cursor.getDefaultCursor());

                try {
                    GetFilmsResponse response = get();
                    popolaListaRisultati(response);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        ProiezionistaChangeProjection.this,
                        "Errore di comunicazione durante la ricerca per titolo: " + ex.getMessage(),
                        "Errore Server",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }.execute();
    }

    private void popolaListaRisultati(GetFilmsResponse response) {
        resultListModel.clear();

        if (response != null && response.getFilms() != null) {
            List<FilmDetails> films = response.getFilms();

            if (films.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Nessun film trovato con i parametri indicati.",
                    "Nessun Risultato",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                resultListModel.addAll(films);
            }
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Risposta non valida ricevuta dal server.",
                "Errore Server",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================================
    // CELL RENDERER
    // =========================================================================

    private static class FilmCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof FilmDetails) {
                setText(((FilmDetails) value).toString());
            }
            setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            return this;
        }
    }
}