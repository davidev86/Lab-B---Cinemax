import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

CercaLibreria extends JDialog {
    RicercaAutorePanel ricercaAutore;
    RicercaTitoloPanel ricercaTitolo;
    JButton scegliLibri;

    public CercaLibreria (JPanel parent) {
        // Configurazione della finestra di dialogo
        super();
        setResizable(false);

        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        JPanel popup = new JPanel();

        popup.setLayout(new BorderLayout(10,10));

        popup.setMinimumSize(new Dimension(800, 600));

        JPanel ricerca = new JPanel();
        JLabel label = new JLabel("Scegli i libri che vuoi aggiungere alla tua libreria:");
        ricerca.setFont(new Font("Tahoma", Font.PLAIN, 12));
        ricerca.setMinimumSize(new Dimension(800, 50));
        ricerca.setMaximumSize(new Dimension(800, 50));
        ricerca.setLayout(new BoxLayout(ricerca, BoxLayout.Y_AXIS));

//      creazione radioButton
        JRadioButton scegliricercaT = new JRadioButton("Ricerca per titolo", true);
        JRadioButton scegliricercaA = new JRadioButton("Ricerca per autore", true);

        scegliricercaT.setEnabled(true);
        scegliricercaA.setEnabled(true);

        scegliricercaA.setVisible(true);
        scegliricercaT.setVisible(true);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(scegliricercaT);
        buttonGroup.add(scegliricercaA);

        ricerca.add(Box.createRigidArea(new Dimension(30, 30)));
        ricerca.add(label);
        ricerca.add(Box.createRigidArea(new Dimension(30, 30)));

        JPanel radioButtons = new JPanel();
        radioButtons.setLayout(new BoxLayout(radioButtons, BoxLayout.X_AXIS));

        radioButtons.add(Box.createHorizontalGlue());
        radioButtons.add(scegliricercaT);
        radioButtons.add(Box.createRigidArea(new Dimension(10, 10)));
        radioButtons.add(scegliricercaA);
        radioButtons.add(Box.createHorizontalGlue());

        ricerca.add(radioButtons);




        popup.add(ricerca,BorderLayout.PAGE_START);



        ricercaTitolo = new RicercaTitoloPanel();
        ricercaAutore = new RicercaAutorePanel();


//        JPanel ricAutore = ricercaAutore.build();


        popup.add(ricercaTitolo, BorderLayout.CENTER);

        scegliLibri = new JButton("Aggiungi alla libreria");
        scegliLibri.setFont(new Font("Tahoma", Font.PLAIN, 14));
        scegliLibri.setMinimumSize(new Dimension(800, 600));

        popup.add(scegliLibri, BorderLayout.PAGE_END);

        popup.setVisible(true);
        add(popup);




        scegliricercaA.addActionListener(new ActionListener() {

            
            public void actionPerformed(ActionEvent e) {
                if (scegliricercaA.isSelected()) {

                    popup.remove(ricercaTitolo);

//                    popup.add(ricAutore,BorderLayout.CENTER);

                    popup.revalidate();
                    popup.repaint();
                }

                else {

//                    popup.remove(ricAutore);
                    popup.revalidate();
                    popup.repaint();
                }
            }

        });


        scegliricercaT.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                if (scegliricercaT.isSelected()) {

//                    popup.remove(ricAutore);

                    popup.add(ricercaTitolo, BorderLayout.CENTER);
                    popup.add(scegliLibri);
                    popup.revalidate();
                    popup.repaint();

                }

                else {

                    popup.remove(ricercaTitolo);
                    popup.revalidate();
                    popup.repaint();

                }

            }
        });
        scegliLibri.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                dispose();
            }

        });







    }



}

