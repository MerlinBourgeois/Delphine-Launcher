package fr.caemur.delphinelauncher.auth;

public class DelphineAuthInfo {
    private final String username, accessToken, refreshToken, playerID;

    public DelphineAuthInfo(String username, String accessToken, String refreshToken, String playerID) {
        this.username = username;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.playerID = playerID;
    }

    public String getUsername() {
        return username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getPlayerID() {
        return playerID;
    }

    @Override
    public String toString() {
        return String.format("Auth info: username=%s, access token=%s, refresh/client token=%s, id=%s",
                username, accessToken == null ? "null" : "not null", refreshToken == null ? "null" : "not null", playerID);
    }
}
