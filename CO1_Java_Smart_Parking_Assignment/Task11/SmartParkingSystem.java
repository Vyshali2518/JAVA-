class ParkingVehicle {
    private final String regNo;
    private final String type;
    private final double ratePerHour;
    private final boolean validMonthlyPass;

    ParkingVehicle(String regNo, String type, double ratePerHour) {
        this(regNo, type, ratePerHour, false);
    }

    ParkingVehicle(String regNo, String type, double ratePerHour,
            boolean validMonthlyPass) {
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number is required");
        }
        if (!"TwoWheeler".equals(type) && !"Car".equals(type)
                && !"ElectricCar".equals(type)) {
            throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
        if (ratePerHour <= 0 || Double.isNaN(ratePerHour)
                || Double.isInfinite(ratePerHour)) {
            throw new IllegalArgumentException("Hourly rate must be positive");
        }
        this.regNo = regNo;
        this.type = type;
        this.ratePerHour = ratePerHour;
        this.validMonthlyPass = validMonthlyPass;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getType() {
        return type;
    }

    public double calculateFee(int minutes) {
        if (validMonthlyPass || minutes <= 0) {
            return 0;
        }
        long hours = ((long) minutes + 59) / 60;
        return hours * ratePerHour;
    }

    public boolean hasValidMonthlyPass() {
        return validMonthlyPass;
    }
}

class ParkingTicket {
    private static int nextNumber = 1;
    private final int ticketNumber;
    private final ParkingVehicle vehicle;
    private final int slotNumber;
    private final long entryTimeMillis;

    ParkingTicket(ParkingVehicle vehicle, int slotNumber) {
        this.ticketNumber = nextNumber++;
        this.vehicle = vehicle;
        this.slotNumber = slotNumber;
        this.entryTimeMillis = System.currentTimeMillis();
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public ParkingVehicle getVehicle() {
        return vehicle;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public long getEntryTimeMillis() {
        return entryTimeMillis;
    }
}

class ParkingFacilitySimple {
    private final ParkingTicket[] tickets;
    private final boolean[] occupied;

    ParkingFacilitySimple(int numberOfSlots) {
        if (numberOfSlots <= 0) {
            throw new IllegalArgumentException("Facility must have at least one slot");
        }
        tickets = new ParkingTicket[numberOfSlots];
        occupied = new boolean[numberOfSlots];
    }

    public ParkingTicket park(ParkingVehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle is required");
        }
        if (findTicketByRegistration(vehicle.getRegNo()) != null) {
            throw new IllegalArgumentException(
                    "Vehicle is already parked: " + vehicle.getRegNo());
        }
        for (int i = 0; i < occupied.length; i++) {
            if (!occupied[i]) {
                occupied[i] = true;
                tickets[i] = new ParkingTicket(vehicle, i + 1);
                return tickets[i];
            }
        }
        return null;
    }

    public ParkingTicket findTicketByRegistration(String regNo) {
        if (regNo == null) {
            return null;
        }
        for (ParkingTicket ticket : tickets) {
            if (ticket != null
                    && ticket.getVehicle().getRegNo().equals(regNo)) {
                return ticket;
            }
        }
        return null;
    }

    public boolean leave(int slotNumber) {
        int index = slotNumber - 1;
        if (index < 0 || index >= occupied.length || !occupied[index]) {
            return false;
        }
        occupied[index] = false;
        tickets[index] = null;
        return true;
    }
}

public class SmartParkingSystem {
    public static void main(String[] args) {
        ParkingFacilitySimple facility = new ParkingFacilitySimple(2);

        ParkingVehicle bike = new ParkingVehicle("TN01AB1111", "TwoWheeler", 10);
        ParkingVehicle car = new ParkingVehicle("TN02CD2222", "Car", 30);
        ParkingVehicle extra = new ParkingVehicle("TN03EF3333", "Car", 30);

        ParkingTicket t1 = facility.park(bike);
        ParkingTicket t2 = facility.park(car);
        ParkingTicket t3 = facility.park(extra);

        System.out.println("Bike ticket: " + t1.getTicketNumber()
                + ", slot: " + t1.getSlotNumber());
        System.out.println("Car ticket: " + t2.getTicketNumber()
                + ", slot: " + t2.getSlotNumber());
        System.out.println("Third vehicle allocated: " + (t3 != null));

        double bikeFee = bike.calculateFee(61);
        double carFee = car.calculateFee(60);
        System.out.println("Bike fee for 61 minutes: Rs " + bikeFee);
        System.out.println("Car fee for 60 minutes: Rs " + carFee);
        System.out.println("Total collection: Rs " + (bikeFee + carFee));

        System.out.println("Release slot 1: " + facility.leave(1));
        System.out.println("Release slot 1 again: " + facility.leave(1));

        System.out.println("Validation tests:");
        System.out.println("Zero-minute fee: Rs " + bike.calculateFee(0));
        System.out.println("60-minute bike fee: Rs " + bike.calculateFee(60));
        System.out.println("61-minute bike fee: Rs " + bike.calculateFee(61));
        ParkingVehicle passHolder = new ParkingVehicle(
                "TN04GH4444", "Car", 30, true);
        System.out.println("Valid monthly-pass fee: Rs "
                + passHolder.calculateFee(120));
        System.out.println("Ticket records entry time: "
                + (t1.getEntryTimeMillis() > 0));
        System.out.println("Ticket lookup by registration: "
                + (facility.findTicketByRegistration("TN02CD2222") == t2));

        boolean invalidSlotRejected = !facility.leave(0)
                && !facility.leave(3);
        System.out.println("Invalid slot numbers rejected: "
                + invalidSlotRejected);

        boolean duplicateRejected = false;
        try {
            facility.park(car);
        } catch (IllegalArgumentException ex) {
            duplicateRejected = true;
        }
        System.out.println("Duplicate registration rejected: "
                + duplicateRejected);

        boolean unknownTypeRejected = false;
        try {
            new ParkingVehicle("TN05ZZ5555", "Spaceship", 50);
        } catch (IllegalArgumentException ex) {
            unknownTypeRejected = true;
        }
        System.out.println("Unknown vehicle type rejected: "
                + unknownTypeRejected);

        ParkingTicket replacement = facility.park(extra);
        System.out.println("Released slot reused: "
                + (replacement != null && replacement.getSlotNumber() == 1));
    }
}
