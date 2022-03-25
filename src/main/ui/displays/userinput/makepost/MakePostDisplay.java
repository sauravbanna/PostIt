package ui.displays.userinput.makepost;

import model.PostIt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static ui.PostItApp.DEFAULT_BACKGROUND_COLOR;
import static ui.PostItApp.DEFAULT_FOREGROUND_COLOR;
import static ui.displays.TwoButtonDisplay.FOREGROUND_BORDER;

// A two-buttoned display allowing the user to make a post with a title and some body,
// to a specified community
public abstract class MakePostDisplay extends JDialog {

    // FIELDS

    protected JPanel panel;
    protected JLabel titleText;
    protected JTextField title;
    protected JLabel bodyText;
    protected JLabel communityText;
    protected JComboBox community;
    protected JButton makePost;
    protected JButton cancel;
    protected PostIt forum;

    // METHODS

    // Constructor
    // EFFECTS: creates a new dialog, initialises its elements, and sets it visible
    //          sets the forum, width and height to the given value
    public MakePostDisplay(PostIt forum, int width, int height) {
        this.forum = forum;
        setTitle("Make a Post");
        setSize(new Dimension(width, height));
        panel = new JPanel(new GridBagLayout());
        setContentPane(panel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        initOnCloseAction();
        initPanelElements();
    }

    // MODIFIES: this, JPanel, JLabel
    // EFFECTS: initialises the panel elements and places it on the panel
    private void initPanelElements() {
        initTitleText();
        initTitle();
        initBodyText();
    }

    // MODIFIES: this
    // EFFECTS: adds a listener to react to when the dialog is closed
    private void initOnCloseAction() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel.doClick();
            }
        });
    }

    // MODIFIES: this, JPanel, JLabel
    // EFFECTS: initialises the title label and places it on the panel
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

    // MODIFIES: this, JPanel, JTextField
    // EFFECTS: initialises the title input text field and places it on the panel
    private void initTitle() {
        title = new JTextField(20);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        panel.add(title, gbc);
    }

    // MODIFIES: this, JPanel, JLabel
    // EFFECTS: initialises the body label and places it on the panel
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

    // MODIFIES: this, JPanel, JLabel
    // EFFECTS: initialises the community label and places it on the panel
    //          at the given y position
    protected void initCommunityText(int ypos) {
        communityText = new JLabel();
        communityText.setText("Community: ");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = ypos;
        gbc.weighty = 0.3;
        panel.add(communityText, gbc);
    }

    // MODIFIES: this, JPanel, JLabel
    // EFFECTS: initialises the community input combo box and places it on the panel
    protected void initCommunity(int ypos) {
        String[] communities = forum.getCommunities().keySet().toArray(new String[0]);
        community = new JComboBox(communities);
        community.setSelectedIndex(0);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = ypos;
        panel.add(community, gbc);
    }

    // MODIFIES: this, JPanel, JLabel
    // EFFECTS: initialises the button holder, adds buttons to it and places it on the panel
    protected void initButtons(int ypos) {
        JPanel buttonHolder = new JPanel();
        buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.X_AXIS));
        buttonHolder.setBackground(DEFAULT_FOREGROUND_COLOR);
        buttonHolder.setBorder(FOREGROUND_BORDER);

        initButtonDesigns();

        buttonHolder.add(Box.createHorizontalGlue());
        buttonHolder.add(cancel);
        buttonHolder.add(Box.createHorizontalGlue());
        buttonHolder.add(makePost);
        buttonHolder.add(Box.createHorizontalGlue());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = ypos;
        gbc.weighty = 0.3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(buttonHolder, gbc);
    }

    // MODIFIES: this, JPanel, JLabel
    // EFFECTS: initialises the make post and cancel buttons and adds design borders to them
    private void initButtonDesigns() {
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
    }

    // MODIFIES: this, JButton
    // EFFECTS: initialises the cancel action for the cancel button
    //          to prompt user to confirm exit when pressed
    protected void initCancelAction(Dialog component) {
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int userChoice = JOptionPane.showConfirmDialog(component,
                        "Are you sure you want to cancel? Your progress will be discarded.",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (userChoice == JOptionPane.YES_OPTION) {
                    component.dispose();
                }
            }
        });
    }

    // EFFECTS: returns false if the given string is null or has 0 length
    protected boolean checkIfEmpty(String str) {
        return (str != null && !(str.length() == 0));
    }

    // EFFECTS: prompts the user to confirm making a post
    //          return the user's choice
    protected boolean confirmMakePost() {
        int userChoice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to post this?",
                "Cofirm Post",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        return userChoice == JOptionPane.YES_OPTION;
    }

    // MODIFIES: JDialog
    // EFFECTS: lets user know that their post was successfully made
    //          and closes the given dialog box
    protected void postSuccessfullyMade(JDialog component) {
        JOptionPane.showMessageDialog(component,
                "Post Successfully Made!",
                "Success",
                JOptionPane.PLAIN_MESSAGE);
        component.dispose();
    }

}
