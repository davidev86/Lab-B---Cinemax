package cinemax.gui.tabpanel;



import javax.swing.*;

import cinemax.application.services.BookingService;
import cinemax.application.services.ProjectionService;
import cinemax.application.services.TcpClient;
import cinemax.contracts.dto.BookingDetails;
import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.responses.GetBookingResponse;
import cinemax.contracts.responses.GetProjectionResponse;
import cinemax.gui.callback.SelezioneBookingCallBack;
import cinemax.gui.callback.SelezioneProjectionCallBack;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ClientBookingPanel extends JPanel {

	private final BookingService bookingService;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final DefaultListModel<BookingDetails> resultListModel;
    private final JList<BookingDetails> listaRisultati;
    
    private final JFormattedTextField textFieldCodicePrenotazione;
    private final JTextField textFieldNome;
    private final JTextField textFieldCognome;
    private final JTextField textFieldTitoloFilm;
    private final JSpinner dateSpinnerInizio;
    private final JSpinner dateSpinnerFine;
    
    
    public ClientBookingPanel(SelezioneBookingCallBack selezioneBookingCallBack, TcpClient tcpClient) {
 	 
    	
    	this.bookingService = new BookingService(tcpClient);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //creazione e composizione del pannello ricerca
        JPanel researchPanel = new JPanel();
        researchPanel.setLayout(new BoxLayout(researchPanel, BoxLayout.Y_AXIS));

        //ricerca per CodicePrenotazione
        JLabel labelCodicePrenotazione = new JLabel("Ricerca prenotazioni:");

        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        integerFormat.setGroupingUsed(false); // Rimuove i punti delle migliaia

        this.textFieldCodicePrenotazione = new JFormattedTextField(integerFormat);
        this.textFieldCodicePrenotazione.setColumns(10);
        this.textFieldCodicePrenotazione.setValue(0);
        
        this.textFieldCodicePrenotazione.setEditable(true);
        this.textFieldCodicePrenotazione.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.textFieldCodicePrenotazione.setPreferredSize(new Dimension(250, 25));
       
        //ricerca per Nome
        JLabel labelNome = new JLabel("Ricerca per Nome:");
        this.textFieldNome = new JTextField();
        this.textFieldNome.setEditable(true);
        this.textFieldNome.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.textFieldNome.setPreferredSize(new Dimension(250, 25));
    
      //ricerca per Cognome
        JLabel labelCognome = new JLabel("Ricerca per Cognome:");
        this.textFieldCognome = new JTextField();
        this.textFieldCognome.setEditable(true);
        this.textFieldCognome.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.textFieldCognome.setPreferredSize(new Dimension(250, 25));
        
        //ricerca per titolo
        JLabel labelTitolo = new JLabel("Ricerca per Titolo Film:");
        this.textFieldTitoloFilm = new JTextField();
        this.textFieldTitoloFilm.setEditable(true);
        this.textFieldTitoloFilm.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.textFieldTitoloFilm.setPreferredSize(new Dimension(250, 25));
        
        //ricerca per data
        JLabel labelData = new JLabel("Ricerca per data:");
        
        JLabel labelDataInizio = new JLabel("data inizio:");
        this.dateSpinnerInizio = new JSpinner(new SpinnerDateModel());
        this.dateSpinnerInizio.setEditor(new JSpinner.DateEditor(dateSpinnerInizio, "dd/MM/yyyy"));
        
        JLabel labelDataFine = new JLabel("data fine:");
        this.dateSpinnerFine = new JSpinner(new SpinnerDateModel());
        this.dateSpinnerFine.setEditor(new JSpinner.DateEditor(dateSpinnerFine, "dd/MM/yyyy"));
                
        JButton button = new JButton("Ricerca");
       // String filePath = "C:/Temp/Output";

        researchPanel.add(labelCodicePrenotazione);
        researchPanel.add(textFieldCodicePrenotazione);
        researchPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        researchPanel.add(labelNome);
        researchPanel.add(textFieldNome);
        researchPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        researchPanel.add(labelCognome);
        researchPanel.add(textFieldCognome);
        researchPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        researchPanel.add(labelTitolo);
        researchPanel.add(textFieldTitoloFilm);
        researchPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        researchPanel.add(labelDataInizio);
        researchPanel.add(dateSpinnerInizio);
        researchPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        researchPanel.add(labelDataFine);
        researchPanel.add(dateSpinnerFine);
        researchPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        researchPanel.add(Box.createRigidArea(new Dimension(0, 10)));
       
        researchPanel.add(button);

     // 2. Inizializzazione Lista e Modello
         
        resultListModel = new DefaultListModel<>();
        listaRisultati = new JList<>(resultListModel);
        listaRisultati.setFont(new Font("Tahoma", Font.PLAIN, 12));   
        
        
     // Gestione click su un elemento della lista
        listaRisultati.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 || e.getClickCount() == 2) {
                    int index = listaRisultati.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        BookingDetails selectedItem = resultListModel.getElementAt(index);
                        if (selezioneBookingCallBack != null) {
                            selezioneBookingCallBack.onSelezione(selectedItem);
                        }
                    }
                }
            }
        });
        
        
        
        JScrollPane scrollPanel = new JScrollPane(listaRisultati);
        scrollPanel.setSize(new Dimension(800, 400));
        scrollPanel.setFont(new Font("Tahoma", Font.PLAIN, 12));


     // 3. Pannello con CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(scrollPanel,
                "scrollPanel");


     // Composizione nel pannello principale
        add(researchPanel);
        add(Box.createRigidArea(new Dimension(10, 10)));
        add(cardPanel);

            
        button.addActionListener(e -> eseguiRicerca());
    }

    private void eseguiRicerca() {
        // Validazione Dati
    	int codicePrenotazione = ((Number) textFieldCodicePrenotazione.getValue()).intValue();
    	String nome = textFieldNome.getText().trim();
    	String cognome = textFieldCognome.getText().trim();
    	String titolo = textFieldTitoloFilm.getText().trim();
        
        
        if (codicePrenotazione==0 && nome.isEmpty() && cognome.isEmpty() && titolo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserire un elemento per effettuare la ricerca.", "Dato Mancante", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Estrazione e Validazione Date
        LocalDate dataInizio = convertToLocalDate((Date) dateSpinnerInizio.getValue());
        LocalDate dataFine = convertToLocalDate((Date) dateSpinnerFine.getValue());

        if (dataInizio.isAfter(dataFine)) {
            JOptionPane.showMessageDialog(this, "La data di inizio non può essere successiva alla data di fine.", "Errore Date", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Esecuzione chiamata protetta da try-catch
        try {
            GetBookingResponse response = bookingService.getBookings(codicePrenotazione, nome, cognome, titolo, dataInizio, dataFine);

            resultListModel.clear();

            if (response != null && response.getBookings() != null) {
                List<BookingDetails> bookings = response.getBookings();
                
                if (bookings.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nessuna prenotazione trovata per i criteri specificati.", "Nessun Risultato", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    for (BookingDetails booking : bookings) {
                        resultListModel.addElement(booking);
                    }
                    cardLayout.show(cardPanel, "scrollPanel");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Risposta non valida dal server.", "Errore Server", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Errore di comunicazione con il server: " + ex.getMessage(), 
                "Errore di Rete", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
        
