

import bookRecommender.callbacks.SelezioneLibroCallBack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

Tab3 extends JPanel {

    public Tab3(SelezioneLibroCallBack onSelezioneLibro) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setMaximumSize(new Dimension(800, 400));

        add(Box.createRigidArea(new Dimension(5, 30)));
        add(getAggiungiLibreria());
        add(Box.createRigidArea(new Dimension(5, 50)));
        add(getCercaLibriPanel(onSelezioneLibro));
        add(Box.createRigidArea(new Dimension(5, 30)));
        add(getLibreria());

        setVisible(true);
    }

    private JLabel getAggiungiLibreria(){
        JLabel aggiungiLibreria  = new JLabel("Crea una nuova libreria");
        aggiungiLibreria.setFont(new Font("Dialog", Font.BOLD, 20));
        aggiungiLibreria.setMinimumSize(new Dimension(700, 40));
        aggiungiLibreria.setHorizontalAlignment(SwingConstants.LEFT);
        aggiungiLibreria.setAlignmentX(Component.LEFT_ALIGNMENT);


        return  aggiungiLibreria;

    }

    private JPanel getCercaLibriPanel(SelezioneLibroCallBack onSelezioneLibro){

        JPanel CercaLibriPanel = new JPanel();
        CercaLibriPanel.setLayout(new BoxLayout(CercaLibriPanel, BoxLayout.X_AXIS));
        CercaLibriPanel.setMinimumSize(new Dimension(800, 40));
        JLabel librerieLabel = new JLabel("Inserisci nome libreria: ");
        JTextField libreriaText = new JTextField(10);
        libreriaText.setMinimumSize(new Dimension(100, 20));
        libreriaText.setMaximumSize(new Dimension(100, 20));
        libreriaText.setEditable(true);

        JButton  ricercaLibri = new JButton("Ricerca Libri");

        ricercaLibri.addActionListener(new ActionListener() {
                                           public void actionPerformed(ActionEvent e) {

//                setPaneloff();
                                               AggiungiLibreria aggiungiLibreria = new AggiungiLibreria(onSelezioneLibro);
                                               aggiungiLibreria.setVisible(true);

                                           }
                                       });


                CercaLibriPanel.add(Box.createHorizontalGlue());
        CercaLibriPanel.add(librerieLabel);
        CercaLibriPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        CercaLibriPanel.add(libreriaText);
        CercaLibriPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        CercaLibriPanel.add(ricercaLibri);

        CercaLibriPanel.add(Box.createHorizontalGlue());

        return  CercaLibriPanel;

    }

    private JPanel getLibreria(){
        JPanel libreria = new JPanel();
        libreria.setLayout(new BoxLayout(libreria, BoxLayout.X_AXIS));

        libreria.setMinimumSize(new Dimension(900, 300));
        libreria.setMaximumSize(new Dimension(900, 300));

        JPanel libreriaScroller = new JPanel();
        libreriaScroller.setMinimumSize(new Dimension(700, 300));
        JScrollPane libreriaScroll = new JScrollPane(libreriaScroller);

        JButton creaLibri = new JButton("Crea Libreria");

        libreria.add(Box.createRigidArea(new Dimension(50, 10)));
        libreria.add(libreriaScroll);
        libreria.add(Box.createRigidArea(new Dimension(20, 10)));
        libreria.add(creaLibri);

        return  libreria;

    }


}
