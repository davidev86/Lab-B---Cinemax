/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.clientCM.tabpanel;

import java.awt.BorderLayout;
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
import cinemax.clientCM.callback.SelezioneProjectionCallBack;
import cinemax.clientCM.dialog.DettaglioProiezioneClienteDialog;
import cinemax.clientCM.dialog.DettaglioProiezioneDialog;
import cinemax.clientCM.dialog.DettaglioProiezioneProiezionistaDialog;
import cinemax.contracts.dto.Enums.Ruolo;
import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.dto.UserMinInfo;
import cinemax.contracts.dto.ui.ProjectionDetailsView;
import cinemax.contracts.responses.DeleteProjectionResponse;
import cinemax.contracts.responses.StoreBookingResponse;

/**
 * Pannello contenitore a schede tabulate che si riconfigura dinamicamente in base al ruolo dell'utente autenticato.
 * <p>
 * Gestisce la visualizzazione selettiva dei moduli dell'applicazione (ricerca proiezioni, gestione catalogo,
 * prenotazioni e storico) adattando le azioni disponibili (apertura modali di dettaglio, prenotazione, modifica
 * o eliminazione proiezioni) in base ai permessi specifici (Cliente, Bigliettaio, Proiezionista o Ospite).
 * </p>
 */
public class TabPanel extends JPanel implements SelezioneProjectionCallBack, LoginCallBack {

    private static final long serialVersionUID = 1L;

    private final TcpClient tcpClient;
    private UserMinInfo user;

    // Componenti e schede del pannello
    private JTabbedPane tabbedPane;
    private SearchProjection searchProjection;
    private SearchBooking searchBookings;
    private ClientBooking clientBooking;
    private ProiezionistaChangeProjection proiezionistaChangeProjection;

    /**
     * Costruisce il pannello a schede tabulate configurando il client di rete.
     *
     * @param tcpClient il client TCP per la comunicazione con il server di backend
     */
    public TabPanel(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
        setLayout(new BorderLayout());
    }

    /**
     * Costruisce e assembla il componente {@link JTabbedPane} con posizionamento delle schede a destra.
     *
     * @return il componente {@link JTabbedPane} inizializzato con i permessi correnti
     */
    public JTabbedPane build() {
        tabbedPane = new JTabbedPane(JTabbedPane.RIGHT);
        aggiornaTabPerRuolo();
        return tabbedPane;
    }

    /**
     * Ricostruisce le schede del {@link JTabbedPane} mostrando esclusivamente i moduli
     * accessibili in funzione del ruolo dell'utente attualmente autenticato.
     */
    public void aggiornaTabPerRuolo() {
        if (tabbedPane == null) {
            return;
        }

        tabbedPane.removeAll();

        // Scheda base sempre visibile (anche per utenti ospiti)
        searchProjection = new SearchProjection(this, tcpClient);
        tabbedPane.addTab("Ricerca proiezioni", searchProjection);

        if (user != null && user.getRuolo() != null) {
            // Moduli per il ruolo BIGLIETTAIO
            if (user.getRuolo() == Ruolo.BIGLIETTAIO) {
                searchBookings = new SearchBooking(tcpClient);
                tabbedPane.addTab("Ricerca prenotazioni", searchBookings);

                SearchBookingCurrentDay searchBookingCurrentDay = new SearchBookingCurrentDay(tcpClient);
                tabbedPane.addTab("Prenotazioni di oggi", searchBookingCurrentDay);
            }

            // Moduli per il ruolo CLIENTE
            if (user.getRuolo() == Ruolo.CLIENTE) {
                clientBooking = new ClientBooking(user, tcpClient);
                tabbedPane.addTab("Le tue prenotazioni", clientBooking);
            }

            // Moduli per il ruolo PROIEZIONISTA
            if (user.getRuolo() == Ruolo.PROIEZIONISTA) {
                proiezionistaChangeProjection = new ProiezionistaChangeProjection(tcpClient);
                tabbedPane.addTab("Inserisci nuova proiezione", proiezionistaChangeProjection);

                SearchProjectionHistory storicoProiezioni = new SearchProjectionHistory(tcpClient);
                tabbedPane.addTab("Storico proiezioni", storicoProiezioni);
            }
        }

        tabbedPane.revalidate();
        tabbedPane.repaint();
    }

 
    /**
     * Notifica l'avvenuto login aggiornando l'utente di sessione e riconfigurando i permessi delle schede.
     *
     * @param user l'utente autenticato con successo
     */
    @Override
    public void onLoginSuccess(UserMinInfo user) {
        this.user = user;
        setPanelforUSerLogged(user);
    }

