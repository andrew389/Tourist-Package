package logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalLogger {
    private Logger logger = Logger.getGlobal();
    private EmailSender emailSender = new EmailSender();

    public LocalLogger (){
        Handler fileHandler = null;
        try {
            fileHandler = new FileHandler("C:/Users/tatya/IdeaProjects/untitled/src/main/java/logger/logHandler.log");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Критична помилка", e);
            emailSender.sendEmail("Критична помилка " + e.getMessage());
        }
        fileHandler.setLevel(Level.ALL);
        logger.addHandler(fileHandler);
    }

    public void logInfo(String message) {
        logger.info(message);
    }

    public void logWarning(String message) {
        logger.warning(message);
    }

    public void logError(String message, Throwable throwable) {
        logger.log(Level.SEVERE, message, throwable);
        emailSender.sendEmail(message);
    }
}
