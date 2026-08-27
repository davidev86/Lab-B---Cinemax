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
 * Finestra di dialogo modale per la consultazione e prenotazione di una proiezione cinematografica da parte del cliente.
 * <p>
 * Mostra la scheda dettagliata dell'opera (titolo, regia, genere, durata, costi e posti disponibili),
 * fornisce un selettore numerico interattivo per la quantità di posti desiderata con ricalcolo in tempo reale
 * del costo totale ed esegue la validazione della disponibilità prima di invocare il callback di salvataggio.
 * </p>
 */
public class DettaglioProiezioneClienteDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    /**
     * Formattatore per la rappresentazione testuale di data e ora dello spettacolo (gg/mm/aaaa hh:mm).
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Costruisce e inizializza la finestra modale per la prenotazione posti da parte del cliente.
     *
     * @param owner                la finestra proprietaria (parent window)
     * @param proiezione           l'istanza {@link ProjectionDetailsView} contenente i dettagli della proiezione selezionata
     * @param storeBookingCallback il callback che accetta il numero di posti prenotati confermati dall'utente
     */
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
        String titolo = (proiezione != null && proiezione.getTitoloFilm() != null) 
                ? proiezione.getTitoloFilm() : "Senza Titolo";
        JLabel lblTitolo = new JLabel(titolo);
        lblTitolo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitolo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitolo.setBorder(new EmptyBorder(10, 0, 15, 0));
        cardPanel.add(lblTitolo);

        // Griglia Dati Tecnici
        JPanel gridDettagli = new JPanel(new GridLayout(6, 2, 10, 8));
        gridDettagli.setBorder(new EmptyBorder(5, 15, 10, 15));

        String dataOra = (proiezione != null && proiezione.getDataOraProiezione() != null) 
                ? proiezione.getDataOraProiezione().format(DATE_TIME_FORMATTER) 
                : "N/D";

        aggiungiRiga(gridDettagli, "Data & Ora:", dataOra);
        aggiungiRiga(gridDettagli, "Regista:", proiezione != null ? proiezione.getRegista() : null);
        aggiungiRiga(gridDettagli, "Genere:", proiezione != null ? proiezione.getGenere() : null);
        aggiungiRiga(gridDettagli, "Anno di Uscita:", (proiezione != null && proiezione.getAnno() != null) ? proiezione.getAnno().toString() : "-");
        aggiungiRiga(gridDettagli, "Durata:", (proiezione != null && proiezione.getDurataMinuti() != null ? proiezione.getDurataMinuti() : 0) + " min");
        aggiungiRiga(gridDettagli, "Età Minima:", (proiezione != null && proiezione.getEtaMinima() != null ? proiezione.getEtaMinima() : 0) + " anni");

        cardPanel.add(gridDettagli);

        // Box Prezzo e Posti Liberi
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        JLabel lblPrezzoTag = new JLabel("Prezzo Unitario: ");
        lblPrezzoTag.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        BigDecimal costoUnitario = (proiezione != null && proiezione.getCosto() != null) ? proiezione.getCosto() : BigDecimal.ZERO;
        String costoFormattato = String.format("€ %.2f", costoUnitario);
        
        JLabel lblPrezzoValore = new JLabel(costoFormattato);
        lblPrezzoValore.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblPrezzoValore.setForeground(new Color(0, 135, 60));

        JLabel postiLiberi = new JLabel(" | Posti liberi: ");
        postiLiberi.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        String postiLiberiStr = (proiezione != null && proiezione.getTotalePostiLiberi() != null) 
                ? proiezione.getTotalePostiLiberi().toString() : "0";
        JLabel conteggioPostiLiberi = new JLabel(postiLiberiStr);
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

        // Ricalcolo del totale al variare dello spinner
        reservedSeats.addChangeListener(e -> {
            int posti = (Integer) reservedSeats.getValue();
            BigDecimal totale = costoUnitario.multiply(BigDecimal.valueOf(posti));
            lblTotaleValore.setText(String.format("€ %.2f", totale));
        });

        selectSeatsContainer.add(spinnerPanel);
        selectSeatsContainer.add(totalPanel);

        // Assembla i pannelli centrali nel contenitore principale
        mainCenterPanel.add(cardPanel);
        mainCenterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainCenterPanel.add(selectSeatsContainer);

        add(mainCenterPanel, BorderLayout.CENTER);

        // BOTTONI DI AZIONE
        JButton btnAnnulla = new JButton("Annulla");
        JButton btnAzione = new JButton("Prenota Posti");     

        btnAzione.addActionListener(e -> {
            if (storeBookingCallback != null) {
                int postiSelezionati = (Integer) reservedSeats.getValue();
                
                if (proiezione != null && proiezione.getTotalePostiLiberi() != null && postiSelezionati > proiezione.getTotalePostiLiberi()) {
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

        getRootPane().setDefaultButton(btnAzione);
    }   

    /**
     * Aggiunge una riga di dettaglio (coppia etichetta-valore) al contenitore con layout a griglia.
     *
     * @param container il pannello contenitore della griglia
     * @param etichetta l'etichetta identificativa della proprietà
     * @param valore    la stringa rappresentante il valore associato
     */
    private void aggiungiRiga(JPanel container, String etichetta, String valore) {
        JLabel lblChiave = new JLabel(etichetta);
        lblChiave.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblChiave.setForeground(Color.DARK_GRAY);
    
        JLabel lblVal = new JLabel(valore != null && !valore.trim().isEmpty() ? valore : "N/D");
        lblVal.setFont(new Font("SansSerif", Font.PLAIN, 12));
    
        container.add(lblChiave);
        container.add(lblVal);
    }
}