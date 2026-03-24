// Version 6.0

import java.util.*;

// ===================== DOMAIN MODEL =====================

abstract class Room {
    private int numberOfBeds;
    private double size;
    private double price;

    public Room(int numberOfBeds, double size, double price) {
        this.numberOfBeds = numberOfBeds;
        this.size = size;
        this.price = price;
    }

    public abstract String getRoomType();
}

// Concrete Rooms
class SingleRoom extends Room {
    public SingleRoom() { super(1, 200, 2000); }
    public String getRoomType() { return "Single Room"; }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super(2, 350, 3500); }
    public String getRoomType() { return "Double Room"; }
}

class SuiteRoom extends Room {
    public SuiteRoom() { super(3, 600, 7000); }
    public String getRoomType() { return "Suite Room"; }
}

// ===================== RESERVATION =====================

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// ===================== INVENTORY SERVICE =====================

class RoomInventory {

    private Map<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();
        availabilityMap.put("Single Room", 2);
        availabilityMap.put("Double Room", 1);
        availabilityMap.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }

    // Decrement inventory (atomic update)
    public void decrement(String roomType) {
        int current = getAvailability(roomType);
        if (current > 0) {
            availabilityMap.put(roomType, current - 1);
        }
    }

    public void displayInventory() {
        System.out.println("\n=== Current Inventory ===");
        for (Map.Entry<String, Integer> e : availabilityMap.entrySet()) {
            System.out.println(e.getKey() + " → " + e.getValue());
        }
    }
}

// ===================== BOOKING QUEUE =====================

class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ===================== BOOKING SERVICE =====================

class BookingService {

    private RoomInventory inventory;

    // Track allocated rooms (uniqueness)
    private Set<String> allocatedRoomIds;

    // Map roomType → allocated room IDs
    private Map<String, Set<String>> allocationMap;

    // Counter for ID generation
    private int idCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRoomIds = new HashSet<>();
        this.allocationMap = new HashMap<>();
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        return roomType.replace(" ", "_") + "_" + (idCounter++);
    }

    // Process queue
    public void processRequests(BookingRequestQueue queue) {

        System.out.println("\n=== Processing Booking Requests ===\n");

        while (!queue.isEmpty()) {

            Reservation r = queue.getNextRequest();
            String roomType = r.getRoomType();

            System.out.println("Processing: " + r.getGuestName() + " → " + roomType);

            // Check availability
            if (inventory.getAvailability(roomType) > 0) {

                // Generate unique ID
                String roomId = generateRoomId(roomType);

                // Ensure uniqueness (extra safety)
                if (!allocatedRoomIds.contains(roomId)) {

                    // Add to global set
                    allocatedRoomIds.add(roomId);

                    // Add to type-wise map
                    allocationMap
                            .computeIfAbsent(roomType, k -> new HashSet<>())
                            .add(roomId);

                    // Decrement inventory (synchronized update)
                    inventory.decrement(roomType);

                    // Confirm reservation
                    System.out.println("✅ Confirmed | Room ID: " + roomId);

                } else {
                    System.out.println("❌ Duplicate Room ID detected!");
                }

            } else {
                System.out.println("❌ No availability. Booking failed.");
            }

            System.out.println("----------------------------------");
        }
    }

    // Display allocation state
    public void displayAllocations() {
        System.out.println("\n=== Allocated Rooms ===");

        for (Map.Entry<String, Set<String>> entry : allocationMap.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

// ===================== MAIN APPLICATION =====================

public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize queue
        BookingRequestQueue queue = new BookingRequestQueue();

        // Add booking requests (FIFO)
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("David", "Suite Room"));

        // Initialize booking service
        BookingService service = new BookingService(inventory);

        // Process requests
        service.processRequests(queue);

        // Show results
        service.displayAllocations();
        inventory.displayInventory();
    }
}