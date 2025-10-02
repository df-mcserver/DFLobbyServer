package uk.co.nikodem.Server.Initialisations;

import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.instance.block.Block;
import uk.co.nikodem.Main;

public class Generation {
    public void setupChunkGeneration(InstanceContainer container) {
        container.setChunkSupplier(LightingChunk::new);
        container.setChunkLoader(new AnvilLoader(Main.config.server.world));
        container.setGenerator(unit -> {
            unit.modifier().fillHeight(0, 0, Block.AIR);
        });

        setTime(container);
    }

    public void setTime(InstanceContainer container) {
        container.setTimeRate(0);
    }
}
