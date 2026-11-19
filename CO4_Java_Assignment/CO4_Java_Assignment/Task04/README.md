# Task 4 – Event-Handling Forensics
Compile: `javac DrawPad.java`  
Run: `java DrawPad`

The correctly spelled `mouseClicked` is overridden. Click positions are stored and redrawn in `paint`, so dots remain after repaint/minimize/restore. Capture screenshots before and after minimizing/restoring. To reproduce the compile-time check, remove `@Override` and change `mouseClicked` to `mouseclicked`; the method no longer overrides the superclass method, which `@Override` detects.
