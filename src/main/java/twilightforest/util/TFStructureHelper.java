package twilightforest.util;

import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

/**
 * Created by Joseph on 7/16/2017.
 */
@Deprecated
public class TFStructureHelper {

    public static final BlockState stoneSlab = getSlab(Blocks.SMOOTH_STONE_SLAB);
    public static final BlockState stoneSlabTop = getSlabTop(Blocks.SMOOTH_STONE_SLAB);
    public static final BlockState stoneSlabDouble = Blocks.SMOOTH_STONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.DOUBLE);

    public static final BlockState birchSlab = getSlab(Blocks.BIRCH_SLAB);
    public static final BlockState birchSlabTop = getSlabTop(Blocks.BIRCH_SLAB);
    public static final BlockState birchPlanks = Blocks.BIRCH_PLANKS.defaultBlockState();


    private static BlockState getSlabType(Block type, SlabType side) {
        return type.defaultBlockState().setValue(SlabBlock.TYPE, side);
    }

    public static BlockState getSlab(Block type) {
        return getSlabType(type, SlabType.BOTTOM);
    }

    public static BlockState getSlabTop(Block type) {
        return getSlabType(type, SlabType.TOP);
    }

    public static BlockState randomPlant(int i) {
        if(i < 4) return randomSapling(i);
        else return randomMushroom(i - 4);
    }

    public static BlockState randomSapling(int i) {
		return switch (i) {
			case 1 -> Blocks.POTTED_SPRUCE_SAPLING.defaultBlockState();
			case 2 -> Blocks.POTTED_BIRCH_SAPLING.defaultBlockState();
			case 3 -> Blocks.POTTED_JUNGLE_SAPLING.defaultBlockState();
			default -> Blocks.POTTED_OAK_SAPLING.defaultBlockState();
		};
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> randomTree(int i) {
        return switch (i) {
            case 1 -> TreeFeatures.SPRUCE;
            case 2 -> TreeFeatures.BIRCH;
            case 3 -> TreeFeatures.JUNGLE_TREE_NO_VINE;
            default -> TreeFeatures.OAK;
        };
    }

    public static BlockState randomMushroom(int i) {
        if (i == 0) return Blocks.POTTED_RED_MUSHROOM.defaultBlockState();
        else return Blocks.POTTED_BROWN_MUSHROOM.defaultBlockState();
    }
}
