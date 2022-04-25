package fr.caemur.delphinelauncher.ui.pages.options;

import fr.caemur.delphinelauncher.ui.Page;
import fr.caemur.delphinelauncher.ui.PageManager;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class OptionsPage extends Page {
    public OptionsPage(PageManager pageManager) {
        super(pageManager, "options", 1);
    }

    @Override
    public void init() {
        super.init();

        ScrollPane scrollPane = new ScrollPane();
        GridPane.setHgrow(scrollPane, Priority.ALWAYS);
        GridPane.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setFitToWidth(true);

        VBox optionsBox = new VBox();
        GridPane.setHgrow(scrollPane, Priority.ALWAYS);
        GridPane.setVgrow(scrollPane, Priority.ALWAYS);
        optionsBox.setPadding(new Insets(30));
        optionsBox.setSpacing(30);

        optionsBox.setOnScroll(event -> {
            double deltaY = event.getDeltaY() * 5;
            double width = scrollPane.getContent().getBoundsInLocal().getWidth();
            double vValue = scrollPane.getVvalue();
            scrollPane.setVvalue(vValue + -deltaY / width); // deltaY/width to make the scrolling equally fast regardless of the actual width of the component
        });

        RamPane ramPane = new RamPane(pageManager.getApp());
        ramPane.init();

        ConnectionPane connectionPane = new ConnectionPane(pageManager.getApp());
        connectionPane.init();

        FilePane filePane = new FilePane(pageManager.getApp());
        filePane.init();

        LogsPane logsPane = new LogsPane(pageManager.getApp());
        logsPane.init();

        optionsBox.getChildren().addAll(ramPane, connectionPane, filePane, logsPane);
        scrollPane.setContent(optionsBox);
        layout.getChildren().add(scrollPane);
    }

    private GridPane getOptionPane() {
        GridPane pane = new GridPane();
        GridPane.setHgrow(pane, Priority.ALWAYS);
        GridPane.setVgrow(pane, Priority.ALWAYS);
        return pane;
    }
}
