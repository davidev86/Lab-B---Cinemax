package cinemax.clientCM.tabpanel;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import cinemax.application.services.ProjectionService;
import cinemax.application.services.TcpClient;
import cinemax.clientCM.callback.SelezioneProjectionCallBack;
import cinemax.contracts.dto.ui.ProjectionDetailsView;
import cinemax.contracts.responses.ui.GetProjectionsResponse;

/**
 * Pannello per la ricerca e consultazione delle proiezioni cinematografiche.
 * <p>
 * Offre due modalità di ricerca commutabili dinamicamente tramite {@link CardLayout}:
 * <ul>
 *   <li><b>Ricerca Rapida per Titolo</b>: ricerca per corrispondenza testuale con intervallo temporale automatico di 3 mesi;</li>
 *   <li><b>Ricerca Avanzata/Completa</b>: filtraggio multi-parametro per titolo, genere, date e range di prezzo.</li>
 * </ul>
 * Le interrogazioni al backend avvengono in modo asincrono mediante {@link SwingWorker}.
 * </p>
 */
public class SearchProjection extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final String CARD_RICERCA_TITOLO = "CARD_TITOLO";
    private static final String CARD_RICERCA_COMPLETA = "CARD_COMPLETA";

    private static final Font FONT_BASE = new Font("Tahoma", Font.PLAIN, 12);
    private static final Font FONT_BOLD = new Font("Tahoma", Font.BOLD, 12);
    private static final Font FONT_SMALL_ITALIC = new Font("Tahoma", Font.ITALIC, 11);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ProjectionService projectionService;
    private final SelezioneProjectionCallBack selezioneProjectionCallBack;

    private final CardLayout formsCardLayout;
    private final JPanel formsContainerPanel;

    private final JRadioButton radioRicercaTitolo;
    private final JRadioButton radioRicercaCompleta;
    private final JButton btnCerca;

    // Controlli Template 1: Ricerca per Titolo
    private final JTextField textFieldTitoloSemplice;

    // Controlli Template 2: Ricerca Completa
    private final JTextField textFieldTitoloFilm;
    private final JTextField textFieldGenere;
    private final JFormattedTextField dataInizio;
    private final JFormattedTextField dataFine;
    private final JFormattedTextField textFieldCostoBigliettoMin;
    private final JFormattedTextField textFieldCostoBigliettoMax;

    // Modello e Lista Risultati
    private final DefaultListModel<ProjectionDetailsView> resultListModel;
    private final JList<ProjectionDetailsView> listaRisultati;

    /**
     * Costruisce e inizializza il pannello di ricerca delle proiezioni.
     *
     * @param selezioneProjectionCallBack callback invocato alla selezione di una proiezione dalla lista
     * @param tcpClient                   il client di rete per l'inoltro delle richieste verso il backend
     */
    public SearchProjection(SelezioneProjectionCallBack selezioneProjectionCallBack, TcpClient tcpClient) {
        this.selezioneProjectionCallBack = selezioneProjectionCallBack;
        this.projectionService = new ProjectionService(tcpClient);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. SELETTORE MODALITÀ (RADIO BUTTONS)
        this.radioRicercaTitolo = new JRadioButton("Ricerca per Titolo (prossimi 3 mesi)", true);
        this.radioRicercaTitolo.setFont(FONT_BASE);

        this.radioRicercaCompleta = new JRadioButton("Ricerca Completa con Filtri Avanzati", false);
        this.radioRicercaCompleta.setFont(FONT_BASE);

        ButtonGroup groupModalita = new ButtonGroup();
        groupModalita.add(this.radioRicercaTitolo);
        groupModalita.add(this.radioRicercaCompleta);

        JPanel panelRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelRadio.setBorder(BorderFactory.createTitledBorder("Seleziona Modalità di Ricerca"));
        panelRadio.add(this.radioRicercaTitolo);
        panelRadio.add(this.radioRicercaCompleta);

        // 2. TEMPLATE 1: FORM RICERCA PER TITOLO
        this.textFieldTitoloSemplice = new JTextField(20);
        this.textFieldTitoloSemplice.setFont(FONT_BASE);

        JPanel panelFormTitolo = new JPanel(new GridBagLayout());
        panelFormTitolo.setBorder(BorderFactory.createTitledBorder("Ricerca per Titolo"));
        GridBagConstraints gbcTitolo = new GridBagConstraints();
        gbcTitolo.insets = new Insets(6, 6, 6, 6);
        gbcTitolo.anchor = GridBagConstraints.WEST;

        // Riga 0: Titolo
        gbcTitolo.gridx = 0;
        gbcTitolo.gridy = 0;
        gbcTitolo.fill = GridBagConstraints.NONE;
        JLabel lblTitoloSemplice = new JLabel("Titolo Film:");
        lblTitoloSemplice.setFont(FONT_BASE);
        panelFormTitolo.add(lblTitoloSemplice, gbcTitolo);

        gbcTitolo.gridx = 1;
        gbcTitolo.gridy = 0;
        gbcTitolo.fill = GridBagConstraints.HORIZONTAL;
        gbcTitolo.weightx = 1.0;
        panelFormTitolo.add(this.textFieldTitoloSemplice, gbcTitolo);

        // Riga 1: Nota data automatica
        gbcTitolo.gridx = 1;
        gbcTitolo.gridy = 1;
        gbcTitolo.weightx = 0.0;
        JLabel labelInfoData = new JLabel("* La ricerca mostrerà le proiezioni disponibili da oggi ai prossimi 3 mesi.");
        labelInfoData.setFont(FONT_SMALL_ITALIC);
        labelInfoData.setForeground(Color.DARK_GRAY);
        panelFormTitolo.add(labelInfoData, gbcTitolo);

        // 3. TEMPLATE 2: FORM RICERCA COMPLETA
        this.textFieldTitoloFilm = new JTextField(20);
        this.textFieldTitoloFilm.setFont(FONT_BASE);

        this.textFieldGenere = new JTextField(20);
        this.textFieldGenere.setFont(FONT_BASE);

        this.dataInizio = creaCampoData();
        this.dataFine = creaCampoData();

        this.textFieldCostoBigliettoMin = creaCampoValuta();
        this.textFieldCostoBigliettoMax = creaCampoValuta();

        JPanel panelFormCompleto = new JPanel(new GridBagLayout());
        panelFormCompleto.setBorder(BorderFactory.createTitledBorder("Filtri Avanzati di Ricerca"));
        GridBagConstraints gbcComp = new GridBagConstraints();
        gbcComp.insets = new Insets(4, 6, 4, 6);
        gbcComp.anchor = GridBagConstraints.WEST;
        gbcComp.fill = GridBagConstraints.HORIZONTAL;

        int riga = 0;
        aggiungiRigaForm(panelFormCompleto, new JLabel("Titolo Film:"), this.textFieldTitoloFilm, gbcComp, riga++);
        aggiungiRigaForm(panelFormCompleto, new JLabel("Genere:"), this.textFieldGenere, gbcComp, riga++);
        aggiungiRigaForm(panelFormCompleto, new JLabel("Data Inizio (gg/mm/aaaa):"), this.dataInizio, gbcComp, riga++);
        aggiungiRigaForm(panelFormCompleto, new JLabel("Data Fine (gg/mm/aaaa):"), this.dataFine, gbcComp, riga++);
        aggiungiRigaForm(panelFormCompleto, new JLabel("Costo Minimo (€):"), this.textFieldCostoBigliettoMin, gbcComp, riga++);
        aggiungiRigaForm(panelFormCompleto, new JLabel("Costo Massimo (€):"), this.textFieldCostoBigliettoMax, gbcComp, riga++);

        // 4. CONTENITORE TEMPLATE CARDLAYOUT
        this.formsCardLayout = new CardLayout();
        this.formsContainerPanel = new JPanel(this.formsCardLayout);
        this.formsContainerPanel.add(panelFormTitolo, CARD_RICERCA_TITOLO);
        this.formsContainerPanel.add(panelFormCompleto, CARD_RICERCA_COMPLETA);

        this.radioRicercaTitolo.addActionListener(e -> formsCardLayout.show(formsContainerPanel, CARD_RICERCA_TITOLO));
        this.radioRicercaCompleta.addActionListener(e -> formsCardLayout.show(formsContainerPanel, CARD_RICERCA_COMPLETA));

        // 5. PULSANTE DI RICERCA
        this.btnCerca = new JButton("Avvia Ricerca");
        this.btnCerca.setFont(FONT_BOLD);
        this.btnCerca.addActionListener(e -> eseguiRicerca());

        JPanel panelBottone = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBottone.add(btnCerca);

        // 6. LISTA RISULTATI
        this.resultListModel = new DefaultListModel<>();
        this.listaRisultati = new JList<>(resultListModel);
        this.listaRisultati.setFont(FONT_BASE);
        this.listaRisultati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaRisultati.setFixedCellHeight(26);
        this.listaRisultati.setCellRenderer(new ProjectionCellRenderer());

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
        scrollPanel.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

        // 7. ASSEMBLAGGIO GENERALE
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.add(panelRadio);
        topContainer.add(formsContainerPanel);
        topContainer.add(panelBottone);

        add(topContainer);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(scrollPanel);
    }

    /**
     * Valida i filtri selezionati ed esegue la ricerca asincrona delle proiezioni tramite {@link SwingWorker}.
     */
    public void eseguiRicerca() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::eseguiRicerca);
            return;
        }

        if (radioRicercaTitolo.isSelected()) {
            eseguiRicercaPerTitolo();
        } else {
            eseguiRicercaCompleta();
        }
    }

    /**
     * Esegue la ricerca asincrona filtrando per titolo con finestra temporale automatica di 3 mesi.
     */
    private void eseguiRicercaPerTitolo() {
        String titoloFilm = textFieldTitoloSemplice.getText().trim();

        if (titoloFilm.isEmpty()) {
            mostraMessaggio("Inserire il titolo del film da cercare.", "Titolo Mancante", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate maxDataPrenotazione = LocalDate.now().plusMonths(3);

        setStatoControlli(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<GetProjectionsResponse, Void>() {
            @Override
            protected GetProjectionsResponse doInBackground() throws Exception {
                return projectionService.getProjectionsByFilmAndDate(titoloFilm, maxDataPrenotazione);
            }

            @Override
            protected void done() {
                setStatoControlli(true);
                setCursor(Cursor.getDefaultCursor());

                try {
                    GetProjectionsResponse response = get();
                    popolaListaRisultati(response);
                    textFieldTitoloSemplice.setText("");
                } catch (Exception ex) {
                    mostraMessaggio("Errore di comunicazione durante la ricerca: " + ex.getMessage(), "Errore Server", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    /**
     * Valida i vincoli dei parametri ed esegue la ricerca asincrona con filtri complessi.
     */
    private void eseguiRicercaCompleta() {
        String titoloFilm = textFieldTitoloFilm.getText().trim();
        String genere = textFieldGenere.getText().trim();

        LocalDate dInizio = parseLocalDate(this.dataInizio);
        LocalDate dFine = parseLocalDate(this.dataFine);

        BigDecimal prezzoMin = getBigDecimalFromField(this.textFieldCostoBigliettoMin);
        BigDecimal prezzoMax = getBigDecimalFromField(this.textFieldCostoBigliettoMax);

        boolean noTitolo = titoloFilm.isEmpty();
        boolean noGenere = genere.isEmpty();
        boolean noDate = (dInizio == null && dFine == null);
        boolean noPrezzoMin = (prezzoMin == null || prezzoMin.compareTo(BigDecimal.ZERO) == 0);
        boolean noPrezzoMax = (prezzoMax == null || prezzoMax.compareTo(BigDecimal.ZERO) == 0);

        if (noTitolo && noGenere && noDate && noPrezzoMin && noPrezzoMax) {
            mostraMessaggio("Inserire almeno un parametro di filtro per la ricerca completa.", "Parametri Insufficienti", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (dInizio != null && dFine != null && dFine.isBefore(dInizio)) {
            mostraMessaggio("La data di fine deve essere successiva o uguale alla data di inizio.", "Intervallo Date Invalido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ((prezzoMin != null && prezzoMin.compareTo(BigDecimal.ZERO) < 0) || (prezzoMax != null && prezzoMax.compareTo(BigDecimal.ZERO) < 0)) {
            mostraMessaggio("I prezzi non possono assumere valori negativi.", "Prezzo Invalido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (prezzoMin != null && prezzoMax != null && prezzoMax.compareTo(BigDecimal.ZERO) > 0 && prezzoMin.compareTo(prezzoMax) > 0) {
            mostraMessaggio("Il prezzo minimo non può superare il prezzo massimo.", "Prezzo Invalido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        setStatoControlli(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<GetProjectionsResponse, Void>() {
            @Override
            protected GetProjectionsResponse doInBackground() throws Exception {
                return projectionService.getProjections(
                        titoloFilm.isEmpty() ? null : titoloFilm,
                        genere.isEmpty() ? null : genere,
                        dInizio,
                        dFine,
                        prezzoMin,
                        prezzoMax
                );
            }

            @Override
            protected void done() {
                setStatoControlli(true);
                setCursor(Cursor.getDefaultCursor());
                pulisciCampi();

                try {
                    GetProjectionsResponse response = get();
                    popolaListaRisultati(response);
                } catch (Exception ex) {
                    mostraMessaggio("Errore di comunicazione durante la ricerca completa: " + ex.getMessage(), "Errore Server", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    /**
     * Popola il modello della lista con le proiezioni restituite dal backend.
     *
     * @param response la risposta contenente la lista di proiezioni
     */
    private void popolaListaRisultati(GetProjectionsResponse response) {
        resultListModel.clear();

        if (response != null && response.getProjections() != null) {
            List<ProjectionDetailsView> projections = response.getProjections();

            if (projections.isEmpty()) {
                mostraMessaggio("Nessuna proiezione trovata con i parametri indicati.", "Nessun Risultato", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (ProjectionDetailsView projection : projections) {
                    resultListModel.addElement(projection);
                }
            }
        } else {
            mostraMessaggio("Risposta nulla o non valida ricevuta dal server.", "Errore Server", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Ripristina i campi del form di ricerca avanzata ai loro valori predefiniti.
     */
    private void pulisciCampi() {
        textFieldTitoloFilm.setText("");
        textFieldGenere.setText("");
        dataInizio.setValue(null);
        dataFine.setValue(null);
        textFieldCostoBigliettoMin.setValue(BigDecimal.ZERO);
        textFieldCostoBigliettoMax.setValue(BigDecimal.ZERO);
    }

    /**
     * Inserisce una riga etichetta-campo nel pannello a griglia.
     *
     * @param panel il pannello con GridBagLayout
     * @param label l'etichetta del campo
     * @param field il componente di input
     * @param gbc   i vincoli di griglia
     * @param riga  l'indice di riga corrente
     */
    private void aggiungiRigaForm(JPanel panel, JLabel label, JComponent field, GridBagConstraints gbc, int riga) {
        gbc.gridx = 0;
        gbc.gridy = riga;
        gbc.weightx = 0.0;
        label.setFont(FONT_BASE);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridy = riga;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    /**
     * Inizializza un campo formattato con maschera numerica per la data (gg/mm/aaaa).
     *
     * @return il componente {@link JFormattedTextField} configurato
     */
    private JFormattedTextField creaCampoData() {
        JFormattedTextField field = new JFormattedTextField();
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            field.setFormatterFactory(new DefaultFormatterFactory(mask));
            field.setColumns(10);
            field.setFont(FONT_BASE);
        } catch (ParseException ignored) {
        }
        return field;
    }

    /**
     * Inizializza un campo formattato per la gestione monetaria in valuta locale (Euro).
     *
     * @return il componente {@link JFormattedTextField} configurato per importi monetari
     */
    private JFormattedTextField creaCampoValuta() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);

        if (currencyFormat instanceof java.text.DecimalFormat) {
            ((java.text.DecimalFormat) currencyFormat).setParseBigDecimal(true);
        }

        JFormattedTextField field = new JFormattedTextField(currencyFormat);
        field.setValue(BigDecimal.ZERO);
        field.setColumns(10);
        field.setFont(FONT_BASE);
        return field;
    }

    /**
     * Estrae in modo sicuro un valore {@link BigDecimal} da un campo valuta formattato.
     *
     * @param field il campo da cui recuperare l'importo
     * @return il valore monetario convertito, o {@link BigDecimal#ZERO} se non valido
     */
    private BigDecimal getBigDecimalFromField(JFormattedTextField field) {
        try {
            field.commitEdit();
        } catch (Exception ignored) {
        }

        Object value = field.getValue();
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        return BigDecimal.ZERO;
    }

    /**
     * Converte la stringa del campo data in un'istanza {@link LocalDate}.
     *
     * @param field il campo formattato contenente la data
     * @return l'oggetto {@link LocalDate}, o {@code null} se il campo è incompleto o non valido
     */
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

    /**
     * Abilita o disabilita interattivamente i pulsanti e i controlli di ricerca.
     *
     * @param enabled {@code true} per abilitare i controlli, {@code false} altrimenti
     */
    private void setStatoControlli(boolean enabled) {
        btnCerca.setEnabled(enabled);
        radioRicercaTitolo.setEnabled(enabled);
        radioRicercaCompleta.setEnabled(enabled);
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
     * Renderer personalizzato per gli elementi {@link ProjectionDetailsView} nella {@link JList}.
     */
    private static class ProjectionCellRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof ProjectionDetailsView) {
                setText(((ProjectionDetailsView) value).toString());
            }
            setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            return this;
        }
    }
}