

import javax.swing.*;
import java.awt.*;

RicercaAutorePanel extends JPanel {


    public JPanel build() {

        JPanel panel = new JPanel();


        JPanel researchPanel = new JPanel();
        researchPanel.setLayout(new BoxLayout(researchPanel, BoxLayout.X_AXIS));

        JLabel label = new JLabel("Ricerca Autore");
        researchPanel.add(label);

        researchPanel.add(Box.createRigidArea(new Dimension(10, 10)));

        JTextField textField = new JTextField();
        textField.setEditable(true);
        textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
        textField.setPreferredSize(new Dimension(250, 25));

        researchPanel.add(textField);

        researchPanel.add(Box.createRigidArea(new Dimension(10, 10)));

        JLabel labelAnno = new JLabel("Anno");
        researchPanel.add(labelAnno);

        researchPanel.add(Box.createRigidArea(new Dimension(10, 10)));

        JTextField textFieldAnno = new JTextField();
        textFieldAnno.setEditable(true);
        textFieldAnno.setFont(new Font("Tahoma", Font.PLAIN, 12));
        textFieldAnno.setPreferredSize(new Dimension(50, 25));
        researchPanel.add(textFieldAnno);

        researchPanel.add(Box.createRigidArea(new Dimension(10, 10)));


        JButton button = new JButton("Ricerca");

        researchPanel.add(button);


        JScrollPane scrollPanel = new JScrollPane();
        JLabel scrollPane = new JLabel("Elenco libri");
        scrollPane.setSize(new Dimension(850, 300));
        scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 40));
        scrollPane.setBackground(Color.BLUE);


        scrollPanel.add(scrollPane);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(researchPanel);
        panel.add(Box.createRigidArea(new Dimension(10, 10)));
        panel.add(scrollPanel);
        panel.setVisible(true);

        return panel;

    }
}
