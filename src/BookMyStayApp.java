import java.util.*;

// Main class
public class BookMyStayApp {

    // ---------------- CUSTOM EXCEPTION ----------------
    static class CancellationException extends Exception {
        public CancellationException(String message) {
            super(message);
        }
    }

    // ---------------- RESERVATION ----------------
    static class Reservation {
        String reservationId;
        String guestName;
        String roomType;
        String roomId;
        boolean isCancelled;

        public Reservation(String reservationId, String guestName, String roomType, String roomId) {
            this.reservationId = reservationId;
            this.guestName = guestName;
            this.roomType = roomType;
            this.roomId = roomId;
            this.isCancelled = false;
        }

        @Override
        public String toString() {
            return "Reservation ID: " + reservationId +
                    ", Guest: " + guestName +
                    ", RoomType: " + roomType +
                    ", RoomId: " + roomId +
                    ", Cancelled: " + isCancelled;
        }
    }

    // ---------------- INVENTORY ----------------
    static class RoomInventory {

        private Map<String, Integer> inventory = new HashMap<>();
        private Map<String, Stack<String>> availableRooms = new HashMap<>();

        public RoomInventory() {
            // Initialize inventory and room IDs
            addRooms("Deluxe", Arrays.asList("D1", "D2"));
            addRooms("Standard", Arrays.asList("S1", "S2"));
        }

        private void addRooms(String type, List<String> roomIds) {
            inventory.put(type, roomIds.size());
            Stack<String> stack = new Stack<>();
            for (String id : roomIds) {
                stack.push(id);
            }
            availableRooms.put(type, stack);
        }

        // Allocate room (LIFO)
        public String allocateRoom(String roomType) throws Exception {
            Stack<String> stack = availableRooms.get(roomType);

            if (stack == null || stack.isEmpty()) {
                throw new Exception("No rooms available for type: " + roomType);
            }

            inventory.put(roomType, inventory.get(roomType) - 1);
            return stack.pop();
        }

        // Release room back (rollback)
        public void releaseRoom(String roomType, String roomId) {
            availableRooms.get(roomType).push(roomId);
            inventory.put(roomType, inventory.get(roomType) + 1);
        }

        public int getAvailableCount(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }
    }

    // ---------------- BOOKING HISTORY ----------------
    static class BookingHistory {
        private Map<String, Reservation> reservations = new HashMap<>();

        public void addReservation(Reservation r) {
            reservations.put(r.reservationId, r);
        }

        public Reservation getReservation(String id) {
            return reservations.get(id);
        }
    }

    // ---------------- CANCELLATION SERVICE ----------------
    static class CancellationService {

        private RoomInventory inventory;
        private BookingHistory history;

        public CancellationService(RoomInventory inventory, BookingHistory history) {
            this.inventory = inventory;
            this.history = history;
        }

        public void cancelBooking(String reservationId) throws CancellationException {

            // Validate existence
            Reservation r = history.getReservation(reservationId);

            if (r == null) {
                throw new CancellationException("Reservation does not exist");
            }

            // Prevent duplicate cancellation
            if (r.isCancelled) {
                throw new CancellationException("Reservation already cancelled");
            }

            // Rollback steps (controlled order)
            String roomType = r.roomType;
            String roomId = r.roomId;

            // 1. Release room (LIFO rollback)
            inventory.releaseRoom(roomType, roomId);

            // 2. Mark reservation as cancelled
            r.isCancelled = true;

            System.out.println("Cancellation successful for: " + reservationId);
        }
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancellationService = new CancellationService(inventory, history);

        try {
            // Create booking
            String roomId = inventory.allocateRoom("Deluxe");
            Reservation r1 = new Reservation("RES1", "Alice", "Deluxe", roomId);
            history.addReservation(r1);

            System.out.println("Booking Confirmed: " + r1);

            // Cancel booking
            cancellationService.cancelBooking("RES1");

            // Try duplicate cancellation
            cancellationService.cancelBooking("RES1");

        } catch (CancellationException e) {
            System.out.println("Cancellation Failed: " + e.getMessage());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Final inventory state
        System.out.println("\nAvailable Deluxe Rooms: " +
                inventory.getAvailableCount("Deluxe"));
    }
}