package uk.co.nikodem.Commands.Conditions;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.condition.CommandCondition;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.Nullable;
import uk.co.nikodem.Server.EditMode;

public class CanEdit implements CommandCondition {
    @Override
    public boolean canUse(CommandSender commandSender, @Nullable String s) {
        if (commandSender instanceof Player plr) {
            return EditMode.canEnterEditMode(plr);
        }
        return true;
    }
}
