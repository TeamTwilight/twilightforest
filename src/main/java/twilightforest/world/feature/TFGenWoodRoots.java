package twilightforest.world.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import twilightforest.block.TFBlocks;
import twilightforest.util.FeatureUtil;

import java.util.Random;
import java.util.function.Function;

public class TFGenWoodRoots extends Feature<NoFeatureConfig> {

	private BlockState rootBlock = TFBlocks.root.get().getDefaultState();
	private BlockState oreBlock = TFBlocks.liveroot_block.get().getDefaultState();

	public TFGenWoodRoots(Function<Dynamic<?>, NoFeatureConfig> configIn) {
		super(configIn);
	}

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
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

		float angle = rand.nextFloat();

		BlockPos dest = FeatureUtil.translate(pos, length, angle, tilt);

		// restrict x and z to within 7
		int limit = 6;
		if (pos.getX() + limit < dest.getX()) {
			dest = new BlockPos(pos.getX() + limit, dest.getY(), dest.getZ());
		}
		if (pos.getX() - limit > dest.getX()) {
			dest = new BlockPos(pos.getX() - limit, dest.getY(), dest.getZ());
		}
		if (pos.getZ() + limit < dest.getZ()) {
			dest = new BlockPos(dest.getX(), dest.getY(), pos.getZ() + limit);
		}
		if (pos.getZ() - limit > dest.getZ()) {
			dest = new BlockPos(dest.getX(), dest.getY(), pos.getZ() - limit);
		}

		// end must be in stone
		if (world.getBlockState(dest).getBlock() != Blocks.STONE) {
			return false;
		}

		// if both the start and the end are in stone, put a root there
		BlockPos[] lineArray = FeatureUtil.getBresehnamArrays(pos, dest);
		for (BlockPos coord : lineArray) {
			this.placeRootBlock(world, coord, rootBlock);
		}


		/*// if we are long enough, make either another root or an oreball
		if (length > 8) {
			if (rand.nextInt(3) > 0) {
				// length > 8, usually split off into another root half as long
				BlockPos nextSrc = FeatureUtil.translate(pos, length / 2, angle, tilt);
				float nextAngle = (angle + 0.25F + (rand.nextFloat() * 0.5F)) % 1.0F;
				float nextTilt = 0.6F + rand.nextFloat() * 0.3F;
				drawRoot(world, rand, oPos, nextSrc, length / 2.0F, nextAngle, nextTilt);
			}
		}*/

		if (length > 6) {
			if (rand.nextInt(4) == 0) {
				// length > 6, potentially make oreball
				BlockPos ballSrc = FeatureUtil.translate(pos, length / 2, angle, tilt);
				BlockPos ballDest = FeatureUtil.translate(ballSrc, 1.5, (angle + 0.5F) % 1.0F, 0.75);

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
	protected void placeRootBlock(IWorld world, BlockPos pos, BlockState state) {
		if (TFTreeGenerator.canRootGrowIn(world, pos)) {
			world.setBlockState(pos, state, 2);
		}
	}
}
