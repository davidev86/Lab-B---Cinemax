package cinemax.gui.tabpanel;


import cinemax.application.services.TcpClient;
import cinemax.gui.callback.SelezioneBookingCallBack;


import javax.swing.*;

public class ClientBookingTab extends JPanel {
    public ClientBookingTab(SelezioneBookingCallBack selezioneBookingCallBack, TcpClient tcpClient) {

        add(new ClientBookingPanel(selezioneBookingCallBack, tcpClient));
    }
}

