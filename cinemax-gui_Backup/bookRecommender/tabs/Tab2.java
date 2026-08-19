

import bookRecommender.callbacks.SelezioneLibroCallBack;

import javax.swing.*;

Tab2  extends JPanel {

    public Tab2(SelezioneLibroCallBack selezioneLibroCallBack) {

      add(new RicercaAutorePanel(selezioneLibroCallBack));


    }

}
