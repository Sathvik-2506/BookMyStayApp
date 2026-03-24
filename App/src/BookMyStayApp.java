// Version 10.0

import java.util.*;

// ===================== RESERVATION =====================

class Reservation {
    private String reservationId;
    private String roomType;
    private String roomId;
    private boolean isActive;

    public Reservation(String reservationId, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isActive = true;
    }

    public String getReservationId() { return reservationId; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }

    public boolean isActive() { return isActive; }
    public void cancel() { this.isActive = false; }
}

// ===================== INVENTORY =====================

class RoomInventory {

    private Map<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();
        availabilityMap.put("Single Room", 1);
        availabilityMap.put("Double Room", 1);
        availabilityMap.put("Suite Room", 1);
    }

    public void increment(String roomType) {
        availabilityMap.put(roomType, availabilityMap.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\n=== Current Inventory ===");
        for (Map.Entry<String, Integer> e : availabilityMap.entrySet()) {
            System.out.println(e.getKey() + " → " + e.getValue());
        }
    }
}

// ===================== BOOKING HISTORY =====================

class BookingHistory {

    private Map<String, Reservation> reservationMap;

    public BookingHistory() {
        reservationMap = new HashMap<>();
    }

    public void addReservation(Reservation r) {
        reservationMap.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String reservationId) {
        return reservationMap.get(reservationId);
    }
}

// ===================== CANCELLATION SERVICE =====================

class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;

    // Stack for rollback (LIFO)
    private Stack<String> releasedRoomStack;

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        this.releasedRoomStack = new Stack<>();
    }

    public void cancelReservation(String reservationId) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        Reservation r = history.getReservation(reservationId);

        // Validation
        if (r == null) {
            System.out.println("❌ Reservation not found.");
            return;
        }

        if (!r.isActive()) {
            System.out.println("❌ Reservation already cancelled.");
            return;
        }

        // Step 1: Push room ID to stack (rollback tracking)
        releasedRoomStack.push(r.getRoomId());

        // Step 2: Restore inventory
        inventory.increment(r.getRoomType());

        // Step 3: Mark reservation cancelled
        r.cancel();

        System.out.println("✅ Cancellation successful. Room released: " + r.getRoomId());
    }

    // View rollback stack
    public void displayRollbackStack() {
        System.out.println("\n=== Rollback Stack (LIFO) ===");
        for (String id : releasedRoomStack) {
            System.out.println(id);
        }
    }
}

// ===================== MAIN APPLICATION =====================

public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from Use Case 6)
        Reservation r1 = new Reservation("RES101", "Single Room", "Single_Room_1");
        Reservation r2 = new Reservation("RES102", "Double Room", "Double_Room_2");

        history.addReservation(r1);
        history.addReservation(r2);

        // Initialize cancellation service
        CancellationService service = new CancellationService(inventory, history);

        // Perform cancellations
        service.cancelReservation("RES101"); // valid
        service.cancelReservation("RES101"); // duplicate cancel
        service.cancelReservation("RES999"); // invalid ID

        // Display rollback stack
        service.displayRollbackStack();

        // Display updated inventory
        inventory.displayInventory();
    }
}