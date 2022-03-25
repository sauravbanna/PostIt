package ui.displays;

import model.PostIt;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static ui.PostItApp.*;

public abstract class TwoButtonDisplay extends JDialog {

    public static final Border LOWERED_BEVEL_BORDER = BorderFactory.createLoweredBevelBorder();
    public static final Border TRANSPARENT_BORDER = BorderFactory.createMatteBorder(PADDING, PADDING, PADDING, PADDING,
            DEFAULT_BACKGROUND_COLOR);
    public static final Border FOREGROUND_BORDER = BorderFactory.createMatteBorder(PADDING, PADDING, PADDING, PADDING,
            DEFAULT_FOREGROUND_COLOR);
    public static final Border THIN_BLACK_BORDER = BorderFactory.createMatteBorder(1, 1, 1, 1,
            Color.BLACK);
    public static final Border COMPOUND_BORDER_TRANSPARENT = BorderFactory.createCompoundBorder(TRANSPARENT_BORDER,
            BorderFactory.createCompoundBorder(THIN_BLACK_BORDER, LOWERED_BEVEL_BORDER));

    protected PostIt forum;
    protected JPanel panel;
    protected JButton accept;
    protected JButton cancel;

    public TwoButtonDisplay(PostIt forum) {
        this.forum = forum;
        this.panel = new JPanel();
        panel.setBackground(DEFAULT_BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING,PADDING,PADDING,PADDING));
        panel.setLayout(new GridBagLayout());



        initLoginElements();
    }

    private void initLoginElements() {
        initButtons();

    }

    protected void initButtons() {
        JPanel buttonHolder = new JPanel();
        buttonHolder.setBackground(DEFAULT_FOREGROUND_COLOR);
        buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.X_AXIS));

        initButtonDesigns();

        buttonHolder.add(Box.createHorizontalGlue());
        buttonHolder.add(cancel);
        buttonHolder.add(Box.createHorizontalGlue());
        buttonHolder.add(accept);
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

    private void initButtonDesigns() {
        accept = new JButton();
        accept.setText("");
        accept.setBorder(FOREGROUND_BORDER);
        accept.setBackground(DEFAULT_FOREGROUND_COLOR);
        accept.setForeground(DEFAULT_BACKGROUND_COLOR);
        cancel = new JButton();
        cancel.setText("Cancel");
        cancel.setBackground(DEFAULT_FOREGROUND_COLOR);
        cancel.setForeground(DEFAULT_BACKGROUND_COLOR);
        cancel.setBorder(FOREGROUND_BORDER);
    }


    public abstract void initButtonActions();

    public abstract void makeVisible();

    public void startDisplay() {
        initButtonActions();

        this.setContentPane(panel);
        setLocationRelativeTo(getParent());
        pack();
        setVisible(true);
    }
}
