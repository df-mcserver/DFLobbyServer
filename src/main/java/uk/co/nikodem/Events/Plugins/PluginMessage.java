package uk.co.nikodem.Events.Plugins;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerPluginMessageEvent;
import net.minestom.server.listener.common.PluginMessageListener;
import uk.co.nikodem.Events.EventHandler;
import uk.co.nikodem.Main;
import uk.co.nikodem.Proxy.PlayerSender;
import uk.co.nikodem.Proxy.PlayerValidation;
import uk.co.nikodem.Utils.StringHelper;

import java.util.List;

public class PluginMessage implements EventHandler {

    // any minimap mods i could find :p
    public static final String sqrmapModNames = "registered_arg_mappingscardinal-components:attachment_sync_v1adventure"; // might be innocent
    public static final String pl3xModNames = "entity_syncpl3xmap:perm_reqpl3xmap:registered_arg_mappingspl3xmap:client_map_datapl3xmap";
    public static final String xaeroModNames = "vibrationxaeroworldmap:1sxaerominimap:client_map_dataxaerominimap:registered_arg_mappingsxaeroworldmap:attachment_sync_v1xaerominimap:attachment_sync_v1xaeroworldmap:mainxaerominimap";
    public static final String journeyMapModNames = "mp_options_reqjourneymap:journeymap:admin_savejourneymap:mainjourneymap:player_locjourneymap:teleport_reqjourneymap:remove_playerjourneymap:versionjourneymap:client_server_datajourneymap:open_screenjourneymap";

    public static final List<String> bannedMods =
            List.of(
                    (pl3xModNames
                            +xaeroModNames
                            +journeyMapModNames
                            +sqrmapModNames
                    ).split(":")
            );

    @Override
    public void setup(GlobalEventHandler eventHandler) {
        eventHandler.addListener(PlayerPluginMessageEvent.class, event -> {
            Player plr = event.getPlayer();

            switch (event.getIdentifier()) {
                case "minecraft:register":
                    // velocity doesn't allow plugins to handle register messages
                    // so I am forced to do it in the lobby instead
                    // only works for fabric (+ their derivatives) and neoforge afaik
                    if (playerContainsBadMod(event)) illegalStateErrorPlayer(plr);
                    break;

                case "pl3xmap:server_server_data":
                    // banned mod plugin message identifier
                    illegalStateErrorPlayer(plr);
                    break;

                default:
                    if (event.getIdentifier().equals(Main.config.proxy.messagingChannel)) {
                        String[] args = event.getMessageString().split(" ");
                        String command = StringHelper.sanitiseString(args[0]);

                        switch (command) {
                            // seems to work half the time on geyser
                            // TODO: investigate
                            case "ConnectStatus":
                                if (Main.config.proxy.expectChannelResponse) {
                                    if (!PlayerSender.getIsBeingSent(plr)) break;
                                    String status = StringHelper.sanitiseString(args[1]);
                                    if (status.equals("false")) {
                                        PlayerSender.informPlayerError(plr);
                                        PlayerSender.removePlayerBeingSent(plr);
                                        Main.logger.warn("Plugin", "Player \""+plr.getUsername()+"\" failed to teleport!");
                                        break;
                                    }
                                    Main.logger.log("Plugin", "Player \""+plr.getUsername()+"\" successfully teleported!");
                                }
                                break;

                            case "RealProtocolVersion":
                                String arg0rpv = StringHelper.sanitiseString(args[1]);
                                int protocolVersion = 0;
                                try {
                                    protocolVersion = Integer.parseInt(arg0rpv);
                                } catch (NumberFormatException e) {
                                    PlayerValidation.getPlayerValidation(plr).markProtocolAsValidated(plr,false);
                                    break;
                                }
                                PlayerValidation.getPlayerValidation(plr).markProtocolAsValidated(plr,
                                        Main.config.connection.minimum_protocol_version <= protocolVersion
                                );
                                break;

                            case "IncompatibleClient":
                                String arg0ic = StringHelper.sanitiseString(args[1]);
                                boolean val = Boolean.parseBoolean(arg0ic);
                                PlayerValidation.getPlayerValidation(plr).markIncompatibilityAsValidated(plr,
                                        !val
                                );
                                break;
                    }

                    break;
                }
            }
        });
    }

    private static boolean playerContainsBadMod(PlayerPluginMessageEvent event) {
        boolean containsBadMod = false;
        List<String> mods = List.of(event.getMessageString().split(":"));
        for (String modName : mods) {
            for (String bannedModName : bannedMods) {
                if (StringHelper.sanitiseString(modName).equals(StringHelper.sanitiseString(bannedModName))) {
                    containsBadMod = true;
                    break;
                }
            }
        }
        return containsBadMod;
    }

    public static void illegalStateErrorPlayer(Player plr) {
        Main.logger.log("Mods", "Player "+plr.getUsername()+" tried to join the server with banned mods!");
        // obfuscated error message
        plr.kick(Component.text("An internal server connection error occured.", NamedTextColor.RED));
    }
}
