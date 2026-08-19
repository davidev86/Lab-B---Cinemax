


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import static javax.imageio.ImageIO.read;

TabPanel extends JPanel /*implements SelezioneLibroCallBack */{

	JTabbedPane tabbedPane;
    JPanel tab1;
    JPanel tab2;
    JPanel tab3;
    JPanel tab4;
    JPanel tab5;
    JButton ricercaLibri;





    public TabPanel() {


    }


    public JTabbedPane build() {

//         Creazione di un JTabbedPane
        tabbedPane = new JTabbedPane(JTabbedPane.RIGHT); // Schede a destra (verticali)

        // Creazione di pannelli da aggiungere come contenuto per le schede
        tab1 = new JPanel();
        tab2 = new JPanel();
        tab3 = new JPanel();
        tab4 = new JPanel();
        tab5 = new JPanel();



/*
        //creazione contenuti tab1
        RicercaTitoloPanel ricercaTitolo = new RicercaTitoloPanel();
        tab1.add(ricercaTitolo);

//       creazione contenuti tab2
        RicercaAutorePanel ricercaAutore = new RicercaAutorePanel();
        tab2.add(ricercaAutore);


//        creazione contenuti tab3

        tab3.setLayout(new BoxLayout(tab3,
                BoxLayout.Y_AXIS));
        tab3.setMaximumSize(new Dimension(800, 400));

        JLabel aggiungiLibreria  = new JLabel("Crea una nuova libreria");
            aggiungiLibreria.setFont(new Font("Dialog", Font.BOLD, 20));
            aggiungiLibreria.setMinimumSize(new Dimension(700, 40));
            aggiungiLibreria.setHorizontalAlignment(SwingConstants.LEFT);
            aggiungiLibreria.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel CercaLibriPanel = new JPanel();
                CercaLibriPanel.setLayout(new BoxLayout(CercaLibriPanel, BoxLayout.X_AXIS));
                CercaLibriPanel.setMinimumSize(new Dimension(800, 40));
                JLabel librerieLabel = new JLabel("Inserisci nome libreria: ");
                JTextField libreriaText = new JTextField(10);
                libreriaText.setMinimumSize(new Dimension(100, 20));
                libreriaText.setMaximumSize(new Dimension(100, 20));
                libreriaText.setEditable(true);


                ricercaLibri = new JButton("Ricerca Libri");


        CercaLibriPanel.add(Box.createHorizontalGlue());
        CercaLibriPanel.add(librerieLabel);
        CercaLibriPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        CercaLibriPanel.add(libreriaText);
        CercaLibriPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        CercaLibriPanel.add(ricercaLibri);

        CercaLibriPanel.add(Box.createHorizontalGlue());


        JPanel libreria = new JPanel();
        libreria.setLayout(new BoxLayout(libreria, BoxLayout.X_AXIS));

        libreria.setMinimumSize(new Dimension(900, 300));
        libreria.setMaximumSize(new Dimension(900, 300));

        JPanel libreriaScroller = new JPanel();
        libreriaScroller.setMinimumSize(new Dimension(700, 300));
        JScrollPane libreriaScroll = new JScrollPane(libreriaScroller);

        JButton creaLibri = new JButton("Crea Libreria");



//        ricercaLibri.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//
////                setPaneloff();
//                AggiungiLibreria aggiungiLibreria = new AggiungiLibreria(onSelezioneLibro);
//                aggiungiLibreria.setVisible(true);
//
//
//            }
//        });




        libreria.add(Box.createRigidArea(new Dimension(50, 10)));
        libreria.add(libreriaScroll);
        libreria.add(Box.createRigidArea(new Dimension(20, 10)));
        libreria.add(creaLibri);

        tab3.add(Box.createRigidArea(new Dimension(5, 30)));
        tab3.add(aggiungiLibreria);
        tab3.add(Box.createRigidArea(new Dimension(5, 50)));
        tab3.add(CercaLibriPanel);
        tab3.add(Box.createRigidArea(new Dimension(5, 30)));
        tab3.add(libreria);







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
        tabbedPane.addTab("Ricerca per titolo", tab1);
        tabbedPane.addTab("Ricerca per autore", tab2);
        tabbedPane.addTab("Crea Nuova Libreria", tab3);
        tabbedPane.addTab("Inserisci Valutazioni", tab4);
        tabbedPane.addTab("Inserisci Suggerimenti", tab5);

        setPaneloff();


*/

        return tabbedPane;

    }

/*
    public void setPanelon(){

        tabbedPane.setEnabledAt(0, true);
        tabbedPane.setEnabledAt(1, true);
        tabbedPane.setEnabledAt(2, true);
        tabbedPane.setEnabledAt(3, true);


    }


    public void setPaneloff(){

//        tabbedPane.setEnabledAt(0, false);
//        tabbedPane.setEnabledAt(1, false);
        tabbedPane.setEnabledAt(2, false);
        tabbedPane.setEnabledAt(3, false);
        tabbedPane.setEnabledAt(4, false);


    }


    public void setPanelforUSerLogged(UserDTO user) {

        tabbedPane.setEnabledAt(2, true);
        tabbedPane.setEnabledAt(3, true);
        tabbedPane.setEnabledAt(4, true);

    }



    
    public void onSelezione(BookDTO book) {
        JOptionPane.showMessageDialog(this, book.title, "Successo", JOptionPane.INFORMATION_MESSAGE);
    }

    
    public void offSelezione(String errorMessage) {

    }



*/

}