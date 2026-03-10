import java.util.HashMap;

/**
 * Abstract Room class representing common room properties.
 */
abstract class Room {

    protected int beds;
    protected double price;
    protected String amenities;

    public Room(int beds, double price, String amenities) {
        this.beds = beds;
        this.price = price;
        this.amenities = amenities;
    }

    public abstract String getRoomType();

    public void displayDetails() {
        System.out.println("Beds: " + beds);
        System.out.println("Price per night: ₹" + price);
        System.out.println("Amenities: " + amenities);
    }
}

/**
 * Single Room implementation
 */
class SingleRoom extends Room {

    public SingleRoom() {
        super(1, 2000, "WiFi, TV, Air Conditioning");
    }

    public String getRoomType() {
        return "Single Room";
    }
}

/**
 * Double Room implementation
 */
class DoubleRoom extends Room {

    public DoubleRoom() {
        super(2, 3500, "WiFi, TV, Air Conditioning, Mini Fridge");
    }

    public String getRoomType() {
        return "Double Room";
    }
}

/**
 * Suite Room implementation
 */
class SuiteRoom extends Room {

    public SuiteRoom() {
        super(3, 7000, "WiFi, TV, AC, Living Area, Mini Bar");
    }

    public String getRoomType() {
        return "Suite Room";
    }
}

/**
 * Centralized Room Inventory using HashMap
 */
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 0);
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

/**
 * Search Service that performs read-only search operations.
 */
class SearchService {

    public void searchAvailableRooms(RoomInventory inventory, Room[] rooms) {

        System.out.println("===== AVAILABLE ROOMS =====");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getRoomType());

            // Defensive check: only display rooms with availability
            if (available > 0) {

                System.out.println("\nRoom Type: " + room.getRoomType());
                room.displayDetails();
                System.out.println("Available Rooms: " + available);

            }
        }
    }
}

/**
 * Main Application Class
 */
public class HotelBookingApp {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Create room objects
        Room[] rooms = {
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        };

        // Initialize search service
        SearchService searchService = new SearchService();

        // Guest searches for available rooms
        searchService.searchAvailableRooms(inventory, rooms);

        System.out.println("\nSearch completed. Inventory state unchanged.");
    }
}