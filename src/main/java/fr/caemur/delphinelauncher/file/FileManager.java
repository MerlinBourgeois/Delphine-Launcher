package fr.caemur.delphinelauncher.file;

import java.io.File;

public class FileManager {
    private final String serverName;

    public FileManager(String serverName) {
        this.serverName = serverName;
    }

    public File createGameDir() {
        final String userHome = System.getProperty("user.home");
        final String fileSeparator = File.separator;

        switch (OS.getCurrentPlatform()) {
            case WINDOWS:
                return new File(userHome + fileSeparator + "AppData" + fileSeparator + "Roaming" + fileSeparator + "." + serverName);

            case MACOS:
                return new File(userHome + fileSeparator + "Library" + fileSeparator + "Application Support" + fileSeparator + this.serverName);

            default:
                return new File(userHome + fileSeparator + "." + this.serverName);
        }
    }

    public File getAssetsFolder() {
        return new File(createGameDir(), "assets");
    }

    public File getNativeFolder() {
        return new File(createGameDir(), "natives");
    }

    public File getLibsFolder() {
        return new File(createGameDir(), "libs");
    }

    public File getGameFolder() {
        return createGameDir();
    }

    public File getRuntimeFolder() {
        return new File(createGameDir(), "runtime");
    }

    public File getModsFolder() {
        return new File(createGameDir(), "mods");
    }

    public File getLauncherOptionsFile() {
        return new File(createGameDir(), "launcherOptions.txt");
    }

    public File getLauncherLogFile() {
        return new File(createGameDir(), "launcher.log");
    }

    public File getAuthFile() {
        return new File(createGameDir(), "ai");
    }
}
