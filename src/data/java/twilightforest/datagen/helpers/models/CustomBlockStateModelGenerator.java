package twilightforest.datagen.helpers.models;

import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
import net.minecraft.world.level.block.Block;

public class CustomBlockStateModelGenerator implements BlockModelDefinitionGenerator {
    private final Block block;
    private final BlockStateModelDispatcher dispatcher;

    public CustomBlockStateModelGenerator(Block block, BlockStateModelDispatcher dispatcher) {
        this.block = block;
        this.dispatcher = dispatcher;
    }

    @Override
    public Block block() {
        return block;
    }

    @Override
    public BlockStateModelDispatcher create() {
        return dispatcher;
    }
}
