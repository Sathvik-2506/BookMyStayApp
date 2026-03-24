// Version 12.0

import java.io.*;
import java.util.*;

// ===================== RESERVATION =====================

class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

// ===================== INVENTORY =====================

class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();
        availabilityMap.put("Single Room", 2);
        availabilityMap.put("Double Room", 1);
        availabilityMap.put("Suite Room", 1);
    }

    public Map<String, Integer> getAvailabilityMap() {
        return availabilityMap;
    }

    public void displayInventory() {
        System.out.println("\n=== Inventory ===");
        for (Map.Entry<String, Integer> e : availabilityMap.entrySet()) {
            System.out.println(e.getKey() + " → " + e.getValue());
        }
    }
}

// ===================== SYSTEM STATE =====================

class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> bookingHistory;
    private RoomInventory inventory;

    public SystemState(List<Reservation> bookingHistory, RoomInventory inventory) {
        this.bookingHistory = bookingHistory;
        this.inventory = inventory;
    }

    public List<Reservation> getBookingHistory() { return bookingHistory; }
    public RoomInventory getInventory() { return inventory; }
}

// ===================== PERSISTENCE SERVICE =====================

class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state (Serialization)
    public void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("✅ System state saved successfully.");

        } catch (IOException e) {
            System.out.println("❌ Error saving state: " + e.getMessage());
        }
    }

    // Load state (Deserialization)
    public SystemState load() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("⚠ No saved state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("✅ System state loaded successfully.");
            return state;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Error loading state. Starting fresh.");
            return null;
        }
    }
}

// ===================== MAIN APPLICATION =====================

public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        PersistenceService persistence = new PersistenceService();

        // Try loading existing state
        SystemState state = persistence.load();

        List<Reservation> history;
        RoomInventory inventory;

        if (state == null) {
            // Fresh start
            history = new ArrayList<>();
            inventory = new RoomInventory();

            // Simulate bookings
            history.add(new Reservation("RES101", "Alice", "Single Room"));
            history.add(new Reservation("RES102", "Bob", "Suite Room"));

        } else {
            // Restore previous state
            history = state.getBookingHistory();
            inventory = state.getInventory();
        }

        // Display recovered data
        System.out.println("\n=== Booking History ===");
        for (Reservation r : history) {
            r.display();
        }

        inventory.displayInventory();

        // Save state before shutdown
        SystemState newState = new SystemState(history, inventory);
        persistence.save(newState);
    }
}