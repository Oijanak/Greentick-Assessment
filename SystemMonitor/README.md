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

### 1. Clone or Download the Project

Clone the project repository or download the files into your local machine.

### 2. Compile the Java Code

Use the following command to compile the `ProcessLogUtils` class:

```bash
javac SystemMonitor.java
java SystemMonitor
```

### 3. Monitor Output and Stop

- The program will display alerts on the console and write them to the alert log file.
- Press Ctrl + C to stop the program from monitoring the log file.
