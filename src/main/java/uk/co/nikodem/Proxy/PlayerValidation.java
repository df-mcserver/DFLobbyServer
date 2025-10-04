package uk.co.nikodem.Proxy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.Nullable;
import uk.co.nikodem.Main;

import java.util.HashMap;

public class PlayerValidation {
    private static HashMap<Player, PlayerValidation> validations = new HashMap<>();

    private boolean validatedProtocolVersion = false;
    private boolean hasValidProtocolVersion = false;

    private boolean validatedIncompatibility = false;
    private boolean hasCompatibleClient = false;

    public void markProtocolAsValidated(Player plr, boolean isValid) {
        validatedProtocolVersion = true;
        hasValidProtocolVersion = isValid;

        if (hasFinishedValidation()) onFinishValidation(plr);
    }

    public void markIncompatibilityAsValidated(Player plr, boolean isValid) {
        validatedIncompatibility = true;
        hasCompatibleClient = isValid;

        if (hasFinishedValidation()) onFinishValidation(plr);
    }

    public boolean hasFinishedValidation() {
        return validatedProtocolVersion && validatedIncompatibility;
    }

    public boolean playerIsValidated() {
        return hasValidProtocolVersion && hasCompatibleClient;
    }

    public void onFinishValidation(Player plr) {
        if (!hasValidProtocolVersion) {
            plr.sendMessage(
                    Component.text("You are on an older version of Minecraft!\nYou will not be able to join any restricted servers.\nPlease update to "+ Main.config.connection.minimum_version_name+" or higher to join servers!", NamedTextColor.RED)
            );
        } else if (!hasCompatibleClient) {
            plr.sendMessage(
                    Component.text("You are on an unsupported Minecraft client!\nYou will not be able to join any restricted servers.\nPlease rejoin on another, officially supported client!", NamedTextColor.RED)
            );
        }
    }

    public static boolean playerIsValidated(Player plr) {
        PlayerValidation validation = validations.get(plr);
        if (validation == null) return false;
        return validation.playerIsValidated();
    }

    public static PlayerValidation getPlayerValidation(Player plr) {
        PlayerValidation validation = validations.get(plr);
        if (validation != null) return validation;

        validation = new PlayerValidation();
        addPlayerValidation(plr, validation);
        return validation;
    }

    @Nullable
    public static void addPlayerValidation(Player plr, PlayerValidation validation) {
        validations.put(plr, validation);
    }

    @Nullable
    public static void removePlayerValidation(Player plr) {
        validations.remove(plr);
    }
}
