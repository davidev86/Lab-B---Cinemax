// Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA)


package cinemax.gui.tabpanel;


import cinemax.application.services.TcpClient;
import cinemax.gui.callback.SelezioneProjectionCallBack;


import javax.swing.*;

public class SearchProjectionTab extends JPanel {
    public SearchProjectionTab(SelezioneProjectionCallBack selezioneProjectionCallBack, TcpClient tcpClient) {

        add(new SearchProjectionPanel(selezioneProjectionCallBack, tcpClient));
    }
}
