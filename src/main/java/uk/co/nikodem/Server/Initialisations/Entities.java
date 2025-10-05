package uk.co.nikodem.Server.Initialisations;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.InstanceContainer;
import uk.co.nikodem.Config.Types.Server;
import uk.co.nikodem.Main;

import java.util.Objects;

public class Entities {
    @SuppressWarnings("deprecation") // entity.setCustomName() is deprecated for some reason
    public void spawnNPCs(InstanceContainer container) {
        for (Server server : Main.config.server.servers) {
            if (server.entity == null) continue;
            Entity entity = new Entity(server.entity);
            if (!Objects.equals(server.name, "")) entity.setCustomName(Component.text(server.name));
            entity.setInstance(container, server.position);
            entity.setCustomNameVisible(!Objects.equals(server.name, ""));

            AliveNPC.alive.add(
                    AliveNPC.create(
                            server, entity
                    )
            );

            Main.logger.log("Servers", "Added NPC \""+server.name+"\" for server \""+server.server+"\" at position "+server.position+"!");
        }
    }
}
