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
}
