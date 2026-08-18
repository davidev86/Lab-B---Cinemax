

import bookRecommender.callbacks.SelezioneLibroCallBack;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

Tab5 extends JPanel{


    public Tab5(SelezioneLibroCallBack selezioneLibroCallBack) {

        setLayout(new BoxLayout(this,
                BoxLayout.Y_AXIS));
        setMaximumSize(new Dimension(800, 400));


        JPanel suggerimenti = new JPanel();

        suggerimenti.setLayout(new BoxLayout(suggerimenti, BoxLayout.X_AXIS));
        suggerimenti.setPreferredSize(new Dimension(800, 25));

        JLabel seleziona_Libreria = new JLabel("Seleziona libreria:");
        JLabel seleziona_Libro = new JLabel("Seleziona libro:");

        suggerimenti.add(Box.createRigidArea(new Dimension(30, 10)));
        suggerimenti.add(seleziona_Libreria);
        suggerimenti.add(Box.createHorizontalGlue());
        suggerimenti.add(seleziona_Libro);
        suggerimenti.add(Box.createRigidArea(new Dimension(300, 10)));

        JPanel librerieVal = new JPanel();
        librerieVal.setLayout(new BoxLayout(librerieVal, BoxLayout.X_AXIS));

        //      In questo Panel vanno inserite le librerie
        JPanel scegliLibreriaVal = new JPanel();
        scegliLibreriaVal.setMaximumSize(new Dimension(400, 150));
        scegliLibreriaVal.setBackground(Color.WHITE);

        //      In questo Panel vanno visualizzati i libri della libreria scelta
        JPanel elencoLibriLibreriaVal = new JPanel();
        elencoLibriLibreriaVal.setMaximumSize(new Dimension(400, 150));
        elencoLibriLibreriaVal.setBackground(Color.WHITE);

        librerieVal.add(scegliLibreriaVal);
        librerieVal.add(Box.createRigidArea(new Dimension(10, 10)));
        librerieVal.add(elencoLibriLibreriaVal);

        JPanel AggiungiSuggerimenti = new JPanel();
        AggiungiSuggerimenti.setLayout(new BoxLayout(AggiungiSuggerimenti, BoxLayout.X_AXIS));
        AggiungiSuggerimenti.setPreferredSize(new Dimension(800, 25));


        JPanel inserisciSuggerimenti = new JPanel();
        inserisciSuggerimenti.setLayout(new GridLayout(6, 2, 10, 10));
        inserisciSuggerimenti.setBorder(new EmptyBorder(10, 30, 10, 30));
        inserisciSuggerimenti.setMaximumSize(new Dimension(300, 200));
        inserisciSuggerimenti.setPreferredSize(new Dimension(300, 200));
        inserisciSuggerimenti.setBackground(Color.WHITE);

        JButton aggiungiSuggerimentiB = new JButton("Aggiungi suggerimenti");


        AggiungiSuggerimenti.add(Box.createRigidArea(new Dimension(30, 10)));
        AggiungiSuggerimenti.add(inserisciSuggerimenti);
        AggiungiSuggerimenti.add(Box.createHorizontalGlue());
        AggiungiSuggerimenti.add(aggiungiSuggerimentiB);
        AggiungiSuggerimenti.add(Box.createRigidArea(new Dimension(300, 10)));


        add(Box.createRigidArea(new Dimension(5, 20)));
        add(suggerimenti);
        add(Box.createRigidArea(new Dimension(5, 5)));
        add(librerieVal);
        add(Box.createRigidArea(new Dimension(5, 10)));
        add(AggiungiSuggerimenti);
        add(Box.createRigidArea(new Dimension(5, 10)));


    }

}
