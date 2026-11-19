# PROGRAMMING IN JAVA – ASSIGNMENT 4 (CO4)
## EVENT-DRIVEN GUI DESIGN

**Student Name:** ____________________  
**Register Number:** ____________________  
**Course:** Programming in Java (CSA09)  
**GitHub Repository:** ____________________  
**Java Version:** ____________________

---

# TASK 1 – GUI DESIGN REVIEW

## 1.1 Design deficiencies

| No. | Deficiency | Possible effect |
|---|---|---|
| 1 | UI, validation, payment and JDBC logic are placed in one listener. | Difficult to test, reuse and maintain. |
| 2 | `==` is used to compare the action-command String. | String content should be compared using `equals`; the Pay branch may not be selected reliably. |
| 3 | No validation for empty registration number, non-numeric, zero or negative amount. | The application may throw an exception or accept invalid fees. |
| 4 | JDBC work and `Thread.sleep(3000)` run on the AWT event-dispatch thread. | The window freezes and stops repainting/responding. |
| 5 | Feedback is printed to the console only. | The student using the GUI may not know whether payment succeeded. |
| 6 | Receipt button is not implemented separately. | Receipt requests fall into the `else` branch and clear the fields. |
| 7 | SQL is built by concatenating user input. | SQL injection and quoting errors are possible. |
| 8 | Connection and Statement are not closed. | Database resources can leak. |
| 9 | No window-closing handling. | The application may not close cleanly. |
| 10 | Checked exceptions are not caught or declared. | The code does not compile as shown and errors are not handled. |

## 1.2 Repaired class structure and event handling

**Class structure**

```text
FeeWindow (Frame)
  - collects registration number and amount
  - handles Pay / Clear / Receipt actions
  - displays result and validation messages
          |
          v
FeeService
  - validates input
  - performs payment operation
  - returns success/failure result
          |
          v
FeeRepository (production extension)
  - JDBC connection management
  - PreparedStatement for INSERT
```

The sample implementation in `Task01` separates `FeeService` from `FeeWindow`. It uses a demonstration service rather than connecting to a real bank or database. A production repository should use `PreparedStatement`, database transactions and try-with-resources. Long-running work should be performed in a `SwingWorker` (Swing) or a background executor (AWT), not on the event-dispatch thread.

**Rewritten event handling (core idea)**

```java
payButton.addActionListener(e -> {
    try {
        String message = feeService.pay(regNoField.getText(), amountField.getText());
        statusLabel.setText(message);
    } catch (IllegalArgumentException ex) {
        statusLabel.setText(ex.getMessage());
    }
});
clearButton.addActionListener(e -> {
    regNoField.setText("");
    amountField.setText("");
    statusLabel.setText("Cleared");
});
receiptButton.addActionListener(e -> showReceipt());
```

## 1.3 Design defence (under 100 words)

The redesigned window only collects input and displays results, while `FeeService` owns validation and payment rules. The service can be tested without opening a window, and the interface can be changed without rewriting payment logic. Separate classes reduce duplication and make errors easier to locate. Database code can later be placed in a repository class and tested independently.

## Alternative concept and complexity

With one listener and an `if–else` chain, dispatch may compare the action command against up to `k` button names, giving up to **O(k)** comparisons per event. With a dedicated listener registered on each button, the source already identifies the handler, so dispatch is approximately **O(1)** with respect to the number of buttons. Separate listener objects require **O(k)** additional references/objects in the straightforward design. A shared Swing `Action` can be reused by buttons, menus and shortcuts, reducing duplicated handler code. Actual memory cost depends on the implementation and JVM.

---

# TASK 2 – FROM REQUIREMENT TO EVENT-DRIVEN DESIGN

## 2.1 Event model

| Event source | Event object | Listener | Action |
|---|---|---|---|
| Patient ID text field | `ActionEvent` | `ActionListener` | Optionally validate ID on Enter |
| Department combo box | `ItemEvent` / `ActionEvent` | `ItemListener` / `ActionListener` | Refresh doctor choices for selected department |
| Doctor combo box | `ActionEvent` | `ActionListener` | Store selected doctor |
| Time-slot combo box | `ActionEvent` | `ActionListener` | Store selected time slot |
| Book button | `ActionEvent` | `ActionListener` | Validate fields and book appointment |
| Window close control | `WindowEvent` | `WindowListener` / `WindowAdapter` | Confirm if booking is in progress, then close or cancel |

## 2.2 Component hierarchy

