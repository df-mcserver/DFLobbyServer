package uk.co.nikodem.Commands.World;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import uk.co.nikodem.Commands.Conditions.CanEdit;
import uk.co.nikodem.Server.EditMode;

public class ExitEditWorldCommand extends Command {
    public ExitEditWorldCommand() {
        super("exiteditworld");
        setCondition(new CanEdit());

        setDefaultExecutor(((commandSender, commandContext) -> {
            if (commandSender instanceof Player plr) {

                if (!EditMode.isInEditMode(plr)) {
                    commandSender.sendMessage(Component.text("You aren't in Edit mode!", NamedTextColor.RED));
                    return;
                }

                EditMode.exitEditMode(plr);
            } else {
                commandSender.sendMessage(Component.text("You are not a player!", NamedTextColor.RED));
            }
        }));
    }
}
