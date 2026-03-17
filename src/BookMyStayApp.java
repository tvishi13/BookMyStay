import java.util.*;

// Main class
public class BookMyStayApp {

    // ---------------- RESERVATION ENTITY ----------------
    static class Reservation {
        private String reservationId;
        private String guestName;
        private String roomType;
        private double price;

        public Reservation(String reservationId, String guestName, String roomType, double price) {
            this.reservationId = reservationId;
            this.guestName = guestName;
            this.roomType = roomType;
            this.price = price;
        }

        public String getReservationId() {
            return reservationId;
        }

        public String getGuestName() {
            return guestName;
        }

        public String getRoomType() {
            return roomType;
        }

        public double getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "Reservation ID: " + reservationId +
                    ", Guest: " + guestName +
                    ", Room: " + roomType +
                    ", Price: ₹" + price;
        }
    }

    // ---------------- BOOKING HISTORY ----------------
    static class BookingHistory {

        // List preserves insertion order
        private List<Reservation> confirmedBookings = new ArrayList<>();

        // Add confirmed booking
        public void addReservation(Reservation reservation) {
            confirmedBookings.add(reservation);
        }

        // Retrieve all bookings
        public List<Reservation> getAllReservations() {
            return new ArrayList<>(confirmedBookings); // return copy (safe)
        }
    }

    // ---------------- REPORT SERVICE ----------------
    static class BookingReportService {

        // Print all bookings
        public void printAllBookings(List<Reservation> reservations) {
            System.out.println("\n--- Booking History ---");
            for (Reservation r : reservations) {
                System.out.println(r);
            }
        }

        // Total revenue report
        public void printTotalRevenue(List<Reservation> reservations) {
            double total = reservations.stream()
                    .mapToDouble(Reservation::getPrice)
                    .sum();

            System.out.println("\nTotal Revenue: ₹" + total);
        }

        // Room type summary
        public void printRoomTypeSummary(List<Reservation> reservations) {
            Map<String, Integer> roomCount = new HashMap<>();

            for (Reservation r : reservations) {
                roomCount.put(r.getRoomType(),
                        roomCount.getOrDefault(r.getRoomType(), 0) + 1);
            }

            System.out.println("\n--- Room Type Summary ---");
            for (String type : roomCount.keySet()) {
                System.out.println(type + ": " + roomCount.get(type));
            }
        }
    }

    // ---------------- SIMULATION ----------------
    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulating confirmed bookings
        Reservation r1 = new Reservation("RES101", "Alice", "Deluxe", 3000);
        Reservation r2 = new Reservation("RES102", "Bob", "Suite", 5000);
        Reservation r3 = new Reservation("RES103", "Charlie", "Deluxe", 3000);

        // Add to history (only after confirmation)
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin retrieves booking history
        List<Reservation> storedBookings = history.getAllReservations();

        // Generate reports
        reportService.printAllBookings(storedBookings);
        reportService.printTotalRevenue(storedBookings);
        reportService.printRoomTypeSummary(storedBookings);
    }
}