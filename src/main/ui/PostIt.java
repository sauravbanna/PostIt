package ui;

import model.Community;
import model.User;
import model.content.posts.Post;
import model.content.posts.TextPost;

import java.util.*;

import static ui.Feed.showCommands;

public class PostIt {

    // CONSTANTS

    public static final String VIEW_COMMUNITY_COMMAND = "/visit";
    public static final String MAKE_POST_COMMAND = "/post";
    public static final String LOGOUT_COMMAND = "/logout";
    public static final String LOGIN_COMMAND = "/login";
    public static final String HOME_COMMAND = "/home";
    public static final String VIEW_USER_COMMAND = "/user";
    public static final String REGISTER_COMMAND = "/register";
    public static final String EXIT_COMMAND = "/exit";
    public static final String NEXT_ACTION_COMMAND = "action";
    public static final String SHOW_AVAILABLE_COMMUNITIES = "/show";
    public static final String SUBSCRIBE_TO_COMMUNITY_COMMAND = "/join";
    public static final String HELP_COMMAND = "/help";

    public static final int MAX_USERNAME_LENGTH = 20;
    public static final int MIN_PASSWORD_LENGTH = 8;

    public static final List<String> DEFAULT_COMMUNITIES =
            Arrays.asList("funny", "news", "mildlyinteresting", "canada", "sports", "gaming");

    // FIELDS
    private Boolean loggedIn;
    private User currentlyLoggedInUser;
    private Feed activeFeed;

    private HashMap<String, User> usernamePasswords;
    private HashMap<String, Community> communities;
    private Scanner input;

    // METHODS

    public PostIt() {
        loggedIn = false;
        usernamePasswords = new HashMap<>();
        communities = new HashMap<>();
        input = new Scanner(System.in);

        for (String community : DEFAULT_COMMUNITIES) {
            communities.put(community, new Community(community, "This is a default community."));
        }

        currentlyLoggedInUser = null;
    }

    // EFFECTS: starts the forum for the user
    public void start() {
        System.out.println("Welcome to PostIt!");
        System.out.println("Type " + HOME_COMMAND + " to browse your home feed, "
                + VIEW_COMMUNITY_COMMAND + " to view a specific community, "
                + SHOW_AVAILABLE_COMMUNITIES + " to see all available communities, "
                + "\n"
                + SUBSCRIBE_TO_COMMUNITY_COMMAND + " to subscribe to a community, "
                + VIEW_USER_COMMAND + " to view a user's profile, "
                + LOGIN_COMMAND + " to log in to your account, "
                + "\n"
                + REGISTER_COMMAND + " to register an account, and "
                + EXIT_COMMAND + " to exit the forum.");
        String userChoice = input.nextLine();
        while (true) {
            switch (userChoice) {
                case HOME_COMMAND:
                    userChoice = showHomeFeed();
                    break;
                case SUBSCRIBE_TO_COMMUNITY_COMMAND:
                    userChoice = subscribeToCommunity();
                    break;
                case SHOW_AVAILABLE_COMMUNITIES:
                    userChoice = showAvailableCommunities();
                    break;
                case VIEW_COMMUNITY_COMMAND:
                    userChoice = visitCommunity();
                    break;
                case VIEW_USER_COMMAND:
                    userChoice = viewUser();
                    break;
                case LOGIN_COMMAND:
                    userChoice = logIntoAccount();
                    break;
                case LOGOUT_COMMAND:
                    System.out.println("Successfully logged out!");
                    loggedIn = false;
                    currentlyLoggedInUser = null;
                    userChoice = NEXT_ACTION_COMMAND;
                    break;
                case MAKE_POST_COMMAND:
                    userChoice = makeTextPost();
                    break;
                case REGISTER_COMMAND:
                    userChoice = registerAccount();
                    break;
                case EXIT_COMMAND:
                    System.out.println("Thanks for using PostIt!");
                    System.exit(0);
                    break;
                case NEXT_ACTION_COMMAND:
                    if (activeFeed != null) {
                        System.out.println("Resuming your previous feed ...");
                        userChoice = activeFeed.start();
                    } else {
                        System.out.println("What would you like to do next?");
                        userChoice = input.nextLine();
                    }
                    break;
                case HELP_COMMAND:
                    showCommands();
                    userChoice = NEXT_ACTION_COMMAND;
                    break;
                default:
                    System.out.println("Sorry, I didn't understand you, please enter a valid command.");
                    userChoice = input.nextLine();
                    break;
            }
        }
    }

