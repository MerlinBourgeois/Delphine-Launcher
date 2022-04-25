package fr.caemur.delphinelauncher.ui;

import fr.arinonia.arilibfx.AriLibFX;
import fr.caemur.delphinelauncher.App;
import fr.caemur.delphinelauncher.Main;
import fr.caemur.delphinelauncher.ui.pages.MainPage;
import fr.caemur.delphinelauncher.ui.pages.about.AboutPage;
import fr.caemur.delphinelauncher.ui.pages.login.LoginPage;
import fr.caemur.delphinelauncher.ui.pages.options.LogsPane;
import fr.caemur.delphinelauncher.ui.pages.options.OptionsPage;
import fr.caemur.delphinelauncher.utils.Constants;
import fr.caemur.delphinelauncher.ui.utils.ImageResize;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class PageManager {
    private final Stage stage;
    private final GridPane layout, mainPane;
    private String pageId = "none";
    private final DropShadow dropShadow;
    private final ArrayList<Label> tabs;
    private final ArrayList<Page> pages;
    private int selectedTab = -1, maxTab = 2;
    private final App app;
    private final HBox tabsBox;
    public static final Image LOADING_IMAGE = new Image(Main.class.getResource(Constants.ICON_LOADING).toExternalForm());
    private final Scene scene;
    private int currentPage = -1;
    private Animation currentAnimation, queuedAnimation;

    public enum AnimationType {
        SLIDE_IN_UP,
        SLIDE_IN_SIDE;
    }

    public PageManager(Stage stage, App app) {
        this.stage = stage;
        this.app = app;

        final int topPaneHeight = 110;

        dropShadow = new DropShadow();
        dropShadow.setSpread(.3);

        tabs = new ArrayList<>();
        pages = new ArrayList<>();
        pages.add(new LoginPage(this));
        pages.add(new MainPage(this));
        pages.add(new OptionsPage(this));
        pages.add(new AboutPage(this));

        layout = new GridPane();
        GridPane.setHgrow(layout, Priority.ALWAYS);
        GridPane.setVgrow(layout, Priority.ALWAYS);
        layout.setStyle(AriLibFX.setResponsiveBackground(Constants.BACKGROUND_IMAGE));
        layout.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());

        GridPane topPane = new GridPane();
        GridPane.setHgrow(topPane, Priority.ALWAYS);
        GridPane.setVgrow(topPane, Priority.ALWAYS);
        topPane.setPadding(new Insets(16));

        RowConstraints topPaneConstraints = new RowConstraints();
        topPaneConstraints.setValignment(VPos.TOP);
        topPaneConstraints.setMinHeight(topPaneHeight);
        topPaneConstraints.setMaxHeight(topPaneHeight);
        layout.getRowConstraints().addAll(topPaneConstraints, new RowConstraints());

        tabsBox = new HBox();
        tabsBox.setSpacing(24);
        tabsBox.setMinHeight(78);
        tabsBox.setMaxHeight(78);

        Label titleLabel = new Label(Constants.NAME);
        GridPane.setValignment(titleLabel, VPos.TOP);
        titleLabel.setStyle("-fx-font-size: 48px; -fx-text-fill: #fff;");
        titleLabel.setEffect(dropShadow);

        addTab("Jouer", "main");
        addTab("Options", "options");
        addTab("A propos", "about");

        tabsBox.getChildren().add(titleLabel);
        tabsBox.getChildren().addAll(tabs);

        HBox linksBox = new HBox();
        GridPane.setHgrow(linksBox, Priority.ALWAYS);
        GridPane.setVgrow(linksBox, Priority.ALWAYS);
        GridPane.setHalignment(linksBox, HPos.RIGHT);
        GridPane.setValignment(linksBox, VPos.CENTER);
        linksBox.setMinSize(95, 40);
        linksBox.setMaxSize(95, 40);
        linksBox.setSpacing(15);

        Image discordImage = ImageResize.resample(new Image(Main.class.getResource(Constants.ICON_DISCORD).toExternalForm()), 2);
        ImageView discordImageView = new ImageView(discordImage);
        GridPane.setValignment(discordImageView, VPos.CENTER);
        discordImageView.setFitHeight(40);
        discordImageView.setFitWidth(40);
        discordImageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);");

        Button discordButton = new Button();
        discordButton.setMinSize(40, 40);
        discordButton.setMaxSize(40, 40);
        discordButton.setGraphic(discordImageView);
        discordButton.setStyle("-fx-background-color: transparent;");
        discordButton.setOnMouseEntered(event -> layout.setCursor(Cursor.HAND));
        discordButton.setOnMouseExited(event -> layout.setCursor(Cursor.DEFAULT));
        discordButton.setOnMouseClicked(event -> App.openUrl(Constants.URL_DISCORD));

        Image linkImage = ImageResize.resample(new Image(Main.class.getResource(Constants.ICON_LINK).toExternalForm()), 2);
        ImageView linkImageView = new ImageView(linkImage);
        GridPane.setValignment(linkImageView, VPos.CENTER);
        linkImageView.setFitHeight(40);
        linkImageView.setFitWidth(40);
        linkImageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);");

        Button linkButton = new Button();
        linkButton.setMinSize(40, 40);
        linkButton.setMaxSize(40, 40);
        linkButton.setGraphic(linkImageView);
        linkButton.setStyle("-fx-background-color: transparent;");
        linkButton.setOnMouseEntered(event -> layout.setCursor(Cursor.HAND));
        linkButton.setOnMouseExited(event -> layout.setCursor(Cursor.DEFAULT));
        linkButton.setOnMouseClicked(event -> App.openUrl(Constants.URL_WEBSITE));

        linksBox.getChildren().addAll(linkButton, discordButton);

        Separator topPaneSeparator = new Separator();
        GridPane.setHgrow(topPaneSeparator, Priority.ALWAYS);
        GridPane.setVgrow(topPaneSeparator, Priority.ALWAYS);
        GridPane.setValignment(topPaneSeparator, VPos.BOTTOM);
        topPaneSeparator.setEffect(dropShadow);

        topPane.getChildren().addAll(tabsBox, topPaneSeparator, linksBox);

        mainPane = new GridPane();
        GridPane.setHgrow(mainPane, Priority.ALWAYS);
        GridPane.setVgrow(mainPane, Priority.ALWAYS);

        // Init pages
        for (Page page : pages) {
            page.init();
        }

        layout.add(topPane, 0, 0);
        layout.add(mainPane, 0, 1);

        // Shortcuts
        scene = new Scene(layout);
        scene.setOnKeyPressed(event -> {
            // Switch pages using ctrl/alt + left/right
            if (event.isAltDown() || event.isControlDown()) {
                if (event.getCode() == KeyCode.LEFT) {
                    if (selectedTab > 0) showPage(pageFromTabId(selectedTab - 1));

                } else if (event.getCode() == KeyCode.RIGHT) {
                    if (selectedTab >= 0 && selectedTab < maxTab) showPage(pageFromTabId(selectedTab + 1));

                } else if (event.isControlDown()) {
                    if (event.isShiftDown() && event.getCode() == KeyCode.D) {
                        if (!pageId.equals("login")) app.logout();

                    } else if (event.getCode() == KeyCode.L) {
                        LogsPane.showLogs(app);
                    }
                }
            } else if (event.getCode() == KeyCode.ENTER) {
                // validate login form using enter
                if (pageId.equals("login")) {
                    ((LoginPage) pages.get(currentPage)).auth();
                } else if (pageId.equals("main")) {
                    ((MainPage) pages.get(currentPage)).launch();
                }
            }
        });
        stage.setScene(scene);
    }

    private int pageFromTabId(int id) {
        return id == 1 || id == 2 ? id + 1 : 1;
    }

    public int pageFromId(String id) {
        return id.equals("main") ? 1 : id.equals("options") ? 2 : id.equals("about") ? 3 : 0;
    }

    public void showPage(int page) {
        showPage(page, AnimationType.SLIDE_IN_SIDE);
    }

    public void showPage(int page, AnimationType type) {
        if (currentPage == page) return;

        pageId = pages.get(page).getPageId();

        // remove underline from current tab
        if (selectedTab != -1) {
            tabs.get(selectedTab).setStyle("");
        }

        final boolean right = selectedTab < pages.get(page).getTabIndex();

        // add underline to new tab
        selectedTab = pages.get(page).getTabIndex();
        if (selectedTab != -1) {
            tabs.get(selectedTab).setStyle("-fx-underline: true");
        }

        // set current page
        currentPage = page;

        pages.get(currentPage).onShow();

        // play animation
        if (type == AnimationType.SLIDE_IN_SIDE)
            slideInSide(page, right);
        else if (type == AnimationType.SLIDE_IN_UP)
            slideInUp(page);
    }

    private void playQueuedAnimation() {
        currentAnimation = queuedAnimation;
        queuedAnimation = null;
        if (currentAnimation != null) currentAnimation.play();
    }

    private void slideInSide(int page, boolean right) {
        Animation animation = slideAnimation(right, false);
        animation.setOnFinished(event -> {
            mainPane.getChildren().clear();
            mainPane.getChildren().add(pages.get(page).getLayout());

            currentAnimation = slideAnimation(!right, true);

            currentAnimation.setOnFinished(eventIn -> {
                mainPane.setTranslateX(0);

                playQueuedAnimation();
            });
            currentAnimation.play();
        });

        if (currentAnimation != null && currentAnimation.getStatus() == Animation.Status.RUNNING)
            queuedAnimation = animation;
        else {
            currentAnimation = animation;
            currentAnimation.play();
        }
    }

    private void slideInUp(int page) {
        mainPane.setTranslateY(app.getStage().getHeight() / 2d);
        mainPane.setOpacity(0);

        mainPane.getChildren().clear();
        mainPane.getChildren().add(pages.get(page).getLayout());

        currentAnimation = new Transition() {
            {
                setCycleDuration(Duration.millis(750));
                setInterpolator(Interpolator.EASE_OUT);
            }

            @Override
            protected void interpolate(double frac) {
                mainPane.setTranslateY((1 - frac) * stage.getHeight() / 2d);
                mainPane.setOpacity(Math.pow(frac, 10));

                if (page == 1) {
                    for (Label tab : tabs) {
                        tab.setOpacity(frac);
                    }
                }
            }
        };

        currentAnimation.setOnFinished(eventIn -> {
            mainPane.setTranslateY(0);
            mainPane.setOpacity(1);

            playQueuedAnimation();
        });

        currentAnimation.play();
    }

    private void addTab(String name, String page) {
        Label tab = new Label(name);
        GridPane.setValignment(tab, VPos.TOP);
        tab.setTranslateY(24);
        tab.getStyleClass().add("tab-label");
        tab.setEffect(dropShadow);
        tab.setOnMouseEntered(event -> layout.setCursor(Cursor.HAND));
        tab.setOnMouseExited(event -> layout.setCursor(Cursor.DEFAULT));
        tab.setOnMouseClicked(event -> showPage(pageFromId(page)));

        tabs.add(tab);
    }

    private Animation slideAnimation(boolean right, boolean in) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(250));
                setInterpolator(in ? Interpolator.EASE_IN : Interpolator.EASE_OUT);
            }

            @Override
            protected void interpolate(double frac) {
                double translate = frac * stage.getWidth();
                if (in) translate = stage.getWidth() - translate;
                if (right) translate *= -1;
                mainPane.setTranslateX(translate);
                mainPane.setOpacity(in ? frac : 1 - frac);
            }
        };
    }

    public App getApp() {
        return app;
    }

    public void setTabVisibility(boolean visibility) {
        for (Label tab : tabs) {
            tab.setVisible(visibility);
        }
    }

    public ArrayList<Label> getTabs() {
        return tabs;
    }

    public Scene getScene() {
        return scene;
    }
}
