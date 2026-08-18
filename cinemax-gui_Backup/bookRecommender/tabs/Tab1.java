

import bookRecommender.callbacks.SelezioneLibroCallBack;

import javax.swing.*;

Tab1 extends JPanel {
    public Tab1(SelezioneLibroCallBack selezioneLibroCallBack) {

        add(new RicercaTitoloPanel(selezioneLibroCallBack));
    }
}
