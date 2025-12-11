package uk.co.nikodem.Events.Plugins.MessageReceivers;

import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPluginMessageEvent;
import uk.co.nikodem.Events.Plugins.DFMessageReceiver;
import uk.co.nikodem.Main;
import uk.co.nikodem.Proxy.PlayerSender;
import uk.co.nikodem.Utils.StringHelper;

public class ConnectStatus implements DFMessageReceiver {
    @Override
    public void run(PlayerPluginMessageEvent event, String[] args) {
        Player plr = event.getPlayer();
        if (Main.config.proxy.getExpectsChannelResponse()) {
            if (!PlayerSender.getIsBeingSent(plr)) return;
            String status = StringHelper.sanitiseString(args[1]);
            if (status.equals("false")) {
                PlayerSender.informPlayerError(plr);
                PlayerSender.removePlayerBeingSent(plr);
                Main.logger.warn("Plugin", "Player \""+plr.getUsername()+"\" failed to teleport!");
                return;
            }
            Main.logger.log("Plugin", "Player \""+plr.getUsername()+"\" successfully teleported!");
        }
    }
}
