package uk.co.nikodem.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.Nullable;
import uk.co.nikodem.Config.Config;
import uk.co.nikodem.Main;
import uk.co.nikodem.Proxy.PlayerSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerCommands extends Command {
    public static String command = "join";
    public static List<String> validServers = new ArrayList<>();
    public static HashMap<String, Config.Servers.ServerInformation> nameToServer = new HashMap<>();

    public static String[] getOtherAliases() {
        ServerCommands.validServers = new ArrayList<>();
        for (Config.Servers.ServerInformation server : Main.config.servers.getServers()) {
            nameToServer.put(server.getServerName(), server);
            validServers.add(server.getServerName());
        }
        return validServers.toArray(new String[0]);
    }

    public ServerCommands() {
        super(command, getOtherAliases());

        setDefaultExecutor((sender, context) -> {
            if (sender instanceof Player plr) {
                String usedCommandName = context.getCommandName();
                if (usedCommandName.equals(ServerCommands.command)) {
                    // take in another argument and send them to that server
                    String fullCommand = context.getInput();
                    List<String> args = List.of(fullCommand.split(" "));

                    if (args.size() == 1) {
                        plr.sendMessage(Component.text("Usage: /join {server-name}", NamedTextColor.LIGHT_PURPLE));
                        return;
                    }

                    String serverName = args.get(1);
                    Config.Servers.ServerInformation server = nameToServer.get(serverName);

                    attemptSendPlayer(plr, server, serverName);
                } else {
                    Config.Servers.ServerInformation server = nameToServer.get(usedCommandName);
                    attemptSendPlayer(plr, server, usedCommandName);
                }
            } else {
                informIsNotPlayer(sender);
            }
        });

        addSyntax((sender, context) -> {}, ArgumentType.String("server-name"));
    }

    public boolean attemptSendPlayer(Player plr, @Nullable Config.Servers.ServerInformation server, String expectedName) {
        if (server == null) {
            informServerDoesntExist(plr, expectedName);
            plr.sendMessage(Component.text("!", NamedTextColor.RED));
            return false;
        }

        if (!PlayerSender.getIsBeingSent(plr)) PlayerSender.sendPlayer(plr, server.getServerName(), server.isUnrestricted());
        else plr.sendMessage(Component.text("You are already trying to connect to a server!", NamedTextColor.RED));
        return true;
    }

    public void informServerDoesntExist(Player plr, String serverName) {
        plr.sendMessage(Component.text("Cannot join \""+serverName+"\", server does not exist!", NamedTextColor.RED));
    }

    public void informIsNotPlayer(CommandSender sender) {
        sender.sendMessage(Component.text("You are not a player!", NamedTextColor.RED));
    }
}