```text
JFrame – Outpatient Appointment Desk
└── JPanel (BorderLayout)
    ├── JPanel (North, FlowLayout) – title / instructions
    ├── JPanel (Center, GridLayout) – appointment form
    │   ├── JLabel + JTextField – patient ID
    │   ├── JLabel + JComboBox – department
    │   ├── JLabel + JComboBox – doctor
    │   └── JLabel + JComboBox – time slot
    ├── JPanel (South, FlowLayout)
    │   ├── JButton – Book
    │   └── JButton – Clear
    └── JLabel – status / confirmation

JOptionPane – booking confirmation, validation errors and close confirmation
```

## 2.3 Handler pseudocode

**(a) Refresh doctor list when department changes**

```text
PROCEDURE RefreshDoctors
    selectedDepartment ← DepartmentComboBox.SelectedItem
    DoctorComboBox.RemoveAllItems
    doctorList ← DoctorMap[selectedDepartment]
    FOR EACH doctor IN doctorList
        DoctorComboBox.AddItem(doctor)
    END FOR
    IF DoctorComboBox has items THEN
        DoctorComboBox.SelectFirstItem
    END IF
END PROCEDURE
```

**(b) Validate and book appointment**

```text
PROCEDURE BookAppointment
    patientId ← PatientIdField.Text
    department ← DepartmentComboBox.SelectedItem
    doctor ← DoctorComboBox.SelectedItem
    slot ← SlotComboBox.SelectedItem
    IF patientId is empty OR department is null OR doctor is null OR slot is null THEN
        Display validation error
        RETURN
    END IF
    bookingInProgress ← TRUE
    TRY
        result ← AppointmentService.Book(patientId, department, doctor, slot)
        Display confirmation using result
    CATCH booking error
        Display error message
    FINALLY
        bookingInProgress ← FALSE
    END TRY
END PROCEDURE
```

## 2.4 Event sequence and applet comparison

1. `main` schedules GUI creation using `SwingUtilities.invokeLater`.
2. Constructor creates components, layouts and listeners.
3. `setVisible(true)` displays the frame; Swing painting is managed by the framework.
4. User actions generate events, which are delivered to registered listeners on the event-dispatch thread.
5. Department selection invokes the doctor-list handler; Book invokes validation and booking.
6. On close, `windowClosing` (or `windowClosing` in a `WindowAdapter`) checks `bookingInProgress`. If true, show a confirmation dialog. Cancel leaves the frame open; confirm disposes it.
7. `windowClosed` may be received after disposal for cleanup.

An applet uses lifecycle callbacks `init`, `start`, `paint`, `stop` and `destroy`, controlled by the browser/applet container. A desktop Swing frame is created by application code and responds to window events; it does not use the applet lifecycle.

## Alternative concept and complexity

If the doctor list is fetched from the database on every department change, each change incurs a database round trip plus query and result-transfer time. This can be represented as **O(Q + m_d)** for a department query returning `m_d` doctors, where `Q` is the database/network overhead. Caching `Map<String, List<Doctor>>` allows lookup by department in expected **O(1)** plus **O(m_d)** to populate the combo box, at the cost of **O(d × m)** stored doctor references in the simplified case of `d` departments and `m` doctors per department (or **O(M)** if `M` is the total number of doctor entries). The cache can become stale and needs refresh/invalidation rules.

---

# TASK 3 – FROM SKETCH TO LAYOUT

## 3.1 Layout manager choices

| Region/container | Layout | Reason |
|---|---|---|
| Main frame | `BorderLayout` | Divides the screen into top, center and bottom regions; center receives extra space. |
| Search row panel | `FlowLayout(LEFT)` | Keeps the field, category and Search button at preferred sizes. |
| Results area | `BorderLayout` with `JScrollPane` | Center area expands when the frame is resized. |
| Bottom navigation panel | `FlowLayout(CENTER)` | Keeps Prev, Next and Reserve buttons at preferred sizes. |
| Optional form rows | `GridLayout` | Gives aligned rows and columns when a form is needed. |

## 3.2 Nested container structure

```text
JFrame (BorderLayout)
├── North: title JLabel
├── Center: JScrollPane → JList (results)
├── South: navigation JPanel (FlowLayout)
│   ├── Prev JButton
│   ├── Next JButton
│   └── Reserve JButton
└── Search row: JPanel (FlowLayout)
    ├── JTextField
    ├── JComboBox / Choice equivalent
    └── Search JButton
```

The implementation uses a Swing `JFrame`, `JPanel`, `JTextField`, `JComboBox`, `JList`, `JScrollPane` and `JButton`. The assignment asks for a title at the top and a search row below it; in a production layout these can be grouped into a single north panel containing a title and search row. The supplied sample is a simple functional layout demonstration.

## 3.3 Implementation

Source: `Task03/LibrarySearchScreen.java`.

## 3.4 Resizing test

