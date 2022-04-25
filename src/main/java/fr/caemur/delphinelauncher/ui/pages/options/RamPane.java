package fr.caemur.delphinelauncher.ui.pages.options;

import fr.caemur.delphinelauncher.App;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.lang.management.ManagementFactory;

public class RamPane extends OptionPane {
    public RamPane(App app) {
        super(app, "Ram");
    }

    @Override
    public void init() {
        super.init();

        GridPane sliderPane = new GridPane();
        GridPane.setHgrow(sliderPane, Priority.ALWAYS);
        GridPane.setVgrow(sliderPane, Priority.ALWAYS);
        sliderPane.setMinWidth(500);
        sliderPane.setMaxWidth(500);

        final int ramMb = app.getOptions().getRam();
        final double ramGb = ramMb / 1024f;

        Label ramLabel = new Label(Math.floor(ramGb) != ramGb
                ? ramMb + " Mo"
                : (int) ramGb + " Go");
        GridPane.setHalignment(ramLabel, HPos.CENTER);
        ramLabel.setStyle("-fx-font-weight: bold;");

        int sliderValue = (int) Math.round(ramGb);
        if (sliderValue < 6) sliderValue = 6;
        if (sliderValue > 12) sliderValue = 12;

        Slider ramSlider = new Slider(6, 12, sliderValue);
        GridPane.setHgrow(ramSlider, Priority.ALWAYS);
        GridPane.setVgrow(ramSlider, Priority.ALWAYS);
        ramSlider.setMajorTickUnit(1);
        ramSlider.setBlockIncrement(1);
        ramSlider.setShowTickMarks(true);
        ramSlider.setMinorTickCount(0);
        ramSlider.setSnapToTicks(true);
        ramSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            ramLabel.setText(newValue.intValue() + " Go");
        });
        ramSlider.valueChangingProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                app.getOptions().setRam((int) (Math.round(ramSlider.getValue()) * 1024));
            }
        });

        sliderPane.add(ramLabel, 0, 0);
        sliderPane.add(ramSlider, 0, 1);

        Label explLabel = new Label("Nous vous recommandons d'allouer au moins 6 Go de ram au jeu, et de laisser" +
                " au moins 2 Go à votre système.");

        getChildren().addAll(sliderPane, explLabel);

        try {
            final long memorySize = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean())
                    .getTotalPhysicalMemorySize();

            if (memorySize < 500000000)
                return;

            Label totalRamLabel = new Label("Votre pc dispose d'environ " + Math.round(memorySize / 1073741824d) + " Go de ram");

            getChildren().add(totalRamLabel);
        } catch (Exception ignored) {
        }
    }
}
