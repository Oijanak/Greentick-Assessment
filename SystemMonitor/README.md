# Log Monitoring System

## Description

This project is a simple log monitoring system that scans a log file for suspicious activity, such as failed logins, unauthorized access attempts, malicious activities, authentication failures, and access denial events. The system detects patterns related to security issues and writes alerts to a separate log file with the timestamp when these patterns occur.

The program continuously monitors the log file, and whenever it detects a suspicious pattern, it writes an alert with the corresponding timestamp to the alert log file.

## Features

- Monitors a log file for specific suspicious patterns (e.g., "failed login", "unauthorized access", etc.).
- Detects various timestamp formats (e.g., `YYYY-MM-DD HH:mm:ss`, `MMM dd yyyy HH:mm:ss`, etc.).
- Writes alerts to a separate log file with the timestamp of the suspicious event.
- Supports continuous monitoring of the log file.

## Steps to Run

### Prerequisites

- Java Development Kit (JDK) installed (version 8 or higher).
- An existing log file that you want to monitor in the file path of System Monitor.

### 1. Compile the Java Code

```bash
cd SystemMonitor
javac SystemMonitor.java
java SystemMonitor
```

### 2. Monitor Output and Stop

- The program will display alerts on the console and write them to the alert log file.
- Press Ctrl + C to stop the program from monitoring the log file.

## Assumptions

- The log file is continuously growing, and the program will monitor only new entries after the last read.
- Timestamps in the log entries are in one of the predefined formats specified in the TIMESTAMP_PATTERNS array.
- The suspicious patterns are hardcoded in the SUSPICIOUS_PATTERNS array, and only these patterns will be detected.
- The system will continue running until manually stopped (e.g., via Ctrl + C).

## Limitations

- The program only checks for the suspicious patterns specified in the code. If you want to monitor additional patterns, you will need to modify the SUSPICIOUS_PATTERNS array.
- The program checks for the most common timestamp formats but may miss other timestamp formats that don't match the predefined patterns.
