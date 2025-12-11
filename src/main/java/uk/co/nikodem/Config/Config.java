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
        private Integer port = 25565;
        private Integer compression_threshold = 0;
        private Boolean online = true;
        private Integer minimum_protocol_version = 0;
        private String minimum_version_name = "";

        public String getAddress() {
            return address;
        }
        public Integer getPort() {
            return port;
        }
        public Integer getCompressionThreshold() {
            return compression_threshold;
        }
        public Boolean isOnline() {
            return online;
        }
        public Integer getMinimumProtocolVersion() {
            return minimum_protocol_version;
        }
        public String getMinimumVersionName() {
            return minimum_version_name;
        }
    }

    public static class Ping {
        private String motd = "DFLobbyServer";
        private Integer fake_max_players = -1;
        private Boolean show_online_players = true;
        private String favicon_path = null;

        public String getMotd() {
            return motd;
        }
        public Integer getFakeMaxPlayers() {
            return fake_max_players;
        }
        public Boolean shouldShowOnlinePlayers() {
            return show_online_players;
        }
        public @Nullable String getFaviconPath() {
            return favicon_path;
        }
    }

    public static class Proxy {
        private String proxy = "";
        private String secret = "";
        private String messaging_channel = "";
        private Boolean expect_channel_response = false;
        private Boolean player_validation = true;

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
        public Boolean doPlayerValidation() {
            return player_validation;
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
            private Boolean combat_enabled = false;
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
                return combat_enabled;
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
    public Ping ping = new Ping();
    public Proxy proxy = new Proxy();
    public Lobby lobby = new Lobby();
    public Servers servers = new Servers();
    public Minigames minigames = new Minigames();
    public Admins admins = new Admins();
}
