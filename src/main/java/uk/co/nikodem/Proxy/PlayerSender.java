package uk.co.nikodem.Proxy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import uk.co.nikodem.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerSender {
    public static List<UUID> playersBeingSent = new ArrayList<>();

    public static boolean getIsBeingSent(Player plr) {
        return playersBeingSent.contains(plr.getUuid());
    }

    public static void sendPlayer(Player plr, String server) {
        if (Main.config.connection.player_validation && !PlayerValidation.playerIsValidated(plr)) return;
        if (getIsBeingSent(plr)) return;
        addPlayerBeingSent(plr);
        PlayerSender.informPlayer(plr, server);
        BungeecordAbstractions.sendPlayerToServer(plr, server);

        Main.scheduler.scheduleTask(() ->
                {
                    if (getIsBeingSent(plr)) {
                        removePlayerBeingSent(plr);
                        informPlayerTimeoutError(plr);
                    }
                },
                TaskSchedule.seconds(5), TaskSchedule.stop(), ExecutionType.TICK_END);
    }

    public static void addPlayerBeingSent(Player plr) {
        playersBeingSent.add(plr.getUuid());
    }

    public static void removePlayerBeingSent(Player plr) {
        // to prevent spamming a delay is put on removal
        Main.scheduler.scheduleTask(
                () -> playersBeingSent.remove(plr.getUuid()),
                TaskSchedule.seconds(1), TaskSchedule.stop(), ExecutionType.TICK_END);
    }

    public static void informPlayer(Player plr, String server) {
        plr.sendMessage(Component.text("Sending you to server "+server+"!", NamedTextColor.AQUA));
    }

    public static void informPlayerError(Player plr) {
        plr.sendMessage(Component.text("Failed to send you to server!", NamedTextColor.RED));
    }

    public static void informPlayerTimeoutError(Player plr) {
        plr.sendMessage(Component.text("Request timed out!", NamedTextColor.RED));
    }
}
