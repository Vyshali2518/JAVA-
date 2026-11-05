# CO1 Java Assignment 1
## Campus Smart-Parking System

**Student Name:** [Enter your name]  
**Register Number:** [Enter your register number]  
**GitHub Link:** [Paste your repository link]  
**Java Version:** JDK 8 or later (tested with OpenJDK 11)

> This is a simple student-level explanation. Read the answers, run the programs, and make sure you can explain them during the viva.

---

## Task 1 – Understand the Problem and Identify the Objects

**Inputs:** Vehicle registration number, vehicle type, user type, entry time, exit time and available parking slots.

**Outputs:** Allocated floor and slot, ticket number, parking duration, parking fee and slot availability.

| Candidate class | Responsibility |
|---|---|
| Vehicle | Stores registration number and vehicle type. |
| ParkingSlot | Stores floor number, slot number and whether it is occupied. |
| ParkingTicket | Stores ticket number, vehicle, entry time and fee. |
| ParkingFacility | Manages floors, slots, vehicle entry and exit. |
| User | Stores whether the person is a student, staff member or visitor. |

**Why these classes are needed:** Each class has one clear responsibility. For example, ParkingSlot manages one slot, while ParkingFacility manages all slots. This makes the program easier to understand and update.

**Business rules**
1. One slot can contain only one vehicle.
2. A ticket number must be unique.
3. A vehicle must have a registration number.
4. An occupied slot cannot be allocated again.
5. A released slot becomes available.
6. Students and staff with a valid monthly pass park free; visitors pay the fee.

**Assumptions:** The program uses a fixed number of floors and slots. Time is entered in minutes for the simple fee example. A started hour is charged as a full hour.

**Alternative: objects vs parallel arrays:** With objects, registration number, type and entry time are stored together in one Vehicle object. Searching is O(n) in a simple list. With parallel arrays, the same vehicle's details are stored at the same index in separate arrays, so all arrays must be updated together. Search is also O(n). Objects use some extra object/reference memory, but adding an attribute is easier because it is added to the class instead of creating and maintaining another parallel array.

## Task 2 – Choose the Data Representation

| Data | Java type | Reason |
|---|---|---|
| Floor number | int | Whole number |
| Slot number | int | Whole number |
| Registration number | String | Contains letters and digits |
| Entry time | long | Can store time in milliseconds |
| Duration | int | Number of minutes |
| Fee | double | Can contain decimal values |

A two-dimensional array works when every floor has the same number of slots. A jagged array (`String[][]`) is better when each floor has a different number of slots. The Task 3 program uses a jagged array.

**Casting and precision:** `130 / 60` using integer division gives `2`, but 130 minutes must be charged as 3 started hours. The fee examples round up with `((long) minutes + 59) / 60`, avoiding both fractional-hour truncation and integer overflow in the addition.

**Input:** `Scanner` can read user input from the keyboard. Command-line arguments can also be used for small test programs.

**Alternative representation:** A one-dimensional array needs a formula to convert floor and slot into one index. A single formula is inconvenient when floors have different slot counts. An `ArrayList<String[]>` can store one array per floor, but it is more advanced than a simple jagged array. Locating a slot by scanning is O(F × S) in the worst case. The total storage is O(total slots).

## Task 3 – Design the Control Logic

**Slot allocation pseudocode**
```text
START
FOR each floor
    FOR each slot in that floor
        IF slot is empty
            Store vehicle number
            RETURN floor and slot
        END IF
    END FOR
END FOR
RETURN no slot available
STOP
```

**Fee pseudocode**
```text
START
IF minutes <= 0
    RETURN 0
END IF
hours = (minutes + 59) / 60
fee = hours * hourly rate
RETURN fee
STOP
```

The `break` statement can stop a loop when a slot is found. `continue` can skip an occupied slot. `return` immediately sends the result back to the caller. If no slot is free, allocation returns `null`.

The fee pseudocode uses one hourly rate as the current fee slab; a valid monthly pass is a zero-fee rule. If more duration slabs are introduced, choose the applicable rate first and then round the started hours up. Searching for the first free slot is O(F × S) in the worst case.

**Completed code:** See `Task03/ParkingFacility.java`.

