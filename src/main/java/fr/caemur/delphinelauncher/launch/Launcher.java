package fr.caemur.delphinelauncher.launch;

import fr.caemur.delphinelauncher.App;
import fr.caemur.delphinelauncher.Main;
import fr.caemur.delphinelauncher.discord.DiscordRpcHandler;
import fr.caemur.delphinelauncher.utils.Constants;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import javafx.application.Platform;

import java.io.File;

public class Launcher {
    private ExternalLaunchProfile launchProfile;

    public Launcher(AuthInfos authInfos, File gameFolder, int ram) {
        final GameInfos delphineGameInfo = new GameInfos(Constants.NAME, gameFolder.toPath(),
                new GameVersion("1.12.2", GameType.V1_8_HIGHER),
                new GameTweak[]{GameTweak.FORGE});

        try {
            launchProfile = MinecraftLauncher.createExternalProfile(delphineGameInfo, GameFolder.FLOW_UPDATER, authInfos);

            final int minRam = Math.max(ram - 1024, 1152);
            launchProfile.getVmArgs().add("-Xmx" + ram + "M");
            launchProfile.getVmArgs().add("-Xms" + minRam + "M");

            Main.getLogger().info("Min ram=" + minRam + " / Max ram=" + ram);
        } catch (LaunchException e) {
            e.printStackTrace();
        }
    }

    public boolean launch(App app) {

        ExternalLauncher launcher = new ExternalLauncher(launchProfile);

        try {
            Process process = launcher.launch();

            try {
                Thread.sleep(5000);

                DiscordRpcHandler.PRESENCE_IN_GAME.startTimestamp = System.currentTimeMillis() / 1000L;
                app.setDiscordPresence(DiscordRpcHandler.PRESENCE_IN_GAME);

                Platform.runLater(() -> app.getStage().hide());

                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (LaunchException e) {
            Main.getLogger().err("Error when launching the game");
            Main.getLogger().printStackTrace(e);
            return false;
        }
        return true;
    }
}
