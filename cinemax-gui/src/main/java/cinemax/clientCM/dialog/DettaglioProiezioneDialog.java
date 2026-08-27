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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import cinemax.contracts.dto.ProjectionDetails;

/**
 * Finestra di dialogo modale per la visualizzazione dei dettagli completi di una proiezione cinematografica.
 * <p>
 * Mostra le specifiche tecniche e artistiche del film (titolo, regia, genere, durata, anno di uscita, età minima),
 * la data e l'orario di programmazione, nonché la tariffa unitaria del biglietto.
 * </p>
 */
public class DettaglioProiezioneDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    /**
     * Formattatore per la visualizzazione di data e ora dello spettacolo (gg/mm/aaaa hh:mm).
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Costruisce e inizializza la finestra modale per la consultazione dei dettagli della proiezione.
     *
     * @param owner      la finestra proprietaria (parent window)
     * @param proiezione l'oggetto {@link ProjectionDetails} contenente le informazioni della proiezione da visualizzare
     */
    public DettaglioProiezioneDialog(Window owner, ProjectionDetails proiezione) {
        super(owner, "Dettagli Proiezione", ModalityType.APPLICATION_MODAL);
         
        setSize(480, 520);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(15, 15));
        setResizable(false);

        // PANNELLO SCHEDA INFORMATIVA
        JPanel cardPanel = new JPanel(); 
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new CompoundBorder(
                new EmptyBorder(15, 15, 10, 15),
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
        JPanel gridDettagli = new JPanel(new GridLayout(6, 2, 10, 10));
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

        // Box Prezzo Biglietto Unitario in evidenza
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pricePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JLabel lblPrezzoTag = new JLabel("Prezzo Unitario: ");
        lblPrezzoTag.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        BigDecimal costoUnitario = (proiezione != null && proiezione.getCosto() != null) ? proiezione.getCosto() : BigDecimal.ZERO;
        JLabel lblPrezzoValore = new JLabel(String.format("€ %.2f", costoUnitario));
        lblPrezzoValore.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblPrezzoValore.setForeground(new Color(0, 135, 60));

        pricePanel.add(lblPrezzoTag);
        pricePanel.add(lblPrezzoValore);
        cardPanel.add(pricePanel);

        add(cardPanel, BorderLayout.CENTER);

        // FOOTER: Pulsante di Chiusura / Annullamento
        JButton btnAnnulla = new JButton("Chiudi");
        btnAnnulla.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnAnnulla.setPreferredSize(new Dimension(85, 28));
        btnAnnulla.addActionListener(e -> dispose());

        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBottoni.add(btnAnnulla);
        add(panelBottoni, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnAnnulla);
    }  

    /**
     * Inserisce una riga descrittiva (coppia etichetta-valore) all'interno del contenitore a griglia.
     *
     * @param container il pannello contenitore della griglia
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