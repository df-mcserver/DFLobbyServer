package uk.co.nikodem.Events.Plugins;

import net.minestom.server.event.player.PlayerPluginMessageEvent;

public interface DFMessageReceiver {
    void run(PlayerPluginMessageEvent event, String[] args);
}
