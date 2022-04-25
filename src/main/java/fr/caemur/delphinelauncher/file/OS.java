package fr.caemur.delphinelauncher.file;

public enum OS {
    LINUX("linux", new String[]{"linux", "unix"}, (byte) 2),
    WINDOWS("window", new String[]{"win"}, (byte) 0),
    MACOS("osx", new String[]{"mac"}, (byte) 1),
    UNKNOWN("unknown", new String[0], (byte) 3);

    private final String name;
    private final String[] aliases;
    private final byte num;

    OS(String name, String[] aliases, byte num) {
        this.name = name;
        this.aliases = ((aliases == null) ? new String[0] : aliases);
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public static OS getCurrentPlatform() {
        final String osName = System.getProperty("os.name").toLowerCase();
        for (final OS os : values()) {
            for (final String alias : os.getAliases()) {
                if (osName.contains(alias)) {
                    return os;
                }
            }
        }
        return OS.UNKNOWN;
    }

    public byte getNum() {
        return num;
    }
}
