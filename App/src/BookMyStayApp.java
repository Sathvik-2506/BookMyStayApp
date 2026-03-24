// Version 11.0

import java.util.*;

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

// ===================== SHARED INVENTORY =====================

class RoomInventory {

    private Map<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();
        availabilityMap.put("Single Room", 2); // limited to show concurrency
    }

    // Thread-safe allocation (critical section)
    public synchronized boolean allocateRoom(String roomType) {

        int available = availabilityMap.getOrDefault(roomType, 0);

        if (available > 0) {
            // Simulate delay (to expose race condition if unsynchronized)
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            availabilityMap.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\n=== Final Inventory ===");
        for (Map.Entry<String, Integer> e : availabilityMap.entrySet()) {
            System.out.println(e.getKey() + " → " + e.getValue());
        }
    }
}

// ===================== SHARED QUEUE =====================

class BookingQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    // Thread-safe add
    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    // Thread-safe retrieval
    public synchronized Reservation getNextRequest() {
        return queue.poll();
    }
}

// ===================== BOOKING PROCESSOR (THREAD) =====================

class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory, String name) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {

            Reservation r;

            // Critical section: fetch request
            synchronized (queue) {
                r = queue.getNextRequest();
            }

            if (r == null) break;

            // Critical section: allocate room
            boolean success = inventory.allocateRoom(r.getRoomType());

            if (success) {
                System.out.println(getName() + " → ✅ Booked for " + r.getGuestName());
            } else {
                System.out.println(getName() + " → ❌ Failed for " + r.getGuestName());
            }
        }
    }
}

// ===================== MAIN APPLICATION =====================

public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        // Shared resources
        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Simulate multiple guest requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room"));
        queue.addRequest(new Reservation("David", "Single Room"));

        // Create multiple threads (simulating concurrent users)
        Thread t1 = new BookingProcessor(queue, inventory, "Thread-1");
        Thread t2 = new BookingProcessor(queue, inventory, "Thread-2");

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {}

        // Final inventory state
        inventory.displayInventory();
    }
}