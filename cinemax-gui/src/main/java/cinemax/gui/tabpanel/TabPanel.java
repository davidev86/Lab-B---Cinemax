package cinemax.gui.tabpanel;

import cinemax.application.services.BookingService;
import cinemax.application.services.ProjectionService;
import cinemax.application.services.TcpClient;
import cinemax.contracts.dto.BookingDetails;
import cinemax.contracts.dto.Enums.Ruolo;
import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.dto.UserMinInfo;
import cinemax.contracts.dto.ui.ProjectionDetailsView;
import cinemax.contracts.responses.StoreBookingResponse;
import cinemax.gui.callback.LoginCallBack;
import cinemax.gui.callback.SelezioneBookingCallBack;
import cinemax.gui.callback.SelezioneProjectionCallBack;
import cinemax.gui.dialog.DettaglioProiezioneClienteDialog;
import cinemax.gui.dialog.DettaglioProiezioneDialog;
import cinemax.gui.dialog.DettaglioProiezioneProiezionistaDialog;
import cinemax.gui.callback.SelezioneFilmCallBack;


import javax.swing.*;
import java.awt.*;

public class TabPanel extends JPanel implements SelezioneProjectionCallBack, LoginCallBack, SelezioneBookingCallBack {

    private final TcpClient tcpClient;
    private UserMinInfo user;
    
    //Tabs
    private JTabbedPane tabbedPane;
    private SearchBooking ricercaPrenotazioni;
    private ClientBooking clientBooking;
    private ProiezionistaChangeProjection proiezionistaChangeProjection;
    private SearchProjection searchProjection;

    public TabPanel(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    public JTabbedPane build() {
        tabbedPane = new JTabbedPane(JTabbedPane.RIGHT); // Schede a destra (verticali)

        // Configura le schede iniziali per utente non loggato (ospite)
        aggiornaTabPerRuolo();

        return tabbedPane;
    }

    /**
     * Ricostruisce le schede del JTabbedPane mostrando solo quelle consentite
     * in base all'utente attualmente autenticato.
     */ 
    public void aggiornaTabPerRuolo() {
        if (tabbedPane == null) return;

        tabbedPane.removeAll(); // Rimuove tutte le schede precedenti per aggiornarle

        // 1. SCHEDA 1: Ricerca Proiezioni (Sempre visibile a chiunque)
        searchProjection = new SearchProjection(this, tcpClient);
        tabbedPane.addTab("Ricerca proiezioni", searchProjection);

        // 2. SCHEDE RISERVATE (Solo se l'utente è loggato)
        if (user != null && user.getRuolo() != null) {

            // Solo per BIGLIETTAIO
            if (user.getRuolo() == Ruolo.BIGLIETTAIO) {
                ricercaPrenotazioni = new SearchBooking(this, tcpClient);
                tabbedPane.addTab("Ricerca prenotazioni", ricercaPrenotazioni);
            }

            // Solo per CLIENTE
            if (user.getRuolo() == Ruolo.CLIENTE) {
            	clientBooking = new ClientBooking(user, this, tcpClient);
                tabbedPane.addTab("Le tue prenotazioni", clientBooking);
              
            }
            
            // Solo per PROIEZIONISTA
            if (user.getRuolo() == Ruolo.PROIEZIONISTA) {
            	proiezionistaChangeProjection = new ProiezionistaChangeProjection(tcpClient);
                tabbedPane.addTab("Inserisci nuova proiezione", proiezionistaChangeProjection);
            }
        }

        tabbedPane.revalidate();
        tabbedPane.repaint();
    }

    // =========================================================================
    // GESTIONE EVENTI LOGIN / LOGOUT
    // =========================================================================

    @Override
    public void onLoginSuccess(UserMinInfo user) {
        this.user = user;
        setPanelforUSerLogged(user);
    }

    @Override
    public void onLoginFailed(String errorMessage) {
        JOptionPane.showMessageDialog(this, "Login fallito: " + errorMessage, "Errore Autenticazione", JOptionPane.ERROR_MESSAGE);
    }



    // =========================================================================
    // GESTIONE SELEZIONE PROIEZIONI
    // =========================================================================

    public void onSelezione(ProjectionDetailsView projection) {
        BookingService bkgService = new BookingService(tcpClient);
        ProjectionService projectionService  = new ProjectionService(tcpClient);
        Window parentWindow = SwingUtilities.getWindowAncestor(TabPanel.this);
        JDialog dialog = null;

        if (user == null) {
            // Utente NON loggato (ospite)
            dialog = new DettaglioProiezioneDialog(parentWindow, projection);
        } else {
            // Utente LOGGATO
            switch (user.getRuolo()) {
                case CLIENTE:
                    dialog = new DettaglioProiezioneClienteDialog(
                        parentWindow,
                        projection,
                        (Integer seats) -> {
                           StoreBookingResponse res = bkgService.insertBooking(user.getId(), projection.getId(), seats);
                           
                           if(res.isSuccess()) {
                        	   
                        	  this.clientBooking.visualizzaBooking();
                        	  this.searchProjection.eseguiRicerca();
                           }  
                        } 
                    );
                    break;

                case PROIEZIONISTA:
                    dialog = new DettaglioProiezioneProiezionistaDialog(
                        parentWindow,
                        projection,
                        (ProjectionDetails projModificata) -> {
                        	projectionService.updateProjection(
                        		    projModificata.getId(), 
                        		    projModificata.getIdFilm(), 
                        		    projModificata.getDataOraProiezione(), 
                        		    projModificata.getCosto()
                        		); },
                        (ProjectionDetails projCancellata) -> {
                            // Cancella proiezione su backend
                        },
                        
                       null
                    );
                                      
                    break;

                default:
                    dialog = new DettaglioProiezioneDialog(parentWindow, projection);
                    break;
            }
        }

        if (dialog != null) {
            dialog.setVisible(true);
        }
    }

    @Override
    public void offSelezione(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Avviso", JOptionPane.WARNING_MESSAGE);
    }
    
    public void setPanelforUSerLogged(UserMinInfo user) {
        this.user = user;
        aggiornaTabPerRuolo(); // Ricostruisce le schede mostrando quelle relative al ruolo
    }   
    
    public void setPanelforUserUnlogged() {
        this.user = null;
        // Ripristina la visualizzazione per utente non autenticato
        aggiornaTabPerRuolo();
    }

	@Override
	public void onSelezione(BookingDetails bookingDetails, Integer idPrenotazione) {
		// TODO Auto-generated method stub
		
	}
    
    
}