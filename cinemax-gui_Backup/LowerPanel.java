//import BookRecommender.Application.DTO.UserDTO;
//
//import javax.swing.*;
//
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.function.Consumer;
//
//import static javax.imageio.ImageIO.read;
//
//LowerPanel extends JPanel implements LoginCallBack {
//
//
//        Consumer<UserDTO> _loginCallBack;
//        Consumer<UserDTO> _logoutCallBack;
//        JPanel lowerPanel;
//        RicercaAutorePanel ricercaAutorePanel;
//        RicercaTitoloPanel ricercaTitoloPanel;
//
//
//    public LowerPanel(Consumer<UserDTO> loginCallBack, Consumer<UserDTO> logoutCallBack) {
//        this._loginCallBack = loginCallBack;
//        this._logoutCallBack = logoutCallBack;
//    }
//
//    public JPanel build() {
//
//        lowerPanel = new JPanel();
//        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));
//
//
//        JRadioButton ricerca_per_titolo = new JRadioButton("Ricerca per titolo", true);
//        JRadioButton ricerca_per_autore = new JRadioButton("Ricerca per autore", true);
//
//        ButtonGroup buttonGroup = new ButtonGroup();
//        buttonGroup.add(ricerca_per_titolo);
//        buttonGroup.add(ricerca_per_autore);
//
//        ricercaAutorePanel = new RicercaAutorePanel();
//        ricercaTitoloPanel = new RicercaTitoloPanel();
//
//
//        lowerPanel.add(ricerca_per_titolo, ricerca_per_autore);
////        lowerPanel.add(ricerca_per_autore);
//        lowerPanel.add(ricercaTitoloPanel);
//        lowerPanel.add(ricercaAutorePanel);
//
//
//        ricerca_per_titolo.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent e) {
//
//                ricercaTitoloPanel.setVisible(true);
//                ricercaAutorePanel.setVisible(false);
//            }
//        });
//
//        ricerca_per_autore.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//
//                ricercaTitoloPanel.setVisible(false);
//                ricercaAutorePanel.setVisible(true);
//            }
//        });
//
//
//
//        return lowerPanel;
//    }
//
//
//    
//    public void onLoginSuccess(UserDTO user) {
//        this._loginCallBack.accept(user);
//
//
//    }
//
//    
//    public void onLoginFailed(String errorMessage) {
//
//    }
//
//
//
//
//
//}
