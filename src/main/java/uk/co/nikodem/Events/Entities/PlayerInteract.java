package uk.co.nikodem.Events.Entities;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import uk.co.nikodem.Events.EventHandler;
import uk.co.nikodem.Main;
import uk.co.nikodem.Proxy.PlayerSender;
import uk.co.nikodem.Server.Initialisations.AliveNPC;

public class PlayerInteract implements EventHandler {
    @Override
    public void setup(GlobalEventHandler eventHandler) {
        eventHandler.addListener(PlayerEntityInteractEvent.class, event -> {
            Player plr = event.getPlayer();
            Entity entity = event.getTarget();

            doJoining(plr, entity);
        });

        eventHandler.addListener(EntityAttackEvent.class, event -> {
            Entity e = event.getEntity();

            if (e instanceof Player plr) {
                Entity entity = event.getTarget();

                doJoining(plr, entity);
            }
        });
    }

    public static void doJoining(Player plr, Entity entity) {
        if (entity.getEntityType() == EntityType.PLAYER)
            return;

        AliveNPC aliveNPC = AliveNPC.find(entity);

        if (aliveNPC == null) return;
        if (PlayerSender.getIsBeingSent(plr)) return;

        PlayerSender.sendPlayer(plr, aliveNPC.serverInformation.getServerName(), aliveNPC.serverInformation.isUnrestricted());

        Main.logger.log("Entities", plr.getUsername()+" interacted with NPC for \""+aliveNPC.serverInformation.getServerName()+"\"!");
    }
}
