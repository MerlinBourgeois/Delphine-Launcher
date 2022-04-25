package fr.caemur.delphinelauncher.ui.pages.options;

import fr.caemur.delphinelauncher.App;
import fr.caemur.delphinelauncher.Main;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LogsPane extends OptionPane {
    public LogsPane(App app) {
        super(app, "Logs");
    }

    @Override
    public void init() {
        super.init();

        Label explLabel = new Label("Si vous rencontrez un problème, nous vous recommandons d'envoyer le contenu des logs quand vous demanderez de l'aide.");
        explLabel.setWrapText(true);

        Button showLogsButton = new Button("Afficher les logs");
        showLogsButton.setOnMouseClicked(event -> showLogs(app));

        getChildren().addAll(explLabel, showLogsButton);
    }

    public static void showLogs(App app) {
        ArrayList<String> logs = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(app.getFileManager().getLauncherLogFile());
            while (scanner.hasNextLine()) {
                logs.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Main.getLogger().err("Couldn't find log file :thinking:");
        }

        Stage logsStage = new Stage();
        logsStage.initOwner(app.getStage());
        logsStage.initModality(Modality.WINDOW_MODAL);
        logsStage.initStyle(StageStyle.UTILITY);
        logsStage.setTitle("Logs - " + app.getFileManager().getLauncherLogFile());

        logsStage.setMinWidth(600);
        logsStage.setWidth(600);
        logsStage.setMinHeight(300);
        logsStage.setHeight(300);

        GridPane logsGridPane = new GridPane();
        logsGridPane.getStylesheets().add(Main.class.getResource("/css/logs.css").toExternalForm());

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(30);
        rowConstraints.setMaxHeight(30);

        logsGridPane.getRowConstraints().addAll(new RowConstraints(), rowConstraints);

        ScrollPane logsScrollPane = new ScrollPane();
        GridPane.setHgrow(logsScrollPane, Priority.ALWAYS);
        GridPane.setVgrow(logsScrollPane, Priority.ALWAYS);
        logsScrollPane.setFitToHeight(true);
        logsScrollPane.setFitToWidth(true);
        VBox logsBox = new VBox();
        GridPane.setHgrow(logsBox, Priority.ALWAYS);

        for (String log : logs) {
            Label logLabel = new Label(log);

            String[] elements = log.split(" ");
            if (elements.length > 3) {
                if (elements[2].equals("[ERROR]:")) logLabel.setStyle("-fx-text-fill: red");
                else if (elements[2].equals("[WARN]:")) logLabel.setStyle("-fx-text-fill: orange");
            }

            logLabel.setOnMouseClicked(event1 -> logLabel.requestFocus());
            logLabel.setMaxWidth(Integer.MAX_VALUE);
            GridPane.setHgrow(logLabel, Priority.ALWAYS);
            logsBox.getChildren().add(logLabel);
        }

        logsScrollPane.setContent(logsBox);
        logsScrollPane.setVvalue(1);

        logsGridPane.add(logsScrollPane, 0, 0);

        Label copiedLabel = new Label("Logs copiés !");
        copiedLabel.setTranslateX(5);
        copiedLabel.setTextFill(Color.GREEN);
        copiedLabel.setOpacity(0);

        Button copyButton = new Button("Copier");
        GridPane.setHalignment(copyButton, HPos.RIGHT);
        copyButton.setOnMouseClicked(event -> {
            copyLogs(logs, copiedLabel);
        });

        logsGridPane.add(copiedLabel, 0, 1);
        logsGridPane.add(copyButton, 0, 1);

        Scene scene = new Scene(logsGridPane);
        scene.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.C) copyLogs(logs, copiedLabel);
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.ESCAPE) logsStage.close();
        });
        logsStage.setScene(scene);

        logsStage.show();
    }

    private static void copyLogs(ArrayList<String> logs, Label label) {
        App.copyToClipboard(String.join("\n", logs));
        label.setOpacity(1);
    }
}
