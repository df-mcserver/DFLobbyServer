package uk.co.nikodem.Config.Types;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;

public class NPC {
    public EntityType entity = null;
    public Pos position = null;
    public String server = null;
    public String name = null;

    public static NPC create(EntityType type, Pos position, String server, String name) {
        NPC npc = new NPC();
        npc.entity = type;
        npc.position = position;
        npc.server = server;
        npc.name = name;
        return npc;
    }
}
