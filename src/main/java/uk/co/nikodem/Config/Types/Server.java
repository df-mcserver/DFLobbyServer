package uk.co.nikodem.Config.Types;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;

public class Server {
    public EntityType entity = null;
    public Pos position = null;
    public String server = null;
    public String name = null;
    public boolean unrestricted = false;

    public static Server create(EntityType type, Pos position, String server, String name, boolean unrestricted) {
        Server npc = new Server();
        npc.unrestricted = unrestricted;
        npc.position = position;
        npc.server = server;
        npc.entity = type;
        npc.name = name;
        return npc;
    }
}
