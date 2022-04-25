package fr.caemur.delphinelauncher.ui.pages.login;

import fr.caemur.delphinelauncher.ui.PageManager;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class LoginMethodPane extends GridPane {
    private final Button microsoftButton, mojangButton;
    private final PageManager pageManager;

    public LoginMethodPane(PageManager pageManager, int formWidth) {
        this.pageManager = pageManager;

        final DropShadow dropShadow = new DropShadow(4, Color.BLACK);
        dropShadow.setSpread(.65);

        setHgrow(this, Priority.ALWAYS);
        setVgrow(this, Priority.ALWAYS);
        setHalignment(this, HPos.CENTER);
        setValignment(this, VPos.TOP);
        setMinWidth(formWidth);
        setMaxWidth(formWidth);
        setMinHeight(64);
        setMaxHeight(64);
        setPadding(new Insets(0, 30, 0, 30));

        Label connectWithLabel = new Label("Se connecter avec :");
        GridPane.setHalignment(connectWithLabel, HPos.CENTER);
        GridPane.setValignment(connectWithLabel, VPos.TOP);
        connectWithLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-style: italic;");
        connectWithLabel.setEffect(dropShadow);

        microsoftButton = new Button("Microsoft");
        GridPane.setHgrow(microsoftButton, Priority.ALWAYS);
        GridPane.setVgrow(microsoftButton, Priority.ALWAYS);
        GridPane.setValignment(microsoftButton, VPos.TOP);
        microsoftButton.setMinWidth(formWidth / 2.f - 30);
        microsoftButton.setMaxWidth(formWidth / 2.f - 30);
        microsoftButton.setMinHeight(40);
        microsoftButton.setMaxHeight(40);
        microsoftButton.setTranslateY(24);
        microsoftButton.setId("microsoft-login");

        mojangButton = new Button("Mojang");
        GridPane.setHgrow(mojangButton, Priority.ALWAYS);
        GridPane.setVgrow(mojangButton, Priority.ALWAYS);
        GridPane.setHalignment(mojangButton, HPos.RIGHT);
        GridPane.setValignment(mojangButton, VPos.TOP);
        mojangButton.setMinWidth(formWidth / 2.f - 30);
        mojangButton.setMaxWidth(formWidth / 2.f - 30);
        mojangButton.setMinHeight(40);
        mojangButton.setMaxHeight(40);
        mojangButton.setTranslateY(24);
        mojangButton.setId("mojang-login");

        microsoftButton.setOnMouseClicked(event -> setTab(false));
        mojangButton.setOnMouseClicked(event -> setTab(true));

        (pageManager.getApp().getOptions().getConnectionMode() == 0
                ? microsoftButton
                : mojangButton
        ).getStyleClass().add("selected-connection");

        getChildren().addAll(connectWithLabel, microsoftButton, mojangButton);
    }

    private void setTab(boolean mojang) {
        if (pageManager.getApp().getOptions().getConnectionMode() != (mojang ? 1 : 0)) {
            pageManager.getApp().getOptions().setConnectionMode((mojang ? 1 : 0));

            (mojang ? microsoftButton : mojangButton).getStyleClass().remove("selected-connection");
            (mojang ? mojangButton : microsoftButton).getStyleClass().add("selected-connection");
        }
    }
}
