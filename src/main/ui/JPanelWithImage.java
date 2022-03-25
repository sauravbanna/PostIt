package ui;

import model.content.posts.Post;

import javax.swing.*;
import java.awt.*;

public class JPanelWithImage extends JPanel {

    private Image img;
    private int imageWidth;
    private int imageHeight;

    public JPanelWithImage(Image img) {
        super();
        this.img = img;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (imageHeight == 0 || imageWidth == 0) {
            graphics.drawImage(img, 0, 0, null);
        } else {
            graphics.drawImage(img.getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT),
                    0, 0, null);
        }
    }

    public void setImageBounds(int width, int height) {
        this.imageWidth = width;
        this.imageHeight = height;
        repaint();
    }


}
