package fr.caemur.delphinelauncher.update;

import fr.caemur.delphinelauncher.Main;
import fr.caemur.delphinelauncher.ui.utils.Dialog;
import fr.caemur.delphinelauncher.utils.Constants;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.json.CurseModPackInfo;
import fr.flowarg.flowupdater.download.json.ExternalFile;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.utils.builderapi.BuilderException;
import fr.flowarg.flowupdater.versions.AbstractForgeVersion;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.flowupdater.versions.VersionType;
import javafx.application.Platform;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class Updater {
    private FlowUpdater updater;

    /**
     * Init flow updater
     *
     * @return fail/success
     */
    public boolean init(IProgressCallback progressCallback) {
        final UpdaterOptions updaterOptions;
        final VanillaVersion vanillaVersion;
        final AbstractForgeVersion forgeVersion;

        final CurseModPackInfo modpack = new CurseModPackInfo(Constants.MODPACK_PROJECT_ID, Constants.MODPACK_FILE_ID, true);

        List<ExternalFile> externalFiles;
        try {
            externalFiles = ExternalFile.getExternalFilesFromJson(Constants.URL_EXTERNAL_FILES);
        } catch (IllegalStateException e) {
            externalFiles = Collections.emptyList();
            Main.getLogger().warn("Failed to load external files");
            Main.getLogger().printStackTrace(e);
        }

        try {
            vanillaVersion = new VanillaVersion.VanillaVersionBuilder()
                    .withName("1.12.2")
                    .withVersionType(VersionType.FORGE)
                    .build();
        } catch (BuilderException e) {
            Main.getLogger().err("Error when creating vanilla version");
            Main.getLogger().printStackTrace(e);
            return false;
        } catch (IllegalStateException e) {
            Main.getLogger().err("(IllegalStateException) Network error when creating vanilla version");
            Main.getLogger().printStackTrace(e);
            Platform.runLater(() -> Dialog.errorDialog("Erreur de la mise à jour\nVérifiez que vous êtes bien connecté à internet"));
            return false;
        }

        try {
            forgeVersion = new ForgeVersionBuilder(ForgeVersionBuilder.ForgeVersionType.NEW)
                    .withForgeVersion("14.23.5.2854")
                    .withCurseModPack(modpack)
                    .build();
        } catch (BuilderException e) {
            Main.getLogger().err("Error when creating forge version");
            Main.getLogger().printStackTrace(e);
            return false;
        }

        try {
            updaterOptions = new UpdaterOptions.UpdaterOptionsBuilder()
                    .withSilentRead(false)
                    .withEnableCurseForgePlugin(true)
                    .withEnableOptifineDownloaderPlugin(false)
                    .build();
        } catch (BuilderException e) {
            Main.getLogger().err("Error when creating updater options");
            Main.getLogger().printStackTrace(e);
            return false;
        }

        try {
            updater = new FlowUpdater.FlowUpdaterBuilder()
                    .withVanillaVersion(vanillaVersion)
                    .withForgeVersion(forgeVersion)
                    .withUpdaterOptions(updaterOptions)
                    .withLogger(Main.getLogger())
                    .withProgressCallback(progressCallback)
                    .withExternalFiles(externalFiles)
                    .build();
        } catch (BuilderException e) {
            Main.getLogger().err("Error when creating updater");
            Main.getLogger().printStackTrace(e);
            return false;
        }
        return true;
    }

    /**
     * Start the game updating process
     *
     * @return Success
     */
    public boolean update(File gameFolder) {
        try {
            updater.update(gameFolder.toPath());
        } catch (Exception e) {
            Main.getLogger().err("Error when updating the game");
            Main.getLogger().printStackTrace(e);
            return false;
        }

        return true;
    }
}
