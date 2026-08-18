

import BookRecommender.Application.DTO.BookDTO;
import BookRecommender.Application.Services.BookService;
import bookRecommender.callbacks.SelezioneLibroCallBack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

AggiungiLibreria extends JDialog {
    RicercaAutorePanel ricercaAutore;
    RicercaTitoloPanel ricercaTitolo;
    JButton scegliLibri;

    BookService bookService;
    DefaultListModel<String> resultList;
    List<BookDTO> list;
    JList<String> listaRisultati;
    BookDTO book;

    Consumer<BookDTO> _onSelezione;
    Consumer<BookDTO> _offSelezione;

    SelezioneLibroCallBack onSelezione;

    public AggiungiLibreria(Consumer<BookDTO> onSelezione, Consumer<BookDTO> offSelezione) {

        this._onSelezione = onSelezione;
        this._offSelezione = offSelezione;

    }


    public AggiungiLibreria (SelezioneLibroCallBack onSelezione) {
        // Configurazione della finestra di dialogo

        setResizable(false);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);


        JPanel popup = new JPanel();

        popup.setLayout(new BorderLayout(10, 10));
        popup.setMinimumSize(new Dimension(800, 600));


        JPanel ricerca = new JPanel();

        ricerca.setMinimumSize(new Dimension(800, 50));
        ricerca.setMaximumSize(new Dimension(800, 50));
        ricerca.setLayout(new BoxLayout(ricerca, BoxLayout.Y_AXIS));


        JLabel label = new JLabel("Seleziona uno o più libri che vuoi aggiungere alla tua libreria:");
        label.setFont(new Font("Tahoma", Font.BOLD, 14));


//          creazione radioButton
        JRadioButton scegliricercaT = new JRadioButton("Ricerca per titolo", true);
        JRadioButton scegliricercaA = new JRadioButton("Ricerca per autore", true);

        scegliricercaT.setEnabled(true);
        scegliricercaA.setEnabled(true);


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


        popup.add(ricerca, BorderLayout.PAGE_START);


        ricercaTitolo = new RicercaTitoloPanel(onSelezione);
        ricercaAutore = new RicercaAutorePanel(onSelezione);

        popup.add(ricercaTitolo, BorderLayout.CENTER);

        scegliLibri = new JButton("Chiudi");
        scegliLibri.setFont(new Font("Tahoma", Font.PLAIN, 14));
        scegliLibri.setMinimumSize(new Dimension(800, 600));


        popup.add(scegliLibri, BorderLayout.PAGE_END);

        scegliLibri.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {


            }

        });


        popup.setVisible(true);

        add(popup);


        scegliricercaA.addActionListener(new ActionListener() {

            
            public void actionPerformed(ActionEvent e) {
                if (scegliricercaA.isSelected()) {

                    popup.remove(ricercaTitolo);
                    popup.add(ricercaAutore, BorderLayout.CENTER);
                    popup.add(scegliLibri, BorderLayout.PAGE_END);
                    popup.revalidate();
                    popup.repaint();
                }

            }

        });


        scegliricercaT.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                if (scegliricercaT.isSelected()) {

                    popup.remove(ricercaAutore);
                    popup.add(ricercaTitolo, BorderLayout.CENTER);
                    popup.add(scegliLibri, BorderLayout.PAGE_END);
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
