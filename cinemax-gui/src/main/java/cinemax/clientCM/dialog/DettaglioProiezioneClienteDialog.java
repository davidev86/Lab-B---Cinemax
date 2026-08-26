/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.clientCM.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import cinemax.contracts.dto.ui.ProjectionDetailsView;

/**
 * Dialog per visualizzare i dettagli di una proiezione e consentire la prenotazione
 * lato cliente. Mostra informazioni sul film, prezzo e fornisce il controllo per
 * selezionare il numero di posti da prenotare.
 */
public class DettaglioProiezioneClienteDialog extends JDialog {

    public DettaglioProiezioneClienteDialog(Window owner, ProjectionDetailsView proiezione, 
            Consumer<Integer> storeBookingCallback) {
        super(owner, "Dettagli Proiezione", ModalityType.APPLICATION_MODAL);
         
        setSize(480, 590);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // CONTENITORE CENTRALE
        JPanel mainCenterPanel = new JPanel();
        mainCenterPanel.setLayout(new BoxLayout(mainCenterPanel, BoxLayout.Y_AXIS));
        mainCenterPanel.setBorder(new EmptyBorder(10, 15, 5, 15));

        // SCHEDA STRUTTURATA DATI PROIEZIONE
        JPanel cardPanel = new JPanel(); 
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new CompoundBorder(
                new EmptyBorder(5, 5, 5, 5),
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(), 
                        " Informazioni Spettacolo ", 
                        TitledBorder.LEFT, 
                        TitledBorder.TOP, 
                        new Font("SansSerif", Font.BOLD, 13)
                )
        ));

        // Intestazione Titolo Film
        JLabel lblTitolo = new JLabel(proiezione.getTitoloFilm() != null ? proiezione.getTitoloFilm() : "Senza Titolo");
        lblTitolo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitolo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitolo.setBorder(new EmptyBorder(10, 0, 15, 0));
        cardPanel.add(lblTitolo);

        // Griglia Dati Tecnici
        JPanel gridDettagli = new JPanel(new GridLayout(6, 2, 10, 8));
        gridDettagli.setBorder(new EmptyBorder(5, 15, 10, 15));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataOra = (proiezione.getDataOraProiezione() != null) 
                ? proiezione.getDataOraProiezione().format(formatter) 
                : "N/D";

        aggiungiRiga(gridDettagli, "Data & Ora:", dataOra);
        aggiungiRiga(gridDettagli, "Regista:", proiezione.getRegista());
        aggiungiRiga(gridDettagli, "Genere:", proiezione.getGenere());
        aggiungiRiga(gridDettagli, "Anno di Uscita:", String.valueOf(proiezione.getAnno() != null ? proiezione.getAnno() : "-"));
        aggiungiRiga(gridDettagli, "Durata:", (proiezione.getDurataMinuti() != null ? proiezione.getDurataMinuti() : 0) + " min");
        aggiungiRiga(gridDettagli, "EtÃ  Minima:", (proiezione.getEtaMinima() != null ? proiezione.getEtaMinima() : 0) + " anni");

        cardPanel.add(gridDettagli);

        // Box Prezzo e Posti Liberi
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        JLabel lblPrezzoTag = new JLabel("Prezzo Unitario: ");
        lblPrezzoTag.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        BigDecimal costoUnitario = proiezione.getCosto() != null ? proiezione.getCosto() : BigDecimal.ZERO;
        String costoFormattato = String.format("€ %.2f", costoUnitario);
        
        JLabel lblPrezzoValore = new JLabel(costoFormattato);
        lblPrezzoValore.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblPrezzoValore.setForeground(new Color(0, 135, 60));

        JLabel postiLiberi = new JLabel(" | Posti liberi: ");
        postiLiberi.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JLabel conteggioPostiLiberi = new JLabel(proiezione.getTotalePostiLiberi() != null ? proiezione.getTotalePostiLiberi().toString() : "0");
        conteggioPostiLiberi.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        pricePanel.add(lblPrezzoTag);
        pricePanel.add(lblPrezzoValore);
        pricePanel.add(postiLiberi);
        pricePanel.add(conteggioPostiLiberi);
        cardPanel.add(pricePanel); 
        
        // SELEZIONE POSTI E TOTALE (SPINNER + TOTALE)
        JPanel selectSeatsContainer = new JPanel();
        selectSeatsContainer.setLayout(new BoxLayout(selectSeatsContainer, BoxLayout.Y_AXIS));
        selectSeatsContainer.setBorder(BorderFactory.createTitledBorder("Prenotazione"));

        // Pannello Spinner
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JLabel lblPrenota = new JLabel("Posti da prenotare:");
        lblPrenota.setFont(new Font("SansSerif", Font.BOLD, 12));
        spinnerPanel.add(lblPrenota);
        
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 20, 1);
        JSpinner reservedSeats = new JSpinner(model);
        reservedSeats.setFont(new Font("SansSerif", Font.PLAIN, 13));
        spinnerPanel.add(reservedSeats);

        // Pannello Costo Totale
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JLabel lblTotaleTag = new JLabel("Costo Totale: ");
        lblTotaleTag.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        JLabel lblTotaleValore = new JLabel(String.format("€ %.2f", costoUnitario));
        lblTotaleValore.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTotaleValore.setForeground(new Color(0, 135, 60));

        totalPanel.add(lblTotaleTag);
        totalPanel.add(lblTotaleValore);

        reservedSeats.addChangeListener(e -> {
            int posti = (Integer) reservedSeats.getValue();
            BigDecimal totale = costoUnitario.multiply(BigDecimal.valueOf(posti));
            lblTotaleValore.setText(String.format("€ %.2f", totale));
        });

        selectSeatsContainer.add(spinnerPanel);
        selectSeatsContainer.add(totalPanel);

        // Assembla i pannelli centrali nel contenitore unico
        mainCenterPanel.add(cardPanel);
        mainCenterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainCenterPanel.add(selectSeatsContainer);

        // Unica aggiunta a BorderLayout.CENTER
        add(mainCenterPanel, BorderLayout.CENTER);

        // BOTTONI DI AZIONE
        JButton btnAnnulla = new JButton("Annulla");
        JButton btnAzione = new JButton("Prenota Posti");     

        btnAzione.addActionListener(e -> {
            if (storeBookingCallback != null) {
                int postiSelezionati = (Integer) reservedSeats.getValue();
                
                if (proiezione.getTotalePostiLiberi() != null && postiSelezionati > proiezione.getTotalePostiLiberi()) {
                     JOptionPane.showMessageDialog(this, 
                             "Il numero di posti inserito è superiore al numero di posti disponibili.", 
                             "Numero posti non valido", 
                             JOptionPane.ERROR_MESSAGE);
                     return;              
                }
                    
                storeBookingCallback.accept(postiSelezionati);  
            }
            dispose();            
        });

        btnAnnulla.addActionListener(e -> dispose());

        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBottoni.add(btnAnnulla);
        panelBottoni.add(btnAzione);
        add(panelBottoni, BorderLayout.SOUTH);
    }   

    private void aggiungiRiga(JPanel container, String etichetta, String valore) {
        JLabel lblChiave = new JLabel(etichetta);
        lblChiave.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblChiave.setForeground(Color.DARK_GRAY);
    
        JLabel lblVal = new JLabel(valore != null && !valore.isEmpty() ? valore : "N/D");
        lblVal.setFont(new Font("SansSerif", Font.PLAIN, 12));
    
        container.add(lblChiave);
        container.add(lblVal);
    }
}

