package ui.displays.userinput.makepost;

import model.PostIt;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MakeImagePostDisplay extends MakePostDisplay {

    private JButton imageChoose;
    private JLabel image;



    public MakeImagePostDisplay(PostIt forum, int width, int height) {
        super(forum, width, height);
        initImageButton();
        initImagePreview();
        initCommunityText(3);
        initCommunity(3);
        initButtons(4);
        initButtonActions();
        setLocationRelativeTo(getParent());
        setVisible(true);
    }

    private void initImageButton() {
        imageChoose = new JButton();
        imageChoose.setText("Choose Image");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(imageChoose, gbc);
    }

    private void initImagePreview() {
        image = new JLabel();
        image.setText("No Image Chosen");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(image, gbc);
    }

    private void initButtonActions() {
        initMakePostAction();

        initImageChooseAction();

        initCancelAction(this);
    }

    private void initMakePostAction() {

    }

    private void initImageChooseAction() {
        imageChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chooseImage();
            }
        });
    }

    private void chooseImage() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Only JPEG, JPG< and PNG files", "jpg", "png", "jpeg");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        fileChooser.showDialog(this, "Open Image");
    }

}
