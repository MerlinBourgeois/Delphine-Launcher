package fr.caemur.delphinelauncher;

import fr.caemur.delphinelauncher.utils.Constants;
import fr.flowarg.flowlogger.Logger;
import javafx.application.Application;

import javax.swing.*;
import java.io.File;

public class Main {
    private static Logger logger;

    public static void main(String[] args) {
        try {
            Class.forName("javafx.application.Application");
            Application.launch(App.class, args);
        } catch (ClassNotFoundException e) {
            logger.warn("JavaFX not found");
            JOptionPane.showMessageDialog(null, "Erreur :\nJavaFX introuvable :/", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void initLogger(File file) {
        logger = new Logger(Constants.NAME, file.toPath());
    }
}
