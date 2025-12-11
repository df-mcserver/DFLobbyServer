package uk.co.nikodem.Config;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;

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
        private String messagingChannel = "";
        private Boolean expectChannelResponse = false;

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
            private EntityType entity = null;
            private Pos position = null;
            private String server = null;
            private String name = null;
            private Boolean unrestricted = false;

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
            private Pos portalInNetherLocation = null;
            private Pos portalInOverworldLocation = null;
            private Pos combatZonePoint1 = null;
            private Pos combatZonePoint2 = null;

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

        public Parkour parkour = new Parkour();
        public Nether nether = new Nether();
    }

    public static class Admins {
        private List<String> byUsername = new ArrayList<>();

        public List<String> getByUsername() {
            return byUsername;
        }
        public boolean isPlayerAnAdmin(Player plr) {
            return byUsername.contains(plr.getUsername());
        }
    }

    public Connection connection = new Connection();
    public Proxy proxy = new Proxy();
    public Lobby lobby = new Lobby();
    public Servers servers = new Servers();
    public Minigames minigames = new Minigames();
    public Admins admins = new Admins();
}
