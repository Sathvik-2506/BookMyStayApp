// Version 7.0

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
}

// ===================== ADD-ON SERVICE =====================

class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() { return serviceName; }
    public double getCost() { return cost; }

    public void displayService() {
        System.out.println(serviceName + " → ₹" + cost);
    }
}

// ===================== ADD-ON SERVICE MANAGER =====================

class AddOnServiceManager {

    // Map: Reservation ID → List of Services
    private Map<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {

        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Added service: " + service.getServiceName() +
                " to Reservation: " + reservationId);
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {

        System.out.println("\n=== Services for Reservation: " + reservationId + " ===");

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        for (AddOnService s : services) {
            s.displayService();
        }
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null) return 0;

        double total = 0;
        for (AddOnService s : services) {
            total += s.getCost();
        }

        return total;
    }
}

// ===================== MAIN APPLICATION =====================

public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        // Existing reservation (from Use Case 6)
        Reservation r1 = new Reservation("RES101", "Alice", "Single Room");

        // Initialize service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Guest selects services
        manager.addService(r1.getReservationId(), new AddOnService("Breakfast", 500));
        manager.addService(r1.getReservationId(), new AddOnService("Airport Pickup", 1200));
        manager.addService(r1.getReservationId(), new AddOnService("Extra Bed", 800));

        // Display selected services
        manager.displayServices(r1.getReservationId());

        // Calculate total add-on cost
        double totalCost = manager.calculateTotalCost(r1.getReservationId());

        System.out.println("\nTotal Add-On Cost: ₹" + totalCost);
    }
}