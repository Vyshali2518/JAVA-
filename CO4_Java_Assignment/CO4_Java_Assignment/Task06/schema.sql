CREATE DATABASE IF NOT EXISTS campus_events;
USE campus_events;

CREATE TABLE IF NOT EXISTS registrations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    register_no VARCHAR(30) NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    event_name VARCHAR(100) NOT NULL,
    registered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_student_event UNIQUE (register_no, event_name)
);

INSERT INTO registrations (register_no, student_name, event_name)
VALUES ('DEMO001', 'Demo Student', 'Tech Fest')
ON DUPLICATE KEY UPDATE student_name = VALUES(student_name);
