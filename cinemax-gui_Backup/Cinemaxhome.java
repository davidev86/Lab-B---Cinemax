import cinemax.serverCM;
import cinemax.serverCM.services;
import cinemax.serverCM.services.Utils;



import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

Cinemaxhome {
	
	 /* 	static UserDTO loggedUser=null;
	    static BookDTO onSelezione=null;
	    static BookDTO onSelezioneLibro=null;*/

	    public static void main(final String[] args) {

	        //Creazione del frame principale
	        JFrame frameHome = new JFrame("Cinemax");
	        frameHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);frameHome.setSize(1000, 800);
	        frameHome.setLocationRelativeTo(null);
	        frameHome.setResizable(false);

	        // Creazione del pannello principale con BoxLayout (verticale)
	        JPanel mainPanel = new JPanel();
	        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));



	        TabPanel tabPanel = new TabPanel();




	        //JOptionPane.showMessageDialog(RicercaAutorePanel.this, "Scheda Libro!", "Successo", JOptionPane.INFORMATION_MESSAGE);


	        RicercaAutorePanel ricercaAutorePanel = new RicercaAutorePanel();




	        LoginPanel loginPanel = new LoginPanel((userDTO )->{

	            loggedUser = userDTO;
	           tabPanel.setPanelforUSerLogged(loggedUser);
	           ricercaAutorePanel.setButtonforUSerLogged(loggedUser);

	        },
	                (userDTO )->{

	                    loggedUser = null;
	                }
	                );


	        //Creazione panel per immagine
	        ImagePanel imagePanel = new ImagePanel("/images/Book Recommender.png");
	            imagePanel.setMinimumSize(new Dimension(1000, 200));
	            imagePanel.setMaximumSize(new Dimension(1000, 200));
	            imagePanel.setPreferredSize(new Dimension(1000, 200));


	        //aggiunta componenti al pannello principale
	        mainPanel.add(loginPanel.build());
	        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	        mainPanel.add(imagePanel);
	        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	        mainPanel.add(tabPanel.build());


	       // Aggiunta del pannello principale al frame
	        frameHome.getContentPane().add(mainPanel);

	        // Visualizzazione del frame
	        frameHome.setVisible(true);





	    }


	 //  public static void login(UserDTO userDTO) {

	    }

