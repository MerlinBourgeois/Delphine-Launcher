package fr.caemur.delphinelauncher.utils;

public enum UpdateStep {
    PREREQUISITES("Chargement ..."),
    READ("Lecture du json"),
    DL_LIBS("Téléchargement des librairies"),
    DL_ASSETS("Téléchargement des assets"),
    EXTRACT_NATIVES("Extraction des natives"),
    FORGE("Installation de forge"),
    MODS("Téléchargement des mods"),
    EXTERNAL_FILES("Téléchargement des fichiers externes"),
    POST_EXECUTIONS("Execution du code final"),
    END("Installation complète");

    String details;

    UpdateStep(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

}