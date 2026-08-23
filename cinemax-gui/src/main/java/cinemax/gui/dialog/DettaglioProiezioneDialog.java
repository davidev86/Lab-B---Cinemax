package cinemax.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
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

public class DettaglioProiezioneDialog extends JDialog {

    public DettaglioProiezioneDialog(Window owner, ProjectionDetails proiezione) {
        super(owner, "Dettagli Proiezione", ModalityType.APPLICATION_MODAL);
         
        setSize(480, 520);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(15, 15));
        setResizable(false);

        // --- 1. SCHEDA STRUTTURATA DATI PROIEZIONE ---
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
        JLabel lblTitolo = new JLabel(proiezione.getTitoloFilm() != null ? proiezione.getTitoloFilm() : "Senza Titolo");
        lblTitolo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitolo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitolo.setBorder(new EmptyBorder(10, 0, 15, 0));
        cardPanel.add(lblTitolo);

        // Griglia Dati Tecnici
        JPanel gridDettagli = new JPanel(new GridLayout(6, 2, 10, 10));
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
        aggiungiRiga(gridDettagli, "Età Minima:", (proiezione.getEtaMinima() != null ? proiezione.getEtaMinima() : 0) + " anni");

        cardPanel.add(gridDettagli);

        // Box Prezzo Biglietto in evidenza
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pricePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JLabel lblPrezzoTag = new JLabel("Prezzo Unitario: ");
        lblPrezzoTag.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        String costoFormattato = (proiezione.getCosto() != null) 
                ? String.format("€ %.2f", proiezione.getCosto()) 
                : "€ 0.00";
        JLabel lblPrezzoValore = new JLabel(costoFormattato);
        lblPrezzoValore.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblPrezzoValore.setForeground(new Color(0, 135, 60));

        pricePanel.add(lblPrezzoTag);
        pricePanel.add(lblPrezzoValore);
        cardPanel.add(pricePanel);

        add(cardPanel, BorderLayout.CENTER);

        // --- 2. PULSANTI D'AZIONE CON LOGICA DI RUOLO 

        JButton btnAnnulla = new JButton("Annulla");

        btnAnnulla.addActionListener(e -> dispose());

        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBottoni.add(btnAnnulla);
        add(panelBottoni, BorderLayout.SOUTH);
    }  
  


	// Helper per popolare le coppie Chiave-Valore
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