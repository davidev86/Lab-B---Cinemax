// Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA)

import cinemax.serverCM;
import cinemax.serverCM.services;
import cinemax.serverCM.services.Utils;

import javax.swing.*;

Tab1 extends JPanel {
    public Tab1(SelezioneLibroCallBack selezioneLibroCallBack) {

        add(new RicercaTitoloPanel(selezioneLibroCallBack));
    }
}
