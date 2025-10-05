package uk.co.nikodem.Server;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.*;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.item.Material;
import uk.co.nikodem.Commands.World.EditWorldCommand;
import uk.co.nikodem.Commands.ServerCommands;
import uk.co.nikodem.Commands.World.Editing.SetBlockCommand;
import uk.co.nikodem.Commands.World.ExitEditWorldCommand;
import uk.co.nikodem.Commands.World.SaveWorldCommand;
import uk.co.nikodem.Events.Entities.PlayerInteract;
import uk.co.nikodem.Events.EventHandler;
import uk.co.nikodem.Events.Players.*;
import uk.co.nikodem.Events.Plugins.PluginMessage;
import uk.co.nikodem.Main;
import uk.co.nikodem.Server.Initialisations.Entities;
import uk.co.nikodem.Server.Initialisations.Generation;

import static uk.co.nikodem.Main.*;

public class Initialiser {
    public void Initialise() {
        setServerVariables();

        InstanceContainer container = setupInstanceContainer();
        GlobalEventHandler globalEventHandler = setupGlobalEventHandler();

        setupGeneration(container);
        setupEntities(container);

        collectivelySetupEventHandlers(globalEventHandler);
        setupCommands();

        setupScheduler();

        addEasterEggAdvancement();

        startServer();
    }

    public void collectivelySetupEventHandlers(GlobalEventHandler eventHandler) {
        setupEventHandler(eventHandler, new PlayerJoining());
        setupEventHandler(eventHandler, new PlayerDisconnecting());
        setupEventHandler(eventHandler, new RestrictedPlayerActions());
        setupEventHandler(eventHandler, new PlayerInteract());
        setupEventHandler(eventHandler, new PlayerInLava());
        setupEventHandler(eventHandler, new PluginMessage());
        setupEventHandler(eventHandler, new PlayerPickBlock());
    }

    public void setupCommands() {
        MinecraftServer.getCommandManager().register(new ServerCommands());

        MinecraftServer.getCommandManager().register(new EditWorldCommand());
        MinecraftServer.getCommandManager().register(new ExitEditWorldCommand());
        MinecraftServer.getCommandManager().register(new SaveWorldCommand());

        MinecraftServer.getCommandManager().register(new SetBlockCommand());
    }

    public void setServerVariables() {
        MinecraftServer.setBrandName("DF//Minestom");
        MinecraftServer.setCompressionThreshold(Main.config.connection.compression_threshold);
    }

    public InstanceContainer setupInstanceContainer() {
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer container = instanceManager.createInstanceContainer();
        Main.container = container;

        return container;
    }

    public GlobalEventHandler setupGlobalEventHandler() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        Main.eventHandler = globalEventHandler;

        return globalEventHandler;
    }

    public void startServer() {
        String addr = config.connection.address;
        int port = config.connection.port;

        try {
            Main.server.start(addr, port);

            logger.log("Main","DFLobbyServer started on "+addr+":"+port+"!");
        } catch (Exception e) {
            logger.error("Main", "Failed to start DFLobbyServer on "+addr+":"+port+"!");
        }
    }

    public void setupGeneration(InstanceContainer container) {
        Generation generation = new Generation();
        generation.setupChunkGeneration(container);
        Main.generation = generation;
    }

    public void setupEntities(InstanceContainer container) {
        Entities entities = new Entities();
        entities.spawnNPCs(container);
        Main.entities = entities;
    }

    public void setupEventHandler(GlobalEventHandler eventHandler, EventHandler handler) {
        handler.setup(eventHandler);
    }

    public void addEasterEggAdvancement() {
        AdvancementManager manager = new AdvancementManager();

        AdvancementRoot root = new AdvancementRoot(
                Component.text("Test"),
                Component.text("Test2"),
                Material.ACACIA_BOAT,
                FrameType.GOAL,0,0, ""
        );

        AdvancementTab tab = manager.createTab("dflobby:eastereggtab", root);

        tab.createAdvancement("dflobby:easteregg", new Advancement(
                Component.text("Test"),
                Component.text("Test2"),
                Material.ACACIA_BOAT,
                FrameType.GOAL,0,0
        ), root);

        Main.advancementManager = manager;
    }

    public void setupScheduler() {
        Main.scheduler = MinecraftServer.getSchedulerManager();
    }
}