**Alternative: free-slot stack:** A stack of available slot positions can provide O(1) allocation and release, provided the stack is maintained correctly. The simple scanning solution takes O(F × S) in the worst case for allocation and O(1) for release after validation. A stack needs extra storage for the free positions, up to O(total slots). The scanning version does not need a separate stack.

## Task 4 – Design Classes, Objects and Constructors

A class is a blueprint. An object is an instance created from that class. The Vehicle class can store registration number and type. ParkingTicket can store ticket details.

A default constructor takes no arguments. A parameterised constructor receives values when the object is created. Overloaded constructors have the same name but different parameter lists. The `this` keyword refers to the current object and can distinguish fields from constructor parameters.

A copy constructor is not necessary for this simple system because a new ticket should receive a new ticket number, not copy an old ticket number.

Objects are created in heap memory using `new`. Reference variables hold references to those objects. Local reference variables are commonly stored in a method's stack frame.

**Alternative:** Creating an empty object and then calling setters can temporarily leave an object with missing or invalid values. A parameterised constructor can check required values during creation. Static factory methods are another option, but are not needed for this beginner project. Creating n objects takes O(n) total time and O(n) object storage in all these approaches; the main difference is validation and code organisation.

## Task 5 – Apply static Members and Object Life Cycle

A ticket counter should be `static` because all tickets share one counter. If it were an instance variable, each facility or ticket could have a separate counter and duplicate numbers could occur. A static method can return the next ticket number because it does not need one particular ticket object.

After a vehicle exits, its ticket can be removed from the active-ticket collection. If no references point to that ticket, it becomes eligible for garbage collection. Java decides when memory is reclaimed.

Objects can be passed as method arguments and returned from methods. The reference is passed, so a method can access the same object.

**Alternative:** If the counter belongs to each ParkingFacility, two facilities can each start at ticket number 1. That is acceptable only if ticket numbers are unique within each facility. For campus-wide unique ticket numbers, use one shared static counter. A static counter uses one counter value; an instance counter uses one value per facility object (O(k) for k facilities).

## Task 6 – Design the Inheritance Hierarchy

The superclass is `Vehicle`. Subclasses are `TwoWheeler`, `Car` and `ElectricCar`. This is hierarchical inheritance because multiple subclasses inherit from Vehicle. ElectricCar extends Car, so it also demonstrates multilevel inheritance.

Common information such as registration number is stored in Vehicle. Each subclass supplies its own hourly rate. The `super` keyword calls the parent constructor.

**Constructor trace for ElectricCar:**
1. `new ElectricCar(...)` starts the ElectricCar constructor.
2. `super(...)` calls the Car constructor.
3. Car calls `super(...)` to call the Vehicle constructor.
4. Vehicle stores the registration number.
5. Control returns to Car, then ElectricCar.

**Simple UML**
```text
             Vehicle (abstract)
             /              \
      TwoWheeler             Car
                                |
                           ElectricCar
                                |
                         implements Chargeable
```

**Alternative: composition:** A Vehicle could contain a VehicleType object that stores the rate. Adding a type would mean creating another type object rather than another Vehicle subclass. Both approaches calculate a fee in O(1). Composition can make changing vehicle types easier, while inheritance is simple for this assignment.

## Task 7 – Override and Specialise Behaviour

The `calculateFee(int minutes)` method is overridden by `TwoWheeler` and `Car`. Each implementation uses the shared `hourlyFee` helper with its own rate. The `@Override` annotation helps the compiler check that the method correctly overrides a parent method.

Overriding requires a compatible method signature and return type, and the subclass cannot reduce the access level. The `final` keyword can protect a value that must not change, prevent a method from being overridden, or prevent a class from being extended.

`equals()` compares registration numbers so that two Vehicle objects with the same registration number are treated as the same registered vehicle. `hashCode()` uses the same registration number so it is consistent with `equals()`. Thus a `Car` and `ElectricCar` with the same registration number compare equal; this is appropriate when registration identifies a physical vehicle, but should be reconsidered if vehicle subtype is part of identity. `toString()` is overridden to display the concrete vehicle class and registration number. The implementation uses `final` for immutable registration data and shared fee logic.

