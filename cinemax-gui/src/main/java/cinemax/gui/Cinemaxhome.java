package cinemax.gui;



import javax.swing.*;

import cinemax.application.services.UserService;
import cinemax.application.services.TcpClient;
import cinemax.contracts.dto.UserMinInfos;
import cinemax.contracts.responses.GetUserDetailsResponse;
import cinemax.gui.login.LoginPanel;

import java.awt.*;


 
public class Cinemaxhome { 
	
	static UserMinInfos loggedUser=null;
 //   static BookDTO onSelezione=null;
 //   static BookDTO onSelezioneLibro=null;

	
	ImagePanel imagePanel;
	LoginPanel loginPanel;
	
	

	    public static void main(final String[] args) {
	    
	 //   	SwingUtilities.invokeLater(() -> {
	        String serverIP = "127.0.0.1"; // localhost
			int serverPort = 12345;
			
	        /*
	         * 
	         * Esempio chiamata. 
	         * Il TCP client andrà poi pasasto ai vari componenti (panel?) che devono raggiungere il DB
	         */
	        
	        TcpClient  tcpClient = new TcpClient(serverIP, serverPort);
	        
	       
	        UserService userService = new UserService(tcpClient);
			
	    

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




	//        JOptionPane.showMessageDialog(RicercaAutorePanel.this, "Scheda Libro!", "Successo", JOptionPane.INFORMATION_MESSAGE);


	//        RicercaAutorePanel ricercaAutorePanel = new RicercaAutorePanel();


 

	   LoginPanel loginPanel = new LoginPanel(tcpClient, (UserMinInfos user)->{

		   loggedUser = user;
	           tabPanel.setPanelforUSerLogged();
	     //      ricercaAutorePanel.setButtonforUSerLogged(loggedUser);
	           tabPanel.revalidate();
               tabPanel.repaint();

	        },
			   
	                (UserMinInfos user)->{

	                    loggedUser = null;
	                }
	            );  



	        GetUserDetailsResponse res =  userService.getUserDetails(1);
	        
	        //Creazione panel per immagine
	        ImagePanel imagePanel = new ImagePanel("/images/20230501_165319.jpg");
	        
	        try {
	        	 
	            imagePanel.setMinimumSize(new Dimension(1000, 200));
	            imagePanel.setMaximumSize(new Dimension(1000, 200));
	            imagePanel.setPreferredSize(new Dimension(1000, 200));
	            mainPanel.add(imagePanel);
	    	} catch (Exception e) {
                System.err.println("Errore nel caricamento immagine: " + e.getMessage());
            }
	            
	 	       // Aggiunta del pannello principale al frame
		        frameHome.getContentPane().add(mainPanel);

		        // Visualizzazione del frame
		        frameHome.setVisible(true);

		    try {			    
	        //aggiunta componenti al pannello principale
		    mainPanel.add(loginPanel.build());
	        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	        mainPanel.add(imagePanel);
	        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	        mainPanel.add(tabPanel.build());
		    } catch (Exception e) {
                System.err.println("Errore di caricamento" + e.getMessage());
            }

	       // Aggiunta del pannello principale al frame
	        frameHome.getContentPane().add(mainPanel);

	        // Visualizzazione del frame
	        frameHome.setVisible(true);

	    	
	    	

	    }


	 //  public static void login(UserDTO userDTO) {

	    }

