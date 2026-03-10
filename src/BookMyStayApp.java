import java.util.*;

/**
 * Reservation class representing a guest booking request
 */
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
}

/**
 * RoomInventory maintains available room counts
 */
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decreaseRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

/**
 * BookingRequestQueue stores booking requests using FIFO
 */
class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean hasRequests() {
        return !queue.isEmpty();
    }
}

/**
 * BookingService processes reservations and allocates rooms
 */
class BookingService {

    private HashMap<String, Set<String>> allocatedRooms = new HashMap<>();

    public void processBookings(BookingRequestQueue requestQueue, RoomInventory inventory) {

        while (requestQueue.hasRequests()) {

            Reservation request = requestQueue.getNextRequest();
            String roomType = request.getRoomType();

            System.out.println("\nProcessing booking for " + request.getGuestName());

            int available = inventory.getAvailability(roomType);

            if (available > 0) {

                // Generate unique room ID
                String roomID = roomType.replace(" ", "").substring(0,2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0,4);

                allocatedRooms.putIfAbsent(roomType, new HashSet<>());
                allocatedRooms.get(roomType).add(roomID);

                // Decrease inventory immediately
                inventory.decreaseRoom(roomType);

                System.out.println("Reservation Confirmed!");
                System.out.println("Guest: " + request.getGuestName());
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomID);

            } else {

                System.out.println("Reservation Failed - No rooms available for " + roomType);
            }
        }
    }
}

/**
 * Main Application
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        BookingRequestQueue queue = new BookingRequestQueue();

        // Booking requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Double Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room"));
        queue.addRequest(new Reservation("David", "Suite Room"));

        BookingService service = new BookingService();

        service.processBookings(queue, inventory);

        inventory.displayInventory();
    }
}