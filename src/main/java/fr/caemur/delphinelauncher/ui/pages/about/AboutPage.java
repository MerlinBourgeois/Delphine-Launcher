package fr.caemur.delphinelauncher.ui.pages.about;

import fr.caemur.delphinelauncher.App;
import fr.caemur.delphinelauncher.Main;
import fr.caemur.delphinelauncher.ui.Page;
import fr.caemur.delphinelauncher.ui.PageManager;
import fr.caemur.delphinelauncher.ui.utils.ImageResize;
import fr.caemur.delphinelauncher.utils.Constants;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class AboutPage extends Page {
    private static final String[][] CREDITS_MEDIA = {
            new String[]{"Arrière plan", "https://imgur.com/gallery/PdzmYL9"},
            new String[]{"Icone discord depuis flaticon par Pixel Perfect", "https://icon54.com"},
            new String[]{"Icone lien depuis flaticon par Freepik", "https://www.freepik.com"},
            new String[]{"Icone rafraichissement depuis flaticon par Freepik", "https://www.freepik.com"},
            new String[]{"Animation de chargement", "https://loading.io"}
    };

    private static final String[][] CREDITS_LIB = {
            new String[]{"AriLibFX", "https://github.com/Arinonia/AriLibFX"},
            new String[]{"Discord RPC", "https://github.com/Vatuu/discord-rpc"},
            new String[]{"Flow Updater", "https://github.com/FlowArg/FlowUpdater"},
            new String[]{"GSON", "https://github.com/google/gson"},
            new String[]{"OpenAuth", "https://github.com/Litarvan/OpenAuth"},
            new String[]{"OpenLauncherLib (fork de flow arg)", "https://github.com/FlowArg/OpenLauncherLib"}
    };

    public AboutPage(PageManager pageManager) {
        super(pageManager, "about", 2);
    }

    @Override
    public void init() {
        super.init();

        ScrollPane scrollPane = new ScrollPane();
        GridPane.setHgrow(scrollPane, Priority.ALWAYS);
        GridPane.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setFitToWidth(true);
        scrollPane.getStylesheets().add(Main.class.getResource("/css/about.css").toExternalForm());
        scrollPane.setPadding(new Insets(30));

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.NEVER);
        columnConstraints.setPercentWidth(100.00);

        GridPane aboutGridPane = new GridPane();
        GridPane.setHgrow(aboutGridPane, Priority.ALWAYS);
        GridPane.setVgrow(aboutGridPane, Priority.ALWAYS);
        aboutGridPane.getColumnConstraints().add(columnConstraints);
        aboutGridPane.setPadding(new Insets(30));
        aboutGridPane.setVgap(10);
        aboutGridPane.setId("about-box");

        aboutGridPane.setOnScroll(event -> {
            double deltaY = event.getDeltaY() * 10;
            double width = scrollPane.getContent().getBoundsInLocal().getWidth();
            double vvalue = scrollPane.getVvalue();
            scrollPane.setVvalue(vvalue + -deltaY / width);
        });

        Label titleLabel = new Label("A propos");
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        titleLabel.getStyleClass().add("about-title");
        titleLabel.setStyle("-fx-font-size: 32px !important;");

        Label devLabel = new Label("Launcher développé par 1z2.");

        Image linkImage = ImageResize.resample(new Image(Main.class.getResource(Constants.ICON_LINK).toExternalForm()), 2);

        Label mediaLabel = new Label("Medias");
        GridPane.setHalignment(mediaLabel, HPos.CENTER);
        mediaLabel.getStyleClass().add("about-title");

        GridPane mediaGridPane = new GridPane();
        mediaGridPane.setHgap(7);
        for (int i = 0; i < CREDITS_MEDIA.length; i++) {
            final String[] credit = CREDITS_MEDIA[i];

            ImageView linkImageView = new ImageView(linkImage);
            GridPane.setValignment(linkImageView, VPos.CENTER);
            linkImageView.setFitHeight(12);
            linkImageView.setFitWidth(12);
            linkImageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);");

            Button linkButton = new Button();
            linkButton.setMinSize(12, 12);
            linkButton.setMaxSize(12, 12);
            linkButton.setGraphic(linkImageView);
            linkButton.setStyle("-fx-background-color: transparent;");
            linkButton.setOnMouseEntered(event -> layout.setCursor(Cursor.HAND));
            linkButton.setOnMouseExited(event -> layout.setCursor(Cursor.DEFAULT));
            linkButton.setOnMouseClicked(event -> App.openUrl(credit[1]));

            mediaGridPane.add(linkButton, 0, i);
            mediaGridPane.add(new Label(credit[0]), 1, i);
        }

        Label libLabel = new Label("Librairies");
        GridPane.setHalignment(libLabel, HPos.CENTER);
        libLabel.getStyleClass().add("about-title");

        GridPane libGridPane = new GridPane();
        libGridPane.setHgap(7);
        for (int i = 0; i < CREDITS_LIB.length; i++) {
            final String[] credit = CREDITS_LIB[i];

            ImageView linkImageView = new ImageView(linkImage);
            GridPane.setValignment(linkImageView, VPos.CENTER);
            linkImageView.setFitHeight(12);
            linkImageView.setFitWidth(12);
            linkImageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);");

            Button linkButton = new Button();
            linkButton.setMinSize(12, 12);
            linkButton.setMaxSize(12, 12);
            linkButton.setGraphic(linkImageView);
            linkButton.setStyle("-fx-background-color: transparent;");
            linkButton.setOnMouseEntered(event -> layout.setCursor(Cursor.HAND));
            linkButton.setOnMouseExited(event -> layout.setCursor(Cursor.DEFAULT));
            linkButton.setOnMouseClicked(event -> App.openUrl(credit[1]));

            libGridPane.add(linkButton, 0, i);
            libGridPane.add(new Label(credit[0]), 1, i);
        }

        aboutGridPane.add(titleLabel, 0, 0);
        aboutGridPane.add(devLabel, 0, 1);
        aboutGridPane.add(mediaLabel, 0, 2);
        aboutGridPane.add(mediaGridPane, 0, 3);
        aboutGridPane.add(libLabel, 0, 4);
        aboutGridPane.add(libGridPane, 0, 5);

        scrollPane.setContent(aboutGridPane);
        layout.getChildren().add(scrollPane);
    }
}
