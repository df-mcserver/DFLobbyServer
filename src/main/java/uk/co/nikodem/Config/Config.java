package uk.co.nikodem.Config;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public static class Connection {
        private String address = "0.0.0.0";
        private int port = 25565;
        private int compression_threshold = 0;
        private boolean online = true;
        private boolean player_validation = true;
        private int minimum_protocol_version = 0;
        private String minimum_version_name = "";

        public String getAddress() {
            return address;
        }
        public int getPort() {
            return port;
        }
        public int getCompressionThreshold() {
            return compression_threshold;
        }
        public boolean isOnline() {
            return online;
        }
        public boolean doPlayerValidation() {
            return player_validation;
        }
        public int getMinimumProtocolVersion() {
            return minimum_protocol_version;
        }
        public String getMinimumVersionName() {
            return minimum_version_name;
        }
    }

    public static class Proxy {
        private String proxy = "";
        private String secret = "";
        private String messaging_channel = "";
        private Boolean expect_channel_response = false;

        public String getProxy() {
            return proxy;
        }
        public String getSecret() {
            return secret;
        }
        public String getMessagingChannel() {
            return messaging_channel;
        }
        public Boolean getExpectsChannelResponse() {
            return expect_channel_response;
        }
    }

    public static class Lobby {
        private String world = "world";
        private Pos spawn = new Pos(0, 40, 0);

        public String getWorldName() {
            return world;
        }
        public Pos getSpawnLocation() {
            return spawn;
        }
    }

    public static class Servers {

        public static class ServerInformation {
            private String entity = null;
            private Pos position = null;
            private String server = null;
            private String name = null;
            private Boolean unrestricted = false;

            public static ServerInformation create(String type, Pos position, String server, String name, boolean unrestricted) {
                ServerInformation npc = new ServerInformation();
                npc.unrestricted = unrestricted;
                npc.position = position;
                npc.server = server;
                npc.entity = type;
                npc.name = name;
                return npc;
            }

            public String getEntityTypeName() {
                return entity;
            }
            public @Nullable EntityType getEntityType() {
                // not the most efficient lol
                for (EntityType type : EntityType.values()) {
                    if (type.key().value().equalsIgnoreCase(entity)) return type;
                }
                return null;
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


        private List<ServerInformation> servers = new ArrayList<>();

        public List<ServerInformation> getServers() {
            return servers;
        }
    }

    public static class Minigames {
        public static class Parkour {
            private Boolean enabled = false;
            private Pos respawn = null;

            public Boolean isEnabled() {
                return enabled;
            }
            public Pos getRespawnLocation() {
                return respawn;
            }
        }

        public static class Nether {
            private String world = "world_nether";
            private Boolean enabled = false;
            private Boolean combatEnabled = false;
            private Pos portal_in_nether_location = null;
            private Pos portal_in_overworld_location = null;
            private Pos combat_zone_point1 = null;
            private Pos combat_zone_point2 = null;

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
                return portal_in_nether_location;
            }
            public Pos getPortalInOverworldLocation() {
                return portal_in_overworld_location;
            }
            public Pos getCombatZonePoint1() {
                return combat_zone_point1;
            }
            public Pos getCombatZonePoint2() {
                return combat_zone_point2;
            }
        }

        public Parkour parkour = new Parkour();
        public Nether nether = new Nether();
    }

    public static class Admins {
        private List<String> by_username = new ArrayList<>();

        public List<String> getByUsername() {
            return by_username;
        }
        public boolean isPlayerAnAdmin(Player plr) {
            return by_username.contains(plr.getUsername());
        }
    }

    public Connection connection = new Connection();
    public Proxy proxy = new Proxy();
    public Lobby lobby = new Lobby();
    public Servers servers = new Servers();
    public Minigames minigames = new Minigames();
    public Admins admins = new Admins();
}
