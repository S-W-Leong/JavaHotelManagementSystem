import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Manager extends Staff {
    private List<Map<String, String>> housekeepingSchedule;
    private List<Reservation> reservations;
    private Map<String, Integer> rooms;
    private Report report;

    public Manager(String username, String password, String role, List<Map<String, String>> housekeepingSchedule) {
        super(username, password, role);
        this.housekeepingSchedule = (housekeepingSchedule != null) ? housekeepingSchedule : new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.rooms = new HashMap<>();
        // Initialize some default room types
        this.rooms.put("Single", 10);
        this.rooms.put("Double", 15);
        this.rooms.put("Suite", 5);
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void setRooms(Map<String, Integer> rooms) {
        this.rooms = rooms;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    @Override
    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Manager Menu ---");
            System.out.println("1. Manage Housekeeping Schedule");
            System.out.println("2. View Housekeeping Schedule");
            System.out.println("3. View Staff");
            System.out.println("4. Generate Reports");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    manageHousekeepingSchedule();
                    break;
                case "2":
                    viewHousekeepingSchedule();
                    break;
                case "3":
                    viewStaff();
                    break;
                case "4":
                    generateReports();
                    break;
                case "5":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void manageHousekeepingSchedule() {
        System.out.println("\n--- Manage Housekeeping Schedule ---");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter room number to add task: ");
        String roomNumber = scanner.nextLine();
        if (!roomNumber.isEmpty()) {
            System.out.print("Enter task name: ");
            String taskName = scanner.nextLine();
            Map<String, String> newTask = new HashMap<>();
            newTask.put("room_number", roomNumber);
            newTask.put("task_name", taskName);
            housekeepingSchedule.add(newTask);
            System.out.println("Housekeeping schedule updated!");
        }
        System.out.println("Current Schedule:");
        if (housekeepingSchedule == null || housekeepingSchedule.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            for (Map<String, String> task : housekeepingSchedule) {
            System.out.printf("Room: %s, \tTask: %s\n", task.get("room_number"), task.get("task_name"));
            }
        }
    }

    private void viewHousekeepingSchedule() {
        System.out.println("\n--- Housekeeping Schedule ---");
        if (housekeepingSchedule.isEmpty()) {
            System.out.println("No housekeeping tasks scheduled.");
            return;
        }
        for (Map<String, String> task : housekeepingSchedule) {
            System.out.printf("Room: %s, \tTask: %s\n", task.get(task.get("room_number")), task.get("task_name"));
            }
    }  

    private void viewStaff() {
        System.out.println("\n--- Staff ---");
        // Hardcoded staff list for simplicity
        System.out.println("manager1: Manager");
        System.out.println("staff1: Housekeeping");
    }

    private void generateReports() {
        if (report == null) {
            // Create a report instance if not already set
            report = new Report(reservations, rooms, housekeepingSchedule);
        }
        
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Get date range from manager
            System.out.print("Enter start date (yyyy-MM-dd): ");
            String startDateStr = scanner.nextLine();
            System.out.print("Enter end date (yyyy-MM-dd): ");
            String endDateStr = scanner.nextLine();

            // Parse dates
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);

            // Generate reports directly
            report.generateTotalSalesReport(startDate, endDate);
            report.generateOperationalReport();
            report.generateGuestAndBookingReport();
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
        }
    }
} 