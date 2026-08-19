import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Objects;
import javax.imageio.*;
import javax.swing.*;


// Classe personalizzata per disegnare un'immagine
class ImagePanel extends Panel {
    private Image image;

    public ImagePanel() {
    }

    public ImagePanel(String imagePath) {
        setImage(imagePath);
    }

    public void setImage(String imagePath) {
        try {
            // Carica l'immagine dal percorso specificato
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream((imagePath))));
//            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore nel caricamento dell'immagine." + imagePath);
        }
        // Richiede il ridisegno del pannello
        repaint();
    }

//    public void setImageFromClasspath(String imagePath) {
//        try {
//            image = ImageIO.read(new File(imagePath));
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Errore nel caricamento dell'immagine dal classpath: " + imagePath);
//        }
//        repaint();
//    }

    
    public void paint(Graphics g) {
        super.paint(g);
        if (image != null) {

            // Disegna l'immagine all'interno del pannello
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }

}