package co.xarx.trix.util;

import org.slf4j.LoggerFactory;

/**
 * Created by misael on 24/10/2015.
 */
public class Logger {
    final static org.slf4j.Logger logger = LoggerFactory.getLogger(Logger.class);
    /**
     * Get the underlying SLF4J logger.
     */
    public org.slf4j.Logger underlying() {
        return logger;
    }

    /**
     * Returns <code>true</code> if the logger instance has TRACE level logging enabled.
     */
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    /**
     * Returns <code>true</code> if the logger instance has DEBUG level logging enabled.
     */
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    /**
     * Returns <code>true</code> if the logger instance has INFO level logging enabled.
     */
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    /**
     * Returns <code>true</code> if the logger instance has WARN level logging enabled.
     */
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    /**
     * Returns <code>true</code> if the logger instance has ERROR level logging enabled.
     */
    public boolean isErrorEnabled() {
        return logger.isWarnEnabled();
    }

    /**
     * Logs a message with the TRACE level.
     *
     * @param message message to log
     */
    public static void trace(String message) {
        logger.trace(message);
    }

    /**
     * Logs a message with the TRACE level.
     *
     * @param message message to log
     * @param args The arguments to apply to the message string
     */
    public static void trace(String message, Object... args) {
        logger.trace(message, args);
    }

    /**
     * Logs a message with the TRACE level, with the given error.
     *
     * @param message message to log
     * @param error associated exception
     */
    public static void trace(String message, Throwable error) {
        logger.trace(message, error);
    }

    /**
     * Logs a message with the DEBUG level.
     *
     * @param message Message to log
     */
    public static void debug(String message) {
        logger.debug(message);
    }

    /**
     * Logs a message with the DEBUG level.
     *
     * @param message Message to log
     * @param args The arguments to apply to the message string
     */
    public static void debug(String message, Object... args) {
        logger.debug(message, args);
    }

    /**
     * Logs a message with the DEBUG level, with the given error.
     *
     * @param message Message to log
     * @param error associated exception
     */
    public static void debug(String message, Throwable error) {
        logger.debug(message, error);
    }

    /**
     * Logs a message with the INFO level.
     *
     * @param message message to log
     */
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * Logs a message with the INFO level.
     *
     * @param message message to log
     * @param args The arguments to apply to the message string
     */
    public static void info(String message, Object... args) {
        logger.info(message, args);
    }

    /**
     * Logs a message with the INFO level, with the given error.
     *
     * @param message message to log
     * @param error associated exception
     */
    public static void info(String message, Throwable error) {
        logger.info(message, error);
    }

    /**
     * Log a message with the WARN level.
     *
     * @param message message to log
     */
    public static void warn(String message) {
        logger.warn(message);
    }

    /**
     * Log a message with the WARN level.
     *
     * @param message message to log
     * @param args The arguments to apply to the message string
     */
    public static void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    /**
     * Log a message with the WARN level, with the given error.
     *
     * @param message message to log
     * @param error associated exception
     */
    public static void warn(String message, Throwable error) {
        logger.warn(message, error);
    }

    /**
     * Log a message with the ERROR level.
     *
     * @param message message to log
     */
    public static void error(String message) {
        logger.error(message);
    }

    /**
     * Log a message with the ERROR level.
     *
     * @param message message to log
     * @param args The arguments to apply to the message string
     */
    public static void error(String message, Object... args) {
        logger.error(message, args);
    }

    /**
     * Log a message with the ERROR level, with the given error.
     *
     * @param message message to log
     * @param error associated exception
     */
    public static void error(String message, Throwable error) {
        logger.error(message, error);
    }
}
