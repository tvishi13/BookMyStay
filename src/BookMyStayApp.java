import java.io.*;
import java.util.*;

// Main class
public class BookMyStayApp {

    // ---------------- RESERVATION ----------------
    static class Reservation implements Serializable {
        private static final long serialVersionUID = 1L;

        String reservationId;
        String guestName;
        String roomType;

        public Reservation(String reservationId, String guestName, String roomType) {
            this.reservationId = reservationId;
            this.guestName = guestName;
            this.roomType = roomType;
        }

        @Override
        public String toString() {
            return reservationId + " | " + guestName + " | " + roomType;
        }
    }

    // ---------------- INVENTORY ----------------
    static class RoomInventory implements Serializable {
        private static final long serialVersionUID = 1L;

        Map<String, Integer> inventory = new HashMap<>();

        public RoomInventory() {
            inventory.put("Deluxe", 2);
            inventory.put("Standard", 3);
        }

        public void allocateRoom(String type) {
            inventory.put(type, inventory.get(type) - 1);
        }

        public void printInventory() {
            System.out.println("Inventory: " + inventory);
        }
    }

    // ---------------- SYSTEM STATE ----------------
    static class SystemState implements Serializable {
        private static final long serialVersionUID = 1L;

        List<Reservation> bookings;
        RoomInventory inventory;

        public SystemState(List<Reservation> bookings, RoomInventory inventory) {
            this.bookings = bookings;
            this.inventory = inventory;
        }
    }

    // ---------------- PERSISTENCE SERVICE ----------------
    static class PersistenceService {

        private static final String FILE_NAME = "system_state.ser";

        // Save state
        public void save(SystemState state) {
            try (ObjectOutputStream oos =
                         new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

                oos.writeObject(state);
                System.out.println("✅ State saved successfully.");

            } catch (IOException e) {
                System.out.println("❌ Failed to save state: " + e.getMessage());
            }
        }

        // Load state
        public SystemState load() {
            File file = new File(FILE_NAME);

            if (!file.exists()) {
                System.out.println("⚠ No previous state found. Starting fresh.");
                return null;
            }

            try (ObjectInputStream ois =
                         new ObjectInputStream(new FileInputStream(FILE_NAME))) {

                SystemState state = (SystemState) ois.readObject();
                System.out.println("✅ State restored successfully.");
                return state;

            } catch (Exception e) {
                System.out.println("❌ Corrupted or invalid state. Starting fresh.");
                return null;
            }
        }
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {

        PersistenceService persistenceService = new PersistenceService();

        // Step 1: Try restoring previous state
        SystemState restoredState = persistenceService.load();

        List<Reservation> bookings;
        RoomInventory inventory;

        if (restoredState != null) {
            bookings = restoredState.bookings;
            inventory = restoredState.inventory;
        } else {
            // Fresh system state
            bookings = new ArrayList<>();
            inventory = new RoomInventory();
        }

        // Step 2: Simulate booking
        Reservation r1 = new Reservation("RES1", "Alice", "Deluxe");
        bookings.add(r1);
        inventory.allocateRoom("Deluxe");

        System.out.println("\nCurrent Bookings:");
        for (Reservation r : bookings) {
            System.out.println(r);
        }

        inventory.printInventory();

        // Step 3: Save state before shutdown
        SystemState currentState = new SystemState(bookings, inventory);
        persistenceService.save(currentState);

        System.out.println("\n🔄 Restart the program to see recovery in action.");
    }
}