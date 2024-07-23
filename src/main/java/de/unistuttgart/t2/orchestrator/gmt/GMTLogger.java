package de.unistuttgart.t2.orchestrator.gmt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Logging of custom messages specific for energy measurements executed with the Green Metrics Tool.
 *
 * @author davidkopp
 */
@Component
public class GMTLogger {

    private final boolean gmtLoggingEnabled;

    public GMTLogger(@Value("${t2.gmt-logging.enabled:false}") boolean gmtLoggingEnabled) {
        this.gmtLoggingEnabled = gmtLoggingEnabled;
    }

    private final Logger LOG_RAW = LoggerFactory.getLogger("raw");

    public void orderStart(String sessionId) {
        writeGmtNote(String.format("Order started (%s)", sessionId));
    }

    public void orderComplete(String sessionId) {
        writeGmtNote(String.format("Order completed (%s)", sessionId));
        writeGmtNote("GMT_SCI_R=1"); // https://docs.green-coding.berlin/docs/measuring/sci/
    }

    /**
     * Write a note for GMT
     * <a href="https://docs.green-coding.berlin/docs/measuring/usage-scenario/#read-notes-stdout-format-specification">→Note format specification</a>
     *
     * @param note Note
     */
    private void writeGmtNote(String note) {
        if (gmtLoggingEnabled) {
            long currentTimeMicroseconds = System.currentTimeMillis() * 1000;
            LOG_RAW.info("{} {}", currentTimeMicroseconds, note);
        }
    }
}
