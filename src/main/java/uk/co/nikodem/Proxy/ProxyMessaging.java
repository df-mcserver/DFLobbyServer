package uk.co.nikodem.Proxy;

import net.minestom.server.entity.Player;
import net.minestom.server.network.NetworkBuffer;
import uk.co.nikodem.Main;

public class ProxyMessaging {
    public static void sendPlayerToServer(Player plr, String server) {
        plr.sendPluginMessage(Main.config.proxy.getMessagingChannel(), NetworkBuffer.makeArray(buffer -> {
            buffer.write(NetworkBuffer.STRING_IO_UTF8, "Connect");
            buffer.write(NetworkBuffer.STRING_IO_UTF8, server);
        }));
    }

    public static void sendRealProtocolVersionMessage(Player plr) {
        plr.sendPluginMessage(Main.config.proxy.getMessagingChannel(), NetworkBuffer.makeArray(buffer -> {
            buffer.write(NetworkBuffer.STRING_IO_UTF8, "RealProtocolVersion");
        }));
    }

    public static void sendIncompatibleClientMessage(Player plr) {
        plr.sendPluginMessage(Main.config.proxy.getMessagingChannel(), NetworkBuffer.makeArray(buffer -> {
            buffer.write(NetworkBuffer.STRING_IO_UTF8, "IncompatibleClient");
        }));
    }

    public static void sendIsGeyserMessage(Player plr) {
        plr.sendPluginMessage(Main.config.proxy.getMessagingChannel(), NetworkBuffer.makeArray(buffer -> {
            buffer.write(NetworkBuffer.STRING_IO_UTF8, "IsGeyser");
        }));
    }
}
