package fr.caemur.delphinelauncher.ui.pages.options;

import fr.caemur.delphinelauncher.App;
import javafx.scene.control.Button;

public class FilePane extends OptionPane {
    public FilePane(App app) {
        super(app, "Fichiers");
    }

    @Override
    public void init() {
        super.init();

        Button openGameFolderButton = new Button("Ouvrir le dossier du jeu");
        openGameFolderButton.setOnMouseClicked(event -> App.openFolder(app.getGameFolder()));

        getChildren().add(openGameFolderButton);
    }
}
