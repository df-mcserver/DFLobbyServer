package uk.co.nikodem.Config;

import com.moandjiezana.toml.Toml;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import org.jspecify.annotations.Nullable;
import uk.co.nikodem.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private final String configName = "server.toml";

    public static class Connection {
        public String address = "0.0.0.0";
        public int port = 25565;
        public int compression_threshold = 0;
        public boolean online = true;
        public boolean player_validation = true;
        public int minimum_protocol_version = 0;
        public String minimum_version_name = "";

        public String getAddress() {
            return address;
        }
        public int getPort() {
            return port;
        }
        public int getCompression_threshold() {
            return compression_threshold;
        }
        public boolean isOnline() {
            return online;
        }
        public boolean doPlayerValidation() {
            return player_validation;
        }
        public int getMinimum_protocol_version() {
            return minimum_protocol_version;
        }
        public String getMinimum_version_name() {
            return minimum_version_name;
        }
    }

    public static class Proxy {
        public String proxy = "";
        public String secret = "";
        public String messagingChannel = "";
        public Boolean expectChannelResponse = false;

        public String getProxy() {
            return proxy;
        }
        public String getSecret() {
            return secret;
        }
        public String getMessagingChannel() {
            return messagingChannel;
        }
        public Boolean getExpectsChannelResponse() {
            return expectChannelResponse;
        }
    }

    public static class Servers {

        public static class ServerInformation {
            public EntityType entity = null;
            public Pos position = null;
            public String server = null;
            public String name = null;
            public Boolean unrestricted = false;

            public static ServerInformation create(EntityType type, Pos position, String server, String name, boolean unrestricted) {
                ServerInformation npc = new ServerInformation();
                npc.unrestricted = unrestricted;
                npc.position = position;
                npc.server = server;
                npc.entity = type;
                npc.name = name;
                return npc;
            }

            public EntityType getEntityType() {
                return entity;
            }
            public Pos getPosition() {
                return position;
            }
            public String getServerName() {
                return server;
            }
            public String getDisplayName() {
                return name;
            }
            public Boolean isUnrestricted() {
                return unrestricted;
            }
        }

        public String world = "world";
        public Pos spawn = new Pos(0, 40, 0);
        public List<ServerInformation> servers = new ArrayList<>();

        public String getWorldName() {
            return world;
        }
        public Pos getSpawnLocation() {
            return spawn;
        }
        public List<ServerInformation> getServers() {
            return servers;
        }
    }

    public static class Minigames {
        public static class Parkour {
            public Boolean enabled = false;
            public Pos respawn = null;

            public Boolean isEnabled() {
                return enabled;
            }
            public Pos getRespawnLocation() {
                return respawn;
            }
        }

        public static class Nether {
            public String world = "world_nether";
            public Boolean enabled = false;
            public Boolean combatEnabled = false;
            public Pos portalInNetherLocation = null;
            public Pos portalInOverworldLocation = null;
            public Pos combatZonePoint1 = null;
            public Pos combatZonePoint2 = null;

            public String getWorldName() {
                return world;
            }
            public Boolean isEnabled() {
                return enabled;
            }
            public Boolean isCombatEnabled() {
                return combatEnabled;
            }
            public Pos getPortalInNetherLocation() {
                return portalInNetherLocation;
            }
            public Pos getPortalInOverworldLocation() {
                return portalInOverworldLocation;
            }
            public Pos getCombatZonePoint1() {
                return combatZonePoint1;
            }
            public Pos getCombatZonePoint2() {
                return combatZonePoint2;
            }
        }
    }

    public static class Admins {
        public List<String> byUsername = new ArrayList<>();

        public List<String> getByUsername() {
            return byUsername;
        }
    }

    public boolean getExists() {
        Path source;
        try {
            source = getConfigFilePath();
        } catch (URISyntaxException | NullPointerException e) {
            return false;
        }

        return source.toFile().exists();
    }

    public boolean getIsValidConfiguration() {
        Path source;
        try {
            source = getConfigFilePath();
        } catch (URISyntaxException e) {
            return false;
        }

        return true;
    }

    public Path getConfigFilePath() throws URISyntaxException, NullPointerException {
        Path path = Path.of(configName);
        return path;
    }

    public Double getCheckedDouble(Toml config, String key, Double defaultValue) {
        try {
            Double val = config.getDouble(key);
            return val == null ? defaultValue : val;
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

    public Float getCheckedFloat(Toml config, String key, Float defaultValue) {
        try {
            Double val = config.getDouble(key);
            return val == null ? defaultValue : val.floatValue();
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

    public Pos getCheckedPos(Toml config, String key, @Nullable Pos defaultValue) {
        Toml pos = config.getTable(key);
        if (defaultValue == null) defaultValue = new Pos(0, 0, 0);
        Double x = getCheckedDouble(pos,"x", defaultValue.x());
        Double y = getCheckedDouble(pos,"y", defaultValue.y());
        Double z = getCheckedDouble(pos,"z", defaultValue.z());
        float yaw = getCheckedFloat(pos,"yaw", defaultValue.yaw());
        float pitch = getCheckedFloat(pos,"pitch", defaultValue.pitch());

        return new Pos(x, y, z, yaw, pitch);
    }

    public void update() {
        try {
            Path source = getConfigFilePath();
            File file = source.toFile();

            Toml defaultConfiguration = new Toml().read(Main.class.getResourceAsStream("/"+configName));
            Toml parsedConfiguration = new Toml(defaultConfiguration).read(file);

            if (this.proxy == null) this.proxy = new Proxy();
            if (this.connection == null) this.connection = new Connection();
            if (this.server == null) this.server = new uk.co.nikodem.Config.Sections.Server();
            if (this.minigames == null) this.minigames = new Minigames();
            if (this.admins == null) this.admins = new Admins();
            if (this.nether == null) this.nether = new Nether();

            // probably a better way to do this
            proxy.proxy = parsedConfiguration.getString("proxy.proxy");
            proxy.secret = parsedConfiguration.getString("proxy.secret");
            proxy.messagingChannel = parsedConfiguration.getString("proxy.messagingChannel");
            proxy.expectChannelResponse = parsedConfiguration.getBoolean("proxy.expectChannelResponse");

            connection.address = parsedConfiguration.getString("connection.address");
            connection.port = parsedConfiguration.getLong("connection.port").intValue();
            connection.compression_threshold = parsedConfiguration.getLong("connection.compression_threshold").intValue();
            connection.online = parsedConfiguration.getBoolean("connection.online");
            connection.player_validation = parsedConfiguration.getBoolean("connection.player_validation", false);
            connection.minimum_protocol_version = parsedConfiguration.getLong("connection.minimum_protocol_version", 0L).intValue();
            connection.minimum_version_name = parsedConfiguration.getString("connection.minimum_version_name");

            server.world = parsedConfiguration.getString("server.world", null);
            server.spawn = getCheckedPos(parsedConfiguration, "server.spawn", null);

            minigames.parkour.respawn = getCheckedPos(parsedConfiguration, "minigames.parkour.respawn", null);
            minigames.parkour.enabled = parsedConfiguration.getBoolean("minigames.parkour.enabled", false);

            admins.byUsername = parsedConfiguration.getList("admins.byUsername", new ArrayList<>());

            nether.enabled = parsedConfiguration.getBoolean("nether.enabled");
            nether.spawnLocation = getCheckedPos(parsedConfiguration, "nether.spawn_location", new Pos(0, 0, 0));
            nether.portalLocation = getCheckedPos(parsedConfiguration, "nether.portal_location", new Pos(0, 0, 0));
            nether.world = parsedConfiguration.getString("nether.world");

            nether.pvpEnabled = parsedConfiguration.getBoolean("nether.pvp_enabled");
            nether.pvpZonePoint1 = getCheckedPos(parsedConfiguration, "nether.pvp_zone_point_1", new Pos(0, 0, 0));
            nether.pvpZonePoint2 = getCheckedPos(parsedConfiguration, "nether.pvp_zone_point_2", new Pos(0, 0, 0));

            List<Object> serverArray = parsedConfiguration.getList("server.servers", new ArrayList<>());
            int i = -1;
            for (Object obj : serverArray) {
                i++;
                Toml table = parsedConfiguration.getTable("server.servers["+i+"]");
                if (table == null) continue;
                if (table.isEmpty()) continue;

                try {
                    String entityTypeName = table.getString("entity", null);
                    EntityType type = entityTypeName == null ? null : EntityType.fromKey(entityTypeName);
                    Pos position = getCheckedPos(table, "position", null);
                    String name = table.getString("name", null);
                    String servername = table.getString("server", null);
                    boolean unrestricted = table.getBoolean("unrestricted", false);

                    Main.logger.log("Servers", "Added server \""+name+"\"!");

                    server.servers.add(Server.create(type, position, servername, name, unrestricted));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public boolean create() {
        try {
            InputStream in = Main.class.getResourceAsStream("/"+configName);
            FileOutputStream out = new FileOutputStream(configName);
            out.write(in.readAllBytes());
            out.close();
            in.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean isPlayerAnAdmin(Player plr) {
        return admins.byUsername.contains(plr.getUsername());
    }
}
