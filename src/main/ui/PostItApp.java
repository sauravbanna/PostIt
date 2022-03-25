package ui;

import exceptions.EmptyFeedException;
import exceptions.EndOfFeedException;
import exceptions.IDAlreadyExistsException;
import exceptions.StartOfFeedException;
import model.Community;
import model.PostIt;
import model.User;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.displays.userinput.CreateCommunityDisplay;
import ui.displays.userinput.LoginDisplay;
import ui.displays.userinput.RegisterDisplay;
import ui.displays.userinput.makepost.MakeImagePostDisplay;
import ui.displays.userinput.makepost.MakeTextPostDisplay;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

// A forum where a user can register an account and login / logout
// Contains communities that the user can create, post to, visit and view the posts of
// as well as user profiles of the user and other registered users
public class PostItApp extends JFrame {

    // CONSTANTS
    public static final String FORUM_DATA = "./data/forum.json";
    public static final String BACKGROUND_IMAGE = "./data/images/background.png";
    public static final String WINDOW_TITLE = "PostIt";
    public static final int DEFAULT_WIDTH_PX = 1280;
    public static final int DEFAULT_HEIGHT_PX = 720;
    public static final int MAX_IMAGE_DIMENSION = 320;
    public static final int TITLE_FONT_SIZE = 40;
    public static final int PADDING = 10;
    public static final Color DEFAULT_FOREGROUND_COLOR = new Color(77, 121, 174);
    public static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    public static final int NUM_COMMENTS_TO_SHOW = 5;
    public static final String DEFAULT_FONT = "Verdana";

    // FIELDS
    private PostIt postIt;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JPanelWithImage forum;
    private JPanel title;
    private JPanel buttons;
    private JPanel feed;
    private JButton backButton;
    private JButton nextButton;
    private JButton makePostButton;
    private JButton editProfileButton;
    private JButton createCommunityButton;
    private JLabel titleText;
    private JLabel aboutSectionText;
    private JLabel subCountText;
    private int widthPx;
    private int heightPx;
    private String userBeingViewed;
    private String communityBeingViewed;
    private Image background;
    private boolean local;

    // METHODS

