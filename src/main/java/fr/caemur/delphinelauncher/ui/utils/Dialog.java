package fr.caemur.delphinelauncher.ui.utils;

import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

public class Dialog {
    public static void errorDialog(String errorMessage) {
        Alert errorDialog = new Alert(Alert.AlertType.WARNING);

        errorDialog.setTitle("Erreur");
        errorDialog.setHeaderText(null);
        errorDialog.setContentText(errorMessage);
        errorDialog.initStyle(StageStyle.UTILITY);
        errorDialog.showAndWait();
    }
}