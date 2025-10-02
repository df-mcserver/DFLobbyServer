package uk.co.nikodem.Config;

import com.moandjiezana.toml.Toml;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import org.jspecify.annotations.Nullable;
import uk.co.nikodem.Config.Sections.*;
import uk.co.nikodem.Config.Types.NPC;
import uk.co.nikodem.Main;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private final String configName = "server.toml";

    public Connection connection;
    public Proxy proxy;
    public Server server;
    public Minigames minigames;
    public Admins admins;

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
        Double val = config.getDouble(key);
        return val == null ? defaultValue : val;
    }

    public Float getCheckedFloat(Toml config, String key, Float defaultValue) {
        Double val = config.getDouble(key);
        return val == null ? defaultValue : val.floatValue();
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
            if (this.server == null) this.server = new Server();
            if (this.minigames == null) this.minigames = new Minigames();
            if (this.admins == null) this.admins = new Admins();

            // probably a better way to do this
            proxy.proxy = parsedConfiguration.getString("proxy.proxy");
            proxy.secret = parsedConfiguration.getString("proxy.secret");
            proxy.messagingChannel = parsedConfiguration.getString("proxy.messagingChannel");
            proxy.expectChannelResponse = parsedConfiguration.getBoolean("proxy.expectChannelResponse");

            connection.address = parsedConfiguration.getString("connection.address");
            connection.port = parsedConfiguration.getLong("connection.port").intValue();
            connection.compression_threshold = parsedConfiguration.getLong("connection.compression_threshold").intValue();
            connection.online = parsedConfiguration.getBoolean("connection.online");

            server.world = parsedConfiguration.getString("server.world", null);
            server.spawn = getCheckedPos(parsedConfiguration, "server.spawn", null);

            minigames.parkour.respawn = getCheckedPos(parsedConfiguration, "minigames.parkour.respawn", null);
            minigames.parkour.enabled = parsedConfiguration.getBoolean("minigames.parkour.enabled", false);

            admins.byUsername = parsedConfiguration.getList("admins.byUsername", new ArrayList<>());

            List<Object> mobArray = parsedConfiguration.getList("server.mobs", new ArrayList<>());
            int i = -1;
            for (Object obj : mobArray) {
                i++;
                Toml table = parsedConfiguration.getTable("server.mobs["+i+"]");
                if (table == null) continue;
                if (table.isEmpty()) continue;

                try {
                    EntityType type = EntityType.fromKey(table.getString("entity", "zombie"));
                    Pos position = getCheckedPos(table, "position", new Pos(0, 0, 0));
                    String name = table.getString("name", null);
                    String servername = table.getString("server", null);

                    server.mobs.add(NPC.create(type, position, servername, name));
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
