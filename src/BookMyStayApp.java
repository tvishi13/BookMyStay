import java.util.*;

// Main class (single file)
public class BookMyStayApp {

    // ---------------- ENTITY ----------------
    static class AddOnService {
        private String serviceId;
        private String name;
        private double price;

        public AddOnService(String serviceId, String name, double price) {
            this.serviceId = serviceId;
            this.name = name;
            this.price = price;
        }

        public double getPrice() {
            return price;
        }

        public String getName() {
            return name;
        }
    }

    // ---------------- MANAGER ----------------
    static class AddOnServiceManager {

        // Map<ReservationId, List of Services>
        private Map<String, List<AddOnService>> reservationServicesMap = new HashMap<>();

        // Add service
        public void addService(String reservationId, AddOnService service) {
            reservationServicesMap
                    .computeIfAbsent(reservationId, k -> new ArrayList<>())
                    .add(service);
        }

        // Get services
        public List<AddOnService> getServices(String reservationId) {
            return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
        }

        // Calculate total add-on cost
        public double calculateTotalCost(String reservationId) {
            return getServices(reservationId)
                    .stream()
                    .mapToDouble(AddOnService::getPrice)
                    .sum();
        }
    }

    // ---------------- SIMULATION (Guest Flow) ----------------
    public static void main(String[] args) {

        AddOnServiceManager manager = new AddOnServiceManager();

        String reservationId = "RES123";

        // Create services
        AddOnService breakfast = new AddOnService("S1", "Breakfast", 500);
        AddOnService airportPickup = new AddOnService("S2", "Airport Pickup", 1200);
        AddOnService spa = new AddOnService("S3", "Spa", 2000);

        // Guest selects services
        manager.addService(reservationId, breakfast);
        manager.addService(reservationId, airportPickup);
        manager.addService(reservationId, spa);

        // Display selected services
        System.out.println("Selected Services for Reservation: " + reservationId);
        for (AddOnService service : manager.getServices(reservationId)) {
            System.out.println("- " + service.getName() + " : ₹" + service.getPrice());
        }

        // Calculate total add-on cost
        double totalCost = manager.calculateTotalCost(reservationId);
        System.out.println("Total Add-On Cost: ₹" + totalCost);

        // NOTE:
        // Core booking and inventory logic are NOT modified.
    }
}