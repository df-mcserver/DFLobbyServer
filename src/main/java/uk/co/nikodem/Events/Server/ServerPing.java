package uk.co.nikodem.Events.Server;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.ping.Status;
import uk.co.nikodem.Events.EventHandler;
import uk.co.nikodem.Main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;

public class ServerPing implements EventHandler {

    @Override
    public void setup(GlobalEventHandler eventHandler) {
        byte[] favicon = new byte[0];

        if (Main.config.ping.getFaviconPath() != null) {
            try (InputStream stream = new FileInputStream(Path.of(Main.config.ping.getFaviconPath()).toFile())) {
                favicon = Objects.requireNonNull(stream).readAllBytes();
            } catch (IOException | NullPointerException e) {
                Main.logger.warn("Favicon", "Favicon path given, but not found!");
                e.printStackTrace();
            }
        } else {
            favicon = null;
        }

        byte[] finalFavicon = favicon;
        eventHandler.addListener(ServerListPingEvent.class, event -> {
            int onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayerCount();

            event.setStatus(
                    Status.builder()
                            .description(MiniMessage.miniMessage().deserialize(Main.config.ping.getMotd()))
                            .favicon(finalFavicon)
                            .playerInfo(onlinePlayers, Main.config.ping.getFakeMaxPlayers())
                            .build()
            );
        });
    }
}
