package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;

public class GrowingBeanstalkBlockEntity extends BlockEntity {

	private int ticker;
	private int layer;
	private boolean isAreaClearEnough = true;

	private int nextLeafY;
	private int yOffset;
	private float cScale;
	private float rScale;
	private int maxY;
	private int blocksSkipped;

	public GrowingBeanstalkBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.BEANSTALK_GROWER.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, GrowingBeanstalkBlockEntity te) {
		te.ticker++;
		if (te.ticker == 1) {
			//initialize shit. We can't do this in the ctor because there is no level yet
			te.nextLeafY = pos.getY() + 10 + level.getRandom().nextInt(10);
			te.yOffset = level.getRandom().nextInt(100);
			te.cScale = level.getRandom().nextFloat() * 0.25F + 0.125F; // spiral tightness scaling  //make this number negative to reverse the spiral
			te.rScale = level.getRandom().nextFloat() * 0.25F + 0.125F; // radius change scaling
			te.maxY = Math.max(pos.getY() + 100, 175);
		}
		if (level.isClientSide()) {
			if (te.ticker < 100) {
				for (int i = 0; i < 20; i++) {
					float x = (pos.getX() + level.getRandom().nextFloat()) + level.getRandom().nextInt(5) * (level.getRandom().nextBoolean() ? -1 : 1);
					float z = (pos.getZ() + level.getRandom().nextFloat()) + level.getRandom().nextInt(5) * (level.getRandom().nextBoolean() ? -1 : 1);

					BlockState underState = level.getBlockState(BlockPos.containing(x, pos.below().getY(), z));
					if (underState.isSolid()) {
						level.addAlwaysVisibleParticle(
								new BlockParticleOption(ParticleTypes.BLOCK, underState),
								x,
								pos.getY(),
								z,
								0.0F, 0.0F, 0.0F);
					}
				}
			}
		} else {
			if (te.ticker > 100 && te.ticker % 2 == 0) {
				int layerYPos = pos.getY() + te.layer;
				if (te.isAreaClearEnough && layerYPos < te.maxY) {

					float x = pos.getX();
					float z = pos.getZ();

					float radius = 4F + Mth.sin((pos.getY() + te.yOffset) * te.rScale) * 3F; // make radius a little wavy
					x -= Mth.sin((pos.getY() + te.yOffset) * te.cScale) * radius;
					z -= Mth.cos((pos.getY() + te.yOffset) * te.cScale) * radius;

					// make radius a little wavy
					radius = 5F + Mth.sin((layerYPos + te.yOffset) * te.rScale) * 2.5F;

					// find center of stalk
					float cx = x + Mth.sin((layerYPos + te.yOffset) * te.cScale) * radius;
					float cz = z + Mth.cos((layerYPos + te.yOffset) * te.cScale) * radius;


					float stalkThickness = 2.5F;

					// reduce thickness near top
					if (te.maxY - layerYPos < 5) {
						stalkThickness *= (te.maxY - layerYPos) / 5F;
					}

					int minX = Mth.floor(x - radius - stalkThickness);
					int maxX = Mth.ceil(x + radius + stalkThickness);
					int minZ = Mth.floor(z - radius - stalkThickness);
					int maxZ = Mth.ceil(z + radius + stalkThickness);

					// generate stalk
					for (int dx = minX; dx < maxX && te.isAreaClearEnough; dx++) {
						for (int dz = minZ; dz < maxZ && te.isAreaClearEnough; dz++) {
							float circle = (dx - cx) * (dx - cx) + (dz - cz) * (dz - cz);
							if (circle < stalkThickness * stalkThickness) {
								te.isAreaClearEnough = te.tryToPlaceStalk(level, new BlockPos(dx, layerYPos, dz), true);
								if (circle < stalkThickness) {
									te.tryToPlaceStalk(level, new BlockPos(dx, layerYPos + 1, dz), false);
									if (circle < stalkThickness / 2) {
										te.tryToPlaceStalk(level, new BlockPos(dx, layerYPos + 2, dz), false);
									}
								}
							}
						}
					}

					//reset skipped blocks as we're moving on to a new layer
					te.blocksSkipped = 0;

					// leaves?
					if (layerYPos == te.nextLeafY) {
						// make leaf blob

						boolean wasAnEvenNumber = te.nextLeafY % 2 == 0;

						float v = radius + (wasAnEvenNumber ? stalkThickness : -stalkThickness);
						int lx = (int) (x + Mth.sin((layerYPos + te.yOffset) * te.cScale) * v);
						int lz = (int) (z + Mth.cos((layerYPos + te.yOffset) * te.cScale) * v);

						te.placeLeaves(level, new BlockPos(lx, layerYPos, lz));

						te.nextLeafY = layerYPos + 5 + level.getRandom().nextInt(10);
						if ((te.nextLeafY % 2 == 0) == wasAnEvenNumber) te.nextLeafY++;
					}
					te.layer++;
				} else {
					level.setBlockAndUpdate(pos, TFBlocks.HUGE_STALK.get().defaultBlockState());
					level.removeBlockEntity(pos);
				}
			}
		}
	}

	private void placeLeaves(Level world, BlockPos pos) {
		// stalk at center
		world.setBlockAndUpdate(pos, TFBlocks.HUGE_STALK.get().defaultBlockState());

		// small squares
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				int distance = Math.abs(dx) + Math.abs(dz) + 1;
				this.tryToPlaceLeaves(world, pos.offset(dx, -1, dz), distance);
				this.tryToPlaceLeaves(world, pos.offset(dx, 1, dz), distance);
			}
		}
		// larger square
		for (int dx = -2; dx <= 2; dx++) {
			for (int dz = -2; dz <= 2; dz++) {
				if (!((dx == 2 || dx == -2) && (dz == 2 || dz == -2))) {
					this.tryToPlaceLeaves(world, pos.offset(dx, 0, dz), Math.max(Math.abs(dx) + Math.abs(dz), 1));
				}
			}
		}
	}

	/**
	 * Place the stalk block only if the destination is clear.  Return false if a layer is blocked by 15 or more blocks.
	 */
	private boolean tryToPlaceStalk(Level level, BlockPos pos, boolean checkBlocked) {
		BlockState state = level.getBlockState(pos);
		if (state.isAir() || (state.canBeReplaced() && !state.is(TFBlocks.BEANSTALK_GROWER)) || (state.isAir() || state.is(BlockTags.LEAVES)) || state.getBlock().equals(TFBlocks.FLUFFY_CLOUD)) {
			level.setBlockAndUpdate(pos, TFBlocks.HUGE_STALK.get().defaultBlockState());
			if (pos.getY() > 150) {
				for (int i = 0; i < 7; i++) {
					if (level.getBlockState(pos.relative(Direction.UP, i)).is(TFBlocks.WISPY_CLOUD) || level.getBlockState(pos.relative(Direction.UP, i)).is(TFBlocks.FLUFFY_CLOUD)) {
						level.setBlockAndUpdate(pos.relative(Direction.UP, i), Blocks.AIR.defaultBlockState());
					}
				}
			}
			return true;
		} else {
			if (!state.is(TFBlocks.HUGE_STALK) && checkBlocked) {
				this.blocksSkipped++;
			}
			return this.blocksSkipped < 15;
		}
	}

	private void tryToPlaceLeaves(Level world, BlockPos pos, int distance) {
		BlockState state = world.getBlockState(pos);
		if (state.isAir() || state.is(BlockTags.LEAVES)) {
			world.setBlock(pos, TFBlocks.BEANSTALK_LEAVES.get().defaultBlockState().setValue(LeavesBlock.DISTANCE, distance), 2);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag compoundTag) {
		super.saveAdditional(compoundTag);
		compoundTag.putInt("ticker", this.ticker);
		compoundTag.putInt("layer", this.layer);
		compoundTag.putBoolean("isAreaClearEnough", this.isAreaClearEnough);

		compoundTag.putInt("nextLeafY", this.nextLeafY);
		compoundTag.putInt("yOffset", this.yOffset);
		compoundTag.putFloat("cScale", this.cScale);
		compoundTag.putFloat("rScale", this.rScale);
		compoundTag.putInt("maxY", this.maxY);
		compoundTag.putInt("blocksSkipped", this.blocksSkipped);
	}

	@Override
	public void load(CompoundTag compoundTag) {
		super.load(compoundTag);
		this.ticker = compoundTag.getInt("ticker");
		this.layer = compoundTag.getInt("layer");
		this.isAreaClearEnough = compoundTag.getBoolean("isAreaClearEnough");

		this.nextLeafY = compoundTag.getInt("nextLeafY");
		this.yOffset = compoundTag.getInt("yOffset");
		this.cScale = compoundTag.getFloat("cScale");
		this.rScale = compoundTag.getFloat("rScale");
		this.maxY = compoundTag.getInt("maxY");
		this.blocksSkipped = compoundTag.getInt("blocksSkipped");
	}

	public boolean isBeanstalkRumbling() {
		return this.ticker < 110;
	}
}
