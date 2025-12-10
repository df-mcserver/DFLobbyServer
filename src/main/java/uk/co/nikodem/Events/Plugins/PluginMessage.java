package uk.co.nikodem.Events.Plugins;

import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerPluginMessageEvent;
import uk.co.nikodem.Events.EventHandler;
import uk.co.nikodem.Events.Plugins.MessageReceivers.ConnectStatus;
import uk.co.nikodem.Events.Plugins.MessageReceivers.IncompatibleClient;
import uk.co.nikodem.Events.Plugins.MessageReceivers.IsGeyser;
import uk.co.nikodem.Events.Plugins.MessageReceivers.RealProtocolVersion;
import uk.co.nikodem.Main;
import uk.co.nikodem.Proxy.PlayerValidation;
import uk.co.nikodem.Utils.StringHelper;

import java.util.HashMap;
import java.util.List;

public class PluginMessage implements EventHandler {

    // any minimap mods i could find :p
    public static final String sqrmapModNames = "registered_arg_mappingscardinal-components:attachment_sync_v1adventure"; // might be innocent
    public static final String pl3xModNames = "entity_syncpl3xmap:perm_reqpl3xmap:registered_arg_mappingspl3xmap:client_map_datapl3xmap";
    public static final String xaeroModNames = "xaero:vibrationxaeroworldmap:1sxaerominimap:client_map_dataxaerominimap:registered_arg_mappingsxaeroworldmap:attachment_sync_v1xaerominimap:attachment_sync_v1xaeroworldmap:mainxaerominimap";
    public static final String journeyMapModNames = "mp_options_reqjourneymap:journeymap:admin_savejourneymap:mainjourneymap:player_locjourneymap:teleport_reqjourneymap:remove_playerjourneymap:versionjourneymap:client_server_datajourneymap:open_screenjourneymap";

    public static final List<String> bannedMods =
            List.of(
                    (pl3xModNames+":"
                            +xaeroModNames+":"
                            +journeyMapModNames+":"
                            +sqrmapModNames
                    ).split(":")
            );

    public final HashMap<String, DFMessageReceiver> receivers = new HashMap<>();

    public PluginMessage() {
        receivers.put("ConnectStatus", new ConnectStatus());
        receivers.put("IncompatibleClient", new IncompatibleClient());
        receivers.put("IsGeyser", new IsGeyser());
        receivers.put("RealProtocolVersion", new RealProtocolVersion());
    }

    @Override
    public void setup(GlobalEventHandler eventHandler) {
        eventHandler.addListener(PlayerPluginMessageEvent.class, event -> {
            Player plr = event.getPlayer();

            if (event.getIdentifier().equals("minecraft:register")) {
                // velocity doesn't allow plugins to handle register messages
                // so I am forced to do it in the lobby instead
                // only works for fabric (+ their derivatives) and neoforge afaik
                if (playerContainsBadMod(event)) invalidatePlayer(plr);
            } else if (event.getIdentifier().equals("pl3xmap:server_server_data")) {
                // banned mod plugin message identifier
                invalidatePlayer(plr);
            } else if (event.getIdentifier().equals(Main.config.proxy.messagingChannel)) {
                String[] args = event.getMessageString().split(" ");
                String command = StringHelper.sanitiseString(args[0]);

                DFMessageReceiver receiver = receivers.get(command);
                if (receiver != null) {
                    receiver.run(event, args);
                }
            }
        });
    }

    private static boolean playerContainsBadMod(PlayerPluginMessageEvent event) {
        boolean containsBadMod = false;
        List<String> mods = List.of(event.getMessageString().split(":"));
        for (String modName : mods) {
            for (String bannedModName : bannedMods) {
                if (StringHelper.sanitiseString(modName).contains(StringHelper.sanitiseString(bannedModName))) {
                    containsBadMod = true;
                    break;
                }
            }
        }
        return containsBadMod;
    }

    public static void invalidatePlayer(Player plr) {
        PlayerValidation.getPlayerValidation(plr).markPlayerAsInvalidModHolder();
    }
}
