import java.util.*;

// Main class
public class BookMyStayApp {

    // ---------------- CUSTOM EXCEPTION ----------------
    static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message);
        }
    }

    // ---------------- RESERVATION ----------------
    static class Reservation {
        private String reservationId;
        private String guestName;
        private String roomType;

        public Reservation(String reservationId, String guestName, String roomType) {
            this.reservationId = reservationId;
            this.guestName = guestName;
            this.roomType = roomType;
        }

        @Override
        public String toString() {
            return "Reservation ID: " + reservationId +
                    ", Guest: " + guestName +
                    ", Room Type: " + roomType;
        }
    }

    // ---------------- INVENTORY ----------------
    static class RoomInventory {
        private Map<String, Integer> inventory = new HashMap<>();

        public RoomInventory() {
            inventory.put("Standard", 2);
            inventory.put("Deluxe", 1);
            inventory.put("Suite", 0); // intentionally 0 for testing
        }

        public boolean isRoomTypeValid(String roomType) {
            return inventory.containsKey(roomType);
        }

        public int getAvailableRooms(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        public void reduceRoom(String roomType) throws InvalidBookingException {
            int available = getAvailableRooms(roomType);

            if (available <= 0) {
                throw new InvalidBookingException("No rooms available for type: " + roomType);
            }

            inventory.put(roomType, available - 1);
        }
    }

    // ---------------- VALIDATOR ----------------
    static class BookingValidator {

        public void validate(String guestName, String roomType, RoomInventory inventory)
                throws InvalidBookingException {

            // Fail-fast validations
            if (guestName == null || guestName.trim().isEmpty()) {
                throw new InvalidBookingException("Guest name cannot be empty");
            }

            if (roomType == null || roomType.trim().isEmpty()) {
                throw new InvalidBookingException("Room type cannot be empty");
            }

            if (!inventory.isRoomTypeValid(roomType)) {
                throw new InvalidBookingException("Invalid room type: " + roomType);
            }

            if (inventory.getAvailableRooms(roomType) <= 0) {
                throw new InvalidBookingException("No available rooms for: " + roomType);
            }
        }
    }

    // ---------------- BOOKING SERVICE ----------------
    static class BookingService {

        private RoomInventory inventory;
        private BookingValidator validator;

        public BookingService(RoomInventory inventory, BookingValidator validator) {
            this.inventory = inventory;
            this.validator = validator;
        }

        public Reservation createBooking(String reservationId, String guestName, String roomType)
                throws InvalidBookingException {

            // Validate BEFORE modifying system state
            validator.validate(guestName, roomType, inventory);

            // Safe to proceed
            inventory.reduceRoom(roomType);

            return new Reservation(reservationId, guestName, roomType);
        }
    }

    // ---------------- MAIN (SIMULATION) ----------------
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingValidator validator = new BookingValidator();
        BookingService bookingService = new BookingService(inventory, validator);

        // Test cases
        String[][] testInputs = {
                {"RES1", "Alice", "Deluxe"},     // valid
                {"RES2", "", "Standard"},        // invalid name
                {"RES3", "Bob", "Luxury"},       // invalid room type
                {"RES4", "Charlie", "Suite"}     // no availability
        };

        for (String[] input : testInputs) {
            try {
                Reservation r = bookingService.createBooking(
                        input[0], input[1], input[2]);

                System.out.println("Booking Successful: " + r);

            } catch (InvalidBookingException e) {
                // Graceful failure handling
                System.out.println("Booking Failed: " + e.getMessage());
            }
        }

        // System continues running safely
        System.out.println("\nSystem is still stable after handling errors.");
    }
}