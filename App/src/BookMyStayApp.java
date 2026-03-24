// Version 4.0

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

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getRoomType();

    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + size + " sq.ft");
        System.out.println("Price: ₹" + price);
    }
}

// Concrete Rooms
class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 200, 2000);
    }

    public String getRoomType() {
        return "Single Room";
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 350, 3500);
    }

    public String getRoomType() {
        return "Double Room";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 600, 7000);
    }

    public String getRoomType() {
        return "Suite Room";
    }
}

// ===================== INVENTORY =====================

class RoomInventory {

    private Map<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();
        availabilityMap.put("Single Room", 5);
        availabilityMap.put("Double Room", 0); // Example: unavailable
        availabilityMap.put("Suite Room", 2);
    }

    // Read-only access
    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }

    public Set<String> getAllRoomTypes() {
        return availabilityMap.keySet();
    }
}

// ===================== SEARCH SERVICE =====================

class RoomSearchService {

    private RoomInventory inventory;
    private Map<String, Room> roomCatalog;

    public RoomSearchService(RoomInventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    // Read-only search
    public void searchAvailableRooms() {

        System.out.println("=== Available Rooms ===\n");

        for (String roomType : inventory.getAllRoomTypes()) {

            int available = inventory.getAvailability(roomType);

            // Validation: only show available rooms
            if (available > 0) {
                Room room = roomCatalog.get(roomType);

                if (room != null) { // Defensive check
                    room.displayDetails();
                    System.out.println("Available: " + available);
                    System.out.println("-----------------------------");
                }
            }
        }
    }
}

// ===================== MAIN APPLICATION =====================

public class UseCase4RoomSearch {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize room catalog (domain data)
        Map<String, Room> roomCatalog = new HashMap<>();
        roomCatalog.put("Single Room", new SingleRoom());
        roomCatalog.put("Double Room", new DoubleRoom());
        roomCatalog.put("Suite Room", new SuiteRoom());

        // Initialize search service
        RoomSearchService searchService = new RoomSearchService(inventory, roomCatalog);

        // Guest performs search
        searchService.searchAvailableRooms();
    }
}