    // Constructor
    // EFFECTS: creates a new PostIt JFrame and asks user if they want to load forum from file
    //          if yes, instantiates the JSON writer and reader to the correct file location
    //          and loads forum
    //          if no, creates default forum
    //          then adds all window elements and starts the forum display
    public PostItApp() {
        if (askUserToLoadData()) {
            jsonWriter = new JsonWriter(FORUM_DATA);
            jsonReader = new JsonReader(FORUM_DATA);
            initForum();
        } else {
            postIt = new PostIt();
            postIt.addDefaultCommunitiesCheck();
        }

        this.widthPx = DEFAULT_WIDTH_PX;
        this.heightPx = DEFAULT_HEIGHT_PX;

        initForumWindow();
        initButtonActions();
        initMenuBar();
        initButtons(false);
        initResizeWindow();
        getHomeFeed();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: asks user if they want to load forum from file or create a new one or cancel
    //          closes if cancel
    private boolean askUserToLoadData() {
        Object[] options = {"Load From File", "Create Local Forum", "Cancel"};
        int userChoice = JOptionPane.showOptionDialog(this,
                "Would you like to load the forum on file, or create a local empty forum?",
                "Start Forum",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (userChoice == JOptionPane.YES_OPTION) {
            local = false;
            return true;
        } else if (userChoice == JOptionPane.NO_OPTION) {
            local = true;
            return false;
        }

        System.exit(0);
        return false;

    }

    // MODIFIES: this
    // EFFECTS: adds a new Window Listener to react to window resizing
    private void initResizeWindow() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeWindow();
            }
        });

        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                resizeWindow();
            }
        });
    }

    // MODIFIES: this, JPanelWithImage
    // EFFECTS: calculates and sets the new height and width of the display
    //          and redraws background
    private void resizeWindow() {
        this.widthPx = getWidth();
        this.heightPx = getHeight();
        forum.setImageBounds(widthPx, heightPx);

    }

    // MODIFIES: this, PostIt
    // EFFECTS: tries to load forum from file, prints to console if failed
    private void initForum() {
        try {
            postIt = jsonReader.read();
        } catch (IOException ioe) {
            System.out.println("File Read failed");
        }
        postIt.addDefaultCommunitiesCheck();
    }

    // MODIFIES: this, JPanelWithImage
    // EFFECTS: initialises the forum window with a background and window elements
    private void initForumWindow() {
        background = getBackgroundImage();
        forum = new JPanelWithImage(background);
        forum.setLayout(new GridBagLayout());
        forum.requestFocusInWindow();
        setContentPane(forum);
        setTitle(WINDOW_TITLE);
        setSize(widthPx, heightPx);

        addForumElements();

        initWindowListener();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    // EFFECTS: tries to get background image from specified file location
    //          return image if successful, prints to console if failed
    private Image getBackgroundImage() {
        Image image = null;
        try {
            image = ImageIO.read(new File(BACKGROUND_IMAGE));
        } catch (IOException ioe) {
            System.out.println("Background Image not Found");
        }

        return image;
    }

    // MODIFIES: this
    // EFFECTS: initialises the forum elements for the forum display
    private void addForumElements() {
        addTitle();

        initTitle();

        addFeed();

        addButtons();
    }

    // MODIFIES: this, JPanelWithImage
    // EFFECTS: initialises the button holder panel for the forum display
    private void addButtons() {
        buttons = new JPanel();
        buttons.setBackground(DEFAULT_FOREGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0.2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        forum.add(buttons, gbc);
    }

    // MODIFIES: this, JPanelWithImage
    // EFFECTS: initialises the feed panel for the forum display
    private void addFeed() {
        feed = new JPanel();
        feed.setLayout(new BoxLayout(feed, BoxLayout.X_AXIS));
        feed.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.8;
        gbc.fill = GridBagConstraints.BOTH;
        forum.add(feed, gbc);
    }

    // MODIFIES: this, JPanelWithImage
    // EFFECTS: initialises the title panel for the forum display
    private void addTitle() {
        title = new JPanel();
        title.setLayout(new GridBagLayout());
        title.setBorder(new MatteBorder(PADDING, PADDING, PADDING, PADDING, DEFAULT_FOREGROUND_COLOR));
        title.setBackground(DEFAULT_FOREGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        forum.add(title, gbc);
    }

    // MODIFIES: this, JLabel
    // EFFECTS: initialises the title text for the title panel
    private void initTitle() {
        titleText = new JLabel();
        titleText.setFont(new Font(DEFAULT_FONT, Font.PLAIN, TITLE_FONT_SIZE));
        aboutSectionText = new JLabel();
        aboutSectionText.setFont(new Font(DEFAULT_FONT, Font.PLAIN, (int)(TITLE_FONT_SIZE / 2)));
        subCountText = new JLabel();
        subCountText.setFont(new Font(DEFAULT_FONT, Font.PLAIN, (int)(TITLE_FONT_SIZE / 2)));

        initTitlePosition();
    }

    // MODIFIES: this, JPanel
    // EFFECTS: sets the title text at their positions on the title panel
    private void initTitlePosition() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        title.add(titleText, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        title.add(aboutSectionText, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        title.add(subCountText, gbc);
    }

    // MODIFIES: this, JLabel
    // EFFECTS: sets the title text based on the given logged in boolean
    private void refreshTitleText(boolean loggedIn) {
        if (loggedIn) {
            User user = postIt.getCurrentUser();
            titleText.setText("Welcome to your custom home feed, " + user.getUserName() + "!");
            aboutSectionText.setText("Here you can see posts from communities you subscribed to");
            subCountText.setText("Current Communities: " + user.getSubscribedCommunities());
        } else {
            titleText.setText("Welcome to the default home feed!");
            aboutSectionText.setText("Here you can see posts from default communities.");
            subCountText.setText("Current Default Communities: " + PostIt.DEFAULT_COMMUNITIES);
        }

        repaint();
    }

    // MODIFIES: this, JLabel
    // EFFECTS: sets the title text based on the given user's profile
    private void refreshTitleText(User user) {
        titleText.setText("Welcome to " + user.getUserName() + "'s profile!");
        aboutSectionText.setText(user.getBio());
        subCountText.setText("Here are " + user.getUserName() + "'s posts:");
        repaint();
    }

    // MODIFIES: this, JLabel
    // EFFECTS: sets the title text based on the given community's information
    private void refreshTitleText(Community community) {
        titleText.setText("Welcome to the " + community.getCommunityName() + " community!");
        aboutSectionText.setText(community.getCommunityAbout());
        subCountText.setText("Current Subscribers: " + community.getSubCount());
        repaint();
    }

    // MODIFIES: this, JLabel
    // EFFECTS: sets the title text based on whether a user, a community, or the home feed is being viewed
    private void refreshTitleText() {
        if (userBeingViewed != null) {
            refreshTitleText(postIt.getUsernamePasswords().get(userBeingViewed));
        } else if (communityBeingViewed != null) {
            refreshTitleText(postIt.getCommunities().get(communityBeingViewed));
        } else {
            refreshTitleText(postIt.getLoggedIn());
        }
    }

    // MODIFIES: this, JPanel
    // EFFECTS: adds buttons to the buttons panel
    //          if editProfile is true, adds edit profile button, doesn't add if false
    private void initButtons(boolean editProfile) {
        buttons.removeAll();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(Box.createHorizontalGlue());
        buttons.add(backButton);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(makePostButton);
        buttons.add(Box.createHorizontalGlue());
        if (editProfile) {
            buttons.add(editProfileButton);
            buttons.add(Box.createHorizontalGlue());
        }
        buttons.add(createCommunityButton);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(nextButton);
        buttons.add(Box.createHorizontalGlue());
    }

    // MODIFIES: this, JButton
    // EFFECTS: initialises the actions for the forum buttons
    private void initButtonActions() {
        initBackButtonAction();
        initNextButtonAction();
        initEditProfileButtonAction();
        initMakePostButtonAction();
        initCreateCommunityButtonAction();

    }

    // MODIFIES: this, JButton
    // EFFECTS: initialises the action for the create community button
    //          opens the community creation display if user is logged in
    //          tells user to log in if not
    private void initCreateCommunityButtonAction() {
        createCommunityButton = new JButton();
        createCommunityButton.setText("Create Community");
        createCommunityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (postIt.getLoggedIn()) {
                    createCommunity();
                } else {
                    loginWarning(PostItApp.this);
                }
            }
        });
    }


    // MODIFIES: this, JButton
    // EFFECTS: initialises the action for the make post button
    //          opens the make post display if user is logged in
    //          tells user to log in if not
    private void initMakePostButtonAction() {
        makePostButton = new JButton();
        makePostButton.setText("Make Post");
        makePostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (postIt.getLoggedIn()) {
                    makePostPressed();
                } else {
                    loginWarning(PostItApp.this);
                }
            }
        });
    }

    // MODIFIES: this, JButton
    // EFFECTS: initialises the action for the edit profile button
    //          opens the edit profile dialog
    private void initEditProfileButtonAction() {
        editProfileButton = new JButton();
        editProfileButton.setText("Edit Profile");
        editProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editProfilePressed();
            }
        });
    }


    // MODIFIES: this, JButton
    // EFFECTS: initialises the action for the next button
    //          shows the next post in the feed
    private void initNextButtonAction() {
        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextButtonPressed();
            }
        });
    }


    // MODIFIES: this, JButton
    // EFFECTS: initialises the action for the back button
    //          shows the previous post in the feed
    private void initBackButtonAction() {
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backButtonPressed();
            }
        });
    }

    // MODIFIES: this, CreateCommunityDisplay
    // EFFECTS: creates a dialog to make a new community
    private void createCommunity() {
        CreateCommunityDisplay display = new CreateCommunityDisplay(postIt);
        display.makeVisible();
    }

    // MODIFIES: this, Feed
    // EFFECTS: if activeFeed is not null, moves back in feed
    //          lets user know if they've reached start of feed
    private void backButtonPressed() {
        if (postIt.getActiveFeed() != null) {
            try {
                postIt.getActiveFeed().back();
            } catch (StartOfFeedException eofe) {
                JOptionPane.showMessageDialog(this,
                        "You've reached the start of the Feed!",
                        "Warning", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // MODIFIES: this, Feed
    // EFFECTS: if activeFeed is not null, moves forward in feed
    //          lets user know if they've reached end of feed
    private void nextButtonPressed() {
        if (postIt.getActiveFeed() != null) {
            try {
                postIt.getActiveFeed().next();
                repaint();
            } catch (EndOfFeedException eofe) {
                JOptionPane.showMessageDialog(this,
                        "You've reached the end of the Feed!",
                        "Warning", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a listener to detect when the forum is closed
    //          prompts user to save if the forum is not local
    private void initWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!local) {
                    closeWithSaving();
                } else {
                    closeWithoutSaving();
                }
            }
        });
    }

    // EFFECTS: asks user to confirm exit, and then ask them if they want to save the forum
    private void closeWithSaving() {
        int userChoice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?",
                "Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (userChoice == JOptionPane.YES_OPTION) {
            saveForumCheck();
        }
    }

    // EFFECTS: asks user to confirm exit and exits
    private void closeWithoutSaving() {
        int userChoice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit? Your posts will not be saved.",
                "Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (userChoice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // EFFECTS: asks user if they want to save the forum
    //          saves and exits if yes, exits if no
    private void saveForumCheck() {
        int userChoice = JOptionPane.showConfirmDialog(this,
                "Would you like to save the forum? Close this message to Auto-save.",
                "Save Posts", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (userChoice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }

        saveForum();
    }

    // MODIFIES: this
    // EFFECTS: tries to save the forum to file
    //          exits if successful
    //          asks user if they want to exit without saving if not
    private void saveForum() {
        try {
            jsonWriter.openWriter();
            jsonWriter.saveForum(postIt);
            jsonWriter.close();
            System.exit(0);
        } catch (FileNotFoundException fe) {
            Object[] options = {"Exit Anyway", "Don't Exit"};
            int userChoice = JOptionPane.showOptionDialog(this,
                    "Unable to save your posts during this session, are you sure you want to exit?",
                    "Unable to Save",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE, null, options, options[0]);

            if (userChoice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    // EFFECTS: creates a navigation menu section and adds options to it
    private JMenu makeNavigateMenu() {
        JMenu navigateMenu = new JMenu("Navigate");
        navigateMenu.setMnemonic(KeyEvent.VK_N);
        addToMenu(navigateMenu, new HomeFeedAction(),
                KeyStroke.getKeyStroke("control H"));
        addToMenu(navigateMenu, new VisitCommunityAction(),
                KeyStroke.getKeyStroke("control V"));
        addToMenu(navigateMenu, new BrowseCommunitiesAction(),
                KeyStroke.getKeyStroke("control B"));
        addToMenu(navigateMenu, new VisitUserAction(),
                KeyStroke.getKeyStroke("control U"));
        return navigateMenu;
    }

    // EFFECTS: creates a user menu section and adds options to it
    private JMenu makeUserMenu() {
        JMenu userMenu = new JMenu("Your Profile");
        userMenu.setMnemonic(KeyEvent.VK_U);
        if (postIt.getLoggedIn()) {
            addToMenu(userMenu, new UserProfileAction(),
                    KeyStroke.getKeyStroke("control P"));
        }
        addToMenu(userMenu, new RegisterAction(),
                KeyStroke.getKeyStroke("control R"));
        addToMenu(userMenu, new LoginAction(),
                KeyStroke.getKeyStroke("control L"));
        addToMenu(userMenu, new LogoutAction(),
                KeyStroke.getKeyStroke("control O"));
        return userMenu;
    }

    // MODIFIES: this
    // EFFECTS: adds navigation and user menu sections to menu and sets the menu to this display
    public void initMenuBar() {
        JMenuBar menu = new JMenuBar();
        menu.add(makeNavigateMenu());
        menu.add(makeUserMenu());
        setJMenuBar(menu);
    }

    // MODIFIES: this, PostIt, Feed
    // EFFECTS: tries to get the home feed of the forum
    //          if feed is empty, tells user that it is
    //          refreshes the title text and buttons
    private void getHomeFeed() {
        try {
            userBeingViewed = null;
            communityBeingViewed = null;
            postIt.startHomeFeed();
            postIt.getActiveFeed().setDisplay(feed);
        } catch (Exception e) {
            emptyFeed("Subscribe to some communities first!");
        }

        initButtons(false);
        refreshTitleText();
    }


    // MODIFIES: this, PostIt, User
    // EFFECTS: prompts user to enter a new bio
    //          updates bio and refreshes title text if entered string is not empty
    //          prompts user to re-enter if empty
    private void editProfilePressed() {
        String newBio = JOptionPane.showInputDialog(this,
                "Please enter your new bio.",
                "Update Profile",
                JOptionPane.QUESTION_MESSAGE);

        if (!checkEmptyString(newBio)) {
            postIt.getCurrentUser().setBio(newBio);
            JOptionPane.showMessageDialog(this,
                    "Profile Successfully Updated!",
                    "Success",
                    JOptionPane.PLAIN_MESSAGE);
            refreshTitleText(postIt.getCurrentUser());
        } else {
            invalidInput(this, "bio");
        }
    }

    // EFFECTS: shows user a dialog saying that the given inputType was invalid
    public static void invalidInput(Container parent, String inputType) {
        JOptionPane.showMessageDialog(parent,
                "Please enter a valid " + inputType,
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE);
    }

    // EFFECTS: prompts user to choose whether they want to make a text or image post
    //          shows a make text post display or make image post display based on choice
    private void makePostPressed() {
        Object[] choices = {"Text Post", "Image Post", "Cancel"};
        int userChoice = JOptionPane.showOptionDialog(this,
                "What kind of post would you like to make?",
                "Make New Post",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                choices,
                choices[0]);

        if (userChoice == JOptionPane.YES_OPTION) {
            MakeTextPostDisplay makePost = new MakeTextPostDisplay(postIt, (int) (widthPx / 2), heightPx);
        } else if (userChoice == JOptionPane.NO_OPTION) {
            MakeImagePostDisplay makeImagePost = new MakeImagePostDisplay(postIt, (int) (widthPx / 2), heightPx);
        }
    }

    // MODIFIES: this, PostIt, Feed
    // EFFECTS: tries to get the community feed of the given community
    //          if feed is empty, tells user that it is
    //          refreshes the title text and buttons
    private void getCommunityFeed(String community) {
        try {
            feed.removeAll();
            postIt.clearActiveFeed();
            userBeingViewed = null;
            communityBeingViewed = community;
            postIt.startCommunityFeed(community);
            postIt.getActiveFeed().setDisplay(feed);
        } catch (EmptyFeedException e) {
            emptyFeed("Be the first to post here!");
        }

        initButtons(false);
        refreshTitleText();
    }

    // MODIFIES: this, Feed, PostIt, JLabel, JPanel
    // EFFECTS: clears the current feed and feed display
    //          sets feed display to the given text
    private void emptyFeed(String str) {
        feed.removeAll();
        postIt.clearActiveFeed();
        JLabel noPosts = new JLabel(str);
        noPosts.setFont(new Font(DEFAULT_FONT, Font.PLAIN, (int)(TITLE_FONT_SIZE / 2)));
        feed.add(Box.createHorizontalGlue());
        feed.add(noPosts);
        feed.add(Box.createHorizontalGlue());
    }

    // MODIFIES: this, PostIt, Feed
    // EFFECTS: tries to get the user feed of the given user
    //          if feed is empty, tells user that it is
    //          refreshes the title text and buttons
    //          if given username is the same as the current user
    //          adds edit profile button
    private void getUserFeed(String username) {
        try {
            userBeingViewed = username;
            communityBeingViewed = null;
            postIt.visitUser(username);
            postIt.getActiveFeed().setDisplay(feed);
        } catch (EmptyFeedException e) {
            emptyFeed("No Posts from this User.");
        }

        if (postIt.getCurrentUser() != null
                && postIt.getCurrentUser().getUserName().equals(username)) {
            initButtons(true);
        } else {
            initButtons(false);
        }
        refreshTitleText();
    }

    // Method taken from AlarmControllerUI class in
    // https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
    // EFFECTS: Adds an item with given handler to the given menu
    private void addToMenu(JMenu theMenu, AbstractAction action, KeyStroke accelerator) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setMnemonic(menuItem.getText().charAt(0));
        menuItem.setAccelerator(accelerator);
        theMenu.add(menuItem);
    }

    // EFFECTS: shows a dialog to the user prompting them to choose from the given array of objects
    //          checks chosen object
    private void getCommunitySelection(Object[] communities) {
        String communityChoice = (String)JOptionPane.showInputDialog(PostItApp.this,
                "Please select the community you would like to visit.",
                "Browse Communities",
                JOptionPane.QUESTION_MESSAGE,
                null,
                communities,
                communities[0]);
        checkCommunityInput(communityChoice);

    }

    // EFFECTS: shows a dialog to the user prompting them to enter a community name
    //          checks input string
    private void getCommunityInput() {
        String communityChoice = JOptionPane.showInputDialog(PostItApp.this,
                "Please select the community you would like to visit.",
                "Visit a Community",
                JOptionPane.QUESTION_MESSAGE);
        checkCommunityInput(communityChoice);
    }

    // EFFECTS: returns true if string is null or has length 0
    public static boolean checkEmptyString(String str) {
        return (str == null || str.length() == 0);
    }

    // EFFECTS: if given community name is a registered community on PostIt
    //          display the posts from that community
    //          else lets user know that community was not found
    private void checkCommunityInput(String communityChoice) {
        if (postIt.getCommunities().containsKey(communityChoice)) {
            getCommunityFeed(communityChoice);
        } else if (!checkEmptyString(communityChoice)) {
            notFoundError("Community");
        }
    }

    // EFFECTS: lets user know that the object with the given name was not found
    private void notFoundError(String notFound) {
        JOptionPane.showMessageDialog(PostItApp.this,
                notFound + " not Found!",
                "Error",
                JOptionPane.WARNING_MESSAGE);
    }

    // EFFECTS: shows a dialog to the user prompting them to enter a user name that they want to view
    //          checks input string
    private void getUserNameInput() {
        String userChoice = JOptionPane.showInputDialog(PostItApp.this,
                "Please select the user you would like to visit.",
                "Visit a User",
                JOptionPane.QUESTION_MESSAGE);
        checkUserNameInput(userChoice);
    }

    // EFFECTS: if given user name is a registered user on PostIt
    //          display the posts from that user
    //          else lets user know that user was not found
    private void checkUserNameInput(String userChoice) {
        if (postIt.getUsernamePasswords().containsKey(userChoice)) {
            getUserFeed(userChoice);
        } else if (!checkEmptyString(userChoice)) {
            notFoundError("User");
        }
    }

    // MODIFIES: RegisterDisplay
    // EFFECTS: if user is logged in, lets them know to log out first
    //          otherwise, shows the register display to register an account
    private void register() {
        if (postIt.getLoggedIn()) {
            JOptionPane.showMessageDialog(this,
                    "You're already logged in, log out first to create a new account.",
                    "You're Logged In",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            RegisterDisplay register = new RegisterDisplay(postIt);
            register.makeVisible();
        }
    }

    // MODIFIES: RegisterDisplay
    // EFFECTS: if user is logged in, lets them know
    //          otherwise, shows the login display to log into an account
    //          refreshes the home feed once logged in
    private void login() {
        if (postIt.getLoggedIn()) {
            JOptionPane.showMessageDialog(this,
                    "You're already logged in!",
                    "You're Logged In",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            LoginDisplay login = new LoginDisplay(postIt);
            login.makeVisible();
            login.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    getHomeFeed();
                }
            });
        }
    }

    // MODIFIES: RegisterDisplay
    // EFFECTS: if user is logged out, lets them know
    //          otherwise, asks user to confirm logout, logs user out if yes
    //          and refreshes home feed and menu bar
    private void logout() {
        if (postIt.getLoggedIn()) {
            int userChoice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to log out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (userChoice == JOptionPane.YES_OPTION) {
                postIt.logOut();
                initMenuBar();
                getHomeFeed();
            }
        } else {
            loginWarning(this);
        }
    }

    // EFFECTS: displays dialog on given parent component
    //          letting user know that they are not logged in
    public static void loginWarning(Component parent) {
        JOptionPane.showMessageDialog(parent,
                "You have to be logged in to do that!",
                "Not Logged In",
                JOptionPane.WARNING_MESSAGE);
    }

    // Represents action to be taken when user wants to view their home feed
    public class HomeFeedAction extends AbstractAction {

        HomeFeedAction() {
            super("Go Home");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getHomeFeed();

        }
    }

    // Represents action to be taken when user wants to visit a community
    public class VisitCommunityAction extends AbstractAction {

        VisitCommunityAction() {
            super("Visit a Community");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getCommunityInput();
        }

    }

    // Represents action to be taken when user wants to browse all communities
    public class BrowseCommunitiesAction extends AbstractAction {

        BrowseCommunitiesAction() {
            super("Browse all Communities");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object[] communities = postIt.getCommunities().keySet().toArray(new String[0]);
            getCommunitySelection(communities);
        }

    }

    // Represents action to be taken when user wants to visit a user's profile
    public class VisitUserAction extends AbstractAction {

        VisitUserAction() {
            super("Visit a User");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getUserNameInput();
        }
    }

    // Represents action to be taken when user wants to view their own profile
    public class UserProfileAction extends AbstractAction {

        UserProfileAction() {
            super("Profile");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getUserFeed(postIt.getCurrentUser().getUserName());
        }

    }

    // Represents action to be taken when user wants to logout
    public class LogoutAction extends AbstractAction {

        LogoutAction() {
            super("Logout");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            logout();
        }

    }

    // Represents action to be taken when user wants to login
    public class LoginAction extends AbstractAction {

        LoginAction() {
            super("Login");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            login();
        }

    }

    // Represents action to be taken when user wants to register an account
    public class RegisterAction extends AbstractAction {

        RegisterAction() {
            super("Register");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            register();
        }

    }

    // MODIFIES: JButton
    // EFFECTS: removes any action listener on the given JButton
    public static void clearActionListeners(JButton button) {
        for (ActionListener al : button.getActionListeners()) {
            button.removeActionListener(al);
        }

    }

    // EFFECTS: tries to get a unique id from the forum
    //          throws IDAlreadyExistsException if generated id is not unique
    public int getRandomID() throws IDAlreadyExistsException {
        return postIt.getRandomID();
    }


}
