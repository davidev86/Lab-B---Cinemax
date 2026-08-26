package cinemax.clientCM.tabpanel;

import java.awt.CardLayout;
import java.awt.Color;
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
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import cinemax.application.services.ProjectionService;
import cinemax.application.services.TcpClient;
import cinemax.clientCM.callback.SelezioneProjectionCallBack;
import cinemax.contracts.dto.ui.ProjectionDetailsView;
import cinemax.contracts.responses.ui.GetProjectionsResponse;

/**
 * Pannello di ricerca per le proiezioni con due template distinti (CardLayout):
 * 1) Ricerca Rapida per Titolo (con finestra temporale automatica di 3 mesi).
 * 2) Ricerca Avanzata/Completa con tutti i filtri (genere, intervallo date, prezzi).
 */
public class SearchProjection extends JPanel {

    // Identificatori per il CardLayout che gestisce i due template di ricerca
    private static final String CARD_RICERCA_TITOLO = "CARD_TITOLO";
    private static final String CARD_RICERCA_COMPLETA = "CARD_COMPLETA";

    // Servizi e gestione liste
    private final ProjectionService projectionService;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final DefaultListModel<ProjectionDetailsView> resultListModel;
    private final JList<ProjectionDetailsView> listaRisultati;

    // Gestione dei due template di form tramite CardLayout
    private final CardLayout formsCardLayout;
    private final JPanel formsContainerPanel;

    // Controlli di selezione modalità
    private final JRadioButton radioRicercaTitolo;
    private final JRadioButton radioRicercaCompleta;

    // Campi per il Template 1: Ricerca Rapida per Titolo
    private final JTextField textFieldTitoloSemplice;

    // Campi per il Template 2: Ricerca Completa
    private final JTextField textFieldTitoloFilm;
    private final JTextField textFieldGenere;
    private final JFormattedTextField dataInizio;
    private final JFormattedTextField dataFine;
    private final JFormattedTextField textFieldCostoBigliettoMin;
    private final JFormattedTextField textFieldCostoBigliettoMax;

