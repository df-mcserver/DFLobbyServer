package uk.co.nikodem.Config.Sections;

import net.minestom.server.coordinate.Pos;
import uk.co.nikodem.Config.Types.NPC;

import java.util.ArrayList;
import java.util.List;

public class Server {
    public String world = "world";
    public Pos spawn = new Pos(0, 40, 0);
    public List<NPC> mobs = new ArrayList<>();
}
