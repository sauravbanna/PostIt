package ui.displays.userinput.makepost;

import exceptions.IDAlreadyExistsException;
import model.PostIt;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static ui.PostItApp.MAX_IMAGE_DIMENSION;
import static ui.PostItApp.invalidInput;

// A two-buttoned dialog that allows the user to input a title, choose an image,
// and make an image post to a community
public class MakeImagePostDisplay extends MakePostDisplay {

    // CONSTANTS

    public static final String JPG = "jpg";
    public static final String PNG = "png";

    // FIELDS

    private JButton imageChoose;
    private JLabel image;
    private ImageIcon previewImage;
    private JFileChooser fileChooser;
    private File imageFile;

    // METHODS

    // Constructor
    // EFFECTS: creates a new dialog, initialises its elements, and sets it visible
    //          sets the forum, width and height to the given value
    public MakeImagePostDisplay(PostIt forum, int width, int height) {
        super(forum, width, height);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Only JPG and PNG files",
                JPG, PNG);
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);

        initImageButton();
        initImagePreview();
        initCommunityText(3);
        initCommunity(3);
        initButtons(4);
        initButtonActions();
        setLocationRelativeTo(getParent());
        setVisible(true);
    }

    // MODIFIES: this, JPanel, JButton
    // EFFECTS: initialises the image selection button and places it on the panel
    private void initImageButton() {
        imageChoose = new JButton();
        imageChoose.setText("Choose Image");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(imageChoose, gbc);
    }

    // MODIFIES: this, JPanel, JLabel
    // EFFECTS: initialises the image preview label and places it on the panel
    private void initImagePreview() {
        image = new JLabel();
        image.setText("No Image Chosen");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(image, gbc);
    }

    // MODIFIES: this, JButton
    // EFFECTS: initialises the button actions for this dialog
    private void initButtonActions() {
        initMakePostAction();

        initImageChooseAction();

        initCancelAction(this);
    }

    // MODIFIES: this, JButton
    // EFFECTS: initialises the button action to make a post
    private void initMakePostAction() {
        makePost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeImagePostIfValid();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: checks if the user inputs are valid, and then makes an image post if they are
    //          prompts user to re-enter inputs if not
    private void makeImagePostIfValid() {
        if (checkIfEmpty(title.getText())) {
            if (imageFile != null) {
                if (confirmMakePost()) {
                    makeImagePost();
                }
            } else {
                invalidInput(this, "picture");
            }
        } else {
            invalidInput(this, "title");
        }
    }

    // MODIFIES: this
    // EFFECTS: makes an image post with a unique id
    //          prompts user to try again if id could not be generated
    //          prompts user to try again if image could not be copied into forum
    //          shows a success dialog box if successful
    private void makeImagePost() {
        String path = imageFile.getName();
        String absolutePath = imageFile.getPath();
        int randomID = 0;
        try {
            randomID = forum.getRandomID();
        } catch (IDAlreadyExistsException ioe) {
            showErrorDialog("make post");
        }

        String targetPath = "./data/images/"
                + randomID
                + path.substring(path.length() - 4);

        try {
            Files.copy(Paths.get(absolutePath),
                    Paths.get(targetPath),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            showErrorDialog("make post");
        }

        forum.makeImagePost(title.getText(), targetPath, (String)community.getSelectedItem(), randomID);
        postSuccessfullyMade(this);

    }

    // EFFECTS: shows an error dialog prompting the user to try the given verb again
    private void showErrorDialog(String verb) {
        JOptionPane.showMessageDialog(this,
                "Unable to " + verb + ", please try again",
                "Error",
                JOptionPane.WARNING_MESSAGE);
    }

    // MODIFIES: this, JPanel, JLabel
    // EFFECTS: initialises the image selection button action
    private void initImageChooseAction() {
        imageChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chooseImage();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: allows the user to choose an image off of their computer
    private void chooseImage() {
        int userChoice = fileChooser.showDialog(this, "Open Image");

        if (userChoice == JFileChooser.APPROVE_OPTION) {
            imageFile = fileChooser.getSelectedFile();
            previewImageLabel();
        }
    }

    // MODIFIES: this, JLabel
    // EFFECTS: sets the image preview label to the current image
    //          prompts user to try again if unable to open image
    private void previewImageLabel() {
        try {
            previewImage = new ImageIcon(ImageIO.read(imageFile));
            image.setPreferredSize(new Dimension(MAX_IMAGE_DIMENSION, MAX_IMAGE_DIMENSION));
            image.setText("");
            image.setIcon(previewImage);
        } catch (IOException ioe) {
            showErrorDialog("open file");
        }
    }

}