    public SearchProjection(SelezioneProjectionCallBack selezioneProjectionCallBack, TcpClient tcpClient) {
        this.projectionService = new ProjectionService(tcpClient);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Font fontBase = new Font("Tahoma", Font.PLAIN, 12);

        // SELETTORE MODALITÀ (RADIO BUTTONS)
        // primo radio button  a selezionato di default 'true' 
        this.radioRicercaTitolo = new JRadioButton("Ricerca per Titolo (prossimi 3 mesi)", true);
        this.radioRicercaCompleta = new JRadioButton("Ricerca Completa con Filtri Avanzati", false);

        ButtonGroup groupModalita = new ButtonGroup();
        groupModalita.add(this.radioRicercaTitolo);
        groupModalita.add(this.radioRicercaCompleta);

        JPanel panelRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelRadio.setBorder(BorderFactory.createTitledBorder("Seleziona Modalità di Ricerca"));
        panelRadio.add(this.radioRicercaTitolo);
        panelRadio.add(this.radioRicercaCompleta);

        // TEMPLATE 1: FORM RICERCA PER TITOLO
        this.textFieldTitoloSemplice = new JTextField(20);
        this.textFieldTitoloSemplice.setFont(fontBase);

        JPanel panelFormTitolo = new JPanel(new GridBagLayout());
        panelFormTitolo.setBorder(BorderFactory.createTitledBorder("Ricerca per Titolo"));
        GridBagConstraints gbcTitolo = new GridBagConstraints();
        gbcTitolo.insets = new Insets(6, 6, 6, 6);
        gbcTitolo.anchor = GridBagConstraints.WEST;

        // Riga 0: Campo Titolo
        gbcTitolo.gridx = 0; gbcTitolo.gridy = 0; gbcTitolo.fill = GridBagConstraints.NONE;
        panelFormTitolo.add(new JLabel("Titolo Film:"), gbcTitolo);

        gbcTitolo.gridx = 1; gbcTitolo.gridy = 0; gbcTitolo.fill = GridBagConstraints.HORIZONTAL; gbcTitolo.weightx = 1.0;
        panelFormTitolo.add(this.textFieldTitoloSemplice, gbcTitolo);

        // Riga 1: Informazione visiva sulla data automatica
        gbcTitolo.gridx = 1; gbcTitolo.gridy = 1; gbcTitolo.weightx = 0.0;
        JLabel labelInfoData = new JLabel("<html><i>* La ricerca mostrerà le proiezioni disponibili da oggi ai prossimi 3 mesi.</i></html>");
        labelInfoData.setForeground(Color.DARK_GRAY);
        panelFormTitolo.add(labelInfoData, gbcTitolo);

        // 3. TEMPLATE 2: FORM RICERCA COMPLETA
        this.textFieldTitoloFilm = new JTextField(20);
        this.textFieldTitoloFilm.setFont(fontBase);

        this.textFieldGenere = new JTextField(20);
        this.textFieldGenere.setFont(fontBase);

        this.dataInizio = creaCampoData();
        this.dataFine = creaCampoData();

        this.textFieldCostoBigliettoMin = creaCampoValuta(fontBase);
        this.textFieldCostoBigliettoMax = creaCampoValuta(fontBase);

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

        // CONTENITORE PER I DUE TEMPLATE
        this.formsCardLayout = new CardLayout();
        this.formsContainerPanel = new JPanel(this.formsCardLayout);
        this.formsContainerPanel.add(panelFormTitolo, CARD_RICERCA_TITOLO);
        this.formsContainerPanel.add(panelFormCompleto, CARD_RICERCA_COMPLETA);

        // Switch tra le due schermate al cambio del radio button
        this.radioRicercaTitolo.addActionListener(e -> formsCardLayout.show(formsContainerPanel, CARD_RICERCA_TITOLO));
        this.radioRicercaCompleta.addActionListener(e -> formsCardLayout.show(formsContainerPanel, CARD_RICERCA_COMPLETA));

        // PULSANTE DI AZIONE E LISTA RISULTATI
        JButton btnCerca = new JButton("Avvia Ricerca");
        btnCerca.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnCerca.addActionListener(e -> eseguiRicerca());

        JPanel panelBottone = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBottone.add(btnCerca);

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

        // Assemblaggio layout generale
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.add(panelRadio);
        topContainer.add(formsContainerPanel);
        topContainer.add(panelBottone);

        add(topContainer);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(cardPanel);
    }

    // CONTROLLO ED ESECUZIONE DELLA RICERCA

