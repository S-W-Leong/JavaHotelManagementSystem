import java.util.*;

public class Authentication {
    private Map<String, Map<String, String>> users;
    private Map<String, User> userObjects;
    private static final int MIN_USERNAME_LENGTH = 4;
    private static final int MAX_USERNAME_LENGTH = 20;
    private static final int MIN_PASSWORD_LENGTH = 8;

    public Authentication(Map<String, Map<String, String>> users, Map<String, User> userObjects) {
        this.users = users;
        this.userObjects = userObjects;
    }

    public User signIn(String userType) {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (users.containsKey(username) && users.get(username).get("password").equals(password)) {
                String role = users.get(username).get("role");
                // Verify user type matches the selected path
                if ((userType.equals("guest") && role.equals("guest")) ||
                    (userType.equals("staff") && (role.equals("manager") || role.equals("housekeeping")))) {
                    System.out.println("Welcome back, " + username + "!");
                    return userObjects.get(role);
                } else {
                    System.out.println("Invalid user type for this login path.");
                }
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        }
        System.out.println("Too many failed attempts. Returning to main menu.");
        return null;
    }

    private boolean isValidUsername(String username) {
        // Check length
        if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
            System.out.println("Username must be between " + MIN_USERNAME_LENGTH + " and " + MAX_USERNAME_LENGTH + " characters.");
            return false;
        }

        // Check if username already exists
        if (users.containsKey(username)) {
            System.out.println("Username already exists. Please choose another.");
            return false;
        }

        // Check for valid characters (letters, numbers, and underscores only)
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            System.out.println("Username can only contain letters, numbers, and underscores.");
            return false;
        }

        return true;
    }

    private boolean isValidPassword(String password) {
        // Check minimum length
        if (password.length() < MIN_PASSWORD_LENGTH) {
            System.out.println("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
            return false;
        }

        // Check for at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            System.out.println("Password must contain at least one uppercase letter.");
            return false;
        }

        // Check for at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            System.out.println("Password must contain at least one lowercase letter.");
            return false;
        }

        // Check for at least one number
        if (!password.matches(".*\\d.*")) {
            System.out.println("Password must contain at least one number.");
            return false;
        }

        // Check for at least one special character
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            System.out.println("Password must contain at least one special character.");
            return false;
        }

        return true;
    }

    public User signUp() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--- Guest Registration ---");
        System.out.println("Username requirements:");
        System.out.println("- Between " + MIN_USERNAME_LENGTH + " and " + MAX_USERNAME_LENGTH + " characters");
        System.out.println("- Can only contain letters, numbers, and underscores");
        System.out.println("\nPassword requirements:");
        System.out.println("- At least " + MIN_PASSWORD_LENGTH + " characters long");
        System.out.println("- At least one uppercase letter");
        System.out.println("- At least one lowercase letter");
        System.out.println("- At least one number");
        System.out.println("- At least one special character");
        
        // Get username with validation
        String username;
        do {
            System.out.print("\nEnter desired username: ");
            username = scanner.nextLine().trim();
        } while (!isValidUsername(username));

        // Get password with validation
        String password;
        do {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            if (!isValidPassword(password)) {
                System.out.println("Please try again with a stronger password.");
            }
        } while (!isValidPassword(password));

        // Confirm password
        String confirmPassword;
        do {
            System.out.print("Confirm password: ");
            confirmPassword = scanner.nextLine();
            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match. Please try again.");
            }
        } while (!password.equals(confirmPassword));

        // Create guest account
        users.put(username, Map.of("password", password, "role", "guest"));
        Guest newGuest = new Guest(username, password, new ArrayList<>(), new HashMap<>());
        userObjects.put("guest", newGuest);
        System.out.println("\nGuest account created successfully!");
        return newGuest;
    }
} 