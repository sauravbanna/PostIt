package ui.displays;

import model.PostIt;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static ui.PostItApp.*;

// A basic display with 2 buttons
public abstract class TwoButtonDisplay extends JDialog {

    // CONSTANTS

    public static final Border LOWERED_BEVEL_BORDER = BorderFactory.createLoweredBevelBorder();
    public static final Border TRANSPARENT_BORDER = BorderFactory.createMatteBorder(PADDING, PADDING, PADDING, PADDING,
            DEFAULT_BACKGROUND_COLOR);
    public static final Border FOREGROUND_BORDER = BorderFactory.createMatteBorder(PADDING, PADDING, PADDING, PADDING,
            DEFAULT_FOREGROUND_COLOR);
    public static final Border THIN_BLACK_BORDER = BorderFactory.createMatteBorder(1, 1, 1, 1,
            Color.BLACK);
    public static final Border COMPOUND_BORDER_TRANSPARENT = BorderFactory.createCompoundBorder(TRANSPARENT_BORDER,
            BorderFactory.createCompoundBorder(THIN_BLACK_BORDER, LOWERED_BEVEL_BORDER));

    // FIELDS

    protected PostIt forum;
    protected JPanel panel;
    protected JButton button2;
    protected JButton button1;

    // METHODS

    // Constructor
    // REQUIRES: given PostIt is not null
    // EFFECTS: creates a new dialog, initialises its elements
    //          sets the forum to the given value
    public TwoButtonDisplay(PostIt forum) {
        this.forum = forum;
        this.panel = new JPanel();
        panel.setBackground(DEFAULT_BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING,PADDING,PADDING,PADDING));
        panel.setLayout(new GridBagLayout());



        initButtons();
    }

    // MODIFIES: this, JPanel, JLabel
    // EFFECTS: initialises the display's buttons and places it on the panel
    protected void initButtons() {
        JPanel buttonHolder = new JPanel();
        buttonHolder.setBackground(DEFAULT_FOREGROUND_COLOR);
        buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.X_AXIS));

        initButtonDesigns();

        buttonHolder.add(Box.createHorizontalGlue());
        buttonHolder.add(button1);
        buttonHolder.add(Box.createHorizontalGlue());
        buttonHolder.add(button2);
        buttonHolder.add(Box.createHorizontalGlue());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0.3;
        gbc.gridwidth = 2;
        gbc.ipadx = PADDING;
        gbc.ipady = PADDING;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(buttonHolder, gbc);
    }

    // MODIFIES: this, JButton
    // EFFECTS: sets the designs for the display's buttons
    private void initButtonDesigns() {
        button2 = new JButton();
        button2.setText("");
        button2.setBorder(FOREGROUND_BORDER);
        button2.setBackground(DEFAULT_FOREGROUND_COLOR);
        button2.setForeground(DEFAULT_BACKGROUND_COLOR);
        button1 = new JButton();
        button1.setText("Cancel");
        button1.setBackground(DEFAULT_FOREGROUND_COLOR);
        button1.setForeground(DEFAULT_BACKGROUND_COLOR);
        button1.setBorder(FOREGROUND_BORDER);
    }

    // MODIFIES: JButton
    // EFFECTS: sets the actions for the 2 buttons on the display
    public abstract void initButtonActions();

    // EFFECTS: makes this display visible
    public abstract void makeVisible();

    // MODIFIES: this
    // EFFECTS: initialises the 2 button's actions
    //          and makes the display visible
    public void startDisplay() {
        initButtonActions();

        this.setContentPane(panel);
        setLocationRelativeTo(getParent());
        pack();
        setVisible(true);
    }
}
