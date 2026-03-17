import java.util.*;

// Main class
public class BookMyStayApp {

    // ---------------- BOOKING REQUEST ----------------
    static class BookingRequest {
        String guestName;
        String roomType;

        public BookingRequest(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    // ---------------- THREAD-SAFE INVENTORY ----------------
    static class RoomInventory {

        private Map<String, Integer> inventory = new HashMap<>();

        public RoomInventory() {
            inventory.put("Deluxe", 2); // only 2 rooms
        }

        // Critical section: synchronized method
        public synchronized boolean allocateRoom(String roomType) {
            int available = inventory.getOrDefault(roomType, 0);

            if (available > 0) {
                System.out.println(Thread.currentThread().getName() +
                        " allocating room. Available before: " + available);

                inventory.put(roomType, available - 1);

                System.out.println(Thread.currentThread().getName() +
                        " SUCCESS. Remaining: " + (available - 1));
                return true;
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " FAILED. No rooms left.");
                return false;
            }
        }

        public int getAvailable(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }
    }

    // ---------------- BOOKING PROCESSOR (THREAD) ----------------
    static class BookingProcessor implements Runnable {

        private Queue<BookingRequest> queue;
        private RoomInventory inventory;

        public BookingProcessor(Queue<BookingRequest> queue, RoomInventory inventory) {
            this.queue = queue;
            this.inventory = inventory;
        }

        @Override
        public void run() {
            while (true) {
                BookingRequest request;

                // Synchronize queue access
                synchronized (queue) {
                    if (queue.isEmpty()) {
                        break;
                    }
                    request = queue.poll();
                }

                // Process booking
                inventory.allocateRoom(request.roomType);

                // Simulate delay (to increase race condition chance if unsynchronized)
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        // Shared booking queue
        Queue<BookingRequest> bookingQueue = new LinkedList<>();

        // Simulate multiple guests (more requests than rooms)
        for (int i = 1; i <= 5; i++) {
            bookingQueue.add(new BookingRequest("Guest" + i, "Deluxe"));
        }

        // Create multiple threads
        Thread t1 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-2");
        Thread t3 = new Thread(new BookingProcessor(bookingQueue, inventory), "Thread-3");

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Final state
        System.out.println("\nFinal Available Rooms: " + inventory.getAvailable("Deluxe"));
    }
}