    // EFFECTS: prints out the names of all currently existing communities
    //          and returns the NEXT_ACTION_COMMAND
    public String showAvailableCommunities() {
        System.out.println("These are the currently available communities.");
        for (String community : communities.keySet()) {
            System.out.print(community + " ");
        }
        System.out.println();

        return NEXT_ACTION_COMMAND;
    }

    // MODIFIES: this
    // EFFECTS: adds the given username and password (if valid) to the usernamePasswords map
    //          and creates a new user based on the given information
    public String registerAccount() {
        System.out.println("You can always type "  + EXIT_COMMAND + " to cancel the registration.");
        System.out.println("Please enter your desired username between 1-" + MAX_USERNAME_LENGTH + " characters.");
        System.out.println("You can't change this later.");
        String username = input.nextLine();

        if (username.equals(EXIT_COMMAND)) {
            return NEXT_ACTION_COMMAND;
        }

        while (username.length() < 1 || username.length() > MAX_USERNAME_LENGTH) {
            System.out.println("Please enter a valid username.");
            username = input.nextLine();

            if (username.equals(EXIT_COMMAND)) {
                return NEXT_ACTION_COMMAND;
            }
        }

        System.out.println("Please enter a password longer than " + MIN_PASSWORD_LENGTH + " characters.");
        String password = input.nextLine();

        if (password.equals(EXIT_COMMAND)) {
            return NEXT_ACTION_COMMAND;
        }

        while (password.length() < MIN_PASSWORD_LENGTH) {
            System.out.println("Please enter a valid password.");
            password = input.nextLine();

            if (password.equals(EXIT_COMMAND)) {
                return NEXT_ACTION_COMMAND;
            }
        }

        usernamePasswords.put(username, new User(username, password));
        System.out.println("Successfully registered your account! Log in by typing " + LOGIN_COMMAND);
        return NEXT_ACTION_COMMAND;
    }

    // MODIFIES: this
    // EFFECTS: if the given password matches the given username, logs in user
    public String logIntoAccount() {
        System.out.println("You can always type "  + EXIT_COMMAND + " to cancel the log in.");
        System.out.println("Please enter your username: ");
        String username = input.nextLine();

        if (username.equals(EXIT_COMMAND)) {
            return NEXT_ACTION_COMMAND;
        }

        System.out.println("Please enter your password: ");
        String password = input.nextLine();

        if (password.equals(EXIT_COMMAND)) {
            return NEXT_ACTION_COMMAND;
        }

        while (!(password.equals(usernamePasswords.get(username).getPassword()))) {
            System.out.println("That's the wrong password!");
            password = input.nextLine();

            if (password.equals(EXIT_COMMAND)) {
                return NEXT_ACTION_COMMAND;
            }
        }

        loggedIn = true;
        currentlyLoggedInUser = usernamePasswords.get(username);

        System.out.println("Successfully logged in!");

        return NEXT_ACTION_COMMAND;
    }

    // EFFECTS: prints out the community's description of the given community
    public void printCommunityInfo(Community c) {
        System.out.println("Welcome to the " + c.getCommunityName() + " community!");
        System.out.println(c.getCommunityAbout());
        System.out.println("Here are the posts from this community: ");

    }

    // EFFECTS: print out the user info of the given user
    public void printUserInfo(User u) {
        System.out.println("Welcome to " + u.getUserName() + "'s profile!");
        System.out.println("Bio: " + u.getBio());
        System.out.println("Subscribed Communities: " + u.getSubscribedCommunities());
    }

    // EFFECTS: if logged in, subscribes user to specified community
    //          if valid community name is given
    //          if not logged in, tells user to log in
    public String subscribeToCommunity() {
        if (loggedIn) {
            System.out.println("Please enter the name of an "
                    + "existing community that you would like to join.");
            String communityChoice = input.nextLine();

            if (communityChoice.equals(EXIT_COMMAND)) {
                return NEXT_ACTION_COMMAND;
            }

            while (!communities.containsKey(communityChoice)) {
                System.out.println("That's not an existing community!");
                communityChoice = input.nextLine();
                if (communityChoice.equals(EXIT_COMMAND)) {
                    return NEXT_ACTION_COMMAND;
                }
            }

            currentlyLoggedInUser.subscribeToCommunity(communities.get(communityChoice));
            System.out.println("Successfully subscribed you to the "
                    + communityChoice + " community!");
        } else {
            System.out.println("You have to be logged in to do that!");
        }

        return NEXT_ACTION_COMMAND;
    }

