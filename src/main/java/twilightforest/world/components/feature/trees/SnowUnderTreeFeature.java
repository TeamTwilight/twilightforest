package twilightforest.world.components.feature.trees;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SnowUnderTreeFeature extends Feature<NoneFeatureConfiguration> {

	public SnowUnderTreeFeature(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		BlockPos pos = ctx.origin();
		WorldGenLevel world = ctx.level();
		BlockPos.MutableBlockPos mPos = new BlockPos.MutableBlockPos();
		BlockPos.MutableBlockPos mPosDown = new BlockPos.MutableBlockPos();

		for (int xi = 0; xi < 16; xi++) {
			for (int zi = 0; zi < 16; zi++) {
				int x = pos.getX() + xi;
				int z = pos.getZ() + zi;
				mPos.set(x, world.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z) - 1, z);

				if (world.getBlockState(mPos).getBlock() instanceof LeavesBlock) {
					BlockState state;
					mPos.set(x, world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);
					state = world.getBlockState(mPos);

					if (state.isAir()) {
						BlockState stateBelow;
						mPosDown.set(mPos).move(Direction.DOWN);
						stateBelow = world.getBlockState(mPosDown);

						if (stateBelow.isFaceSturdy(world, mPosDown, Direction.UP)) {
							world.setBlock(mPos, Blocks.SNOW.defaultBlockState(), Block.UPDATE_CLIENTS);

							if (stateBelow.hasProperty(SnowyDirtBlock.SNOWY)) {
								world.setBlock(mPosDown, stateBelow.setValue(SnowyDirtBlock.SNOWY, true), Block.UPDATE_CLIENTS);
							}
						}
					}
				}
			}
		}
		return true;
	}
}