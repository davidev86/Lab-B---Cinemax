package cinemax.gui.login;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoggedPanel extends JPanel {

    public JPanel build() {

        //Creazione pannello logged
        JPanel logged = new JPanel();
        logged.setLayout(new BoxLayout(logged, BoxLayout.X_AXIS));

        //Creazione del bottone registrati
        JButton esciBotton = new JButton("Esci");
        esciBotton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {

            }
        });

        esciBotton.setVisible(true);

        //Creazione del bottone login
        JLabel label = new JLabel("Ciao" + "username");


        logged.add(Box.createHorizontalGlue());
        logged.add(esciBotton);
        logged.add(Box.createRigidArea(new Dimension(5, 10)));
        logged.add(label);
        logged.add(Box.createRigidArea(new Dimension(5, 10)));
        logged.setVisible(true);

        return logged;
    }


}
