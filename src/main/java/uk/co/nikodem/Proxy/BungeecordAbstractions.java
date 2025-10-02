package uk.co.nikodem.Proxy;

import net.minestom.server.entity.Player;
import net.minestom.server.network.NetworkBuffer;
import uk.co.nikodem.Main;

public class BungeecordAbstractions {
    public static void sendPlayerToServer(Player plr, String server) {
        plr.sendPluginMessage(Main.config.proxy.messagingChannel, NetworkBuffer.makeArray(buffer -> {
            buffer.write(NetworkBuffer.STRING_IO_UTF8, "Connect");
            buffer.write(NetworkBuffer.STRING_IO_UTF8, server);
        }));
    }
}
