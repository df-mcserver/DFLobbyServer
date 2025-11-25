package uk.co.nikodem.Config.Sections;

import net.minestom.server.coordinate.Pos;

public class Nether {
    public String world = "world_nether";
    public boolean enabled = false;
    public Pos spawnLocation = new Pos(0, 0, 0);
    public Pos portalLocation = new Pos(0, 0, 0);

    public boolean pvpEnabled = false;
    public Pos pvpZonePoint1 = new Pos(0, 0, 0);
    public Pos pvpZonePoint2 = new Pos(0, 0, 0);
}
