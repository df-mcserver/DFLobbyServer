package uk.co.nikodem.Server.Initialisations;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.InstanceContainer;
import uk.co.nikodem.Config.Types.NPC;
import uk.co.nikodem.Main;

import java.util.Objects;

public class Entities {
    @SuppressWarnings("deprecation") // entity.setCustomName() is deprecated for some reason
    public void spawnNPCs(InstanceContainer container) {
        for (NPC npc : Main.config.server.mobs) {
            Entity entity = new Entity(npc.entity);
            if (!Objects.equals(npc.name, "")) entity.setCustomName(Component.text(npc.name));
            entity.setInstance(container, npc.position);
            entity.setCustomNameVisible(!Objects.equals(npc.name, ""));

            AliveNPC.alive.add(
                    AliveNPC.create(
                            npc, entity
                    )
            );
        }
    }
}
