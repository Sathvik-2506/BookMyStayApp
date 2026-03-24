abstract class Room {
    private int numberOfBeds;
    private double size;
    private double price;

    // Constructor
    public Room(int numberOfBeds, double size, double price) {
        this.numberOfBeds = numberOfBeds;
        this.size = size;
        this.price = price;
    }

    // Getters (Encapsulation)
    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    // Abstract method (forces subclasses to define)
    public abstract String getRoomType();

    // Common method
    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + size + " sq.ft");
        System.out.println("Price: ₹" + price);
    }
}

// Concrete Class - Single Room
class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 200, 2000);
    }

    @Override
    public String getRoomType() {
        return "Single Room";
    }
}

// Concrete Class - Double Room
class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 350, 3500);
    }

    @Override
    public String getRoomType() {
        return "Double Room";
    }
}

// Concrete Class - Suite Room
class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 600, 7000);
    }

    @Override
    public String getRoomType() {
        return "Suite Room";
    }
}

// Main Class
public class UseCase2RoomInitialization {

    public static void main(String[] args) {

        // Creating Room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability (simple variables)
        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 2;

        // Display details
        System.out.println("=== Hotel Room Availability ===\n");

        single.displayDetails();
        System.out.println("Available: " + singleAvailability);
        System.out.println("-----------------------------");

        doubleRoom.displayDetails();
        System.out.println("Available: " + doubleAvailability);
        System.out.println("-----------------------------");

        suite.displayDetails();
        System.out.println("Available: " + suiteAvailability);
        System.out.println("-----------------------------");
    }
}