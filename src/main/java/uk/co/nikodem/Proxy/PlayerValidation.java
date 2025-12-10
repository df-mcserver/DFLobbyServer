package uk.co.nikodem.Proxy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.Nullable;
import uk.co.nikodem.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerValidation {
    private static HashMap<Player, PlayerValidation> validations = new HashMap<>();

    private boolean validatedProtocolVersion = false;
    private boolean hasValidProtocolVersion = false;

    private boolean validatedIsBedrock = false;
    private boolean isBedrock = false;

    private boolean validatedIncompatibility = false;
    private boolean hasCompatibleClient = false;

    private boolean invalidModDetected = false;

    public void markPlayerAsInvalidModHolder() {
        this.invalidModDetected = true;
    }

    public void markProtocolAsValidated(Player plr, boolean isValid) {
        this.validatedProtocolVersion = true;
        this.hasValidProtocolVersion = isValid;

        if (this.hasFinishedValidation()) this.onFinishValidation(plr);
    }

    public void markIncompatibilityAsValidated(Player plr, boolean isValid) {
        this.validatedIncompatibility = true;
        this.hasCompatibleClient = isValid;

        if (this.hasFinishedValidation()) this.onFinishValidation(plr);
    }

    public void markIsBedrockAsValidated(Player plr, boolean isBedrock) {
        this.validatedIsBedrock = true;
        this.isBedrock = isBedrock;

        if (this.hasFinishedValidation()) this.onFinishValidation(plr);
    }

    public boolean hasFinishedValidation() {
        return validatedProtocolVersion && validatedIncompatibility && validatedIsBedrock;
    }

    public boolean playerIsValidated() {
        return hasValidProtocolVersion && hasCompatibleClient && !(invalidModDetected);
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
        } else {
            List<Component> messages = isBedrock ? createBedrockMessages() : createJavaMessages();
            for (Component msg : messages) {
                plr.sendMessage(msg);
            }
        }

        if (invalidModDetected) {
            plr.sendMessage(Component.text("An unsupported mod has been detected! You will not be able to join restricted servers until this mod is removed!", NamedTextColor.RED));
        }
    }

    public static boolean playerIsValidated(Player plr) {
        PlayerValidation validation = validations.get(plr);
        if (validation == null) return false;
        return validation.playerIsValidated();
    }

    public static boolean playerFinishedValidation(Player plr) {
        PlayerValidation validation = validations.get(plr);
        if (validation == null) return false;
        return validation.hasFinishedValidation();
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

    public static void sendValidationTimeout(Player plr) {
        plr.sendMessage(Component.text("Player validation timed out! Please rejoin and try again.", NamedTextColor.RED));
    }

    public static List<Component> createBedrockMessages() {
        List<Component> result = new ArrayList<>();

        result.add(Component.text("Welcome to the server!")
                .color(TextColor.color(0x03989e)));
        result.add(Component.text("Please note that you're playing on Bedrock, which is supported but doesn't work as well as Java.")
                .color(TextColor.color(0xB2482D)));
        result.add(Component.text("If you encounter any issues, or want to stay up to date, join the discord server!")
                .color(TextColor.color(0xB22D23)));
        result.add(Component.text("You can find the discord server at https://discord.gg/SpukTa6jBf")
                .color(TextColor.color(0xB22824)));

        return result;
    }

    public static List<Component> createJavaMessages() {
        List<Component> result = new ArrayList<>();

        result.add(Component.text("Welcome to the server!")
                .color(TextColor.color(0x03989e)));
        result.add(Component.text("You can join the discord server to keep updated with the server's updates!")
                .color(TextColor.color(0x5d782e)));
        result.add(MiniMessage.miniMessage().deserialize("<#588163>You can find the discord server at <click:open_url:'https://discord.gg/SpukTa6jBf'>https://discord.gg/SpukTa6jBf</click>"));

        return result;
    }
}
