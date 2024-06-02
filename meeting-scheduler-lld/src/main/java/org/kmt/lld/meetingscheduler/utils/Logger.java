package org.kmt.lld.meetingscheduler.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The Logger class is implemented as a Singleton to ensure a single instance is used
 * throughout the application. It provides methods for logging messages with different
 * severity levels: info, error, and debug. This implementation also supports enabling
 * or disabling debug messages and formats log messages with timestamps.
 *
 * Key Features:
 * - Singleton pattern to ensure a single instance
 * - Thread-safe lazy initialization
 * - Log methods for different severity levels (info, error, debug)
 * - Configurable debug logging
 * - Timestamps included in log messages
 */
public class Logger {

    private static Logger instance;
    private boolean debugEnabled = true; // Enable or disable debug logging

    // Private constructor to prevent instantiation
    private Logger(){

    }

    /**
     * Provides access to the single instance of the Logger class.
     * This method uses lazy initialization with double-checked locking
     * to ensure that the instance is created in a thread-safe manner.
     */
    public static Logger getInstance() {
        // Check if instance is null, without synchronization for performance
        if(instance == null) {
            // Enter a synchronized block to ensure that only one thread can create the instance.
            synchronized (Logger.class){
                // Checks again if the instance is still null, To handle the case where multiple threads might have passed the first check.
                if(instance == null){
                    // Create new instance
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    public void info(String message){
        System.out.println(formatMessage("INFO", message));
    }

    public void error(String message){
        System.err.println(formatMessage("ERROR", message));
    }

    public void debug(String message){
        if(debugEnabled){
            System.out.println(formatMessage("DEBUG", message));
        }
    }

    private String formatMessage(String level, String message){
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        return String.format("%s [%s]: %s", timestamp, level, message);
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }


}
