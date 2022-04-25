package fr.caemur.delphinelauncher.ui.pages.options;

import fr.caemur.delphinelauncher.App;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class OptionPane extends VBox {
    private final String title;
    protected final App app;

    public OptionPane(App app, String title) {
        super();
        this.app = app;
        this.title = title;
    }

    public void init() {
        GridPane.setHgrow(this, Priority.ALWAYS);
        GridPane.setVgrow(this, Priority.ALWAYS);

        getStyleClass().add("option-pane");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("option-pane-title");

        getChildren().add(titleLabel);
    }
}
