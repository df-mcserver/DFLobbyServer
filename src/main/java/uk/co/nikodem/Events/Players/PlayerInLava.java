package uk.co.nikodem.Events.Players;

import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.block.Block;
import uk.co.nikodem.Events.EventHandler;
import uk.co.nikodem.Main;
import uk.co.nikodem.Server.EditMode;

public class PlayerInLava implements EventHandler {
    @Override
    public void setup(GlobalEventHandler eventHandler) {
        if (Main.config.minigames.parkour.isEnabled()) {
            eventHandler.addListener(PlayerMoveEvent.class, event -> {
                if (EditMode.isInEditMode(event.getPlayer())) return;
                Block newBlock = event.getInstance().getBlock(event.getNewPosition().sub(0D, 1D, 0D));
                if (newBlock.isLiquid() && newBlock.name().equals("minecraft:lava")) {
                    Player plr = event.getPlayer();
                    plr.teleport(Main.config.minigames.parkour.getRespawnLocation());
                }
            });
        }
    }
}
