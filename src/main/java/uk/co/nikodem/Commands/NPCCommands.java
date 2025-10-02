package uk.co.nikodem.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import uk.co.nikodem.Proxy.PlayerSender;
import uk.co.nikodem.Server.Initialisations.AliveNPC;

import java.util.ArrayList;
import java.util.List;

public class NPCCommands extends Command {
    public static String command = "join";
    public static List<String> validServers = new ArrayList<>();

    public static String[] getOtherAliases() {
        NPCCommands.validServers = new ArrayList<>();
        for (AliveNPC npc : AliveNPC.alive) {
            validServers.add(npc.npcinfo.server);
        }
        return validServers.toArray(new String[0]);
    }

    public NPCCommands() {
        super(command, getOtherAliases());

        setDefaultExecutor((sender, context) -> {
            if (sender instanceof Player plr) {
                String usedCommandName = context.getCommandName();
                if (usedCommandName.equals(NPCCommands.command)) {
                    // take in another argument and send them to that server
                    String fullCommand = context.getInput();
                    List<String> args = List.of(fullCommand.split(" "));

                    if (args.size() == 1) {
                        plr.sendMessage(Component.text("Usage: /join {server-name}", NamedTextColor.LIGHT_PURPLE));
                        return;
                    }

                    String serverName = args.get(1);

                    if (!attemptSendPlayer(plr, serverName)) {
                        plr.sendMessage(Component.text("Cannot join \""+serverName+"\", server does not exist!", NamedTextColor.RED));
                    }
                } else {
                    attemptSendPlayer(plr, usedCommandName);
                }
            } else {
                sender.sendMessage(Component.text("You are not a player!", NamedTextColor.RED));
            }
        });

        addSyntax((sender, context) -> {}, ArgumentType.String("server-name"));
    }

    public boolean attemptSendPlayer(Player plr, String name) {
        if (validServers.contains(name)) {
            if (!PlayerSender.getIsBeingSent(plr)) PlayerSender.sendPlayer(plr, name);
            else plr.sendMessage(Component.text("You are already trying to connect to a server!", NamedTextColor.RED));
            return true;
        }
        return false;
    }
}
