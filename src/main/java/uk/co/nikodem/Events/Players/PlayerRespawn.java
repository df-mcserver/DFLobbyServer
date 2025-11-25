package uk.co.nikodem.Events.Players;

import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerRespawnEvent;
import uk.co.nikodem.Events.EventHandler;
import uk.co.nikodem.Main;

public class PlayerRespawn implements EventHandler {
    @Override
    public void setup(GlobalEventHandler eventHandler) {
        eventHandler.addListener(PlayerRespawnEvent.class, event -> {
            Player plr = event.getPlayer();

            if (PlayerCombat.playersInCombat.contains(plr.getUuid())) {
                if (PlayerMoving.lastBlockWasPortal.containsKey(plr.getUuid())) PlayerMoving.lastBlockWasPortal.replace(plr.getUuid(), true);
                else PlayerMoving.lastBlockWasPortal.put(plr.getUuid(), true);

                event.setRespawnPosition(Main.config.nether.spawnLocation);

                PlayerCombat.playerRespawn(plr);
            }
        });
    }
}
