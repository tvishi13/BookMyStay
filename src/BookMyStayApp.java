import java.util.HashMap;

/**
 * RoomInventory class manages room availability using a centralized HashMap.
 * It acts as the single source of truth for all room availability data.
 *
 * @author Softy
 * @version 1.0
 */

class RoomInventory {

    // HashMap to store room type and available count
    private HashMap<String, Integer> inventory;

    // Constructor to initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Register room types with availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Method to get availability of a room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update availability
    public void updateAvailability(String roomType, int newCount) {
        inventory.put(roomType, newCount);
    }

    // Display complete inventory
    public void displayInventory() {
        System.out.println("===== HOTEL ROOM INVENTORY =====");

        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + " : " + inventory.get(roomType) + " rooms available");
        }
    }
}


/**
 * Main Application Class
 * Demonstrates centralized room inventory using HashMap
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize inventory system
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        // Example availability check
        System.out.println("\nChecking availability for Single Room...");
        System.out.println("Available: " + inventory.getAvailability("Single Room"));

        // Update availability
        System.out.println("\nUpdating availability after booking...");
        inventory.updateAvailability("Single Room", 4);

        // Display updated inventory
        System.out.println("\nUpdated Inventory:");
        inventory.displayInventory();

        System.out.println("\nSystem execution completed.");
    }
}