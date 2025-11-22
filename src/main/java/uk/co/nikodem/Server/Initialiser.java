package uk.co.nikodem.Server;

import io.github.togar2.pvp.MinestomPvP;
import io.github.togar2.pvp.feature.CombatFeatureSet;
import io.github.togar2.pvp.feature.CombatFeatures;
import io.github.togar2.pvp.utils.CombatVersion;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.*;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.world.DimensionType;
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

        InstanceManager manager = MinecraftServer.getInstanceManager();
        InstanceContainer lobby_container = setupInstanceContainer(manager);
        Main.container = lobby_container;
        Main.generation = setupGeneration(lobby_container, config.server.world);
        setupEntities(lobby_container);

        GlobalEventHandler globalEventHandler = setupGlobalEventHandler();

        collectivelySetupEventHandlers(globalEventHandler);
        setupCommands();

        setupScheduler();

        addEasterEggAdvancement();

        InstanceContainer nether_container = setupNether(manager);
        Main.nether_container = nether_container;
        nether_generation = setupGeneration(nether_container, config.nether.world);
        setupPVP(globalEventHandler);

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
        setupEventHandler(eventHandler, new PlayerMoving());
        setupEventHandler(eventHandler, new PlayerCombat());
    }

    public void setupCommands() {
        MinecraftServer.getCommandManager().register(new ServerCommands());

        MinecraftServer.getCommandManager().register(new EditWorldCommand());
        MinecraftServer.getCommandManager().register(new ExitEditWorldCommand());
        MinecraftServer.getCommandManager().register(new SaveWorldCommand());

        MinecraftServer.getCommandManager().register(new SetBlockCommand());
    }

    public void setServerVariables() {
        MinecraftServer.setBrandName("DFLobbyServer");
        MinecraftServer.setCompressionThreshold(Main.config.connection.compression_threshold);
    }

    public InstanceContainer setupInstanceContainer(InstanceManager manager) {
        return manager.createInstanceContainer(DimensionType.OVERWORLD);
    }

    public GlobalEventHandler setupGlobalEventHandler() {
        return MinecraftServer.getGlobalEventHandler();
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

    public Generation setupGeneration(InstanceContainer container, String world_name) {
        Generation generation = new Generation();
        generation.setupChunkGeneration(container, world_name);
        return generation;
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

    public InstanceContainer setupNether(InstanceManager manager) {
        return manager.createInstanceContainer(DimensionType.THE_NETHER);
    }

    public void setupPVP(GlobalEventHandler handler) {
        MinestomPvP.init();

        CombatFeatureSet modernVanilla = CombatFeatures.empty()
                .version(CombatVersion.MODERN)
                .add(CombatFeatures.VANILLA_SWEEPING)
                .add(CombatFeatures.VANILLA_ARMOR)
                .add(CombatFeatures.VANILLA_ATTACK)
                .add(CombatFeatures.VANILLA_ATTACK_COOLDOWN)
                .add(CombatFeatures.VANILLA_BLOCK)
                .add(CombatFeatures.VANILLA_CRITICAL)
                .add(CombatFeatures.VANILLA_DAMAGE)
                .add(CombatFeatures.VANILLA_EQUIPMENT)
                .build();
        handler.addChild(modernVanilla.createNode());
    }

    public void setupScheduler() {
        Main.scheduler = MinecraftServer.getSchedulerManager();
    }
}
