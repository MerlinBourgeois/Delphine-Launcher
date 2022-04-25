package fr.caemur.delphinelauncher.auth;

import fr.caemur.delphinelauncher.Main;
import fr.litarvan.openauth.AuthPoints;
import fr.litarvan.openauth.AuthenticationException;
import fr.litarvan.openauth.Authenticator;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.model.AuthAgent;
import fr.litarvan.openauth.model.response.AuthResponse;
import fr.litarvan.openauth.model.response.RefreshResponse;

import java.util.UUID;

public class Auth {
    private static final Authenticator MOJANG_AUTH = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);
    private static final MicrosoftAuthenticator MICROSOFT_AUTH = new MicrosoftAuthenticator();

    // MOJANG

    public static DelphineAuthInfo authMojang(String email, String password) {
        AuthResponse response;

        try {
            response = MOJANG_AUTH.authenticate(AuthAgent.MINECRAFT, email, password, UUID.randomUUID().toString());
        } catch (AuthenticationException e) {
            Main.getLogger().err("Mojang authentication error");
            Main.getLogger().printStackTrace(e);
            return null;
        }

        return new DelphineAuthInfo(response.getSelectedProfile().getName(), response.getAccessToken(),
                response.getClientToken(), response.getSelectedProfile().getId());
    }

    public static DelphineAuthInfo refreshMojang(String accessToken, String clientToken) {
        RefreshResponse response;
        try {
            response = MOJANG_AUTH.refresh(accessToken, clientToken);
        } catch (AuthenticationException e) {
            Main.getLogger().err("Error when refreshing mojang token");
            Main.getLogger().printStackTrace(e);
            return null;
        }

        return new DelphineAuthInfo(response.getSelectedProfile().getName(), response.getAccessToken(),
                response.getClientToken(), response.getSelectedProfile().getId());
    }

    public static void invalidateMojang(String accessToken, String clientToken) {
        try {
            MOJANG_AUTH.invalidate(accessToken, clientToken);
        } catch (AuthenticationException e) {
            Main.getLogger().err("Error when invalidating mojang token");
            Main.getLogger().printStackTrace(e);
        }
    }

    // MICROSOFT

    public static DelphineAuthInfo authMicrosoft(String email, String password) {
        MicrosoftAuthResult response;

        try {
            response = MICROSOFT_AUTH.loginWithCredentials(email, password);
        } catch (MicrosoftAuthenticationException e) {
            Main.getLogger().err("Microsoft authentication error");
            Main.getLogger().printStackTrace(e);
            return null;
        }

        return new DelphineAuthInfo(response.getProfile().getName(), response.getAccessToken(),
                response.getRefreshToken(), response.getProfile().getId());
    }

    public static DelphineAuthInfo refreshMicrosoft(String refreshToken) {
        MicrosoftAuthResult response;

        try {
            response = MICROSOFT_AUTH.loginWithRefreshToken(refreshToken);
        } catch (MicrosoftAuthenticationException e) {
            Main.getLogger().err("Error when refreshing microsoft token");
            Main.getLogger().printStackTrace(e);
            return null;
        }

        return new DelphineAuthInfo(response.getProfile().getName(), response.getAccessToken(),
                response.getRefreshToken(), response.getProfile().getId());
    }
}