    public void eseguiRicerca() {
    	
    	if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::eseguiRicerca);
            return;
        }
        //RICERCA PER TITOLO
        if (radioRicercaTitolo.isSelected()) {
            String titoloFilm = textFieldTitoloSemplice.getText().trim();

            if (titoloFilm.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Inserire il titolo del film da cercare.",
                    "Titolo Mancante",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Calcolo automatico della data limite impostata a 3 mesi da oggi
            LocalDate maxDataPrenotazione = LocalDate.now().plusMonths(3);

            try {
                // Invocazione metodo dedicato
                GetProjectionsResponse response = projectionService.getProjectionsByFilmAndDate(titoloFilm, maxDataPrenotazione);
                popolaListaRisultati(response);
                textFieldTitoloSemplice.setText("");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Errore di comunicazione durante la ricerca per titolo: " + ex.getMessage(),
                    "Errore Server",
                    JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        // RICERCA COMPLETA
        String titoloFilm = textFieldTitoloFilm.getText().trim();
        String genere = textFieldGenere.getText().trim();

        LocalDate dInizio = parseLocalDate(this.dataInizio);
        LocalDate dFine = parseLocalDate(this.dataFine);

        BigDecimal prezzoMin = getBigDecimalFromField(this.textFieldCostoBigliettoMin);
        BigDecimal prezzoMax = getBigDecimalFromField(this.textFieldCostoBigliettoMax);
       
        boolean noTitolo = (titoloFilm == null || titoloFilm.trim().isEmpty());
        boolean noGenere = (genere == null || genere.trim().isEmpty());
        boolean noDate = (dInizio == null && dFine == null);
        boolean noPrezzoMin = (prezzoMin == null || prezzoMin.compareTo(BigDecimal.ZERO) == 0);
        boolean noPrezzoMax = (prezzoMax == null || prezzoMax.compareTo(BigDecimal.ZERO) == 0);

        if (noTitolo && noGenere && noDate && noPrezzoMin && noPrezzoMax) {
            JOptionPane.showMessageDialog(this,
                "Inserire almeno un parametro o un genere per la ricerca completa.",
                "Parametri Insufficienti",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        
        if (dFine != null && dInizio != null && dFine.isBefore(dInizio)) {
            JOptionPane.showMessageDialog(this,
                "La data di fine deve essere successiva o uguale alla data di inizio.",
                "Intervallo Date Invalido",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
         
       
        if (prezzoMin.compareTo(BigDecimal.ZERO) < 0 || prezzoMax.compareTo(BigDecimal.ZERO) < 0) {
            JOptionPane.showMessageDialog(this,
                "I prezzi non possono essere valori negativi.",
                "Prezzo Invalido",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (prezzoMax.equals(null) && prezzoMin.equals(null) && prezzoMax.compareTo(BigDecimal.ZERO) > 0 && prezzoMin.compareTo(prezzoMax) > 0) {
            JOptionPane.showMessageDialog(this,
                "Il prezzo minimo non può superare il prezzo massimo.",
                "Prezzo Invalido",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Invocazione metodo con tutti i filtri avanzati
            GetProjectionsResponse response = projectionService.getProjections(titoloFilm, genere, dInizio, dFine, prezzoMin, prezzoMax);
            popolaListaRisultati(response);
            pulisciCampi();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Errore di comunicazione durante la ricerca completa: " + ex.getMessage(),
                "Errore Server",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Aggiorna il modello della lista con i dati restituiti dal server.
     */
    private void popolaListaRisultati(GetProjectionsResponse response) {
        resultListModel.clear();

        if (response != null && response.getProjections() != null) {
            List<ProjectionDetailsView> projections = response.getProjections();
            
            if (projections.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Nessuna proiezione trovata con i parametri indicati.",
                    "Nessun Risultato",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (ProjectionDetailsView projection : projections) {
                    resultListModel.addElement(projection);
                }
                cardLayout.show(cardPanel, "scrollPanel");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Risposta nulla o non valida ricevuta dal server.", "Errore Server", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    //pulisci i textfield dopo esecuzione ricerca, oppure cambiando la selezione con il radiobutton
    private void pulisciCampi() {
    	
    		// Reset JTextField standard
        textFieldTitoloFilm.setText("");
        textFieldGenere.setText("");
        
        // Reset JFormattedTextField (date e numeri)
        dataInizio.setValue(null);
        dataFine.setValue(null);
        
        // Per campi valuta o numerici, reimposta il valore di default
        textFieldCostoBigliettoMin.setValue(BigDecimal.ZERO);
        textFieldCostoBigliettoMax.setValue(BigDecimal.ZERO);
        
    }
 
    // METODI DI FORMATTAZIONE e GUI
    private void aggiungiRigaForm(JPanel panel, JLabel label, JComponent field, GridBagConstraints gbc, int riga) {
        gbc.gridx = 0; gbc.gridy = riga; gbc.weightx = 0.0;
        panel.add(label, gbc);

        gbc.gridx = 1; gbc.gridy = riga; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private JFormattedTextField creaCampoData() {
        JFormattedTextField field = new JFormattedTextField();
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            field.setFormatterFactory(new DefaultFormatterFactory(mask));
            field.setColumns(10);
            field.setFont(new Font("Tahoma", Font.PLAIN, 12));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return field;
    }

    private JFormattedTextField creaCampoValuta(Font font) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);

        if (currencyFormat instanceof java.text.DecimalFormat) {
            ((java.text.DecimalFormat) currencyFormat).setParseBigDecimal(true);
        }

        JFormattedTextField field = new JFormattedTextField(currencyFormat);
        field.setValue(BigDecimal.ZERO);
        field.setColumns(10);
        field.setFont(font);
        
        
        
        return field;
    }

    private BigDecimal getBigDecimalFromField(JFormattedTextField field) {
        try {
            field.commitEdit();
        } catch (Exception ignored) {}

        Object value = field.getValue();
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        return BigDecimal.ZERO;
    }

    private LocalDate parseLocalDate(JFormattedTextField field) {
        String text = field.getText().trim();
        if (text.contains("_") || text.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            return LocalDate.parse(text, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
