package twilightforest.structures;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;

import java.util.Random;

public abstract class RandomizedTemplateProcessor implements ITemplateProcessor {

    protected final Random random;
    private final float integrity;

    public RandomizedTemplateProcessor(BlockPos pos, PlacementSettings settings) {
        this.integrity = settings.getIntegrity();
        this.random = settings.getRandom(pos);
    }

    protected boolean shouldPlaceBlock() {
        return integrity >= 1.0F || random.nextFloat() > integrity;
    }

    protected Block randomBlock(Block... blocks) {
        return blocks[random.nextInt(blocks.length)];
    }

    protected static <T extends Comparable<T>> IBlockState translateState(IBlockState stateIn, Block blockOut, IProperty<T> property) {
        return blockOut.getDefaultState().withProperty(property, stateIn.getValue(property));
    }

    protected static IBlockState translateState(IBlockState stateIn, Block blockOut, IProperty<?>... properties) {
        IBlockState stateOut = blockOut.getDefaultState();
        for (IProperty<?> property : properties) stateOut = copyValue(stateIn, stateOut, property);
        return stateOut;
    }

    private static <T extends Comparable<T>> IBlockState copyValue(IBlockState from, IBlockState to, IProperty<T> property) {
        return to.withProperty(property, from.getValue(property));
    }
}
