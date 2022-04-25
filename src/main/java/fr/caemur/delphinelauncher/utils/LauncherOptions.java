package fr.caemur.delphinelauncher.utils;

public class LauncherOptions {
    private int ram, connectionMode;
    private boolean saveEmail;
    private String email;

    public static final LauncherOptions DEFAULT_OPTIONS = new LauncherOptions(6144, 0, true, "");

    public LauncherOptions(int ram, int connectionMode, boolean saveEmail, String email) {
        this.connectionMode = connectionMode;
        this.ram = ram;
        this.saveEmail = saveEmail;
        this.email = email;
    }

    public int getRam() {
        return ram;
    }

    public int getConnectionMode() {
        return connectionMode;
    }

    public boolean isSaveEmail() {
        return saveEmail;
    }

    public String getEmail() {
        return email;
    }


    public void setRam(int ram) {
        this.ram = ram;
    }

    public void setConnectionMode(int connectionMode) {
        this.connectionMode = connectionMode;
    }

    public void setSaveEmail(boolean saveEmail) {
        this.saveEmail = saveEmail;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
