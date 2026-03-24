// Version 5.0

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

// ===================== RESERVATION =====================

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayRequest() {
        System.out.println("Guest: " + guestName + " | Requested: " + roomType);
    }
}

// ===================== BOOKING QUEUE =====================

class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests (without removing)
    public void displayQueue() {
        System.out.println("\n=== Booking Request Queue (FIFO Order) ===\n");

        if (requestQueue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : requestQueue) {
            r.displayRequest();
        }

        System.out.println("------------------------------------------");
    }

    // Get next request (for future processing, not used yet)
    public Reservation peekNextRequest() {
        return requestQueue.peek();
    }
}

// ===================== MAIN APPLICATION =====================

public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        // Initialize queue
        BookingRequestQueue queue = new BookingRequestQueue();

        // Guests submit booking requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Suite Room"));
        queue.addRequest(new Reservation("Charlie", "Double Room"));
        queue.addRequest(new Reservation("David", "Single Room"));

        // Display queue (FIFO order preserved)
        queue.displayQueue();

        // Peek next request (no removal, no allocation)
        System.out.println("\nNext request to be processed:");
        Reservation next = queue.peekNextRequest();

        if (next != null) {
            next.displayRequest();
        }
    }
}