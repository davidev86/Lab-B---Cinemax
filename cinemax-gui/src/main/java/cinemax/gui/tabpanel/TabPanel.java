package cinemax.gui.tabpanel;



import cinemax.application.services.BookingService;
import cinemax.application.services.TcpClient;
import cinemax.contracts.dto.BookingDetails;
import cinemax.contracts.dto.ProjectionDetails;
import cinemax.contracts.dto.UserMinInfo;
import cinemax.gui.callback.LoginCallBack;
import cinemax.gui.callback.SelezioneBookingCallBack;
import cinemax.gui.callback.SelezioneFilmCallBack;
import cinemax.gui.callback.SelezioneProjectionCallBack;
import cinemax.gui.dialog.DettaglioProiezioneClienteDialog;
import cinemax.gui.dialog.DettaglioProiezioneDialog;
import cinemax.gui.dialog.DettaglioProiezioneProiezionistaDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import static javax.imageio.ImageIO.read;



public class TabPanel extends JPanel implements SelezioneProjectionCallBack, LoginCallBack, SelezioneBookingCallBack{

	JTabbedPane tabbedPane;
    JPanel SearchProjectionTab;
    JPanel SearchBookingTab;
    JPanel ClientBookingTab;
    JPanel tab4;
    JPanel tab5;
    JButton ricerca;
    TcpClient tcpClient;
    UserMinInfo user;

    public TabPanel(TcpClient tcpClient) {
    		this.tcpClient = tcpClient;	
    }


