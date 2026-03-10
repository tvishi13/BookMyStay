/**
 * Hotel Booking Application
 * Demonstrates abstraction, inheritance, polymorphism,
 * and static availability of different room types.
 *
 * @author Softy
 * @version 1.0
 */

// Abstract class representing a generic Room
abstract class Room {

    protected int beds;
    protected double size;
    protected double price;

    public Room(int beds, double size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    // Method to display room details
    public void displayRoomDetails() {
        System.out.println("Beds: " + beds);
        System.out.println("Room Size: " + size + " sq.ft");
        System.out.println("Price per night: ₹" + price);
    }

    // Abstract method
    public abstract String getRoomType();
}

// Single Room class
class SingleRoom extends Room {

    public SingleRoom() {
        super(1, 180, 2000);
    }

    public String getRoomType() {
        return "Single Room";
    }
}

// Double Room class
class DoubleRoom extends Room {

    public DoubleRoom() {
        super(2, 250, 3500);
    }

    public String getRoomType() {
        return "Double Room";
    }
}

// Suite Room class
class SuiteRoom extends Room {

    public SuiteRoom() {
        super(3, 450, 7000);
    }

    public String getRoomType() {
        return "Suite Room";
    }
}

// Main Application Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Static availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Creating room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        System.out.println("===== HOTEL ROOM AVAILABILITY =====");

        System.out.println("\nRoom Type: " + single.getRoomType());
        single.displayRoomDetails();
        System.out.println("Available Rooms: " + singleAvailable);

        System.out.println("\nRoom Type: " + doubleRoom.getRoomType());
        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleAvailable);

        System.out.println("\nRoom Type: " + suite.getRoomType());
        suite.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteAvailable);

        System.out.println("\nApplication finished successfully.");
    }
}