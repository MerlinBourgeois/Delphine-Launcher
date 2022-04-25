package fr.caemur.delphinelauncher.ui.pages.login;

import fr.caemur.delphinelauncher.ui.PageManager;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class LoginForm extends GridPane {
    private final PageManager pageManager;
    private final LoginPage loginPage;
    private GridPane formPane;
    private TextField emailField, passwordField;
    private Button loginButton;
    private ImageView loadingImageView;
    private LoginMethodPane loginMethodPane;

    public LoginForm(PageManager pageManager, LoginPage loginPage, int formWidth) {
        super();
        this.pageManager = pageManager;
        this.loginPage = loginPage;

        DropShadow dropShadow = new DropShadow(4, Color.BLACK);
        dropShadow.setSpread(.65);

        GridPane.setHgrow(this, Priority.ALWAYS);
        GridPane.setVgrow(this, Priority.ALWAYS);
        setPadding(new Insets(20));

        formPane = new GridPane();
        GridPane.setHgrow(formPane, Priority.ALWAYS);
        GridPane.setVgrow(formPane, Priority.ALWAYS);
        GridPane.setHalignment(formPane, HPos.RIGHT);
        GridPane.setValignment(formPane, VPos.CENTER);
        formPane.setMaxWidth(formWidth);
        formPane.setMinWidth(formWidth);
        formPane.setMaxHeight(316);
        formPane.setMinHeight(316);


        Label emailLabel = new Label("Adresse mail");
        GridPane.setValignment(emailLabel, VPos.TOP);
        emailLabel.setTranslateX(20);
        emailLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        emailLabel.setEffect(dropShadow);


        emailField = new TextField();
        GridPane.setValignment(emailField, VPos.TOP);
        emailField.setMaxWidth(formWidth);
        emailField.setMinWidth(formWidth);
        emailField.setMaxHeight(50);
        emailField.setMinHeight(50);
        emailField.setTranslateY(24);
        emailField.setPadding(new Insets(10, 20, 10, 20));


        Label passwordLabel = new Label("Mot de passe");
        GridPane.setValignment(passwordLabel, VPos.TOP);
        passwordLabel.setTranslateX(20);
        passwordLabel.setTranslateY(84);
        passwordLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        passwordLabel.setEffect(dropShadow);


        passwordField = new PasswordField();
        GridPane.setValignment(passwordField, VPos.TOP);
        passwordField.setMaxWidth(formWidth);
        passwordField.setMinWidth(formWidth);
        passwordField.setMaxHeight(50);
        passwordField.setMinHeight(50);
        passwordField.setTranslateY(108);
        passwordField.setPadding(new Insets(10, 20, 10, 20));

        loadingImageView = new ImageView(PageManager.LOADING_IMAGE);
        loadingImageView.setFitHeight(40);
        loadingImageView.setFitWidth(40);

        loginMethodPane = new LoginMethodPane(pageManager, formWidth);
        loginMethodPane.setTranslateY(168);

        loginButton = new Button("Connexion");
        GridPane.setValignment(loginButton, VPos.TOP);
        loginButton.setMaxWidth(formWidth);
        loginButton.setMinWidth(formWidth);
        loginButton.setMaxHeight(50);
        loginButton.setMinHeight(50);
        loginButton.setTranslateY(266);
        loginButton.setId("login-button");
        loginButton.setOnMouseEntered(event -> setCursor(Cursor.HAND));
        loginButton.setOnMouseExited(event -> setCursor(Cursor.DEFAULT));
        loginButton.setOnMouseClicked(event -> auth());

        formPane.getChildren().addAll(emailLabel, emailField, passwordLabel, passwordField, loginButton, loginMethodPane);
        getChildren().add(formPane);
    }

    public void auth() {
        loginPage.auth(emailField.getText(), passwordField.getText());
    }

    public void onShow() {
        enable();
        passwordField.clear();
        if (!pageManager.getApp().getOptions().isSaveEmail()) emailField.clear();
        else emailField.setText(pageManager.getApp().getOptions().getEmail());
        setOpacity(1);
        setTranslateY(0);

        Platform.runLater(() -> {
            (!emailField.getText().isEmpty() && passwordField.getText().isEmpty()
                    ? passwordField : emailField).requestFocus();
        });
    }

    /**
     * Enables login button and remove loading animation
     */
    public void enable() {
        loginButton.setGraphic(null);
        loginButton.setDisable(false);
        loginButton.setText("Connexion");
    }

    /**
     * Disables login button and shows loading animation
     */
    public void disable() {
        loginButton.setDisable(true);
        loginButton.setGraphic(loadingImageView);
        loginButton.setText("");
    }
}
