package uk.co.nikodem.Events.Players;

import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import uk.co.nikodem.Events.EventHandler;
import uk.co.nikodem.Server.EditMode;

public class RestrictedPlayerActions implements EventHandler {
    @Override
    public void setup(GlobalEventHandler eventHandler) {
        eventHandler.addListener(PlayerBlockBreakEvent.class, event -> {
            if (!EditMode.isInEditMode(event.getPlayer())) event.setCancelled(true);
        });
        eventHandler.addListener(PlayerStartDiggingEvent.class, event -> {
            if (!EditMode.isInEditMode(event.getPlayer())) event.setCancelled(true);
        });
        eventHandler.addListener(PlayerBlockInteractEvent.class, event -> {
            if (!EditMode.isInEditMode(event.getPlayer())) event.setCancelled(true);
        });
    }
}
