package uk.co.nikodem.Events.Players;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import uk.co.nikodem.Events.EventHandler;
import uk.co.nikodem.Main;
import uk.co.nikodem.Proxy.PlayerSender;
import uk.co.nikodem.Proxy.PlayerValidation;
import uk.co.nikodem.Server.EditMode;

public class PlayerDisconnecting implements EventHandler {
    @Override
    public void setup(GlobalEventHandler eventHandler) {
        eventHandler.addListener(PlayerDisconnectEvent.class, event -> {
            final Player player = event.getPlayer();

            String msg = player.getUsername()+" has left the lobby";
            if (PlayerSender.getIsBeingSent(player)) {
                msg = player.getUsername()+" has joined another server";
                PlayerSender.removePlayerBeingSent(player);
            }

            for (Player plr : Main.container.getPlayers()) {
                plr.sendMessage(Component.text(msg, NamedTextColor.YELLOW));
            }

            PlayerValidation.removePlayerValidation(player);
            PlayerMoving.lastBlockWasPortal.remove(player.getUuid());
            PlayerCombat.playerLeave(player);
            EditMode.removePlayer(player);

            Main.logger.log("Players", msg);
        });
    }
}
