

import BookRecommender.Application.DTO.BookDTO;
import BookRecommender.Application.DTO.UserDTO;
import bookRecommender.callbacks.LoginCallBack;
import bookRecommender.callbacks.SelezioneLibroCallBack;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

TabPanel extends JTabbedPane implements SelezioneLibroCallBack {

    public TabPanel() {

//         Creazione di un JTabbedPane

       setTabPlacement(RIGHT); // Schede a destra (verticali)

        addTab("Ricerca per titolo", new Tab1(this));
        addTab("Ricerca per autore", new Tab2(this));
        addTab("Crea Nuova Libreria",  new Tab3(this));
        addTab("Inserisci Valutazioni", new Tab4(this));
        addTab("Inserisci Suggerimenti", new Tab5(this));

        setPaneloff();


    }


    public void setPanelon(){

        setEnabledAt(0, true);
        setEnabledAt(1, true);
        setEnabledAt(2, true);
        setEnabledAt(3, true);


    }


    public void setPaneloff(){

        setEnabledAt(2, false);
        setEnabledAt(3, false);
        setEnabledAt(4, false);

    }


    public void setPanelforUSerLogged(UserDTO user) {

        setEnabledAt(2, true);
        setEnabledAt(3, true);
        setEnabledAt(4, true);

    }



    
    public void onSelezione(BookDTO book) {
        JOptionPane.showMessageDialog(this, book.title, "Successo", JOptionPane.INFORMATION_MESSAGE);
    }

    
    public void offSelezione(String errorMessage) {

    }





}