    /**
     * Notifica il fallimento della procedura di login mostrando un messaggio di errore.
     *
     * @param errorMessage il messaggio descrittivo dell'errore
     */
    @Override
    public void onLoginFailed(String errorMessage) {
        JOptionPane.showMessageDialog(this, "Login fallito: " + errorMessage, "Errore Autenticazione", JOptionPane.ERROR_MESSAGE);
    }

     /**
     * Gestisce la selezione di una proiezione dalla lista, aprendo la finestra modale
     * appropriata in base al ruolo dell'utente (Consultazione, Prenotazione Cliente o Gestione Proiezionista).
     *
     * @param projection la proiezione selezionata
     */
    @Override
    public void onSelezione(ProjectionDetailsView projection) {
        if (projection == null) {
            return;
        }

        BookingService bkgService = new BookingService(tcpClient);
        ProjectionService projectionService = new ProjectionService(tcpClient);
        Window parentWindow = SwingUtilities.getWindowAncestor(TabPanel.this);
        JDialog dialog = null;

        if (user == null) {
            // Utente NON loggato (ospite)
            dialog = new DettaglioProiezioneDialog(parentWindow, projection);
        } else {
            // Utente autenticato
            switch (user.getRuolo()) {
                case CLIENTE:
                    dialog = new DettaglioProiezioneClienteDialog(
                            parentWindow,
                            projection,
                            (Integer seats) -> {
                                StoreBookingResponse res = bkgService.insertBooking(user.getId(), projection.getId(), seats);
                                if (res != null && res.isSuccess()) {
                                    JOptionPane.showMessageDialog(parentWindow,
                                            "Prenotazione avvenuta con successo.\nCodice prenotazione: " + res.getId(),
                                            "Operazione Riuscita",
                                            JOptionPane.INFORMATION_MESSAGE);
                                    if (this.clientBooking != null) {
                                        this.clientBooking.visualizzaBooking();
                                    }
                                    if (this.searchProjection != null) {
                                        this.searchProjection.eseguiRicerca();
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(parentWindow,
                                            "Posti non disponibili per questa prenotazione.",
                                            "Prenotazione non valida",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                    );
                    break;

                case PROIEZIONISTA:
                    dialog = new DettaglioProiezioneProiezionistaDialog(
                            parentWindow,
                            projection,
                            // Callback Modifica Proiezione
                            (ProjectionDetails projModificata) -> {
                                try {
                                    projectionService.updateProjection(
                                            projModificata.getId(),
                                            projModificata.getIdFilm(),
                                            projModificata.getDataOraProiezione(),
                                            projModificata.getCosto()
                                    );

                                    JOptionPane.showMessageDialog(this, "Proiezione modificata con successo!", "Esito Modifica", JOptionPane.INFORMATION_MESSAGE);
                                    if (this.searchProjection != null) {
                                        this.searchProjection.eseguiRicerca();
                                    }
                                    return true;

                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore Modifica", JOptionPane.ERROR_MESSAGE);
                                    return false;
                                }
                            },
                            // Callback Cancellazione Proiezione
                            (ProjectionDetails projCancellata) -> {
                                try {
                                    DeleteProjectionResponse res = projectionService.deleteProjection(projCancellata.getId());

                                    if (res != null && res.isSuccess()) {
                                        JOptionPane.showMessageDialog(this, "Proiezione annullata con successo!", "Esito Annullamento", JOptionPane.INFORMATION_MESSAGE);
                                        if (this.searchProjection != null) {
                                            this.searchProjection.eseguiRicerca();
                                        }
                                        return true;
                                    } else {
                                        JOptionPane.showMessageDialog(this, "Impossibile annullare la proiezione. Verificare che non ci siano prenotazioni collegate.", "Errore Annullamento", JOptionPane.ERROR_MESSAGE);
                                        return false;
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

    /**
     * Notifica l'avviso di de-selezione o errore di selezione.
     *
     * @param errorMessage il messaggio descrittivo
     */
    @Override
    public void offSelezione(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Avviso", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Aggiorna lo stato del pannello a seguito dell'autenticazione dell'utente.
     *
     * @param user le informazioni minime dell'utente autenticato
     */
    public void setPanelforUSerLogged(UserMinInfo user) {
        this.user = user;
        aggiornaTabPerRuolo();
    }

    /**
     * Alias normalizzato per l'aggiornamento dello stato utente autenticato.
     *
     * @param user le informazioni minime dell'utente autenticato
     */
    public void setPanelForUserLogged(UserMinInfo user) {
        setPanelforUSerLogged(user);
    }

    /**
     * Reimposta il pannello per la consultazione anonima (utente non autenticato).
     */
    public void setPanelforUserUnlogged() {
        this.user = null;
        aggiornaTabPerRuolo();
    }

    /**
     * Alias normalizzato per la de-autenticazione dell'utente nel pannello.
     */
    public void setPanelForUserUnlogged() {
        setPanelforUserUnlogged();
    }
}