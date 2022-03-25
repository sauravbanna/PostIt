package ui.displays.userinput;

import model.PostIt;

import javax.swing.*;
import java.awt.*;

public abstract class UsernamePasswordInput extends UserInput {

    protected JPasswordField password;

    public UsernamePasswordInput(PostIt forum) {
        super(forum);
        initPassword();
    }

    private void initPassword() {
        password = new JPasswordField(15);
        password.setBorder(COMPOUND_BORDER_TRANSPARENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(password, gbc);
    }


}
