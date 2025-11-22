package uk.co.nikodem.Events.Players;

import io.github.togar2.pvp.events.PrepareAttackEvent;
import net.minestom.server.event.GlobalEventHandler;
import uk.co.nikodem.Events.EventHandler;

public class PlayerCombat implements EventHandler {
    @Override
    public void setup(GlobalEventHandler eventHandler) {
        eventHandler.addListener(PrepareAttackEvent.class, event -> {
            event.setCancelled(true);
        });
    }
}
