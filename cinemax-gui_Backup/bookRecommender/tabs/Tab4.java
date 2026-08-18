

import bookRecommender.callbacks.SelezioneLibroCallBack;

import javax.swing.*;
import java.awt.*;

Tab4 extends JPanel {

    public Tab4(SelezioneLibroCallBack selezioneLibroCallBack) {


        setLayout(new BoxLayout(this,
                BoxLayout.Y_AXIS));
        setMaximumSize(new Dimension(800, 400));

        //header
        JPanel valutazioni = new JPanel();

        valutazioni.setLayout(new BoxLayout(valutazioni, BoxLayout.X_AXIS));
        valutazioni.setPreferredSize(new Dimension(800, 25));

        JLabel selezionaLibreria = new JLabel("Seleziona libreria:");
        JLabel selezionaLibro = new JLabel("Seleziona libro:");

        valutazioni.add(Box.createRigidArea(new Dimension(30, 10)));
        valutazioni.add(selezionaLibreria);
        valutazioni.add(Box.createHorizontalGlue());
        valutazioni.add(selezionaLibro);
        valutazioni.add(Box.createRigidArea(new Dimension(300, 10)));


        //        panel libreria + libro
        JPanel librerie = new JPanel();
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
        campivalutazioni.setPreferredSize(new Dimension(800, 25));


        JPanel inserisciValutazioni = new JPanel();
        inserisciValutazioni.setLayout(new GridLayout(6, 2, 10, 10));

        inserisciValutazioni.setMaximumSize(new Dimension(250, 200));
        inserisciValutazioni.setPreferredSize(new Dimension(250, 200));

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
        descrizioniValutazioni.setPreferredSize(new Dimension(550, 200));
        descrizioniValutazioni.setMaximumSize(new Dimension(550, 200));
        descrizioniValutazioni.setBackground(Color.WHITE);


        campivalutazioni.add(Box.createRigidArea(new Dimension(5, 20)));
        campivalutazioni.add(inserisciValutazioni);
        campivalutazioni.add(Box.createRigidArea(new Dimension(5, 20)));
        campivalutazioni.add(descrizioniValutazioni);
        campivalutazioni.add(Box.createRigidArea(new Dimension(5, 20)));


        add(Box.createRigidArea(new Dimension(5, 20)));
        add(valutazioni);
        add(Box.createRigidArea(new Dimension(5, 5)));
        add(librerie);
        add(Box.createRigidArea(new Dimension(5, 10)));
//        tab4.add(inserisciValutazioni);
//        tab4.add(Box.createRigidArea(new Dimension(5, 10)));
        add(campivalutazioni);
        add(Box.createRigidArea(new Dimension(5, 10)));

    }
}