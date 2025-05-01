import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Report {
    private List<Reservation> reservations;
    private Map<String, Integer> rooms;
    private List<Map<String, String>> housekeepingSchedule;
    
    public Report(List<Reservation> reservations, Map<String, Integer> rooms, List<Map<String, String>> housekeepingSchedule) {
        this.reservations = reservations;
        this.rooms = rooms;
        this.housekeepingSchedule = housekeepingSchedule;
    }
    
    private String createTableHeader(String[] headers) {
        StringBuilder header = new StringBuilder();
        // Calculate total width needed
        int totalWidth = 60; // Fixed width to match Authentication style
        
        // Create header row
        header.append("\n                  === ").append(headers[0]).append(" ===\n\n");
        header.append("╔════════════════════════════════════════════════════════════════╗\n");
        
        // Add header text
        for (String headerText : headers) {
            header.append("║").append(" ".repeat((totalWidth - headerText.length()) / 2))
                  .append(headerText)
                  .append(" ".repeat(totalWidth - headerText.length() - ((totalWidth - headerText.length()) / 2)))
                  .append("║\n");
        }
        
        header.append("╠════════════════════════════════════════════════════════════════╣\n");
        
        return header.toString();
    }

    private String createTableRow(String[] values, int[] widths) {
        StringBuilder row = new StringBuilder("║");
        int totalWidth = 60; // Fixed width to match Authentication style
        
        // Format each value with proper spacing
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            int padding = (totalWidth - value.length()) / 2;
            row.append(" ".repeat(padding))
               .append(value)
               .append(" ".repeat(totalWidth - value.length() - padding))
               .append("║\n");
        }
        
        return row.toString();
    }

    private String createTableFooter() {
        return "╚════════════════════════════════════════════════════════════════╝\n";
    }
    
    private String padRight(String text, int length) {
        return String.format("%-" + length + "s", text);
    }
    
    public void generateTotalSalesReport(Date startDate, Date endDate) {
        double totalRevenue = 0.0;
        int totalBookings = 0;
        Map<String, Integer> bookingsByRoomType = new HashMap<>();
        Map<String, Double> revenueByRoomType = new HashMap<>();
        
        // Initialize room type counters
        for (String roomType : rooms.keySet()) {
            bookingsByRoomType.put(roomType, 0);
            revenueByRoomType.put(roomType, 0.0);
        }
        
        // Calculate totals
        for (Reservation reservation : reservations) {
            // Check if the reservation's check-out date falls within the report period
            // and the reservation is in "Checked-Out" status
            if (reservation.getStatus().equals("Checked-Out") && 
                !reservation.getCheckOut().before(startDate) && 
                !reservation.getCheckOut().after(endDate)) {
                
                double cost = reservation.calculateTotalCost();
                totalRevenue += cost;
                totalBookings++;
                
                String roomType = reservation.getRoomType();
                bookingsByRoomType.put(roomType, bookingsByRoomType.get(roomType) + 1);
                revenueByRoomType.put(roomType, revenueByRoomType.get(roomType) + cost);
            }
        }
        
        // Format the report
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    SALES REPORT SUMMARY                        ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                ║");
        System.out.println("║  REPORT PERIOD:                                                ║");
        System.out.println("║    From: " + padRight(dateFormat.format(startDate), 50) + "    ║");
        System.out.println("║    To: " + padRight(dateFormat.format(endDate), 53) + "   ║");
        System.out.println("║                                                                ║");
        System.out.println("║  SUMMARY:                                                      ║");
        System.out.println("║    Total Revenue: $" + padRight(String.format("%.2f", totalRevenue), 43) + " ║");
        System.out.println("║    Total Bookings: " + padRight(String.valueOf(totalBookings), 42) + "  ║");
        System.out.println("║                                                                ║");
        System.out.println("║  ROOM TYPE BREAKDOWN:                                          ║");
        
        for (String roomType : rooms.keySet()) {
            System.out.println("║    " + padRight(roomType + " Rooms:", 52) + "        ║");
            System.out.println("║    - Bookings: " + padRight(String.valueOf(bookingsByRoomType.get(roomType)), 46) + "  ║");
            System.out.println("║    - Revenue: $" + padRight(String.format("%.2f", revenueByRoomType.get(roomType)), 45) + "   ║");
        }
        
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
    }
    
    /**
     * Generates an operational report showing room occupancy and housekeeping status
     * @return A formatted string containing the operational report
     */
    public void generateOperationalReport() {
        Map<String, Integer> occupiedRooms = new HashMap<>();
        Map<String, Integer> availableRooms = new HashMap<>();
        
        for (String roomType : rooms.keySet()) {
            occupiedRooms.put(roomType, 0);
            availableRooms.put(roomType, rooms.get(roomType));
        }
        
        for (Reservation reservation : reservations) {
            if (reservation.getStatus().equals("Checked-In")) {
                String roomType = reservation.getRoomType();
                occupiedRooms.put(roomType, occupiedRooms.get(roomType) + 1);
                availableRooms.put(roomType, availableRooms.get(roomType) - 1);
            }
        }
        
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    OPERATIONAL REPORT                          ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                ║");
        System.out.println("║  DATE: " + padRight(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), 53) + "   ║");
        System.out.println("║                                                                ║");
        System.out.println("║  ROOM STATUS:                                                  ║");
        
        for (String roomType : rooms.keySet()) {
            int total = rooms.get(roomType);
            int occupied = occupiedRooms.get(roomType);
            int available = availableRooms.get(roomType);
            
            System.out.println("║    " + padRight(roomType + " Rooms:", 52) + "        ║");
            System.out.println("║    - Total: " + padRight(String.valueOf(total), 49) + "  ║");
            System.out.println("║    - Occupied: " + padRight(String.valueOf(occupied), 47) + " ║");
            System.out.println("║    - Available: " + padRight(String.valueOf(available), 47) + "║");
        }
        
        if (housekeepingSchedule != null && !housekeepingSchedule.isEmpty()) {
            System.out.println("║                                                                ║");
            System.out.println("║  HOUSEKEEPING STATUS:                                        ║");
            for (Map<String, String> task : housekeepingSchedule) {
                System.out.println("║    Room " + padRight(task.get("roomNumber") + ":", 51) + "║");
                System.out.println("║    - Status: " + padRight(task.get("status"), 48) + "║");
                System.out.println("║    - Last Updated: " + padRight(task.get("lastUpdated"), 46) + "║");
            }
        }
        
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
    }
    
    /**
     * Generates a guest and booking report
     * @return A formatted string containing the guest and booking report
     */
    public void generateGuestAndBookingReport() {
        Map<String, List<Reservation>> reservationsByStatus = new HashMap<>();
        reservationsByStatus.put("Reserved", new ArrayList<>());
        reservationsByStatus.put("Checked-In", new ArrayList<>());
        reservationsByStatus.put("Checked-Out", new ArrayList<>());
        reservationsByStatus.put("Cancelled", new ArrayList<>());
        
        Map<String, List<Reservation>> reservationsByGuest = new HashMap<>();
        
        for (Reservation reservation : reservations) {
            String status = reservation.getStatus();
            reservationsByStatus.get(status).add(reservation);
            
            String guestName = reservation.getGuestName();
            reservationsByGuest.computeIfAbsent(guestName, k -> new ArrayList<>()).add(reservation);
        }
        
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    BOOKING STATISTICS                          ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                ║");
        
        for (String status : reservationsByStatus.keySet()) {
            String statusLine = status + ": " + reservationsByStatus.get(status).size() + " bookings";
            System.out.println("║    " + padRight(statusLine, 56) + "    ║");
        }
        
        System.out.println("║                                                                ║");
        System.out.println("║  TOP GUESTS BY BOOKINGS:                                       ║");
        
        List<Map.Entry<String, List<Reservation>>> sortedGuests = new ArrayList<>(reservationsByGuest.entrySet());
        sortedGuests.sort((e1, e2) -> e2.getValue().size() - e1.getValue().size());
        
        int topCount = Math.min(5, sortedGuests.size());
        for (int i = 0; i < topCount; i++) {
            Map.Entry<String, List<Reservation>> entry = sortedGuests.get(i);
            String guestInfo = (i + 1) + ". " + entry.getKey() + ": " + entry.getValue().size() + " bookings";
            System.out.println("║    " + padRight(guestInfo, 56) + "    ║");
        }
        
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
    }
    
    /**
     * Displays a menu to check different types of reports
     */
    public void checkReport() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n=== REPORT MENU ===");
            System.out.println("1. View Total Sales Report");
            System.out.println("2. View Operational Report");
            System.out.println("3. View Guest and Booking Report");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1 -> {
                        // Get date range for sales report
                        System.out.print("Enter start date (yyyy-MM-dd): ");
                        String startDateStr = scanner.nextLine();
                        System.out.print("Enter end date (yyyy-MM-dd): ");
                        String endDateStr = scanner.nextLine();
                        
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date startDate = dateFormat.parse(startDateStr);
                            Date endDate = dateFormat.parse(endDateStr);
                            
                            generateTotalSalesReport(startDate, endDate);
                        } catch (ParseException e) {
                            System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
                        }
                    }
                    case 2 -> generateOperationalReport();
                    case 3 -> generateGuestAndBookingReport();
                    case 4 -> exit = true;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number between 1-4");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }
}