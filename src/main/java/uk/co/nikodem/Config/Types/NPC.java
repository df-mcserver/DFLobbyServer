package uk.co.nikodem.Config.Types;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;

public class NPC {
    public EntityType entity = null;
    public Pos position = null;
    public String server = null;
    public String name = null;
    public boolean unrestricted = false;

    public static NPC create(EntityType type, Pos position, String server, String name, boolean unrestricted) {
        NPC npc = new NPC();
        npc.unrestricted = unrestricted;
        npc.position = position;
        npc.server = server;
        npc.entity = type;
        npc.name = name;
        return npc;
    }
}
