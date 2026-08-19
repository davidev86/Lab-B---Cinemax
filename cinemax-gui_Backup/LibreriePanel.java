import BookRecommender.Application.DTO.UserDTO;
import BookRecommender.Application.Services.UserService;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import static javax.imageio.ImageIO.read;



LibreriePanel  extends JPanel implements LoginCallBack{

    Consumer<UserDTO> _loginCallBack;
    Consumer<UserDTO> _logoutCallBack;
    JPanel nuovaLibreria;



    public LibreriePanel(Consumer<UserDTO> loginCallBack, Consumer<UserDTO> logoutCallBack) {
        this._loginCallBack = loginCallBack;
        this._logoutCallBack = logoutCallBack;
    }


    public JPanel build() {
        nuovaLibreria = new JPanel();
            nuovaLibreria.setLayout(new BoxLayout(nuovaLibreria, BoxLayout.Y_AXIS));
            nuovaLibreria.setPreferredSize(new Dimension(300,300));
                JLabel titolo = new JLabel("Inserisci nuova libreria:");
                titolo.setLayout(new BoxLayout(titolo, BoxLayout.X_AXIS));
                JLabel  nomeLibreria = new JLabel("Nome Libreria:");
                JTextField nuovoNome = new JTextField();
                JButton crea = new JButton("Crea Liberia");

                titolo.add(nomeLibreria);
                titolo.add(nuovoNome);
                titolo.add(crea);

                JScrollPane aggiungiLibri = new JScrollPane(nuovaLibreria);
                aggiungiLibri.setPreferredSize(new Dimension(300,300));

            nuovaLibreria.add(titolo);



        crea.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {

            }
        });


        return nuovaLibreria;
    }










    
    public void onLoginSuccess(UserDTO user) {
        this._loginCallBack.accept(user);

    }

    
    public void onLoginFailed(String errorMessage) {

    }


}
