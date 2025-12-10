package uk.co.nikodem.Events.Players;

import io.github.togar2.pvp.events.PrepareAttackEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import uk.co.nikodem.Events.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerCombat implements EventHandler {
    public static List<UUID> playersInCombat = new ArrayList<>();

    @Override
    public void setup(GlobalEventHandler eventHandler) {
        eventHandler.addListener(PrepareAttackEvent.class, event -> {
            if (event.getTarget() instanceof Player target && event.getEntity() instanceof Player attacker) {
                if (!playersInCombat.contains(target.getUuid()) || !playersInCombat.contains(attacker.getUuid())) event.setCancelled(true);
            } else event.setCancelled(true);
        });
    }

    public static void playerZoneCheck(boolean inZone, Player plr) {
        if (inZone) {
            if (!playersInCombat.contains(plr.getUuid())) {
                playersInCombat.add(plr.getUuid());

                plr.getInventory().changeHeld(plr, 0, 0);
                plr.setHeldItemSlot((byte) 0);

                plr.sendActionBar(Component.text("You are now in the PVP Zone!", NamedTextColor.RED));

                plr.getInventory().setItemStack(0, ItemStack.of(Material.IRON_SWORD));
                plr.getInventory().setItemStack(1, ItemStack.of(Material.IRON_AXE));
                plr.getInventory().setItemStack(2, ItemStack.of(Material.GOLDEN_APPLE, 5));
                plr.getInventory().setItemStack(3, ItemStack.of(Material.BREAD, 64));

                plr.getAttribute(Attribute.ARMOR).setBaseValue(15D); // iron armour work around
            }
        } else {
            if (playersInCombat.contains(plr.getUuid())) {
                playersInCombat.remove(plr.getUuid());
                plr.sendActionBar(Component.text("You are no longer in the PVP Zone!", NamedTextColor.GREEN));
                plr.getInventory().clear();
                plr.getAttribute(Attribute.ARMOR).setBaseValue(0D);
                plr.setHealth(20f);
                plr.setFood(20);
                plr.setFoodSaturation(3f);
            }
        }
    }

    public static void playerLeave(Player plr) {
        playersInCombat.remove(plr.getUuid());
    }

    public static void playerRespawn(Player plr) {
        plr.getInventory().clear();
        playersInCombat.remove(plr.getUuid());
    }
}
