package cinemax.gui.tabpanel;


import cinemax.application.services.TcpClient;
import cinemax.gui.callback.SelezioneBookingCallBack;


import javax.swing.*;

public class SearchBookingTab extends JPanel {
    public SearchBookingTab(SelezioneBookingCallBack selezioneBookingCallBack, TcpClient tcpClient) {

        add(new SearchBookingPanel(selezioneBookingCallBack, tcpClient));
    }
}

