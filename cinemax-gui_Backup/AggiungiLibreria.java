//import BookRecommender.Application.DTO.BookDTO;
//import BookRecommender.Application.Services.BookService;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.ItemEvent;
//import java.util.List;
//import java.util.function.Consumer;
//
//AggiungiLibreria extends JDialog {
//    RicercaAutorePanel ricercaAutore;
//    RicercaTitoloPanel ricercaTitolo;
//    JButton scegliLibri;
//
//    BookService bookService;
//    DefaultListModel<String> resultList;
//    List<BookDTO> list;
//    JList<String> listaRisultati;
//    BookDTO book;
//
//    Consumer<BookDTO> _onSelezione;
//    Consumer<BookDTO> _offSelezione;
//
//  //  SelezioneLibroCallBack onSelezione;
//
//    public AggiungiLibreria(Consumer<BookDTO> onSelezione, Consumer<BookDTO> offSelezione) {
//
//
//        this._onSelezione = onSelezione;
//        this._offSelezione = offSelezione;
//
//
//    }
//
//
//    public JPanel build () {
//        // Configurazione della finestra di dialogo
//
//        setResizable(false);
//        setMinimumSize(new Dimension(800, 600));
//        setLocationRelativeTo(null);
//
//
//        JPanel popup = new JPanel();
//
//        popup.setLayout(new BorderLayout(10, 10));
//        popup.setMinimumSize(new Dimension(800, 600));
//
//
//        JPanel ricerca = new JPanel();
//
//        ricerca.setMinimumSize(new Dimension(800, 50));
//        ricerca.setMaximumSize(new Dimension(800, 50));
//        ricerca.setLayout(new BoxLayout(ricerca, BoxLayout.Y_AXIS));
//
//
//        JLabel label = new JLabel("Seleziona uno o più libri che vuoi aggiungere alla tua libreria:");
//        label.setFont(new Font("Tahoma", Font.BOLD, 14));
//
//
////          creazione radioButton
//        JRadioButton scegliricercaT = new JRadioButton("Ricerca per titolo", true);
//        JRadioButton scegliricercaA = new JRadioButton("Ricerca per autore", true);
//
//        scegliricercaT.setEnabled(true);
//        scegliricercaA.setEnabled(true);
//
//
//        ButtonGroup buttonGroup = new ButtonGroup();
//        buttonGroup.add(scegliricercaT);
//        buttonGroup.add(scegliricercaA);
//
//
//        ricerca.add(Box.createRigidArea(new Dimension(30, 30)));
//        ricerca.add(label);
//        ricerca.add(Box.createRigidArea(new Dimension(30, 30)));
//
//
//        JPanel radioButtons = new JPanel();
//        radioButtons.setLayout(new BoxLayout(radioButtons, BoxLayout.X_AXIS));
//        radioButtons.add(Box.createHorizontalGlue());
//        radioButtons.add(scegliricercaT);
//        radioButtons.add(Box.createRigidArea(new Dimension(10, 10)));
//        radioButtons.add(scegliricercaA);
//        radioButtons.add(Box.createHorizontalGlue());
//
//
//        ricerca.add(radioButtons);
//
//
//        popup.add(ricerca, BorderLayout.PAGE_START);
//
//
//        ricercaTitolo = new RicercaTitoloPanel();
//        ricercaAutore = new RicercaAutorePanel();
//
//        JPanel ricTitolo = ricercaTitolo.build(onSelezione);
//        JPanel ricAutore = ricercaAutore.build(onSelezione);
//
//
//        popup.add(ricTitolo, BorderLayout.CENTER);
//
//        scegliLibri = new JButton("Chiudi");
//        scegliLibri.setFont(new Font("Tahoma", Font.PLAIN, 14));
//        scegliLibri.setMinimumSize(new Dimension(800, 600));
//
//
//        popup.add(scegliLibri, BorderLayout.PAGE_END);
//
//        scegliLibri.addActionListener(new ActionListener() {
//            
//            public void actionPerformed(ActionEvent e) {
//
//
//            }
//
//        });
//
//
//        popup.setVisible(true);
//
//        add(popup);
//
//
//        scegliricercaA.addActionListener(new ActionListener() {
//
//            
//            public void actionPerformed(ActionEvent e) {
//                if (scegliricercaA.isSelected()) {
//
//                    popup.remove(ricTitolo);
//                    popup.add(ricAutore, BorderLayout.CENTER);
//                    popup.add(scegliLibri, BorderLayout.PAGE_END);
//                    popup.revalidate();
//                    popup.repaint();
//                }
//
//            }
//
//        });
//
//
//        scegliricercaT.addActionListener(new ActionListener() {
//            
//            public void actionPerformed(ActionEvent e) {
//                if (scegliricercaT.isSelected()) {
//
//                    popup.remove(ricAutore);
//                    popup.add(ricTitolo, BorderLayout.CENTER);
//                    popup.add(scegliLibri, BorderLayout.PAGE_END);
//                    popup.revalidate();
//                    popup.repaint();
//                }
//
//            }
//        });
//        scegliLibri.addActionListener(new ActionListener() {
//            
//            public void actionPerformed(ActionEvent e) {
//
//
//                dispose();
//
//            }
//
//        });
//
//        return popup;
//    }
//}
