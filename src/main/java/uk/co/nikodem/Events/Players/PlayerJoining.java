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
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import uk.co.nikodem.Events.EventHandler;
import uk.co.nikodem.Main;
import uk.co.nikodem.Proxy.BungeecordAbstractions;
import uk.co.nikodem.Proxy.PlayerValidation;

public class PlayerJoining implements EventHandler {
    public void setup(GlobalEventHandler eventHandler) {
        eventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            Player player = event.getPlayer();
            event.setSpawningInstance(Main.container);
            player.setRespawnPoint(Main.config.server.spawn);
            player.setGameMode(Main.DEFAULT_GAMEMODE);
            Main.logger.log("Players", player.getUsername()+" is connecting to the server...");

            String msg = player.getUsername()+" has joined the lobby";

            for (Player plr : Main.container.getPlayers()) {
                plr.sendMessage(Component.text(msg, NamedTextColor.YELLOW));
            }

            Main.logger.log("Players", msg);
        });
        eventHandler.addListener(PlayerLoadedEvent.class, event -> {
            Player player = event.getPlayer();

            for (AdvancementTab tab : Main.advancementManager.getTabs()) {
                tab.addViewer(player);
            }

            if (Main.config.connection.player_validation) {
                if (PlayerValidation.playerIsValidated(player)) return;
                Main.logger.log("Players", "Attempting to validate "+player.getUsername());
                BungeecordAbstractions.sendIncompatibleClientMessage(player);
                BungeecordAbstractions.sendRealProtocolVersionMessage(player);
                BungeecordAbstractions.sendIsGeyserMessage(player);

                Main.scheduler.scheduleTask(() ->
                        {
                            if (!PlayerValidation.getPlayerValidation(player).hasFinishedValidation()) {
                                PlayerValidation.sendValidationTimeout(player);
                            }
                        },
                        TaskSchedule.seconds(5), TaskSchedule.stop(), ExecutionType.TICK_END);
            }
        });
        eventHandler.addListener(PlayerSkinInitEvent.class, event -> {
            PlayerSkin skin = event.getSkin();
            event.setSkin(skin);
        });
    }
}
