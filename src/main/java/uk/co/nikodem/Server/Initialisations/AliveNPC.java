package uk.co.nikodem.Server.Initialisations;

import net.minestom.server.entity.Entity;
import org.jspecify.annotations.Nullable;
import uk.co.nikodem.Config.Types.NPC;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AliveNPC {
    public static List<AliveNPC> alive = new ArrayList<>();

    public NPC npcinfo;
    public UUID entity;

    public static AliveNPC create(NPC npcinfo, Entity entity) {
        AliveNPC a = new AliveNPC();
        a.npcinfo = npcinfo;
        a.entity = entity.getUuid();
        return a;
    }

    @Nullable
    public static AliveNPC find(Entity entity) {
        for (AliveNPC aliveNPC : alive) {
            if (aliveNPC.entity == entity.getUuid()) return aliveNPC;
        }
        return null;
    }
}
