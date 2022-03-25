package ui;

import model.content.posts.Post;

import javax.swing.*;
import java.awt.*;

// A JPanel with an Image background
public class JPanelWithImage extends JPanel {

    // FIELDS

    private Image img;
    private int imageWidth;
    private int imageHeight;

    // METHODS

    // Constructor
    // EFFECTS: creates a new JPanel and sets the image to the given image
    public JPanelWithImage(Image img) {
        super();
        this.img = img;
    }

    // MODIFIES: this
    // EFFECTS: if one of image height and width fields == 0,
    //          draws image background at image's natural size
    //          else, scales image and draws it as background
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

    // MODIFIES: this
    // EFFECTS: updates the image height and width and redraws the panel
    public void setImageBounds(int width, int height) {
        this.imageWidth = width;
        this.imageHeight = height;
        repaint();
    }


}
