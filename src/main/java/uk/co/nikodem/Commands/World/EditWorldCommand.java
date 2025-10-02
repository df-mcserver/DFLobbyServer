package uk.co.nikodem.Commands.World;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import uk.co.nikodem.Commands.Conditions.CanEdit;
import uk.co.nikodem.Server.EditMode;

public class EditWorldCommand extends Command {
    public EditWorldCommand() {
        super("editworld");
        setCondition(new CanEdit());

        setDefaultExecutor(((commandSender, commandContext) -> {
            if (commandSender instanceof Player plr) {
                if (!EditMode.canEnterEditMode(plr)) {
                    commandSender.sendMessage(Component.text("You do not have the permission to run this command!", NamedTextColor.RED));
                    return;
                }

                if (EditMode.isInEditMode(plr)) {
                    commandSender.sendMessage(Component.text("You are already in Edit mode!", NamedTextColor.RED));
                    return;
                }

                EditMode.enterEditMode(plr);
            } else {
                commandSender.sendMessage(Component.text("You are not a player!", NamedTextColor.RED));
            }
        }));
    }
}
