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

    protected static IBlockState translateState(IBlockState stateIn, Block blockOut, IProperty property) {
        //noinspection unchecked
        return blockOut.getDefaultState().withProperty(property, stateIn.getValue(property));
    }

    protected static IBlockState translateState(IBlockState stateIn, Block blockOut, IProperty p1, IProperty p2, IProperty p3, IProperty p4) {
        //noinspection unchecked
        return blockOut.getDefaultState()
                .withProperty(p1, stateIn.getValue(p1))
                .withProperty(p2, stateIn.getValue(p2))
                .withProperty(p3, stateIn.getValue(p3))
                .withProperty(p4, stateIn.getValue(p4));
    }

    protected static IBlockState translateState(IBlockState stateIn, Block blockOut, IProperty p1, IProperty p2, IProperty p3, IProperty p4, IProperty p5) {
        //noinspection unchecked
        return blockOut.getDefaultState()
                .withProperty(p1, stateIn.getValue(p1))
                .withProperty(p2, stateIn.getValue(p2))
                .withProperty(p3, stateIn.getValue(p3))
                .withProperty(p4, stateIn.getValue(p4))
                .withProperty(p5, stateIn.getValue(p5));
    }
}