**Alternative: switch statement:** One method could check a type field using `switch`. Fee calculation would still be O(1) for a fixed number of types. However, adding a new type requires editing the switch method. With overriding, a new subclass can provide its own fee rule. The switch approach has fewer classes but can become harder to maintain.

## Task 8 – Apply Polymorphism

Compile-time polymorphism is method overloading. In `Vehicle`, `calculateFee(int minutes)` and `calculateFee(int minutes, boolean validMonthlyPass)` are overloaded; the compiler chooses based on the number and types of arguments. The second overload returns zero for a valid pass and otherwise calls the one-argument method.

Runtime polymorphism occurs when a Vehicle reference points to a subclass object. A `Vehicle[]` stores TwoWheeler, Car and ElectricCar objects. When `calculateFee(130)` is called, Java runs the overridden method for the actual object.

**Dispatch trace:** Declared type = `Vehicle`; actual object = `Car`; method called = `Car.calculateFee(int)`; it invokes the shared hourly-fee helper with the car rate.

This avoids writing a long if-else chain for every vehicle type.

**Completed code:** See `Task08/FeeDemo.java`.

**Alternative: instanceof checks:** The program could check each object type and cast it before calculating a fee. For k vehicle types, it may perform up to k checks. Dynamic dispatch selects the correct overridden method directly. Both fee calculations are O(1) for a fixed set of classes, but dynamic dispatch is easier to extend.

## Task 9 – Introduce Abstraction

`Vehicle` is abstract because a general vehicle does not have one fixed fee rule. Its `calculateFee(int)` method is abstract and is implemented by subclasses.

`Chargeable` is an interface because charging is a capability that only some vehicles have. ElectricCar implements it. A class can implement more than one interface, so it can have multiple capabilities.

An abstract class can store common fields and constructors. An interface describes behaviour that implementing classes promise to provide. A default interface method can provide a common method body; a static interface method can be called using the interface name.

**Alternative:** If Vehicle were an interface, it could not use a normal instance constructor to initialise registration number. State would need to be stored in implementing classes. If Chargeable became an abstract class, a class already extending Vehicle could not also extend it because Java classes have single inheritance. Method-call time is effectively O(1) in both simple designs; object storage depends on the fields used.

## Task 10 – Enforce Encapsulation through Packages and Access Modifiers

Suggested package structure:
```text
parking
  model
  service
  app
```

Fields should normally be `private` so other classes cannot change them directly. Public getters provide controlled access. Setters or constructors should validate values, for example rejecting a blank registration number, unsupported vehicle type, non-positive hourly rate, negative parking duration or invalid slot number. `protected` is useful for members intended for subclasses. Default access is limited to the same package. A possible package structure is a design proposal; the short runnable examples use the default package to keep compilation simple.

Wrapper classes such as `Integer` can be used when an object is required. Autoboxing converts between `int` and `Integer`. Comparing two Integer objects using `==` can compare references instead of numeric values; use `.equals()` to compare their values.

**Alternative: public fields:** Any class could change a field to an invalid value, such as a negative fee. Validation would need to be repeated in every calling class. A setter performs a constant-time O(1) validation per update and keeps the check in one place.

## Task 11 – Implement, Test and Defend the Design

**Working implementation:** See `Task11/SmartParkingSystem.java`.

The program demonstrates vehicle types, slot allocation, unique ticket creation, entry-time recording, fee calculation, pass handling and slot release. It uses simple arrays and classes. Invalid vehicle types and duplicate registrations are rejected with an `IllegalArgumentException`; an unavailable facility returns `null`, while invalid slot releases return `false`.

**Executed test results**

| Test | Expected result | Actual result |
|---|---|---|
| Park a two-wheeler, then a car | Different slots and unique tickets | Tickets 1 and 2; slots 1 and 2 |
| Fill the two-slot facility and park another vehicle | No slot available | `false` (third ticket is `null`) |
| Record entry time and find an active ticket by registration | Both lookups succeed | `true`; `true` |
| Release an occupied slot; release it again | First succeeds; second fails | `true`; `false` |
| Reuse the released slot | The new vehicle gets slot 1 | `true` |
| Calculate a zero-minute stay | Rs 0 | Rs 0.0 |
| Calculate 60 and 61 minutes for a two-wheeler | Rs 10 and Rs 20 | Rs 10.0 and Rs 20.0 |
| Valid monthly-pass holder | Rs 0 | Rs 0.0 |
| Release invalid slot numbers 0 and 3 | Both rejected | `true` |
| Park a duplicate registration or unsupported type | Both rejected | `true`; `true` |

