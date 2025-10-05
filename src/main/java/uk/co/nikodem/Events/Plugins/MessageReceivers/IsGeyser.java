package uk.co.nikodem.Events.Plugins.MessageReceivers;

import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPluginMessageEvent;
import uk.co.nikodem.Events.Plugins.DFMessageReceiver;
import uk.co.nikodem.Proxy.PlayerValidation;
import uk.co.nikodem.Utils.StringHelper;

public class IsGeyser implements DFMessageReceiver {
    @Override
    public void run(PlayerPluginMessageEvent event, String[] args) {
        Player plr = event.getPlayer();
        String arg = StringHelper.sanitiseString(args[1]);
        boolean val = Boolean.parseBoolean(arg);
        PlayerValidation.getPlayerValidation(plr).markIsBedrockAsValidated(plr,
                val
        );
    }
}
