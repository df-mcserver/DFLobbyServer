package uk.co.nikodem.Events.Players;

import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerPickBlockEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.registry.RegistryData;
import uk.co.nikodem.Events.EventHandler;
import uk.co.nikodem.Server.EditMode;

public class PlayerPickBlock implements EventHandler {
    @Override
    public void setup(GlobalEventHandler eventHandler) {
        eventHandler.addListener(PlayerPickBlockEvent.class, event -> {
            Player plr = event.getPlayer();
            if (EditMode.isInEditMode(plr)) {
                Block block = event.getBlock();
                RegistryData.BlockEntry entry = block.registry();
                Material material = entry.material();
                if (material == null) return;
                plr.setItemInMainHand(ItemStack.of(material));
            }
        });
    }
}
