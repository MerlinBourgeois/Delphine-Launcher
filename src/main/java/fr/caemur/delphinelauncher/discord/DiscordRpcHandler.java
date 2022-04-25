package fr.caemur.delphinelauncher.discord;

import fr.caemur.delphinelauncher.Main;
import fr.caemur.delphinelauncher.utils.Constants;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class DiscordRpcHandler {
    public static final DiscordRichPresence PRESENCE_LAUNCHER = new DiscordRichPresence.Builder("Sur le launcher")
            .setDetails(Constants.URL_WEBSITE_SHORT)
            .setBigImage(Constants.DISCORD_PRESENCE_IMAGE, Constants.DISCORD_PRESENCE_IMAGE_ALT)
            .setSmallImage(Constants.DISCORD_PRESENCE_SMALL_IMAGE, Constants.DISCORD_PRESENCE_SMALL_IMAGE_ALT)
            .build();

    public static final DiscordRichPresence PRESENCE_IN_GAME = new DiscordRichPresence.Builder("En jeu")
            .setDetails(Constants.URL_WEBSITE_SHORT)
            .setBigImage(Constants.DISCORD_PRESENCE_IMAGE, Constants.DISCORD_PRESENCE_IMAGE_ALT)
            .setSmallImage(Constants.DISCORD_PRESENCE_SMALL_IMAGE, Constants.DISCORD_PRESENCE_SMALL_IMAGE_ALT)
            .build();

    public void init() {
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder()
                .setReadyEventHandler(user -> {
                    Main.getLogger().info("Discord RPC is ready !");
                })
                .setDisconnectedEventHandler((i, s) -> {
                    Main.getLogger().warn("Discord RPC disconnected");
                })
                .build();

        DiscordRPC.discordInitialize(Constants.DISCORD_APPLICATION_ID, handlers, true);
        DiscordRPC.discordRunCallbacks();
    }

    public void setPresence(DiscordRichPresence presence) {
        DiscordRPC.discordUpdatePresence(presence);
        Main.getLogger().info("Discord rpc set to \"" + presence.state + "\"");
    }

    public void shutdown() {
        DiscordRPC.discordShutdown();
    }
}