These results are printed by `Task11/SmartParkingSystem.java`; rerun it to reproduce the actual column. The third-vehicle case checks a full facility, and the slot-reuse case checks the last free slot after an exit.

**Daily collection example:** One two-wheeler parked for 61 minutes at Rs 10 per hour pays Rs 20. One car parked for 60 minutes at Rs 30 per hour pays Rs 30. Total collection is Rs 50. A valid monthly-pass holder pays Rs 0.

**Evaluation:** Encapsulation protects vehicle and ticket data. Inheritance shares common vehicle details. Polymorphism allows each vehicle to define its own fee. Abstraction defines common vehicle behaviour. One limitation is that this simple program does not save data permanently after it closes; entry time is recorded as milliseconds but the demonstration uses supplied minutes rather than calculating elapsed time from a clock.

**Alternative data structures:** An array requires a search through up to n tickets, O(n). An ArrayList also requires O(n) search by registration number. A HashMap keyed by registration number usually gives average O(1) lookup and uses O(n) space, but it is a little more advanced.

## Task 12 – Re-evaluate and Adapt the Design

**A. Impact analysis:** The new requirements add e-bikes, delivery vans and one-day visitor passes. A new vehicle subclass is needed for each fee rule and a pass class is needed to represent its validity date. The facility capacity changes from the original three-floor layout to five floors with unequal capacities. A jagged array is suitable because each floor can have its own slot count; the example uses capacities `{2, 3, 1, 2, 2}`.

**B. Evaluation:** The original common vehicle concept remains useful, and existing regular-vehicle examples need not be redesigned. The delivery-van pricing rule belongs in its fee method because it depends on entry hour; the visitor pass is modeled separately because its date validity is not a vehicle type. This keeps those changes out of unrelated vehicle classes.

**C. Adaptation:** `Task12/DesignChallenge.java` implements an e-bike flat fee, a delivery van charged at twice the Rs 30 car rate from 9:00 (inclusive) to 17:00 (exclusive), a date-specific `VisitorOneDayPass`, and a five-floor jagged array. Outside that time window, the van uses the car rate. The example retains the simple original structure and adds only the classes needed for the new rules.

**D. Evidence-based defence:** Compared with the original vehicle/facility model, the adaptation introduces three concepts/classes (`EBike`, `DeliveryVan`, and `VisitorOneDayPass`) and adapts the jagged parking facility. Existing common vehicle behavior remains usable. The tests below cover the new fee rules and pass validity. An expired pass returns the regular fee. Future fee categories can be added as subclasses; this is straightforward for a few categories, though a separate pricing strategy would reduce subclass growth if many overlapping rules are introduced.

| Test | Expected result | Actual result |
|---|---|---|
| E-bike stays 90 minutes | Flat Rs 20 | Rs 20.0 |
| Van enters at 8 AM | Regular car rate, Rs 30 | Rs 30.0 |
| Van enters at 9 AM or 4 PM | Double car rate, Rs 60 | Rs 60.0 at both times |
| Van enters at 5 PM | Regular car rate, Rs 30 | Rs 30.0 |
| Visitor uses pass on its valid date | Rs 0 | Rs 0.0 |
| Visitor uses expired pass on next date | Regular fee, Rs 60 | Rs 60.0 |
| Five-floor facility has unequal capacities | Counts `{2, 3, 1, 2, 2}` are stored by floor | Configured as `{2, 3, 1, 2, 2}` |

---

## Final submission checklist
- [ ] Replace student details.
- [ ] Run all Java files.
- [ ] Capture output screenshots from your own computer.
- [ ] Add the GitHub repository link.
- [ ] Commit task folders progressively.
- [ ] Check that the repository is accessible to your faculty.
