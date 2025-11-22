package uk.co.nikodem.Events.Players;

import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.world.DimensionType;
import uk.co.nikodem.Events.EventHandler;
import uk.co.nikodem.Main;
import uk.co.nikodem.Server.EditMode;

import java.util.HashMap;
import java.util.UUID;

public class PlayerMoving implements EventHandler {
    public static HashMap<UUID, Boolean> lastBlockWasPortal = new HashMap<>();

    @Override
    public void setup(GlobalEventHandler eventHandler) {
        eventHandler.addListener(PlayerMoveEvent.class, event -> {
            Player plr = event.getPlayer();
            if (plr.getInstance().getBlock(event.getNewPosition()).equals(Block.NETHER_PORTAL)) {
                boolean is_allowed = Main.config.nether.enabled;

                if (EditMode.isInEditMode(plr)) is_allowed = false;
                if (Boolean.TRUE.equals(lastBlockWasPortal.get(plr.getUuid()))) is_allowed = false;

                if (lastBlockWasPortal.containsKey(plr.getUuid())) lastBlockWasPortal.replace(plr.getUuid(), true);
                else lastBlockWasPortal.put(plr.getUuid(), true);

                if (is_allowed) {
                    if (plr.getInstance().getDimensionType() == DimensionType.OVERWORLD) plr.setInstance(Main.nether_container, Main.config.nether.spawnLocation);
                    else plr.setInstance(Main.container, Main.config.nether.portalLocation);
                }
            } else {
                if (lastBlockWasPortal.containsKey(plr.getUuid())) lastBlockWasPortal.replace(plr.getUuid(), false);
                else lastBlockWasPortal.put(plr.getUuid(), false);
            }
        });
    }
}
