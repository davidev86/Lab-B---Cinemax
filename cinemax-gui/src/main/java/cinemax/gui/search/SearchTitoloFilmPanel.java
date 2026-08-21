package cinemax.gui.search;


import javax.swing.*;

import cinemax.application.services.TcpClient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchTitoloFilmPanel extends JPanel{


        public JPanel build(TcpClient tcpClient) {
 

            JPanel panel = new JPanel();

            JPanel header = new JPanel();
            header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
            header.add(head);

            //crea pannello ricerca
            JPanel researchPanel = new JPanel();
            researchPanel.setLayout(new BoxLayout(researchPanel, BoxLayout.X_AXIS));

            JLabel label = new JLabel("Ricerca per titolo film");
            researchPanel.add(label);

            researchPanel.add(Box.createRigidArea(new Dimension(10, 10)));

            JTextField textField = new JTextField();
            textField.setEditable(true);
            textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
           textField.setPreferredSize(new Dimension(250, 25));

            researchPanel.add(textField);

            researchPanel.add(Box.createRigidArea(new Dimension(10, 10)));
            JButton button = new JButton("Ricerca");
//            button.addActionListener(new ActionListener() {
//                
//                public void actionPerformed(ActionEvent e) {
//                    String text = textField.getText();
//                    Boolean okText;
//
//                    if (okText=true) {
//                        header.setVisible(false);
//                    } else {
//                        header.setVisible(true);
//                    }
//                }
//            });

            researchPanel.add(button);

//            //crea pannello risultati
//            JScrollPane scrollPanel = new JScrollPane();
//            JLabel scrollPane = new JLabel("Elenco libri");
//            scrollPane.setSize(new Dimension(850, 300));
//            scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
//
//
//            scrollPanel.add(scrollPane);

            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(researchPanel);
            panel.add(Box.createRigidArea(new Dimension(10, 10)));
            panel.add(header);
            panel.setVisible(true);

            return panel;

        }
    }
