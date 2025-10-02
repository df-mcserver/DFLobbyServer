package uk.co.nikodem.Commands.World.Editing;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentBlockState;
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeBlockPosition;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;
import uk.co.nikodem.Commands.Conditions.CanEdit;
import uk.co.nikodem.Server.EditMode;

import static net.minestom.server.command.builder.arguments.ArgumentType.BlockState;
import static net.minestom.server.command.builder.arguments.ArgumentType.RelativeBlockPosition;

public class SetBlockCommand extends Command {
    public SetBlockCommand() {
        super("setblock");
        setCondition(new CanEdit());

        final ArgumentRelativeBlockPosition position = RelativeBlockPosition("position");
        final ArgumentBlockState block = BlockState("block");

        setDefaultExecutor(((commandSender, commandContext) -> {
            commandSender.sendMessage(Component.text("Usage: /setblock <position> <block>", NamedTextColor.LIGHT_PURPLE));
        }));

        addSyntax((sender, context) -> {
            final Player player = (Player) sender;

            if (!EditMode.isInEditMode(player)) {
                sender.sendMessage(Component.text("You cannot perform this action outside of Edit mode!", NamedTextColor.RED));
                return;
            }

            Block blockToPlace = context.get(block);
            player.getInstance().setBlock(context.get(position).from(player), blockToPlace);
        }, position, block);
    }
}
