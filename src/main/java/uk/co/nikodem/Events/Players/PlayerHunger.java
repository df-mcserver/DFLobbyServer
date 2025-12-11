package uk.co.nikodem.Events.Players;

import io.github.togar2.pvp.events.PlayerExhaustEvent;
import net.minestom.server.event.GlobalEventHandler;
import uk.co.nikodem.Events.EventHandler;

public class PlayerHunger implements EventHandler {
    @Override
    public void setup(GlobalEventHandler eventHandler) {
        eventHandler.addListener(PlayerExhaustEvent.class, event -> {
            if (!PlayerCombat.playersInCombat.contains(event.getPlayer().getUuid())) event.setCancelled(true);
        });
    }
}
