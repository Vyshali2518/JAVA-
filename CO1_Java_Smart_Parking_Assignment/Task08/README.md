## Task 8 – Vehicle hierarchy and polymorphism

Compile and run from this folder:

```powershell
javac FeeDemo.java
java FeeDemo
```

The demo uses a `Vehicle[]` containing two-wheelers, cars, and an electric car. Overridden fee methods dispatch by runtime type; the `Chargeable` interface adds the EV charging fee. The output also demonstrates registration-based equality.
