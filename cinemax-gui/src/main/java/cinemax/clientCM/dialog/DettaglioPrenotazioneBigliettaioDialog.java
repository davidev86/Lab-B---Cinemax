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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import cinemax.contracts.dto.BookingDetails;
import cinemax.contracts.dto.ui.ProjectionDetailsView;

/**
 * Finestra di dialogo modale per la visualizzazione dettagliata di una prenotazione da parte dell'operatore (biglietteria).
 * <p>
 * Mostra il riepilogo completo della prenotazione, inclusi i dati identificativi dell'ordine, l'intestatario,
 * le informazioni tecniche ed artistiche del film in proiezione, il numero di posti riservati e il calcolo del costo complessivo.
 * </p>
 */
public class DettaglioPrenotazioneBigliettaioDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    /**
     * Formattatore per la visualizzazione standard di data e ora dello spettacolo (gg/mm/aaaa hh:mm).
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Costruisce e inizializza la finestra di dialogo modale con il dettaglio della prenotazione.
     *
     * @param owner        la finestra proprietaria (parent window)
     * @param proiezione   l'oggetto {@link ProjectionDetailsView} contenente i dettagli della proiezione
     * @param prenotazione l'oggetto {@link BookingDetails} contenente le informazioni della prenotazione
     */
    public DettaglioPrenotazioneBigliettaioDialog(Window owner, ProjectionDetailsView proiezione, BookingDetails prenotazione) {
        super(owner, "Dettagli Prenotazione", ModalityType.APPLICATION_MODAL);
         
        setSize(480, 580);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // CONTENITORE CENTRALE
        JPanel mainCenterPanel = new JPanel();
        mainCenterPanel.setLayout(new BoxLayout(mainCenterPanel, BoxLayout.Y_AXIS));
        mainCenterPanel.setBorder(new EmptyBorder(10, 15, 5, 15));

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

        // Intestazione: Codice, Intestatario, Data Spettacolo
        String idText = (prenotazione != null && prenotazione.getIdPrenotazione() != null) 
                ? "Codice prenotazione: " + prenotazione.getIdPrenotazione() : "Codice prenotazione: N/D";
        JLabel lblIdPrenotazione = new JLabel(idText);
        lblIdPrenotazione.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblIdPrenotazione.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblIdPrenotazione.setBorder(new EmptyBorder(6, 0, 4, 0));
        cardPanel.add(lblIdPrenotazione);

        String clienteText = (prenotazione != null) 
                ? (prenotazione.getNomeCliente() != null ? prenotazione.getNomeCliente() : "") + " " +
                  (prenotazione.getCognomeCliente() != null ? prenotazione.getCognomeCliente() : "")
                : "Cliente Sconosciuto";
        JLabel lblNomeCognome = new JLabel("Cliente: " + clienteText.trim());
        lblNomeCognome.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblNomeCognome.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNomeCognome.setBorder(new EmptyBorder(2, 0, 4, 0));
        cardPanel.add(lblNomeCognome);

        String dataOraText = (prenotazione != null && prenotazione.getDataOraProiezione() != null)
                ? prenotazione.getDataOraProiezione().format(DATE_TIME_FORMATTER)
                : "Data non disponibile";
        JLabel lblDataOra = new JLabel("Data e Ora: " + dataOraText);
        lblDataOra.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblDataOra.setForeground(Color.DARK_GRAY);
        lblDataOra.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDataOra.setBorder(new EmptyBorder(2, 0, 10, 0));
        cardPanel.add(lblDataOra);
        
        // Griglia Dati Tecnici Film
        JPanel gridDettagli = new JPanel(new GridLayout(6, 2, 10, 6));
        gridDettagli.setBorder(new EmptyBorder(5, 15, 10, 15));

        aggiungiRiga(gridDettagli, "Titolo Film:", proiezione != null ? proiezione.getTitoloFilm() : null);
        aggiungiRiga(gridDettagli, "Regista:", proiezione != null ? proiezione.getRegista() : null);
        aggiungiRiga(gridDettagli, "Genere:", proiezione != null ? proiezione.getGenere() : null);
        aggiungiRiga(gridDettagli, "Anno di Uscita:", (proiezione != null && proiezione.getAnno() != null) ? proiezione.getAnno().toString() : null);
        aggiungiRiga(gridDettagli, "Durata:", (proiezione != null && proiezione.getDurataMinuti() != null) ? proiezione.getDurataMinuti() + " min" : null);
        aggiungiRiga(gridDettagli, "Età Minima:", (proiezione != null && proiezione.getEtaMinima() != null) ? proiezione.getEtaMinima() + " anni" : null);

        cardPanel.add(gridDettagli);

        // Box Prezzo Unitario e Posti Liberi
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        JLabel lblPrezzoTag = new JLabel("Prezzo Unitario: ");
        lblPrezzoTag.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        BigDecimal costoUnitario = (proiezione != null && proiezione.getCosto() != null) ? proiezione.getCosto() : BigDecimal.ZERO;
        JLabel lblPrezzoValore = new JLabel(String.format("€ %.2f", costoUnitario));
        lblPrezzoValore.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblPrezzoValore.setForeground(new Color(0, 135, 60));

        JLabel postiLiberi = new JLabel(" | Posti liberi: ");
        postiLiberi.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        String postiLiberiStr = (proiezione != null && proiezione.getTotalePostiLiberi() != null) 
                ? proiezione.getTotalePostiLiberi().toString() : "0";
        JLabel conteggioPostiLiberi = new JLabel(postiLiberiStr);
        conteggioPostiLiberi.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        pricePanel.add(lblPrezzoTag);
        pricePanel.add(lblPrezzoValore);
        pricePanel.add(postiLiberi);
        pricePanel.add(conteggioPostiLiberi);
        cardPanel.add(pricePanel); 
        
        // Pannello Riepilogo Costi e Posti Prenotati
        JPanel selectSeatsContainer = new JPanel();
        selectSeatsContainer.setLayout(new BoxLayout(selectSeatsContainer, BoxLayout.Y_AXIS));
        selectSeatsContainer.setBorder(BorderFactory.createTitledBorder("Riepilogo Costi"));

        // Posti prenotati
        JPanel numPostiPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JLabel lblPrenotati = new JLabel("Posti prenotati:");
        lblPrenotati.setFont(new Font("SansSerif", Font.BOLD, 12));
        numPostiPanel.add(lblPrenotati);
        
        int numeroPosti = (prenotazione != null && prenotazione.getNumeroPosti() != null) ? prenotazione.getNumeroPosti() : 0;
        JLabel lblNumPosti = new JLabel(String.valueOf(numeroPosti));
        lblNumPosti.setFont(new Font("SansSerif", Font.BOLD, 13));
        numPostiPanel.add(lblNumPosti);
        selectSeatsContainer.add(numPostiPanel);

        // Calcolo e visualizzazione del costo totale
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JLabel lblTotaleTag = new JLabel("Costo Totale: ");
        lblTotaleTag.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        BigDecimal costoTotale = costoUnitario.multiply(BigDecimal.valueOf(numeroPosti));
        JLabel lblTotaleValore = new JLabel(String.format("€ %.2f", costoTotale));
        lblTotaleValore.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTotaleValore.setForeground(new Color(0, 135, 60));

        totalPanel.add(lblTotaleTag);
        totalPanel.add(lblTotaleValore);
        selectSeatsContainer.add(totalPanel);

        mainCenterPanel.add(cardPanel);
        mainCenterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainCenterPanel.add(selectSeatsContainer);

        add(mainCenterPanel, BorderLayout.CENTER);

        // FOOTER: Pulsante di Chiusura
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton btnChiudi = new JButton("Chiudi");
        btnChiudi.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnChiudi.setPreferredSize(new Dimension(85, 28));
        btnChiudi.addActionListener(e -> dispose());
        footerPanel.add(btnChiudi);
        
        add(footerPanel, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(btnChiudi);
    }
      
    /**
     * Inserisce una riga descrittiva (coppia etichetta-valore) all'interno del pannello a griglia.
     *
     * @param container il pannello contenitore con layout a griglia
     * @param etichetta l'etichetta descrittiva del campo
     * @param valore    il valore testuale associato (sostituito con "N/D" se nullo o vuoto)
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