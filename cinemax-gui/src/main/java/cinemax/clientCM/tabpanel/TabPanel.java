package cinemax.clientCM.tabpanel;

import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import cinemax.application.services.BookingService;
import cinemax.application.services.ProjectionService;
import cinemax.application.services.TcpClient;
import cinemax.clientCM.callback.LoginCallBack;
import cinemax.clientCM.callback.SelezioneBookingCallBack;
import cinemax.clientCM.callback.SelezioneProjectionCallBack;
import cinemax.clientCM.dialog.DettaglioProiezioneClienteDialog;
import cinemax.clientCM.dialog.DettaglioProiezioneDialog;
import cinemax.clientCM.dialog.DettaglioProiezioneProiezionistaDialog;
import cinemax.contracts.dto.BookingDetails;
import cinemax.contracts.dto.Enums.Ruolo;
import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.dto.UserMinInfo;
import cinemax.contracts.dto.ui.ProjectionDetailsView;
import cinemax.contracts.responses.DeleteProjectionResponse;
import cinemax.contracts.responses.StoreBookingResponse;
import cinemax.contracts.responses.ui.GetProjectionResponse;

public class TabPanel extends JPanel implements SelezioneProjectionCallBack, LoginCallBack {

    private final TcpClient tcpClient;
    private UserMinInfo user;
    
    // Tabs
    private JTabbedPane tabbedPane;
    private SearchBooking searchBookings;
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
                searchBookings = new SearchBooking(tcpClient);
                tabbedPane.addTab("Ricerca prenotazioni", searchBookings);
                SearchBookingCurrentDay searchBookingCurrentDay = new SearchBookingCurrentDay(tcpClient);
                tabbedPane.addTab("Prenotazioni di oggi", searchBookingCurrentDay);
            }  

            // Solo per CLIENTE
            if (user.getRuolo() == Ruolo.CLIENTE) {
                clientBooking = new ClientBooking(user, tcpClient);
                tabbedPane.addTab("Le tue prenotazioni", clientBooking);
            }
            
            // Solo per PROIEZIONISTA
            if (user.getRuolo() == Ruolo.PROIEZIONISTA) {
                proiezionistaChangeProjection = new ProiezionistaChangeProjection(tcpClient);
                tabbedPane.addTab("Inserisci nuova proiezione", proiezionistaChangeProjection);
                
                SearchProjectionHistory storiproiezionistaChangeProjection = new SearchProjectionHistory(tcpClient);
                tabbedPane.addTab("Storico proiezioni", storiproiezionistaChangeProjection);
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

    @Override
    public void onSelezione(ProjectionDetailsView projection) {
        BookingService bkgService = new BookingService(tcpClient);
        ProjectionService projectionService = new ProjectionService(tcpClient);
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
                            if (res != null && res.isSuccess()) {
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

                        // 1. Callback MODIFICA
                        (ProjectionDetails projModificata) -> {
                            try {
                                projectionService.updateProjection(
                                    projModificata.getId(), 
                                    projModificata.getIdFilm(), 
                                    projModificata.getDataOraProiezione(), 
                                    projModificata.getCosto()
                                );

                                JOptionPane.showMessageDialog(this, "Proiezione modificata con successo!", "Esito Modifica", JOptionPane.INFORMATION_MESSAGE);
                                this.searchProjection.eseguiRicerca();
                                return true; // Operazione riuscita: chiude la dialog

                            } catch (Exception ex) {
                                // Intercetta l'IllegalArgumentException lanciata dal service (es. sovrapposizione orari)
                                JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore Modifica", JOptionPane.ERROR_MESSAGE);
                                return false; // Mantiene la dialog aperta per correggere i dati
                            }
                        },

                        // 2. Callback CANCELLAZIONE
                        (ProjectionDetails projCancellata) -> {
                        	try {
                                DeleteProjectionResponse res = projectionService.deleteProjection(projCancellata.getId());

                                if (res != null && res.isSuccess()) {
                                    JOptionPane.showMessageDialog(this, "Proiezione annullata con successo!", "Esito Annullamento", JOptionPane.INFORMATION_MESSAGE);
                                    this.searchProjection.eseguiRicerca();
                                    return true; // Chiude il dialog
                                } else {
                                    JOptionPane.showMessageDialog(this, "Impossibile annullare la proiezione. Verificare che non ci siano prenotazioni collegate.", "Errore Annullamento", JOptionPane.ERROR_MESSAGE);
                                    return false; // Mantiene il dialog aperto
                                }

                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore Annullamento", JOptionPane.ERROR_MESSAGE);
                                return false;
                            }
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
}