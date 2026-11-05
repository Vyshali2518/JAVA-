interface Chargeable {
    double chargingFee(int kWh);
}

abstract class Vehicle {
    private final String regNo;

    protected Vehicle(String regNo) {
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number is required");
        }
        this.regNo = regNo;
    }

    public String getRegNo() {
        return regNo;
    }

    public abstract double calculateFee(int minutes);

    public double calculateFee(int minutes, boolean validMonthlyPass) {
        return validMonthlyPass ? 0 : calculateFee(minutes);
    }

    protected final double hourlyFee(int minutes, double ratePerHour) {
        if (minutes <= 0) {
            return 0;
        }
        long hours = ((long) minutes + 59) / 60;
        return hours * ratePerHour;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vehicle)) {
            return false;
        }
        Vehicle other = (Vehicle) o;
        return regNo.equals(other.regNo);
    }

    @Override
    public int hashCode() {
        return regNo.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[regNo=" + regNo + "]";
    }
}

class TwoWheeler extends Vehicle {
    TwoWheeler(String regNo) {
        super(regNo);
    }

    @Override
    public double calculateFee(int minutes) {
        return hourlyFee(minutes, 10);
    }
}

class Car extends Vehicle {
    Car(String regNo) {
        super(regNo);
    }

    @Override
    public double calculateFee(int minutes) {
        return hourlyFee(minutes, 30);
    }
}

class ElectricCar extends Car implements Chargeable {
    ElectricCar(String regNo) {
        super(regNo);
    }

    @Override
    public double chargingFee(int kWh) {
        return kWh * 12;
    }
}

public class FeeDemo {
    public static void main(String[] args) {
        Vehicle[] parked = {
            new TwoWheeler("TN01A1"),
            new Car("TN02B2"),
            new ElectricCar("TN03C3")
        };

        for (Vehicle v : parked) {
            double fee = v.calculateFee(130);
            if (v instanceof Chargeable) {
                Chargeable c = (Chargeable) v;
                fee += c.chargingFee(10);
            }
            System.out.println(v.getClass().getSimpleName() + " "
                    + v.getRegNo() + " -> Rs " + fee);
        }

        if (new Car("TN04D4").calculateFee(130, true) != 0) {
            throw new IllegalStateException("A valid monthly pass must be free");
        }

        System.out.println(new Car("TN02B2").equals(
                new ElectricCar("TN02B2")));
    }
}
