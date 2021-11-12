package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.block.TFBlocks;
import twilightforest.util.FeatureLogic;
import twilightforest.util.VoxelBresenhamIterator;

import java.util.Random;

public class TFGenWoodRoots extends Feature<NoneFeatureConfiguration> {

	private final BlockState rootBlock = TFBlocks.ROOT_BLOCK.get().defaultBlockState();
	private final BlockState oreBlock = TFBlocks.LIVEROOT_BLOCK.get().defaultBlockState();

	public TFGenWoodRoots(Codec<NoneFeatureConfiguration> configIn) {
		super(configIn);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		Random rand = ctx.random();

		// start must be in stone
		if (world.getBlockState(pos).getBlock() != Blocks.STONE) {
			return false;
		}

		float length = rand.nextFloat() * 6.0F + rand.nextFloat() * 6.0F + 4.0F;
		if (length > pos.getY()) {
			length = pos.getY();
		}

		// tilt between 0.6 and 0.9
		float tilt = 0.6F + rand.nextFloat() * 0.3F;

		return drawRoot(world, rand, pos, length, rand.nextFloat(), tilt);
	}

	private boolean drawRoot(LevelAccessor world, Random rand, BlockPos pos, float length, float angle, float tilt) {
		// put origin at where we start
		return this.drawRoot(world, rand, pos, pos, length, angle, tilt);
	}

	private boolean drawRoot(LevelAccessor world, Random rand, BlockPos oPos, BlockPos pos, float length, float angle, float tilt) {
		// generate a direction and a length
		BlockPos dest = FeatureLogic.translate(pos, length, angle, tilt);

		// restrict x and z to within 7
		int limit = 6;
		if (oPos.getX() + limit < dest.getX()) {
			dest = new BlockPos(oPos.getX() + limit, dest.getY(), dest.getZ());
		}
		if (oPos.getX() - limit > dest.getX()) {
			dest = new BlockPos(oPos.getX() - limit, dest.getY(), dest.getZ());
		}
		if (oPos.getZ() + limit < dest.getZ()) {
			dest = new BlockPos(dest.getX(), dest.getY(), oPos.getZ() + limit);
		}
		if (oPos.getZ() - limit > dest.getZ()) {
			dest = new BlockPos(dest.getX(), dest.getY(), oPos.getZ() - limit);
		}

		// end must be in stone
		if (world.getBlockState(dest).getBlock() != Blocks.STONE) {
			return false;
		}

		// if both the start and the end are in stone, put a root there
		for (BlockPos coord : new VoxelBresenhamIterator(pos, dest)) {
			this.placeRootBlock(world, coord, rootBlock);
		}


		// if we are long enough, make either another root or an oreball
		if (length > 8) {
			if (rand.nextInt(3) > 0) {
				// length > 8, usually split off into another root half as long
				BlockPos nextSrc = FeatureLogic.translate(pos, length / 2, angle, tilt);
				float nextAngle = (angle + 0.25F + (rand.nextFloat() * 0.5F)) % 1.0F;
				float nextTilt = 0.6F + rand.nextFloat() * 0.3F;
				drawRoot(world, rand, oPos, nextSrc, length / 2.0F, nextAngle, nextTilt);
			}
		}

		if (length > 6) {
			if (rand.nextInt(4) == 0) {
				// length > 6, potentially make oreball
				BlockPos ballSrc = FeatureLogic.translate(pos, length / 2, angle, tilt);
				BlockPos ballDest = FeatureLogic.translate(ballSrc, 1.5, (angle + 0.5F) % 1.0F, 0.75);

				this.placeRootBlock(world, ballSrc, oreBlock);
				this.placeRootBlock(world, new BlockPos(ballSrc.getX(), ballSrc.getY(), ballDest.getZ()), oreBlock);
				this.placeRootBlock(world, new BlockPos(ballDest.getX(), ballSrc.getY(), ballSrc.getZ()), oreBlock);
				this.placeRootBlock(world, new BlockPos(ballSrc.getX(), ballSrc.getY(), ballDest.getZ()), oreBlock);
				this.placeRootBlock(world, new BlockPos(ballSrc.getX(), ballDest.getY(), ballSrc.getZ()), oreBlock);
				this.placeRootBlock(world, new BlockPos(ballSrc.getX(), ballDest.getY(), ballDest.getZ()), oreBlock);
				this.placeRootBlock(world, new BlockPos(ballDest.getX(), ballDest.getY(), ballSrc.getZ()), oreBlock);
				this.placeRootBlock(world, ballDest, oreBlock);
			}
		}

		return true;
	}

	/**
	 * Function used to actually place root blocks if they're not going to break anything important
	 */
	protected void placeRootBlock(LevelAccessor world, BlockPos pos, BlockState state) {
		if (FeatureLogic.canRootGrowIn(world, pos)) {
			world.setBlock(pos, state, 3);
		}
	}
}
