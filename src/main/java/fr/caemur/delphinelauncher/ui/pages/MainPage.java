package fr.caemur.delphinelauncher.ui.pages;

import fr.caemur.delphinelauncher.Main;
import fr.caemur.delphinelauncher.ui.Page;
import fr.caemur.delphinelauncher.ui.PageManager;
import fr.caemur.delphinelauncher.utils.Constants;
import fr.caemur.delphinelauncher.utils.UpdateStep;
import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Random;

public class MainPage extends Page {
    private Button playButton;
    private Label stepLabel;
    private static final int layoutPadding = 40;
    private ImageView loadingImageView;

    public MainPage(PageManager pageManager) {
        super(pageManager, "main", 0);
    }

    @Override
    public void init() {
        super.init();

        final int playButtonHeight = 60;

        layout.setPadding(new Insets(layoutPadding));

        GridPane mainPane = new GridPane();
        GridPane.setHgrow(mainPane, Priority.ALWAYS);
        GridPane.setVgrow(mainPane, Priority.ALWAYS);

        loadingImageView = new ImageView(PageManager.LOADING_IMAGE);
        loadingImageView.setFitWidth(40);
        loadingImageView.setFitHeight(40);

        stepLabel = new Label();
        GridPane.setHalignment(stepLabel, HPos.CENTER);
        GridPane.setValignment(stepLabel, VPos.BOTTOM);
        stepLabel.setStyle("-fx-text-fill:white;-fx-font-size:15;");
        stepLabel.setVisible(false);
        stepLabel.setManaged(false);

        playButton = new Button("Jouer");
        GridPane.setHalignment(playButton, HPos.CENTER);
        GridPane.setValignment(playButton, VPos.BOTTOM);
        playButton.setMinWidth(200);
        playButton.setMaxWidth(200);
        playButton.setMinHeight(playButtonHeight);
        playButton.setMaxHeight(playButtonHeight);
        playButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.4); -fx-background-radius: 20px; -fx-font-size: 26px;");
        playButton.setOnMouseEntered(event -> {
            layout.setCursor(Cursor.HAND);
            playButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5); -fx-background-radius: 16px; -fx-font-size: 27px;");
        });
        playButton.setOnMouseExited(event -> {
            layout.setCursor(Cursor.DEFAULT);
            playButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.4); -fx-background-radius: 20px; -fx-font-size: 26px;");
        });
        playButton.setOnMouseClicked(event -> launch());

        layout.add(mainPane, 0, 0);
        layout.add(stepLabel, 0, 1);
        layout.add(playButton, 0, 1);
    }

    @Override
    public void onShow() {
        pageManager.setTabVisibility(true);
    }

    public void launch() {
        playButton.setDisable(true);
        playButton.setText("");
        playButton.setGraphic(loadingImageView);
        new Thread(() -> {
            IProgressCallback callback = new IProgressCallback() {
                private Step previousStep, currentStep;
                private long previousDownloaded;

                @Override
                public void init(ILogger logger) {
                }

                @Override
                public void step(Step step) {
                    this.currentStep = step;
                }

                @Override
                public void update(long downloaded, long max) {
                    if (!stepLabel.isVisible() && currentStep.ordinal() > 0) {
                        Animation anim = new Transition() {
                            {
                                setCycleDuration(Duration.millis(300));
                            }

                            @Override
                            protected void interpolate(double frac) {
                                playButton.setOpacity(1 - frac);
                                playButton.setTranslateY(layoutPadding / 2.f * frac);
                                stepLabel.setOpacity(frac);
                            }
                        };
                        anim.setOnFinished(event1 -> {
                            playButton.setVisible(false);
                            playButton.setManaged(false);
                        });

                        Platform.runLater(() -> {
                            stepLabel.setVisible(true);
                            stepLabel.setManaged(true);
                            playButton.setText("Jouer");
                            playButton.setGraphic(null);
                            anim.play();
                        });
                    }

                    StringBuilder message = new StringBuilder(UpdateStep.valueOf(currentStep.name()).getDetails());
                    message.append(" - ");
                    message.append(Math.round(downloaded * 1000.d / max) / 10.d);
                    message.append("%");

                    Platform.runLater(() -> {
                        stepLabel.setText(message.toString());
                    });
                }
            };

            try {
                if (!pageManager.getApp().update(callback)) {
                    Platform.runLater(() -> {
                        playButton.setDisable(false);
                        playButton.setText("Jouer");
                        playButton.setGraphic(null);
                    });
                }
            } catch (UnknownHostException e) {
                Main.getLogger().err("Connection error when updating the game");
                Main.getLogger().printStackTrace(e);

                playButton.setDisable(false);
                playButton.setText("Jouer");
                playButton.setGraphic(null);
            }
        }).start();
    }
}
