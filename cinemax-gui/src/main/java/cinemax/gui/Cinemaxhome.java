package cinemax.gui;



import javax.swing.*;

import cinemax.application.services.UserService;
import cinemax.application.services.TcpClient;
import cinemax.contracts.dto.*;
import cinemax.contracts.responses.GetUserDetailsResponse;
import cinemax.gui.callback.LoginCallBack;
import cinemax.gui.callback.LogoutCallBack;
import cinemax.gui.callback.SelezioneProjectionCallBack;
import cinemax.gui.login.LoginPanel;
import cinemax.gui.tabpanel.SearchProjection;
import cinemax.gui.tabpanel.TabPanel;

import java.awt.*;


 
public class Cinemaxhome { 
	
	static UserMinInfo loggedUser=null;
    static ProjectionDetails onSelezione=null;
    static ProjectionDetails onSelezioneLibro=null;
    static SelezioneProjectionCallBack selezioneProjectionCallBack;
    LoginPanel loginPanel;
    SearchProjection searchProjection;
    static TabPanel tabPanel;
	

	    public static void main(final String[] args) {
	    
	 //   	SwingUtilities.invokeLater(() -> {
	        String serverIP = "127.0.0.1"; // localhost
			int serverPort = 12345;
			
	        // Il TCP client servrà ai componenti (panel) che devono comunicare con i DAO
	        
	        
	        TcpClient  tcpClient = new TcpClient(serverIP, serverPort);

	        //Creazione del frame principale
	        JFrame frameHome = new JFrame("Cinemax");
	        frameHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);frameHome.setSize(1000, 800);
	        frameHome.setLocationRelativeTo(null);
	        frameHome.setResizable(false);

	        // Creazione del pannello principale con BoxLayout (verticale)
	        JPanel mainPanel = new JPanel();
	        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));



	        tabPanel = new TabPanel(tcpClient);
	        SearchProjection searchProjection = new SearchProjection(selezioneProjectionCallBack, tcpClient);
	        searchProjection.setVisible(true);


 

	        LoginPanel loginPanel = new LoginPanel(tcpClient, (UserMinInfo user)->{
	           loggedUser = user;
	           tabPanel.setPanelforUSerLogged(user);
	           tabPanel.revalidate();
               tabPanel.repaint();
               
	        },
			   
	                ()->{

	                    loggedUser = null;
	                    tabPanel.setPanelforUserUnlogged();
	                }
	            );  

	        
	 	       // Aggiunta del pannello principale al frame
		        frameHome.getContentPane().add(mainPanel);

		        // Visualizzazione del frame
		        frameHome.setVisible(true);

		    try {			    
	        //aggiunta componenti al pannello principale
		    mainPanel.add(loginPanel.build());
	        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	   //     mainPanel.add(imagePanel);
	   //     mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	        mainPanel.add(tabPanel.build());
		    } catch (Exception e) {
                System.err.println("Errore di caricamento" + e.getMessage());
            }
 
	       // Aggiunta del pannello principale al frame
	        frameHome.getContentPane().add(mainPanel);

	        // Visualizzazione del frame
	        frameHome.setVisible(true);


	    }


	 public static void login(UserMinInfo userMinInfo) {
		 
	    }


	
	  

}