package ui.displays.userinput;

import model.PostIt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static ui.PostItApp.*;
import static ui.displays.TwoButtonDisplay.FOREGROUND_BORDER;

public class MakePostDisplay extends JDialog {

    private JPanel panel;
    private JLabel titleText;
    private JTextField title;
    private JLabel bodyText;
    private JTextArea body;
    private JLabel communityText;
    private JComboBox community;
    private JButton makePost;
    private JButton cancel;
    private PostIt forum;


    public MakePostDisplay(PostIt forum, int width, int height) {
        this.forum = forum;
        setTitle("Make a Post");
        setSize(new Dimension(width, height));
        panel = new JPanel(new GridBagLayout());
        setContentPane(panel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        initOnCloseAction();
        initPanelElements();
        initButtonActions();
        setLocationRelativeTo(getParent());
        setVisible(true);
    }

    private void initPanelElements() {
        initTitleText();
        initTitle();
        initBodyText();
        initBody();
        initCommunityText();
        initCommunity();
        initButtons();
    }

    private void initOnCloseAction() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel.doClick();
            }
        });
    }

    private void initTitleText() {
        titleText = new JLabel();
        titleText.setText("Title of Post: ");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.3;
        panel.add(titleText, gbc);
    }

    private void initTitle() {
        title = new JTextField(20);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        panel.add(title, gbc);
    }

    private void initBodyText() {
        bodyText = new JLabel();
        bodyText.setText("Body of Post: ");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.3;
        panel.add(bodyText, gbc);
    }

    private void initBody() {
        body = new JTextArea(20, 20);
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(body, gbc);
    }

    private void initCommunityText() {
        communityText = new JLabel();
        communityText.setText("Community: ");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0.3;
        panel.add(communityText, gbc);
    }

    private void initCommunity() {
        String[] communities = forum.getCommunities().keySet().toArray(new String[0]);
        community = new JComboBox(communities);
        community.setSelectedIndex(0);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(community, gbc);
    }

    private void initButtons() {
        JPanel buttonHolder = new JPanel();
        buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.X_AXIS));
        buttonHolder.setBackground(DEFAULT_FOREGROUND_COLOR);
        buttonHolder.setBorder(FOREGROUND_BORDER);
        makePost = new JButton();
        makePost.setText("Make Post");
        makePost.setBackground(DEFAULT_FOREGROUND_COLOR);
        makePost.setBorder(FOREGROUND_BORDER);
        makePost.setForeground(DEFAULT_BACKGROUND_COLOR);
        cancel = new JButton();
        cancel.setText("Cancel");
        cancel.setBackground(DEFAULT_FOREGROUND_COLOR);
        cancel.setBorder(FOREGROUND_BORDER);
        cancel.setForeground(DEFAULT_BACKGROUND_COLOR);
        buttonHolder.add(Box.createHorizontalGlue());
        buttonHolder.add(cancel);
        buttonHolder.add(Box.createHorizontalGlue());
        buttonHolder.add(makePost);
        buttonHolder.add(Box.createHorizontalGlue());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0.3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(buttonHolder, gbc);
    }

    private void initButtonActions() {
        initMakePostAction();

        initCancelAction();
    }

    private void initMakePostAction() {
        makePost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makePostIfValid();
            }
        });
    }

    private void makePostIfValid() {
        if (checkIfEmpty(title.getText())) {
            if (checkIfEmpty(body.getText())) {
                if (confirmMakePost()) {
                    forum.makeTextPost(title.getText(),
                            body.getText(),
                            (String)(community.getSelectedItem()));
                    postSuccessfullyMade();
                }
            } else {
                invalidInput(this, "body");
            }
        } else {
            invalidInput(this, "title");
        }
    }



    private boolean checkIfEmpty(String str) {
        return (str != null && !(str.length() == 0));
    }

    private boolean confirmMakePost() {
        int userChoice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to post this?",
                "Cofirm Post",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (userChoice == JOptionPane.YES_OPTION) {
            return true;
        }

        return false;
    }

    private void postSuccessfullyMade() {
        JOptionPane.showMessageDialog(this,
                "Post Successfully Made!",
                "Success",
                JOptionPane.PLAIN_MESSAGE);
        this.dispose();
    }

    private void initCancelAction() {
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int userChoice = JOptionPane.showConfirmDialog(MakePostDisplay.this,
                        "Are you sure you want to cancel? Your progress will be discarded.",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (userChoice == JOptionPane.YES_OPTION) {
                    MakePostDisplay.this.dispose();
                }
            }
        });
    }
}
