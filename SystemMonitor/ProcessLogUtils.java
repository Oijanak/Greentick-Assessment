import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ProcessLogUtils {

    private static final String[] SUSPICIOUS_PATTERNS = {
        "failed login",
        "unauthorized access",
        "malicious activity detected",
        "authentication failure",
        "access denied",
        "error"
    };

   
    private static final List<Pattern> TIMESTAMP_PATTERNS = Arrays.asList(
        Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"),  // YYYY-MM-DD HH:mm:ss
        Pattern.compile("[A-Za-z]{3} \\d{2} \\d{4} \\d{2}:\\d{2}:\\d{2}"),  // MMM dd yyyy HH:mm:ss
        Pattern.compile("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}"),  // yyyy-MM-dd'T'HH:mm:ss
        Pattern.compile("\\d{2}/[A-Za-z]{3}/\\d{4}:\\d{2}:\\d{2}:\\d{2}"),  // dd/MMM/yyyy:HH:mm:ss
        Pattern.compile("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}"),  // MM/DD/YYYY HH:mm:ss
        Pattern.compile("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}")   // YYYY/MM/DD HH:mm:ss
    );

    public static void monitorLogFile(String logFilePath, String alertLogFilePath) {
        try (BufferedWriter alertWriter = new BufferedWriter(new FileWriter(alertLogFilePath, true));
            RandomAccessFile logFile = new RandomAccessFile(logFilePath, "r")) {

            long filePointer = 0;
            Pattern[] suspiciousPatterns = compileSuspiciousPatterns();

            System.out.println("Starting log monitoring... Press Ctrl+C to stop.");

            while (true) {
                long fileLength = logFile.length();

                if (fileLength < filePointer) {
                    filePointer = 0;
                }

                if (fileLength > filePointer) {
                    logFile.seek(filePointer);
                    String line;
                    while ((line = logFile.readLine()) != null) {
                        processLogLine(line, suspiciousPatterns, alertWriter);
                    }
                    filePointer = logFile.getFilePointer();
                }

               
                Thread.sleep(1000); 
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Pattern[] compileSuspiciousPatterns() {
        Pattern[] patterns = new Pattern[SUSPICIOUS_PATTERNS.length];
        for (int i = 0; i < SUSPICIOUS_PATTERNS.length; i++) {
            patterns[i] = Pattern.compile(SUSPICIOUS_PATTERNS[i], Pattern.CASE_INSENSITIVE);
        }
        return patterns;
    }

    private static void processLogLine(String line, Pattern[] suspiciousPatterns, BufferedWriter alertWriter) throws IOException {
       
        String timestamp = extractTimestamp(line); 

        for (Pattern pattern : suspiciousPatterns) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
              
                String alertMessage = String.format(
                    "ALERT: %s DETECTED AT %s",
                    pattern.pattern().toUpperCase(), timestamp != null ? timestamp : "UNKNOWN"
                );

                System.out.println(alertMessage);
                
                alertWriter.write(alertMessage);
                alertWriter.newLine();
                alertWriter.flush(); 
            }
        }
    }

    private static String extractTimestamp(String line) {
        for (Pattern pattern : TIMESTAMP_PATTERNS) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                return matcher.group();
            }
        }
        return null;
    }
}
