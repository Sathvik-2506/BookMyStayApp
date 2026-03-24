// Version 8.0

import java.util.*;

// ===================== RESERVATION =====================

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// ===================== BOOKING HISTORY =====================

class BookingHistory {

    // Ordered storage (chronological)
    private List<Reservation> historyList;

    public BookingHistory() {
        historyList = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        historyList.add(reservation);
        System.out.println("Stored reservation: " + reservation.getReservationId());
    }

    // Get all reservations (read-only)
    public List<Reservation> getAllReservations() {
        return historyList;
    }
}

// ===================== REPORT SERVICE =====================

class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Display full booking history
    public void displayAllBookings() {

        System.out.println("\n=== Booking History ===\n");

        List<Reservation> list = history.getAllReservations();

        if (list.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : list) {
            r.display();
        }
    }

    // Generate summary report
    public void generateSummaryReport() {

        System.out.println("\n=== Booking Summary Report ===\n");

        Map<String, Integer> countByRoomType = new HashMap<>();

        for (Reservation r : history.getAllReservations()) {
            countByRoomType.put(
                    r.getRoomType(),
                    countByRoomType.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        for (Map.Entry<String, Integer> entry : countByRoomType.entrySet()) {
            System.out.println(entry.getKey() + " → Total Bookings: " + entry.getValue());
        }
    }
}

// ===================== MAIN APPLICATION =====================

public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from Use Case 6)
        history.addReservation(new Reservation("RES101", "Alice", "Single Room"));
        history.addReservation(new Reservation("RES102", "Bob", "Suite Room"));
        history.addReservation(new Reservation("RES103", "Charlie", "Single Room"));
        history.addReservation(new Reservation("RES104", "David", "Double Room"));

        // Initialize reporting service
        BookingReportService reportService = new BookingReportService(history);

        // Admin views full history
        reportService.displayAllBookings();

        // Admin generates summary report
        reportService.generateSummaryReport();
    }
}