    // REQUIRES: DEFAULT_COMMUNITIES has at least 1 community
    // EFFECTS: if user is logged in, shows user posts from their subscribed communities
    //          if user is not logged in, shows user posts from default communities
    public String showHomeFeed() {
        System.out.println("This is your Home Feed!");
        LinkedList<Post> currentFeed = new LinkedList<>();
        if (loggedIn) {
            System.out.println("Showing you posts from your favorite communities!");
            for (String community : currentlyLoggedInUser.getSubscribedCommunities()) {
                currentFeed.addAll(communities.get(community).getPosts());
            }

            if (currentFeed.size() == 0) {
                System.out.println("There are no posts to show, please subscribe to some communities!");
                return NEXT_ACTION_COMMAND;
            }
        } else {
            System.out.println("Showing you posts from default communities, "
                    + "log in to see posts from your favorite communities!");
            for (String community : DEFAULT_COMMUNITIES) {
                currentFeed.addAll(communities.get(community).getPosts());
            }
        }

        activeFeed = new Feed(currentFeed, loggedIn, currentlyLoggedInUser);
        return activeFeed.start();
    }

    // EFFECTS: prompts user to type in a community name
    //          if valid community name, shows user posts of that community
    //          if invalid, tells user that community was not found and returns the next command
    public String visitCommunity() {
        System.out.println("Please enter the name of the community you want to visit.");
        System.out.println("Type " + EXIT_COMMAND + " to cancel.");
        String userChoice = input.nextLine();

        if (userChoice.equals(EXIT_COMMAND)) {
            return NEXT_ACTION_COMMAND;
        }

        if (communities.containsKey(userChoice)) {
            printCommunityInfo(communities.get(userChoice));
            activeFeed = new Feed(communities.get(userChoice).getPosts(),
                    loggedIn, currentlyLoggedInUser);
            return activeFeed.start();
        } else {
            System.out.println("Sorry, that community was not found.");
            return NEXT_ACTION_COMMAND;
        }
    }

    // EFFECTS: prompts user to enter a username
    //          if user is a valid user (exists in list of registered users), displays user profile
    //          else, tells user that the user was not found
    //          returns the next command
    public String viewUser() {
        activeFeed = null;
        System.out.println("Please enter the name of a registered user who's profile you want to view.");
        String userName = input.nextLine();

        if (usernamePasswords.containsKey(userName)) {
            printUserInfo(usernamePasswords.get(userName));
        } else {
            System.out.println("Sorry, that user was not found.");
        }

        return NEXT_ACTION_COMMAND;
    }

    // EFFECTS: if user is logged in, makes a post according to user specifications
    //          in a valid, existing community
    //          if user is not logged in, tells user that they have to log in
    public String makeTextPost() {
        if (loggedIn) {
            System.out.println("You can cancel making a post at anytime by typing " + EXIT_COMMAND);
            System.out.println("Please enter what community you want to post in: ");
            String communityChoice = input.nextLine();

            if (communityChoice.equals(EXIT_COMMAND)) {
                return NEXT_ACTION_COMMAND;
            }

            while (!communities.containsKey(communityChoice)) {
                System.out.println("That's not an existing community!");
                communityChoice = input.nextLine();
                if (communityChoice.equals(EXIT_COMMAND)) {
                    return NEXT_ACTION_COMMAND;
                }
            }

            System.out.println("Please enter your post's title:");
            String title = input.nextLine();

            if (title.equals(EXIT_COMMAND)) {
                return NEXT_ACTION_COMMAND;
            }

            System.out.println("Please enter your post body:");
            String body = input.nextLine();

            if (body.equals(EXIT_COMMAND)) {
                return NEXT_ACTION_COMMAND;
            }

            Post newPost = new TextPost(currentlyLoggedInUser.getUserName(), title, body, communityChoice);
            communities.get(communityChoice).addPost(newPost);
            System.out.println("Successfully made post!");
        } else {
            System.out.println("You have to be logged in to do that!");
        }

        return NEXT_ACTION_COMMAND;
    }


}
