


import BookRecommender.Application.DTO.UserDTO;
import bookRecommender.login.LoginPanel;
import bookRecommender.tabs.TabPanel;

import javax.swing.*;
import java.awt.*;

Main {

        private static UserDTO loggedUser;

        public static void main(final String[] args) {

            //Creazione del frame principale
            JFrame frameHome = new JFrame("Book Recommender");
            frameHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);frameHome.setSize(1000, 800);
            frameHome.setLocationRelativeTo(null);
            frameHome.setResizable(false);

            // Creazione del pannello principale con BoxLayout (verticale)
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));



            TabPanel tabPanel = new TabPanel();




            //JOptionPane.showMessageDialog(RicercaAutorePanel.this, "Scheda Libro!", "Successo", JOptionPane.INFORMATION_MESSAGE);


            LoginPanel loginPanel = new LoginPanel((userDTO )->{
                loggedUser = userDTO;
                tabPanel.setPanelforUSerLogged(loggedUser);

            },
                    (userDTO )->{

                        loggedUser = null;
                    }
            );



            //aggiunta componenti al pannello principale
            mainPanel.add(loginPanel);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            mainPanel.add(GetHeaderImagePanel());
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            mainPanel.add(tabPanel);


            // Aggiunta del pannello principale al frame
            frameHome.getContentPane().add(mainPanel);

            // Visualizzazione del frame
            frameHome.setVisible(true);





        }

    private static ImagePanel GetHeaderImagePanel() {
        ImagePanel imagePanel = new ImagePanel("/images/Book Recommender.png");
        imagePanel.setMinimumSize(new Dimension(1000, 200));
        imagePanel.setMaximumSize(new Dimension(1000, 200));
        imagePanel.setPreferredSize(new Dimension(1000, 200));
        return imagePanel;
    }

}