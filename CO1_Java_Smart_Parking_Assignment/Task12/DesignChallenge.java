import java.time.LocalDate;

abstract class NewVehicle {
    private final String regNo;

    NewVehicle(String regNo) {
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number is required");
        }
        this.regNo = regNo;
    }

    public String getRegNo() {
        return regNo;
    }

    public abstract double calculateFee(int minutes, int entryHour);
}

class EBike extends NewVehicle {
    EBike(String regNo) {
        super(regNo);
    }

    @Override
    public double calculateFee(int minutes, int entryHour) {
        validateEntry(minutes, entryHour);
        return minutes == 0 ? 0 : 20;
    }

    private void validateEntry(int minutes, int entryHour) {
        if (minutes < 0 || entryHour < 0 || entryHour > 23) {
            throw new IllegalArgumentException("Invalid duration or entry hour");
        }
    }
}

class DeliveryVan extends NewVehicle {
    DeliveryVan(String regNo) {
        super(regNo);
    }

    @Override
    public double calculateFee(int minutes, int entryHour) {
        if (minutes < 0 || entryHour < 0 || entryHour > 23) {
            throw new IllegalArgumentException("Invalid duration or entry hour");
        }
        if (minutes == 0) {
            return 0;
        }
        long hours = ((long) minutes + 59) / 60;
        int carRate = 30;
        int rate = (entryHour >= 9 && entryHour < 17)
                ? carRate * 2 : carRate;
        return hours * rate;
    }
}

class VisitorOneDayPass {
    private final LocalDate validDate;

    VisitorOneDayPass(LocalDate validDate) {
        if (validDate == null) {
            throw new IllegalArgumentException("Pass date is required");
        }
        this.validDate = validDate;
    }

    public boolean isValidOn(LocalDate date) {
        return validDate.equals(date);
    }

    public double parkingFee(LocalDate date, double regularFee) {
        if (regularFee < 0) {
            throw new IllegalArgumentException("Regular fee cannot be negative");
        }
        return isValidOn(date) ? 0 : regularFee;
    }
}

class SimpleJaggedParking {
    private final String[][] slots;

    SimpleJaggedParking(int[] slotsPerFloor) {
        if (slotsPerFloor == null || slotsPerFloor.length == 0) {
            throw new IllegalArgumentException("At least one floor is required");
        }
        slots = new String[slotsPerFloor.length][];
        for (int i = 0; i < slotsPerFloor.length; i++) {
            if (slotsPerFloor[i] < 0) {
                throw new IllegalArgumentException("Slot count cannot be negative");
            }
            slots[i] = new String[slotsPerFloor[i]];
        }
    }

    public boolean park(String regNo) {
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number is required");
        }
        for (int f = 0; f < slots.length; f++) {
            for (int s = 0; s < slots[f].length; s++) {
                if (slots[f][s] == null) {
                    slots[f][s] = regNo;
                    System.out.println(regNo + " parked at floor "
                            + (f + 1) + ", slot " + (s + 1));
                    return true;
                }
            }
        }
        return false;
    }
}

public class DesignChallenge {
    public static void main(String[] args) {
        SimpleJaggedParking parking =
                new SimpleJaggedParking(new int[] { 2, 3, 1, 2, 2 });

        EBike ebike = new EBike("TN10EB1234");
        DeliveryVan van = new DeliveryVan("TN20DV5678");

        parking.park(ebike.getRegNo());
        parking.park(van.getRegNo());

        System.out.println("E-bike flat fee: Rs "
                + ebike.calculateFee(90, 10));
        System.out.println("Delivery van fee at 10 AM: Rs "
                + van.calculateFee(60, 10));

        System.out.println("Delivery van fee at 8 AM: Rs "
                + van.calculateFee(60, 8));
        System.out.println("Delivery van fee at 9 AM: Rs "
                + van.calculateFee(60, 9));
        System.out.println("Delivery van fee at 4 PM: Rs "
                + van.calculateFee(60, 16));
        System.out.println("Delivery van fee at 5 PM: Rs "
                + van.calculateFee(60, 17));

        LocalDate today = LocalDate.now();
        VisitorOneDayPass pass = new VisitorOneDayPass(today);
        double visitorFee = pass.parkingFee(today, van.calculateFee(60, 11));
        System.out.println("Visitor one-day pass fee: Rs " + visitorFee);
        System.out.println("Pass invalid on the next day: "
                + !pass.isValidOn(today.plusDays(1)));
        System.out.println("Visitor fee when pass is expired: Rs "
                + pass.parkingFee(today.plusDays(1),
                        van.calculateFee(60, 11)));
    }
}
