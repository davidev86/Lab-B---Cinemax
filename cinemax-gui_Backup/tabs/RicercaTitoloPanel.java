

import BookRecommender.Application.DTO.BookDTO;
import BookRecommender.Application.Services.BookService;
import bookRecommender.callbacks.SelezioneLibroCallBack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


RicercaTitoloPanel extends JPanel {

    BookService bookService;
    CardLayout cardLayout;
    JPanel cardPanel;
    JScrollPane scrollPanel;
    DefaultListModel<BookDTO> resultList;
    List<BookDTO> list;
    JList<BookDTO> listaRisultati;
    BookDTO book;

    public RicercaTitoloPanel(SelezioneLibroCallBack selezioneLibroCallBack) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //creazione e composizione del pannello ricerca
        JPanel researchPanel = new JPanel();
        researchPanel.setLayout(new BoxLayout(researchPanel, BoxLayout.X_AXIS));

        JLabel label = new JLabel("Ricerca Titolo");

        JTextField textField = new JTextField();
        textField.setEditable(true);
        textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
        textField.setPreferredSize(new Dimension(250, 25));
        textField.setMaximumSize(new Dimension(250, 25));

        JButton button = new JButton("Ricerca");
        String filePath = "C:/Temp/Output";

        researchPanel.add(Box.createRigidArea(new Dimension(10, 80)));
        researchPanel.add(label);
        researchPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        researchPanel.add(textField);
        researchPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        researchPanel.add(button);

        //composizione pannello dei risultati
        Panel imagePanel = new Panel();
        //      ImagePanel imagePanel = new ImagePanel("/images/libri.jpg");
        imagePanel.setPreferredSize(new Dimension(800, 400));
        imagePanel.setBackground(Color.WHITE);

        scrollPanel = new JScrollPane(listaRisultati);
        scrollPanel.setSize(new Dimension(800, 400));
        scrollPanel.setFont(new Font("Tahoma", Font.PLAIN, 12));


        cardPanel = new JPanel(cardLayout = new CardLayout());


        cardPanel.add(imagePanel,
                "imagePanel");
        cardPanel.add(scrollPanel,
                "scrollPanel");


        //composizione pannello principale
        add(researchPanel);
        add(Box.createRigidArea(new Dimension(10, 10)));
        add(cardPanel);
        setVisible(true);

        button.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {

                bookService = new BookService(filePath);
                resultList = new DefaultListModel<BookDTO>();

                String titolo = new String(textField.getText());

                list = bookService.listByTitle(titolo);

                resultList.addAll(list);
                listaRisultati = new JList<>(resultList);
                listaRisultati.setPreferredSize(new Dimension(800, 400));


//                listaRisultati.addMouseListener(new MouseAdapter() {
//                    
//                    public void mouseClicked(MouseEvent e) {
//
//
//                        if (e.getClickCount() == 1) { // Clic singolo
//                            //  JOptionPane.showMessageDialog(RicercaTitoloPanel.this, "Scheda Libro!", "Successo", JOptionPane.INFORMATION_MESSAGE);
//                            int index = listaRisultati.locationToIndex(e.getPoint());
//                            BookDTO selectedItem = listaRisultati.getModel().getElementAt(index);
//                            selezioneLibroCallBack.onSelezione(selectedItem);
//
//                        }
//                            if (index != -1) { // Controllo per evitare clic fuori lista
//                                String selectedItem = list.getModel().getElementAt(index);
//                                externalClass.addItem(selectedItem); // Aggiungi alla lista esterna
//                        }
//                    }
//
//                });


                scrollPanel.setViewportView(listaRisultati);
                cardLayout.show(cardPanel, "scrollPanel");

            }
        });

    }
}