Run the program and capture three genuine screenshots: (1) initial size, (2) medium resized window, and (3) maximized window. Verify that the results scroll pane grows while the search controls and navigation buttons retain their preferred sizes. Add screenshots to `Task03/screenshots/` and describe the observed behaviour. No screenshots are pre-created in this package.

## Alternative concept and complexity

`GridBagLayout` offers flexible constraints and can also resize the results component, but requires more constraint configuration. Absolute positioning with `setLayout(null)` and `setBounds` fixes coordinates and sizes; controls do not automatically adapt to resizing, font changes or platform differences. Layout computation is generally proportional to the number of child components for ordinary layouts (approximately **O(n)** for `n` components); exact costs vary by layout and hierarchy. Nested panels add **O(p)** container objects for `p` panels but improve separation and resizing behaviour. Absolute positioning may use fewer containers but transfers all geometry responsibility to the programmer.

---

# TASK 4 – EVENT-HANDLING FORENSICS

## 4.1 Locate the defect

The listener method is written as `mouseclicked`, but Java's `MouseAdapter` callback is named `mouseClicked` (capital `C`). Java is case-sensitive, so the method does not override the callback and is never invoked. The label is also never updated in the original code.

## 4.2 Failing interaction

Click to draw a dot, then minimize the window and restore it. The dot disappears because the original implementation draws directly using `getGraphics()`. That drawing is temporary and is not part of the component's persistent painting state. A repaint after restoring the window clears it.

## 4.3 Repaired program

`Task04/DrawPad.java` completes both TODOs: it stores each click as a `Point`, updates the label, calls `repaint()` and redraws all points in `paint(Graphics)`. Each dot is a 6 × 6 oval centered on the click coordinate.

## 4.4 Prevent recurrence

`@Override` tells the compiler that the method must override a method from a superclass or implement an interface method. With the misspelled `mouseclicked`, compilation fails with an error similar to: `method does not override or implement a method from a supertype`.

## Alternative concept and complexity

With a list of `n` points, each repaint draws all points: **O(n)** drawing operations and **O(n)** point storage. An off-screen `BufferedImage` can preserve the pixels; painting the image is approximately one image-copy operation (proportional to the pixel area, typically **O(width × height)** in the simplest model), while each click updates the image once. The image requires **O(width × height)** pixel storage, often more memory than a point list for sparse drawings. The image must be resized/recreated carefully when the drawing surface changes size.

---

# TASK 5 – SAME SCREEN, DIFFERENT EVENT-HANDLING STYLES

## 5.1 Four implementations

The `Task05` folder contains four separate programs:

1. `ConverterFrameListener.java` – frame implements `ActionListener`.
2. `ConverterInnerClass.java` – named inner handler class.
3. `ConverterAnonymous.java` – anonymous `ActionListener` class.
4. `ConverterLambda.java` – lambda listener shared by the Convert button and Enter key.

All versions use `WindowAdapter` for window closing. Each accepts a non-negative numeric INR amount and displays the converted amount to two decimal places. The example rates are fixed demonstration values (USD 0.012 and EUR 0.011 per INR), not live exchange rates.

## 5.2 Requirement change

The Enter key is handled by registering the same handler on the amount field as on the Convert button. The result is shown in the result label in the completed source code. To match a dialog-box requirement, replace the result-label update with `JOptionPane.showMessageDialog(this, message)` in each handler; the conversion and validation logic remains unchanged.

## 5.3 Comparison

| Style | Readability | Access to frame fields | Reuse | Ease of change |
|---|---|---|---|---|
| Frame implements listener | Simple for a very small frame; many actions can make the frame crowded. | Direct | Low; frame itself is the handler | Changes affect the frame class. |
| Named inner class | Clear named responsibility; useful for larger handlers. | Direct access to enclosing instance fields | Good within the enclosing class | Easy to extend and debug. |
| Anonymous inner class | Keeps a small handler near the component. | Direct | Usually limited to the registration location/reference | More verbose than lambda. |
| Lambda | Concise for a single-method listener. | Can access enclosing fields | Reusable if stored in a variable | Easy to attach to multiple triggers. |

For a large application, a reusable `Action` or separate controller/service class is preferable because one action can be shared by a button, menu item and keyboard shortcut without duplicating business logic.

## Alternative concept and complexity

A method reference such as `convertButton.addActionListener(e -> convert())` can be written as a method reference only when the method signature matches the functional interface; for `ActionListener`, use `e -> convert()` or define a compatible method. A shared Swing `Action` centralizes name, enabled state and action execution for multiple UI triggers. One shared action can reduce duplicated listener logic and the work needed to add a new trigger. The number of event triggers is `t`; attaching one action to `t` controls needs **O(t)** registrations/references, while the action implementation itself is shared. The four styles all require event dispatch; the main differences are organization, object allocation and maintainability rather than asymptotic dispatch time.

