import BookRecommender.Application.DTO.BookDTO;
import BookRecommender.Application.DTO.UserDTO;
import BookRecommender.Application.Services.BookService;
import BookRecommender.Application.Services.UserService;
import BookRecommender.Data.Models.Book;
import bookRecommender.callbacks.SelezioneLibroCallBack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;


RicercaAutorePanel extends JPanel {

    BookService bookService;
    CardLayout cardLayout;
    JPanel cardPanel;
    JScrollPane scrollPanel;
    DefaultListModel<String> resultListAutor;
    List<BookDTO> list;
    JList<String> listaRisultati;
    BookDTO book;

    UserService userService;
    UserDTO user;



    public JPanel build(SelezioneLibroCallBack selezioneLibroCallBack) {

        JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel researchPanel = new JPanel();
            researchPanel.setLayout(new BoxLayout(researchPanel, BoxLayout.X_AXIS));

        JLabel label = new JLabel("Ricerca Autore");

        JTextField textField = new JTextField();
            textField.setEditable(true);
            textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
            textField.setPreferredSize(new Dimension(250, 25));
            textField.setMaximumSize(new Dimension(250, 25));

        JLabel labelAnno = new JLabel("Anno");

        JTextField textFieldAnno = new JTextField();
            textFieldAnno.setEditable(true);
            textFieldAnno.setFont(new Font("Tahoma", Font.PLAIN, 12));
            textFieldAnno.setPreferredSize(new Dimension(50, 25));
            textFieldAnno.setMaximumSize(new Dimension(50, 25));

        JButton button = new JButton("Ricerca");

        researchPanel.add(Box.createRigidArea(new Dimension(10, 80)));
        researchPanel.add(label);
        researchPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        researchPanel.add(textField);
        researchPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        researchPanel.add(labelAnno);
        researchPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        researchPanel.add(textFieldAnno);
        researchPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        researchPanel.add(button);

        //creazione e composizione pannello risultati

        scrollPanel = new JScrollPane();

        JLabel scrollPane = new JLabel("Elenco libri");
            scrollPane.setSize(new Dimension(800, 300));
            scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 40));


        scrollPanel.add(scrollPane);


        Panel imagePanel = new Panel();
//        ImagePanel imagePanel = new ImagePanel("/images/libri.jpg");
        imagePanel.setPreferredSize(new Dimension(800, 400));
        imagePanel.setBackground(Color.WHITE);

        cardPanel = new JPanel(cardLayout = new CardLayout());

        cardPanel.add(imagePanel,
                "imagePanel");
        cardPanel.add(scrollPanel,
                "scrollPanel");


        panel.add(researchPanel);
        panel.add(Box.createRigidArea(new Dimension(10, 10)));
        panel.add(cardPanel);
        panel.setVisible(true);

        String filePath = "C:/sources/";

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                bookService = new BookService(filePath);
                resultListAutor = new DefaultListModel<>();

                String autore = new String(textField.getText());

                list = bookService.listByAuthor(autore);

                List<String> risultati =list.stream()
                        .map(BookDTO::toString)
                        .collect(Collectors.toList());

                resultListAutor.addAll(risultati);
                listaRisultati = new JList<>(resultListAutor);
                listaRisultati.setPreferredSize(new Dimension(800, 400));
                scrollPanel.setViewportView(listaRisultati);
                cardLayout.show(cardPanel, "scrollPanel");


                listaRisultati.addMouseListener(new MouseAdapter() {
                    
                    public void mouseClicked(MouseEvent e) {


                        if (e.getClickCount() == 1) { // Clic singolo

                            selezioneLibroCallBack.onSelezione(book);


                        }
//                            if (index != -1) { // Controllo per evitare clic fuori lista
//                                String selectedItem = list.getModel().getElementAt(index);
//                                externalClass.addItem(selectedItem); // Aggiungi alla lista esterna
//                        }
                    }

                });





            }
        });



        return panel;

    }

    public void setButtonforUSerLogged(UserDTO user) {

        setButtonforUSerLogged(user);


    }



}
