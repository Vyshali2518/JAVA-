# Q5 – Race Conditions in a Cinema Seat-Booking System

Compile: `javac SeatBooking.java`  
Run: `java SeatBooking`

The completed program makes check-and-book atomic using a synchronized method. It launches 100 users competing for 10 seats and should report exactly 10 successful bookings.

**Student A:** create an unsynchronized variant and repeat ten times to expose the race; document a two-thread interleaving.  
**Student B:** benchmark synchronized method and synchronized-block versions for 10,000 booking attempts and compare timings. Timing values are machine-dependent.
