package fr.caemur.delphinelauncher;

import fr.caemur.delphinelauncher.auth.Auth;
import fr.caemur.delphinelauncher.auth.DelphineAuthInfo;
import fr.caemur.delphinelauncher.discord.DiscordRpcHandler;
import fr.caemur.delphinelauncher.file.FileManager;
import fr.caemur.delphinelauncher.file.savers.AuthSaver;
import fr.caemur.delphinelauncher.file.savers.OptionSaver;
import fr.caemur.delphinelauncher.launch.Launcher;
import fr.caemur.delphinelauncher.ui.PageManager;
import fr.caemur.delphinelauncher.update.Updater;
import fr.caemur.delphinelauncher.utils.Constants;
import fr.caemur.delphinelauncher.utils.LauncherOptions;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.arikia.dev.drpc.DiscordRichPresence;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class App extends Application {
    private PageManager pageManager;
    private final DiscordRpcHandler discordRpcHandler = new DiscordRpcHandler();

    private LauncherOptions launcherOptions;
    private DelphineAuthInfo authInfo;
    private FileManager fileManager;
    private File gameFolder;

    private Stage stage;

    @Override
    public void start(Stage stage) {
        fileManager = new FileManager(Constants.GAME_FOLDER_NAME);
        gameFolder = fileManager.createGameDir();
        gameFolder.mkdir();
        Main.initLogger(fileManager.getLauncherLogFile());

        this.stage = stage;
        stage.setTitle(Constants.LAUNCHER_NAME);
        stage.setWidth(Constants.DEFAULT_WIDTH);
        stage.setHeight(Constants.DEFAULT_HEIGHT);
        stage.setMinWidth(Constants.DEFAULT_WIDTH);
        stage.setMinHeight(Constants.DEFAULT_HEIGHT);
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
        stage.centerOnScreen();

        discordRpcHandler.init();
        discordRpcHandler.setPresence(DiscordRpcHandler.PRESENCE_LAUNCHER);

        // remove discord presence and save data when program stops
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Main.getLogger().info("Shutdown hook (shutdown discord rpc & save)");

            discordRpcHandler.shutdown();

            OptionSaver.save(fileManager.getLauncherOptionsFile(), launcherOptions);
            AuthSaver.save(fileManager.getAuthFile(), authInfo);
        }));

        // options
        OptionSaver.initFile(fileManager.getLauncherOptionsFile());
        launcherOptions = OptionSaver.readOptions(fileManager.getLauncherOptionsFile());

        // auth saver
        try {
            fileManager.getAuthFile().createNewFile();
        } catch (IOException e) {
            Main.getLogger().err("Error when creating auth info file");
            Main.getLogger().printStackTrace(e);
        }

        authInfo = AuthSaver.load(fileManager.getAuthFile());

        pageManager = new PageManager(stage, this);

        // tries to refresh auth ; shows main page on success, login page on failure
        String startupPage = "login";

        if (authInfo != null) {
            final DelphineAuthInfo result = launcherOptions.getConnectionMode() == 1
                    ? Auth.refreshMojang(authInfo.getAccessToken(), authInfo.getRefreshToken())
                    : Auth.refreshMicrosoft(authInfo.getRefreshToken());


            if (result != null) {
                authInfo = result;
                startupPage = "main";
            }
        }
        Main.getLogger().info(String.valueOf(authInfo));

        stage.show();

        pageManager.showPage(pageManager.pageFromId(startupPage), PageManager.AnimationType.SLIDE_IN_UP);
    }

    // GETTERS

    public Stage getStage() {
        return stage;
    }

    public LauncherOptions getOptions() {
        return launcherOptions;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public File getGameFolder() {
        return gameFolder;
    }

    // AUTHENTICATION

    /**
     * Authenticate a user, based on the connection mode stored in the launcherOptions
     *
     * @return Success
     */
    public boolean auth(String email, String password) {
        authInfo = launcherOptions.getConnectionMode() == 1
                ? Auth.authMojang(email, password)
                : Auth.authMicrosoft(email, password);

        return authInfo != null;
    }

    /**
     * Log out the user, and shows the login page
     */
    public void logout() {
        Main.getLogger().info("Logout");
        authInfo = null;
        pageManager.showPage(pageManager.pageFromId("login"), PageManager.AnimationType.SLIDE_IN_UP);
    }

    // UPDATE

    /**
     * Update the game
     *
     * @return Success
     */
    public boolean update(IProgressCallback progressCallback) throws UnknownHostException {
        Updater updater = new Updater();
        if (updater.init(progressCallback)) {
            if (updater.update(fileManager.createGameDir())) {
                return launch();
            } else {
                Main.getLogger().warn("Failed to update the game :(");
                return false;
            }
        } else {
            Main.getLogger().warn("Failed to init flow updater");
            return false;
        }
    }

    // LAUNCHING

    /**
     * Launch the game
     *
     * @return Success
     */
    public boolean launch() {
        AuthInfos launchAuthInfo;

        if (authInfo == null) {
            launchAuthInfo = new AuthInfos("Crack", "faketoken", "fakeuuid");
            Main.getLogger().warn("Launching in offline mode");
        } else {
            launchAuthInfo = new AuthInfos(authInfo.getUsername(), authInfo.getAccessToken(), authInfo.getPlayerID());
        }

        Launcher launcher = new Launcher(new AuthInfos("dev", "d07aeeb6352f4cef9bc45c221ab469f5", "d07aeeb6352f4cef9bc45c221ab469f5"), fileManager.getGameFolder(), launcherOptions.getRam());

        if (!launcher.launch(this)) {
            Main.getLogger().warn("Failed to launch game >:(");
            return false;
        }
        return true;
    }

    // UTILITIES

    /**
     * Changes the active discord presence
     */
    public void setDiscordPresence(DiscordRichPresence presence) {
        discordRpcHandler.setPresence(presence);
    }

    /**
     * Opens an url in the user's default browser
     */
    public static void openUrl(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException exception) {
            Main.getLogger().warn(exception.getMessage());
        }
    }

    /**
     * Opens a folder in the system file explorer
     */
    public static void openFolder(File file) {
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            Main.getLogger().warn("Erreur lors de l'ouverture du dossier " + file);
        }
    }


    /**
     * Copy a string to the system clipboard
     */
    public static void copyToClipboard(String text) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
    }
}
