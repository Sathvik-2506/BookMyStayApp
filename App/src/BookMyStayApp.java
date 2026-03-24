// Version 9.0

import java.util.*;

// ===================== CUSTOM EXCEPTION =====================

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
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

// ===================== INVENTORY =====================

class RoomInventory {

    private Map<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();
        availabilityMap.put("Single Room", 2);
        availabilityMap.put("Double Room", 1);
        availabilityMap.put("Suite Room", 0); // No availability
    }

    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, -1);
    }

    public boolean isValidRoomType(String roomType) {
        return availabilityMap.containsKey(roomType);
    }

    public void decrement(String roomType) throws InvalidBookingException {
        int current = getAvailability(roomType);

        if (current <= 0) {
            throw new InvalidBookingException("No availability for " + roomType);
        }

        availabilityMap.put(roomType, current - 1);
    }
}

// ===================== VALIDATOR =====================

class BookingValidator {

    public static void validate(Reservation r, RoomInventory inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (r.getGuestName() == null || r.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type exists
        if (!inventory.isValidRoomType(r.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + r.getRoomType());
        }

        // Validate availability
        if (inventory.getAvailability(r.getRoomType()) <= 0) {
            throw new InvalidBookingException("Room not available: " + r.getRoomType());
        }
    }
}

// ===================== BOOKING SERVICE =====================

class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation r) {

        try {
            // Step 1: Validate (Fail-Fast)
            BookingValidator.validate(r, inventory);

            // Step 2: Allocate (only if valid)
            inventory.decrement(r.getRoomType());

            System.out.println("✅ Booking confirmed for " + r.getGuestName() +
                    " (" + r.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("❌ Booking failed: " + e.getMessage());
        }
    }
}

// ===================== MAIN APPLICATION =====================

public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        // Test cases (valid + invalid)

        Reservation r1 = new Reservation("Alice", "Single Room");   // valid
        Reservation r2 = new Reservation("", "Double Room");        // invalid name
        Reservation r3 = new Reservation("Bob", "Luxury Room");     // invalid type
        Reservation r4 = new Reservation("Charlie", "Suite Room");  // no availability

        service.processBooking(r1);
        service.processBooking(r2);
        service.processBooking(r3);
        service.processBooking(r4);
    }
}