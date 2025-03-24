package org.example.black_sea_walnut.util;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;


class LogUtilTest {
    @Test
    void shouldLogInitNotification() {
        Logger mockLogger = Mockito.mock(Logger.class);
        LogUtil logUtil = new LogUtil();
        String message = "Test Initialization";
        logUtil.logInitNotification(message);
    }

    @Test
    void shouldLogInfoMessage() {
        Logger mockLogger = Mockito.mock(Logger.class);
        LogUtil logUtil = new LogUtil();
        String message = "Info message";
        logUtil.logInfo(message);
    }

    @Test
    void shouldLogErrorMessage() {
        Logger mockLogger = Mockito.mock(Logger.class);
        LogUtil logUtil = new LogUtil();
        String message = "Error message";
        Throwable exception = new RuntimeException("Test exception");
        logUtil.logError(message, exception);
    }

    @Test
    void shouldLogErrorMessage_WhenErrorNull() {
        Logger mockLogger = Mockito.mock(Logger.class);
        LogUtil logUtil = new LogUtil();
        String message = "Error message";
        logUtil.logError(message, null);
    }

    @Test
    void shouldLogWarningMessage() {
        Logger mockLogger = Mockito.mock(Logger.class);
        LogUtil logUtil = new LogUtil();
        String message = "Warning message";
        logUtil.logWarning(message);
    }
}