package uk.co.nikodem.Server.Initialisations;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.InstanceContainer;
import uk.co.nikodem.Config.Config;
import uk.co.nikodem.Main;

import java.util.Objects;

public class Entities {
    @SuppressWarnings("deprecation") // entity.setCustomName() is deprecated for some reason
    public void spawnNPCs(InstanceContainer container) {
        for (Config.Servers.ServerInformation server : Main.config.servers.getServers()) {
            if (server.getEntityType() == null) continue;
            Entity entity = new Entity(server.getEntityType());

            if (!Objects.equals(server.getDisplayName(), "")) entity.setCustomName(MiniMessage.miniMessage().deserialize(server.getDisplayName()));
            entity.setNoGravity(true);
            entity.setInstance(container, server.getPosition());
            entity.setCustomNameVisible(!Objects.equals(server.getDisplayName(), ""));

            AliveNPC.alive.add(
                    AliveNPC.create(
                            server, entity
                    )
            );

            Main.logger.log("Servers", "Added NPC \""+server.getDisplayName()+"\" for server \""+server.getServerName()+"\" at position "+server.getPosition()+"!");
        }
    }
}
