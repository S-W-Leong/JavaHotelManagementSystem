import java.util.*;

class HotelSystem {
    private Map<String, User> users;
    private List<Reservation> reservations;
    private Map<String, Integer> rooms;
    private List<Map<String, String>> housekeepingSchedule;
    private Authentication auth;
    private Map<String, Map<String, String>> userCredentials;
    private void displayLogo() {
        System.out.println("\n");
        System.out.println("    ██╗ █████╗ ██████╗  ██████╗ ███████╗██████╗ ██╗   ██╗ ██████╗");
        System.out.println("    ██║██╔══██╗██╔══██╗██╔═══██╗██╔════╝██╔══██╗██║   ██║██╔════╝");
        System.out.println("    ██║███████║██████╔╝██║   ██║███████╗██████╔╝██║   ██║██║      ");
        System.out.println("    ██║██╔══██║██╔══██╗██║   ██║╚════██║██╔══██╗██║   ██║██║      ");
        System.out.println("    ██║██║  ██║██║  ██║╚██████╔╝███████║██║  ██║╚██████╔╝╚██████╗");
        System.out.println("    ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝  ╚═════╝");
        System.out.println("                                                                  ");
        System.out.println("             ██╗  ██╗ ██████╗ ████████╗███████╗██╗     ");
        System.out.println("             ██║  ██║██╔═══██╗╚══██╔══╝██╔════╝██║     ");
        System.out.println("             ███████║██║   ██║   ██║   █████╗  ██║     ");
        System.out.println("             ██╔══██║██║   ██║   ██║   ██╔══╝  ██║     ");
        System.out.println("             ██║  ██║╚██████╔╝   ██║   ███████╗███████╗");
        System.out.println("             ╚═╝  ╚═╝ ╚═════╝    ╚═╝   ╚══════╝╚══════╝");
        System.out.println("\n");
    }

    public HotelSystem() {
        // Initialize data
        reservations = new ArrayList<>();
        rooms = new HashMap<>();
        rooms.put("Single", 20);
        rooms.put("Double", 20);
        rooms.put("Suite", 10);

        // Initialize users
        users = new HashMap<>();
        users.put("guest", new Guest("guest1", "guest123", reservations, rooms));
        users.put("manager", new Manager("manager1", "manager123", "Manager", housekeepingSchedule));
        users.put("housekeeping", new HousekeepingStaff("staff1", "staff123", "Housekeeping", housekeepingSchedule));

        // Initialize user credentials
        userCredentials = new HashMap<>();
        userCredentials.put("guest1", Map.of("password", "guest123", "role", "guest"));
        userCredentials.put("manager1", Map.of("password", "manager123", "role", "manager"));
        userCredentials.put("staff1", Map.of("password", "staff123", "role", "housekeeping"));

        // Initialize authentication
        auth = new Authentication(userCredentials, users);
    }

    public void start() {
        displayLogo();
        System.out.println("Welcome to the Hotel Management System!");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Select User Type ---");
            System.out.println("1. Guest");
            System.out.println("2. Staff");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleGuestFlow();
                    break;
                case "2":
                    handleStaffFlow();
                    break;
                case "3":
                    System.out.println("Exiting the system. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleGuestFlow() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Guest Menu ---");
            System.out.println("1. Sign In");
            System.out.println("2. Sign Up");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            User loggedInUser = null;
            switch (choice) {
                case "1":
                    loggedInUser = auth.signIn("guest");
                    if (loggedInUser != null) {
                        loggedInUser.mainMenu();
                    }
                    break;
                case "2":
                    loggedInUser = auth.signUp();
                    if (loggedInUser != null) {
                        loggedInUser.mainMenu();
                    }
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleStaffFlow() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Staff Menu ---");
            System.out.println("1. Sign In");
            System.out.println("2. Back to Main Menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            User loggedInUser = null;
            switch (choice) {
                case "1":
                    loggedInUser = auth.signIn("staff");
                    if (loggedInUser != null) {
                        loggedInUser.mainMenu();
                    }
                    break;
                case "2":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}