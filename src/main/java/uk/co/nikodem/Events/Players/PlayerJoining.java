package uk.co.nikodem.Events.Players;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.advancements.AdvancementTab;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerLoadedEvent;
import net.minestom.server.event.player.PlayerSkinInitEvent;
import uk.co.nikodem.Events.EventHandler;
import uk.co.nikodem.Main;
import uk.co.nikodem.Proxy.BungeecordAbstractions;

public class PlayerJoining implements EventHandler {
    public void setup(GlobalEventHandler eventHandler) {
        eventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(Main.container);
            player.setRespawnPoint(Main.config.server.spawn);
            player.setGameMode(Main.DEFAULT_GAMEMODE);
            Main.logger.log("Players", player.getUsername()+" is connecting to the server...");
        });
        eventHandler.addListener(PlayerLoadedEvent.class, event -> {
            final Player player = event.getPlayer();

            if (Main.config.connection.player_validation) {
                System.out.println("Attempting to validate player..");
                BungeecordAbstractions.sendIncompatibleClientMessage(player);
                BungeecordAbstractions.sendRealProtocolVersionMessage(player);
            }

            String msg = player.getUsername()+" has joined the lobby";

            for (Player plr : Main.container.getPlayers()) {
                plr.sendMessage(Component.text(msg, NamedTextColor.YELLOW));
            }

            for (AdvancementTab tab : Main.advancementManager.getTabs()) {
                tab.addViewer(player);
            }

            Main.logger.log("Players", msg);
        });
        eventHandler.addListener(PlayerSkinInitEvent.class, event -> {
            PlayerSkin skin = event.getSkin();
            event.setSkin(skin);
        });
    }
}