---

# TASK 6 – CAMPUS EVENT REGISTRATION DESKTOP APPLICATION

## Stage 1 – Users, screens, data and testable requirements

**Users:** Students (register) and cultural committee staff (view, filter and export).  
**Screens:** Registration form; registrations table/filter screen; confirmation/error dialogs.  
**Data:** Student register number, student name, event name, registration timestamp and registration ID.

**Rules / testable requirements**
1. A student must provide a non-empty register number, name and event.
2. The application must reject invalid or blank values with a clear message.
3. A student cannot register twice for the same event; the database enforces uniqueness on `(register_no, event_name)`.
4. Staff can view registrations and filter them by event.
5. Staff can export the currently loaded list as CSV.
6. Database reads and writes must run off the Swing event-dispatch thread using `SwingWorker`.
7. The GUI must show success, duplicate and failure feedback.

## Stage 2 – Screen and event flow

```text
Main JFrame + JMenuBar
  ├── Registration menu → Registration form
  │      └── Register button → validate → background JDBC insert
  │                              ├── success dialog/status
  │                              └── duplicate/error dialog/status
  └── View menu → Registration table
         ├── Event filter JComboBox + Filter button
         └── Export CSV button → Save file dialog → CSV file

Window close → confirm if a database task is running → close/cancel
```

## Stage 3 – Critical design decisions

- Validate required fields in the UI for quick feedback and again in the service layer for correctness.
- Use a unique database constraint for duplicate prevention; a pre-check alone is not safe under concurrent registrations.
- Use `PreparedStatement` to avoid SQL injection and parameter quoting errors.
- Use `SwingWorker` for database calls; update Swing components in `done()` on the event-dispatch thread.
- Use try-with-resources to close JDBC connections, statements and result sets.

## Stage 4 – Class design and handler pseudocode

```text
EventRegistrationApp (JFrame / UI)
        |
        v
RegistrationService (validation and application rules)
        |
        v
RegistrationDAO (JDBC queries and updates)
        |
        v
MySQL: campus_events.registrations
```

**Register action pseudocode**

```text
ON RegisterButton Action
    read registerNo, studentName, eventName
    IF any required field is blank THEN show validation message; RETURN
    disable RegisterButton
    START SwingWorker
        call RegistrationService.register(registerNo, studentName, eventName)
    WHEN worker completes
        enable RegisterButton
        IF insert succeeded THEN show success
        ELSE IF unique constraint violation THEN show duplicate message
        ELSE show database error
END
```

**Filter action pseudocode**

```text
ON FilterButton Action
    selectedEvent ← EventFilter.SelectedItem
    START SwingWorker
        rows ← RegistrationService.findByEvent(selectedEvent)
    WHEN worker completes
        clear JTable model
        add each returned row to JTable model
        display row count / error
END
```

## Stage 5 – Implementation and test cases

`Task06` contains a Swing application, service and DAO classes, plus `schema.sql`. It uses a paged query with `LIMIT` and `OFFSET`, and the table loads the first page asynchronously. The user can filter by event and export the currently displayed page to CSV.

**Test cases to execute and capture**

| Test | Input/action | Expected result |
|---|---|---|
| Normal registration | Valid register number, name and event | Success message and row stored in database |
| Invalid registration | Blank register number or name | Clear validation message; no insert |
| Duplicate registration | Submit same register number and event twice | Second insert rejected by unique key and duplicate message shown |
| Filter | Select an event and click Filter | Only matching registrations appear |
| Export | Click Export CSV | CSV file saved with currently displayed rows |

## Alternative concept – load all 100,000 rows at once

Loading every row at once transfers and stores all 100,000 records in the client. The database and transfer work are at least **O(N)** for `N = 100,000`, and the client model uses **O(N)** memory. If performed on the event-dispatch thread, the window can freeze until loading finishes. Pagination fetches only `P` rows per page, so each page uses **O(P)** client row memory and transfers approximately `P` records at a time; the database still needs to locate/skip rows depending on the query and indexes. `SwingWorker` keeps the UI responsive while a page is fetched. For large offsets, keyset pagination can outperform `OFFSET`, but the supplied assignment specifically asks for `LIMIT` and `OFFSET`.

---

## Final execution and submission checklist

- [ ] Execute every Java task individually.
- [ ] Capture real screenshots for each tested interaction and add them to the task folders.
- [ ] Add Task 6 database screenshots and the SQL script.
- [ ] Add student details and GitHub repository URL.
- [ ] Commit each task progressively to `CSA09-<RegisterNumber>-Assignment4`.
- [ ] Be ready to explain the event flow, listeners, layout decisions, JDBC layers and test results.