    public JTabbedPane build() {

//         Creazione di un JTabbedPane
        tabbedPane = new JTabbedPane(JTabbedPane.RIGHT); // Schede a destra (verticali)

        // Creazione di pannelli da aggiungere come contenuto per le schede
        SearchProjectionTab = new JPanel();
        SearchBookingTab = new JPanel();
        ClientBookingTab = new JPanel();
        tab4 = new JPanel();
        tab5 = new JPanel();
     
        //creazione contenuti SearchProjectionTab
        SearchProjectionTab ricercaProiezioni = new SearchProjectionTab(this, tcpClient);
        SearchProjectionTab.add(ricercaProiezioni);
             
//       creazione contenuti SearchBookingTab
        SearchBookingTab ricercaPrenotazioni = new SearchBookingTab(this, tcpClient);
        SearchBookingTab.add(ricercaPrenotazioni);
//*
//        creazione contenuti ClientBookingTab
        ClientBookingTab ClientBooking = new ClientBookingTab(this, tcpClient);
        ClientBookingTab.add(ClientBooking);

/*


//      creazione tab4

        tab4.setLayout(new BoxLayout(tab4,
                BoxLayout.Y_AXIS));
        tab4.setMaximumSize(new Dimension(800, 400));

        //header
        JPanel valutazioni  = new JPanel();

            valutazioni.setLayout(new BoxLayout(valutazioni, BoxLayout.X_AXIS));
            valutazioni.setPreferredSize(new Dimension(800,25));

                JLabel selezionaLibreria = new JLabel("Seleziona libreria:");
                JLabel selezionaLibro = new JLabel("Seleziona libro:");

            valutazioni.add(Box.createRigidArea(new Dimension(30,10)));
            valutazioni.add(selezionaLibreria);
            valutazioni.add(Box.createHorizontalGlue());
            valutazioni.add(selezionaLibro);
            valutazioni.add(Box.createRigidArea(new Dimension(300,10)));


//        panel libreria + libro
        JPanel librerie= new JPanel();
        librerie.setLayout(new BoxLayout(librerie, BoxLayout.X_AXIS));

//      In questo Panel vanno inserite le librerie

        JPanel scegliLibreria = new JPanel();

        scegliLibreria.setMaximumSize(new Dimension(250, 200));
        scegliLibreria.setPreferredSize(new Dimension(250, 200));
        scegliLibreria.setBackground(Color.WHITE);




        JScrollPane scegliLibreriaElenco = new JScrollPane(scegliLibreria);







//      In questo Panel vanno visualizzati i libri della libreria scelta
        JPanel elencoLibriLibreria = new JPanel();
            elencoLibriLibreria.setMaximumSize(new Dimension(550, 200));
            elencoLibriLibreria.setPreferredSize(new Dimension(550, 200));
            elencoLibriLibreria.setBackground(Color.WHITE);

            librerie.add(scegliLibreria);
            librerie.add(Box.createRigidArea(new Dimension(10, 10)));
            librerie.add(elencoLibriLibreria);

        JPanel campivalutazioni = new JPanel();
        campivalutazioni.setLayout(new BoxLayout(campivalutazioni, BoxLayout.X_AXIS));
        campivalutazioni.setPreferredSize(new Dimension(800,25));


        JPanel inserisciValutazioni = new JPanel();
        inserisciValutazioni.setLayout(new GridLayout(6, 2, 10, 10));

        inserisciValutazioni.setMaximumSize(new Dimension(250, 200));
        inserisciValutazioni.setPreferredSize(new Dimension(250,200));

        JButton aggiungiValutazioni = new JButton("Ok");


        // Etichette e campi di testo
        JLabel stile = new JLabel("Stile:");
        JTextField stileField = new JTextField();

        JLabel contenuto = new JLabel("Contenuto:");
        JTextField contenutoField = new JTextField();
        JLabel gradevolezza = new JLabel("Gradevolezza:");
        JTextField gradevolezzaField = new JTextField();
        JLabel originalità = new JLabel("Originalità:");
        JTextField originalitàField = new JTextField();
        JLabel edizione = new JLabel("Edizione:");
        JTextField edizioneField = new JTextField();
        JLabel totale = new JLabel("");
        JTextField totaleField = new JPasswordField();


        inserisciValutazioni.add(stile);
        inserisciValutazioni.add(stileField);
        inserisciValutazioni.add(contenuto);
        inserisciValutazioni.add(contenutoField);
        inserisciValutazioni.add(gradevolezza);
        inserisciValutazioni.add(gradevolezzaField);
        inserisciValutazioni.add(originalità);
        inserisciValutazioni.add(originalitàField);
        inserisciValutazioni.add(edizione);
        inserisciValutazioni.add(edizioneField);
        inserisciValutazioni.add(totale);
//        inserisciValutazioni.add(totaleField);
        inserisciValutazioni.add(aggiungiValutazioni);



        JPanel descrizioniValutazioni = new JPanel();
        descrizioniValutazioni.setPreferredSize(new Dimension(550,200));
        descrizioniValutazioni.setMaximumSize(new Dimension(550,200));
        descrizioniValutazioni.setBackground(Color.WHITE);


        campivalutazioni.add(Box.createRigidArea(new Dimension(5, 20)));
        campivalutazioni.add(inserisciValutazioni);
        campivalutazioni.add(Box.createRigidArea(new Dimension(5, 20)));
        campivalutazioni.add(descrizioniValutazioni);
        campivalutazioni.add(Box.createRigidArea(new Dimension(5, 20)));


        tab4.add(Box.createRigidArea(new Dimension(5, 20)));
        tab4.add(valutazioni);
        tab4.add(Box.createRigidArea(new Dimension(5, 5)));
        tab4.add(librerie);
        tab4.add(Box.createRigidArea(new Dimension(5, 10)));
//        tab4.add(inserisciValutazioni);
//        tab4.add(Box.createRigidArea(new Dimension(5, 10)));
        tab4.add(campivalutazioni);
        tab4.add(Box.createRigidArea(new Dimension(5, 10)));


//        Creazione tab5


        tab5.setLayout(new BoxLayout(tab5,
                BoxLayout.Y_AXIS));
        tab5.setMaximumSize(new Dimension(800, 400));

        JPanel suggerimenti  = new JPanel();

        suggerimenti.setLayout(new BoxLayout(suggerimenti, BoxLayout.X_AXIS));
        suggerimenti.setPreferredSize(new Dimension(800,25));

        JLabel seleziona_Libreria = new JLabel("Seleziona libreria:");
        JLabel seleziona_Libro = new JLabel("Seleziona libro:");

        suggerimenti.add(Box.createRigidArea(new Dimension(30,10)));
        suggerimenti.add(seleziona_Libreria);
        suggerimenti.add(Box.createHorizontalGlue());
        suggerimenti.add(seleziona_Libro);
        suggerimenti.add(Box.createRigidArea(new Dimension(300,10)));

        JPanel librerieVal= new JPanel();
        librerieVal.setLayout(new BoxLayout(librerieVal, BoxLayout.X_AXIS));

//      In questo Panel vanno inserite le librerie
        JPanel scegliLibreriaVal = new JPanel();
        scegliLibreriaVal.setMaximumSize(new Dimension(400, 150));
        scegliLibreriaVal.setBackground(Color.WHITE);

//      In questo Panel vanno visualizzati i libri della libreria scelta
        JPanel elencoLibriLibreriaVal= new JPanel();
        elencoLibriLibreriaVal.setMaximumSize(new Dimension(400, 150));
        elencoLibriLibreriaVal.setBackground(Color.WHITE);

        librerieVal.add(scegliLibreriaVal);
        librerieVal.add(Box.createRigidArea(new Dimension(10, 10)));
        librerieVal.add(elencoLibriLibreriaVal);

        JPanel AggiungiSuggerimenti = new JPanel();
        AggiungiSuggerimenti.setLayout(new BoxLayout(AggiungiSuggerimenti, BoxLayout.X_AXIS));
        AggiungiSuggerimenti.setPreferredSize(new Dimension(800,25));


        JPanel inserisciSuggerimenti = new JPanel();
        inserisciSuggerimenti.setLayout(new GridLayout(6, 2, 10, 10));
        inserisciSuggerimenti.setBorder(new EmptyBorder(10, 30, 10, 30));
        inserisciSuggerimenti.setMaximumSize(new Dimension(300, 200));
        inserisciSuggerimenti.setPreferredSize(new Dimension(300, 200));
        inserisciSuggerimenti.setBackground(Color.WHITE);

        JButton aggiungiSuggerimentiB = new JButton("Aggiungi suggerimenti");



        AggiungiSuggerimenti.add(Box.createRigidArea(new Dimension(30,10)));
        AggiungiSuggerimenti.add(inserisciSuggerimenti);
        AggiungiSuggerimenti.add(Box.createHorizontalGlue());
        AggiungiSuggerimenti.add(aggiungiSuggerimentiB);
        AggiungiSuggerimenti.add(Box.createRigidArea(new Dimension(300,10)));


        tab5.add(Box.createRigidArea(new Dimension(5, 20)));
        tab5.add(suggerimenti);
        tab5.add(Box.createRigidArea(new Dimension(5, 5)));
        tab5.add(librerieVal);
        tab5.add(Box.createRigidArea(new Dimension(5, 10)));
        tab5.add(AggiungiSuggerimenti);
        tab5.add(Box.createRigidArea(new Dimension(5, 10)));







        // Aggiunta delle schede al JTabbedPane
        tabbedPane.addTab("Ricerca proiezioni", SearchProjectionTab);
        tabbedPane.addTab("Ricerca per autore", tab2);
        tabbedPane.addTab("Crea Nuova Libreria", tab3);
        tabbedPane.addTab("Inserisci Valutazioni", tab4);
        tabbedPane.addTab("Inserisci Suggerimenti", tab5);

        setPaneloff();

*/
        	
        tabbedPane.addTab("Ricerca proiezioni", SearchProjectionTab);
        tabbedPane.addTab("Ricerca prenotazioni", SearchBookingTab);
        tabbedPane.addTab("Le tue prenotazioni", ClientBookingTab);
    
        
        return tabbedPane;

    }


