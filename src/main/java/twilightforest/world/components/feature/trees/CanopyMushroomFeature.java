package twilightforest.world.components.feature.trees;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import twilightforest.init.TFBlocks;
import twilightforest.util.FeatureLogic;
import twilightforest.util.VoxelBresenhamIterator;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class CanopyMushroomFeature extends AbstractHugeMushroomFeature {
	private int bugsLeft;

	public CanopyMushroomFeature(Codec<HugeMushroomFeatureConfiguration> featureConfigurationCodec) {
		super(featureConfigurationCodec);
	}

	/**
	 * How much space our mushroom needs, this seems to be about right
	 */
	@Override
	protected int getTreeRadiusForHeight(int i, int i1, int foliageRadius, int treeHeight) {
		return treeHeight <= 3 ? 0 : (int) ((float) foliageRadius * 1.5F);
	}

	@Override
	protected void placeTrunk(LevelAccessor levelAccessor, RandomSource random, BlockPos pos, HugeMushroomFeatureConfiguration featureConfiguration, int height, BlockPos.MutableBlockPos mutableBlockPos) {
		for (int i = 0; i < height; ++i) {
			mutableBlockPos.set(pos).move(Direction.UP, i);
			if (!levelAccessor.getBlockState(mutableBlockPos).isSolidRender(levelAccessor, mutableBlockPos)) {
				this.setBlock(levelAccessor, mutableBlockPos, featureConfiguration.stemProvider.getState(random, pos));

				if (this.bugsLeft > 0 && i > height / 2 && random.nextInt(10) == 9) addFirefly(levelAccessor, mutableBlockPos, random);
			} else {
				height = i;
				break;
			}
		}

		int numBranches = this.getBranches(random);
		float offset = random.nextFloat();
		for (int b = 0; b < numBranches; b++) {
			buildABranch(levelAccessor, pos, height - 6 + b, this.getLength(random), 0.3 * b + offset, random, new HugeMushroomFeatureConfiguration(featureConfiguration.capProvider, featureConfiguration.stemProvider, featureConfiguration.foliageRadius - 1));
		}
	}

	/**
	 * Add a firefly on a RandomSource face of a block
	 */
	protected void addFirefly(LevelAccessor levelAccessor, BlockPos pos, RandomSource random) {
		Direction direction = Direction.getRandom(random);
		if (direction.getAxis() != Direction.Axis.Y) {
			BlockPos.MutableBlockPos bugPos = new BlockPos.MutableBlockPos();
			bugPos.set(pos).move(direction);
			if (!levelAccessor.getBlockState(bugPos).isSolidRender(levelAccessor, bugPos)) {
				BlockState bugState = TFBlocks.FIREFLY.get().defaultBlockState().setValue(DirectionalBlock.FACING, direction);
				this.setBlock(levelAccessor, bugPos, bugState);
				this.bugsLeft--;
			}
		}
	}

	@Override
	protected int getTreeHeight(RandomSource random) {
		return 9 + random.nextInt(5);
	}

	protected abstract int getBranches(RandomSource random);

	protected abstract double getLength(RandomSource random);

	private void buildABranch(LevelAccessor levelAccessor, BlockPos pos, int height, double length, double angle, RandomSource random, HugeMushroomFeatureConfiguration featureConfiguration) {
		BlockPos src = pos.above(height);
		BlockPos dest = FeatureLogic.translate(src, length, angle, 0.2);

		for (BlockPos pixel : new VoxelBresenhamIterator(src, new BlockPos(dest.getX(), src.getY(), dest.getZ()))) {
			BlockState blockstate = featureConfiguration.stemProvider.getState(random, pos);

			if (blockstate.hasProperty(HugeMushroomBlock.UP) && blockstate.hasProperty(HugeMushroomBlock.DOWN)) {
				blockstate = blockstate.setValue(HugeMushroomBlock.DOWN, true).setValue(HugeMushroomBlock.UP, true);//Seal up the ups and downs
			}

			this.setBlock(levelAccessor, pixel, blockstate);
		}

		int max = Math.max(src.getY(), dest.getY());

		for (int i = Math.min(src.getY(), dest.getY()); i < max + 1; i++) {
			BlockState blockstate = featureConfiguration.stemProvider.getState(random, pos);

			if (blockstate.hasProperty(HugeMushroomBlock.DOWN)) {
				if (i == Math.min(src.getY(), dest.getY())) blockstate = blockstate.setValue(HugeMushroomBlock.DOWN, true);//Seal up the bottom one, so it looks better
			}

			BlockPos blockPos = new BlockPos(dest.getX(), i, dest.getZ());

			this.setBlock(levelAccessor, blockPos, blockstate);

			if (this.bugsLeft > 0 && i > Math.min(src.getY(), dest.getY()) / 2 && random.nextInt(20) == 0) addFirefly(levelAccessor, blockPos, random);
		}

		this.makeCap(levelAccessor, random, dest, 1, new BlockPos.MutableBlockPos(), featureConfiguration);//Branches need caps as well, height in this case is set to 1
	}

	@Override//Pretty much a 1:1 vanilla copy of the big brown mushroom cap code
	protected void makeCap(LevelAccessor levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, HugeMushroomFeatureConfiguration featureConfiguration) {
		int i = featureConfiguration.foliageRadius;

		for (int x = -i; x <= i; ++x) {
			for (int z = -i; z <= i; ++z) {
				boolean xIsMin = x == -i;
				boolean xIsMax = x == i;
				boolean zIsMin = z == -i;
				boolean zIsMax = z == i;
				boolean xMinMax = xIsMin || xIsMax;
				boolean zMinMax = zIsMin || zIsMax;
				if (!xMinMax || !zMinMax) {
					mutableBlockPos.setWithOffset(pos, x, height, z);
					if (!levelAccessor.getBlockState(mutableBlockPos).isSolidRender(levelAccessor, mutableBlockPos)) {
						boolean xMinOrZ = xIsMin || zMinMax && x == 1 - i;
						boolean xMaxOrZ = xIsMax || zMinMax && x == i - 1;
						boolean zMinOrX = zIsMin || xMinMax && z == 1 - i;
						boolean zMaxOrX = zIsMax || xMinMax && z == i - 1;
						BlockState blockstate = featureConfiguration.capProvider.getState(random, pos);

						if (blockstate.hasProperty(HugeMushroomBlock.WEST) && blockstate.hasProperty(HugeMushroomBlock.EAST) && blockstate.hasProperty(HugeMushroomBlock.NORTH) && blockstate.hasProperty(HugeMushroomBlock.SOUTH)) {
							blockstate = blockstate.setValue(HugeMushroomBlock.WEST, xMinOrZ).setValue(HugeMushroomBlock.EAST, xMaxOrZ).setValue(HugeMushroomBlock.NORTH, zMinOrX).setValue(HugeMushroomBlock.SOUTH, zMaxOrX);
						}

						this.setBlock(levelAccessor, mutableBlockPos, blockstate);
					}
				}
			}
		}
	}

	@Override
	public boolean place(FeaturePlaceContext<HugeMushroomFeatureConfiguration> context) {
		this.bugsLeft = Math.max(0, context.random().nextInt(10) - 4) / 2; //Weird math, I know, but I like the odds (and weird math, sue me)
		return super.place(context);
	}
}
