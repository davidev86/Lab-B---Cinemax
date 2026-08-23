package cinemax.gui.tabpanel;


import cinemax.application.services.TcpClient;
import cinemax.gui.callback.SelezioneFilmCallBack;
import cinemax.gui.callback.SelezioneProjectionCallBack;


import javax.swing.*;

public class ProiezionistaChangeProjectionTab extends JPanel {
    public ProiezionistaChangeProjectionTab(SelezioneFilmCallBack selezioneFilmCallBack) {

        add(new ProiezionistaChangeProjection(selezioneFilmCallBack));
    }
}