    public void setPanelon(){

        tabbedPane.setEnabledAt(0, true);
        tabbedPane.setEnabledAt(1, true);
        tabbedPane.setEnabledAt(2, true);
        tabbedPane.setEnabledAt(3, true);


    }


    public void setPaneloff(){

  //      tabbedPane.setEnabledAt(0, false);
        tabbedPane.setEnabledAt(1, false);
        tabbedPane.setEnabledAt(2, false);
        tabbedPane.setEnabledAt(3, false);
        tabbedPane.setEnabledAt(4, false);


    }


    public void setPanelforUSerLogged(UserMinInfo user) {

    		this.user = user;
    	
        tabbedPane.setEnabledAt(1, true);
        tabbedPane.setEnabledAt(2, true);
        tabbedPane.setEnabledAt(3, true);

    }



    public void onSelezione(ProjectionDetails projection) {
        BookingService bkgService = new BookingService(tcpClient);
        Window parentWindow = SwingUtilities.getWindowAncestor(TabPanel.this);
        JDialog dialog = null;

        if (user == null) {
            // Utente NON loggato (ospite)
            dialog = new DettaglioProiezioneDialog(
                parentWindow,
                projection
            );
        } else {
            // Utente LOGGATO
            switch (user.getRuolo()) {
                case CLIENTE:
                    dialog = new DettaglioProiezioneClienteDialog(
                        parentWindow,
                        projection,
                        (Integer seats) -> {
                            bkgService.insertBooking(user.getId(), projection.getId(), seats);
                        }
                    );
                    break;

                case PROIEZIONISTA:
                    dialog = new DettaglioProiezioneProiezionistaDialog(
                        parentWindow,
                        projection,
                       (ProjectionDetails projModificata) -> {
                            // Callback modifica: adatta con il metodo del tuo ProjectionService
                            // projectionService.updateProjection(projModificata);
                        },
                        (ProjectionDetails projCancellata) -> {
                            // Callback cancellazione: adatta con il metodo del tuo ProjectionService
                            // projectionService.deleteProjection(projCancellata.getId());
                        }
                    );
                    break;

                default:
                    throw new IllegalArgumentException("Ruolo non gestito: " + user.getRuolo());
            }
        }

        if (dialog != null) {
            dialog.setVisible(true);
        }
    }

    
    public void offSelezione(String errorMessage) {

    }


	@Override
	public void onLoginSuccess(UserMinInfo user) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onLoginFailed(String errorMessage) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSelezione(BookingDetails bookingDetails) {
		// TODO Auto-generated method stub
		
	}
}