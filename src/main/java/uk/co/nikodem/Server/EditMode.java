package uk.co.nikodem.Server;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import uk.co.nikodem.Main;
import uk.co.nikodem.Proxy.PlayerValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EditMode {
    public static List<UUID> editModePlayers = new ArrayList<>();

    public static boolean canEnterEditMode(Player plr) {
        return Main.config.isPlayerAnAdmin(plr) && (!Main.config.connection.player_validation || PlayerValidation.playerIsValidated(plr));
    }

    public static boolean isInEditMode(Player plr) {
        return editModePlayers.contains(plr.getUuid());
    }

    public static void addPlayer(Player plr) {
        editModePlayers.add(plr.getUuid());
    }

    public static void removePlayer(Player plr) {
        editModePlayers.remove(plr.getUuid());
    }

    public static void saveWorld(Player plr) {
        Main.logger.log("Edit", "World is being saved by "+plr.getUsername()+"!");
        plr.getInstance().saveChunksToStorage();
        plr.sendMessage(Component.text("Successfully saved world!", NamedTextColor.LIGHT_PURPLE));
    }

    public static void enterEditMode(Player plr) {
        Main.logger.log("Edit", plr.getUsername()+" is entering Edit mode!");
        plr.setGameMode(GameMode.CREATIVE);
        plr.getInventory().clear();
        EditMode.addPlayer(plr);
        plr.sendMessage(Component.text("You are now in Edit mode!", NamedTextColor.LIGHT_PURPLE));
    }

    public static void exitEditMode(Player plr) {
        Main.logger.log("Edit", plr.getUsername()+" is exiting Edit mode!");
        plr.setGameMode(Main.DEFAULT_GAMEMODE);
        plr.getInventory().clear();
        EditMode.removePlayer(plr);
        plr.sendMessage(Component.text("You are no longer in Edit mode!", NamedTextColor.LIGHT_PURPLE));
    }
}
