package uk.co.nikodem;

import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.AdvancementManager;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.timer.Scheduler;
import uk.co.nikodem.Config.Config;
import uk.co.nikodem.Server.Initialisations.Entities;
import uk.co.nikodem.Server.Initialiser;
import uk.co.nikodem.Utils.Logger;
import uk.co.nikodem.Server.Initialisations.Generation;

public class Main {
    public static Logger logger = new Logger();
    public static Config config = new Config();

    public static MinecraftServer server;

    public static InstanceContainer container;
    public static Generation generation;
    public static Entities entities;

    public static InstanceContainer nether_container;
    public static Generation nether_generation;

    public static GlobalEventHandler eventHandler;

    public static AdvancementManager advancementManager;
    public static Scheduler scheduler;

    public static GameMode DEFAULT_GAMEMODE = GameMode.ADVENTURE;

    public static void main(String[] args) {
        // load configuration
        logger.log("Main", "Beginning execution!");

        if (!config.getExists()) {
            logger.warn("Config", "Configuration doesn't exist! Creating now..");

            if (config.create()) logger.log("Config", "Created new configuration!");
            else logger.error("Config", "Failed to create new configuration!");

            synchronized (config) {
                config.update();
            }
        } else {
            if (!config.getIsValidConfiguration()) {
                logger.error("Config", "Invalid configuration!");
                return;
            }

            synchronized (config) {
                config.update();
            }

            logger.log("Config", "Loaded valid configuration.");
        }

        logger.log("Main", "Initialising Minecraft server..");

        Boolean success = false;

        // initialise proxy
        switch (config.proxy.proxy) {
            case "velocity":
            case "gate":
                if (config.connection.online) {
                    logger.error("Proxy", "Proxy server support for \""+config.proxy.proxy+"\" cannot be enabled, because online mode is enabled!");
                    logger.warn("Proxy", "Defaulting to offline authentication!");
                    Main.server = MinecraftServer.init(new Auth.Offline());
                    success = true;
                    break;
                }
                try {
                    Main.server = MinecraftServer.init(new Auth.Velocity(config.proxy.secret));
                    logger.log("Proxy", "Velocity support enabled!");
                    success = true;
                } catch (IllegalArgumentException e) {
                    logger.error("Proxy", "Velocity support not enabled, invalid secret given!");
                }

                break;
            default:
                if (config.connection.online) Main.server = MinecraftServer.init(new Auth.Online());
                else Main.server = MinecraftServer.init(new Auth.Offline());
                success = true;
                break;
        }

        if (success) {
            // initialise server
            Initialiser initialiser = new Initialiser();
            initialiser.Initialise();

            Runtime.getRuntime().addShutdownHook(new Thread(MinecraftServer::stopCleanly));
        } else {
            logger.error("Main", "Server failed to setup initialisation, stopping execution!");
        }
    }
}