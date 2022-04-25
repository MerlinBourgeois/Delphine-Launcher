package fr.caemur.delphinelauncher.ui.pages.options;

import fr.caemur.delphinelauncher.App;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;

public class ConnectionPane extends OptionPane {

    public ConnectionPane(App app) {
        super(app, "Connexion");
    }

    @Override
    public void init() {
        super.init();

        CheckBox saveMailCheckBox = new CheckBox("Enregistrer mon adresse email");
        saveMailCheckBox.setSelected(app.getOptions().isSaveEmail());
        saveMailCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            app.getOptions().setSaveEmail(newValue);

            if (!newValue)
                app.getOptions().setEmail("");
        });

        Separator sep = new Separator();
        sep.setPadding(new Insets(10));
        sep.setOpacity(0);

        Button deleteConnectionInfoButton = new Button("Se déconnecter");
        deleteConnectionInfoButton.setOnMouseClicked(event -> app.logout());

        getChildren().addAll(saveMailCheckBox, sep, deleteConnectionInfoButton);
    }
}
