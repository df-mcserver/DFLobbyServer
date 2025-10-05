package uk.co.nikodem.Events.Plugins.MessageReceivers;

import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPluginMessageEvent;
import uk.co.nikodem.Events.Plugins.DFMessageReceiver;
import uk.co.nikodem.Main;
import uk.co.nikodem.Proxy.PlayerValidation;
import uk.co.nikodem.Utils.StringHelper;

public class RealProtocolVersion implements DFMessageReceiver {
    @Override
    public void run(PlayerPluginMessageEvent event, String[] args) {
        Player plr = event.getPlayer();
        String arg = StringHelper.sanitiseString(args[1]);
        int protocolVersion = 0;
        try {
            protocolVersion = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            PlayerValidation.getPlayerValidation(plr).markProtocolAsValidated(plr,false);
            return;
        }
        PlayerValidation.getPlayerValidation(plr).markProtocolAsValidated(plr,
                Main.config.connection.minimum_protocol_version <= protocolVersion
        );
    }
}
