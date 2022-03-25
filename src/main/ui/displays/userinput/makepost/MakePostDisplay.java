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

public abstract class MakePostDisplay extends JDialog {

    protected JPanel panel;
    protected JLabel titleText;
    protected JTextField title;
    protected JLabel bodyText;
    protected JLabel communityText;
    protected JComboBox community;
    protected JButton makePost;
    protected JButton cancel;
    protected PostIt forum;

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

    private void initPanelElements() {
        initTitleText();
        initTitle();
        initBodyText();
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

    protected void initButtons(int ypos) {
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
        gbc.gridy = ypos;
        gbc.weighty = 0.3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(buttonHolder, gbc);
    }

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
}
