package twilightforest.world.components;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.apache.commons.lang3.mutable.MutableBoolean;
import twilightforest.block.TFBlocks;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

//Framework taken from CaveWorldCarver, everything worth knowing is documented for easier changes in the future
public class TFCavesCarver extends WorldCarver<CaveCarverConfiguration> {

	private final boolean isHighlands;

	public TFCavesCarver(Codec<CaveCarverConfiguration> codec, boolean isHighlands) {
		super(codec);
		this.liquids = ImmutableSet.of(Fluids.WATER, Fluids.LAVA);
		this.replaceableBlocks = ImmutableSet.of(Blocks.SAND, Blocks.GRAVEL);
		this.isHighlands = isHighlands;
	}

	public boolean isStartChunk(CaveCarverConfiguration config, Random rand) {
		return rand.nextFloat() <= config.probability;
	}

	public boolean carve(CarvingContext ctx, CaveCarverConfiguration config, ChunkAccess access, Function<BlockPos, Biome> biomePos, Random rand, Aquifer aquifer, ChunkPos accessPos, BitSet bitSet) {
		int i = SectionPos.sectionToBlockCoord(this.getRange() * 2 - 1);
		int j = rand.nextInt(rand.nextInt(rand.nextInt(this.getCaveBound()) + 1) + 1);

		for(int k = 0; k < j; ++k) {
			double x = accessPos.getBlockX(rand.nextInt(16));
			double y = config.y.sample(rand, ctx);
			double z = accessPos.getBlockZ(rand.nextInt(16));
			double horiz = config.horizontalRadiusMultiplier.sample(rand);
			double vert = config.verticalRadiusMultiplier.sample(rand);
			double floor = config.floorLevel.sample(rand);
			WorldCarver.CarveSkipChecker checker = (p_159202_, p_159203_, p_159204_, p_159205_, p_159206_) -> shouldSkip(p_159203_, p_159204_, p_159205_, floor);
			int l = 1;
			if (rand.nextInt(4) == 0 && isHighlands) {
				double d6 = config.yScale.sample(rand);
				float f1 = 1.0F + rand.nextFloat() * 6.0F;
				this.createRoom(ctx, config, access, biomePos, rand.nextLong(), aquifer, x, y, z, f1, d6, bitSet, checker);
				l += rand.nextInt(4);
			}

			for(int k1 = 0; k1 < l; ++k1) {
				float f = rand.nextFloat() * ((float)Math.PI * 2F);
				float f3 = (rand.nextFloat() - 0.5F) / 4.0F;
				float f2 = this.getThickness(rand);
				int i1 = i - rand.nextInt(i / 4);
				this.createTunnel(ctx, config, access, biomePos, rand.nextLong(), aquifer, x, y, z, horiz, vert, f2, f, f3, 0, i1, this.getYScale(), bitSet, checker);
			}
		}

		return true;
	}

	@Override
	protected boolean carveBlock(CarvingContext ctx, CaveCarverConfiguration config, ChunkAccess access, Function<BlockPos, Biome> biomePos, BitSet bitset, Random rand, BlockPos.MutableBlockPos pos, BlockPos.MutableBlockPos posUp, Aquifer aquifer, MutableBoolean isSurface) {
		BlockState blockstate = access.getBlockState(pos);
		BlockState blockstate1 = access.getBlockState(posUp.setWithOffset(pos, Direction.UP));
		if (blockstate.is(Blocks.GRASS_BLOCK) || blockstate.is(Blocks.MYCELIUM) || blockstate.is(Blocks.PODZOL) || blockstate.is(Blocks.DIRT_PATH)) {
			isSurface.setTrue();
		}

		//We dont want caves to go so far down you can see bedrock, so lets stop them right before
		if(pos.getY() < access.getMinBuildHeight() + 6) return false;

		if (!this.canReplaceBlock(blockstate, blockstate1) && !isDebugEnabled(config)) {
			return false;
		} else {
			BlockState blockstate2 = this.getCarveState(ctx, config, pos, aquifer);
			if (blockstate2 == null) {
				return false;
			} else {
				//here's the code for preventing floating water and making dirt roofs. Enjoy :)

				for (Direction facing : Direction.values()) {
					FluidState aboveSurface = access.getFluidState(posUp.offset(pos.offset(0, 1, 0)));
					FluidState areaAround = access.getFluidState(posUp.relative(facing));
					FluidState areaAboveAround = access.getFluidState(posUp.offset(pos.offset(0, 1, 0).relative(facing)));

					if (areaAround.is(FluidTags.WATER) || areaAboveAround.is(FluidTags.WATER) || aboveSurface.is(FluidTags.WATER)) {
						return false;
					} else {
						if (rand.nextInt(10) == 0 && access.getBlockState(pos).isAir() && access.getBlockState(pos.relative(facing)).is(BlockTags.BASE_STONE_OVERWORLD) && this.isHighlands) {
							access.setBlockState(pos.relative(facing), TFBlocks.TROLLSTEINN.get().defaultBlockState(), false);
						}
						access.setBlockState(pos, CAVE_AIR, false);

						if ((access.getBlockState(pos.above()).is(BlockTags.BASE_STONE_OVERWORLD) || access.getFluidState(pos.above()).is(FluidTags.WATER)) && access.getBlockState(pos).isAir() && !this.isHighlands) {
							switch(rand.nextInt(5)) {
								case 0, 1, 2 -> access.setBlockState(pos.above(), Blocks.DIRT.defaultBlockState(), false);
								case 3 -> access.setBlockState(pos.above(), Blocks.ROOTED_DIRT.defaultBlockState(), false);
								case 4 -> access.setBlockState(pos.above(), Blocks.COARSE_DIRT.defaultBlockState(), false);
							}
						}
						if (isSurface.isTrue()) {
							BlockPos posDown = pos.setWithOffset(pos, Direction.DOWN).below();
							if (access.getBlockState(posDown).is(Blocks.DIRT)) {
								access.setBlockState(posDown, biomePos.apply(pos).getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial(), false);
							}
						}

					}
				}
				return true;
			}
		}
	}

	protected int getCaveBound() {
		return 15;
	}

	protected float getThickness(Random rand) {
		float f = rand.nextFloat() * 2.0F + rand.nextFloat();
		if (rand.nextInt(10) == 0) {
			f *= rand.nextFloat() * rand.nextFloat() * 3.0F + 1.0F;
		}

		return f;
	}

	protected double getYScale() {
		return 1.0D;
	}

	protected void createRoom(CarvingContext ctx, CaveCarverConfiguration config, ChunkAccess access, Function<BlockPos, Biome> biomePos, long seed, Aquifer aquifer, double posX, double posY, double posZ, float radius, double horizToVertRatio, BitSet bitSet, WorldCarver.CarveSkipChecker checker) {
		double d0 = 1.5D + (double)(Mth.sin(((float)Math.PI / 2F)) * radius);
		double d1 = d0 * horizToVertRatio;
		this.carveEllipsoid(ctx, config, access, biomePos, seed, aquifer, posX + 1.0D, posY, posZ, d0, d1, bitSet, checker);
	}

	protected void createTunnel(CarvingContext ctx, CaveCarverConfiguration config, ChunkAccess access, Function<BlockPos, Biome> biomePos, long seed, Aquifer aquifer, double posX, double posY, double posZ, double horizMult, double vertMult, float thickness, float yaw, float pitch, int branchIndex, int branchCount, double horizToVertRatio, BitSet bitSet, WorldCarver.CarveSkipChecker checker) {
		Random random = new Random(seed);
		int i = random.nextInt(branchCount / 2) + branchCount / 4;
		boolean flag = random.nextInt(6) == 0;
		float f = 0.0F;
		float f1 = 0.0F;

		for(int j = branchIndex; j < branchCount; ++j) {
			double d0 = 1.5D + (double)(Mth.sin((float)Math.PI * (float)j / (float)branchCount) * thickness);
			double d1 = d0 * horizToVertRatio;
			float f2 = Mth.cos(pitch);
			posX += Mth.cos(yaw) * f2;
			posY += Mth.sin(pitch);
			posZ += Mth.sin(yaw) * f2;
			pitch = pitch * (flag ? 0.92F : 0.7F);
			pitch = pitch + f1 * 0.1F;
			yaw += f * 0.1F;
			f1 = f1 * 0.9F;
			f = f * 0.75F;
			f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
			if (j == i && thickness > 1.0F) {
				this.createTunnel(ctx, config, access, biomePos, random.nextLong(), aquifer, posX, posY, posZ, horizMult, vertMult, random.nextFloat() * 0.5F + 0.5F, yaw - ((float)Math.PI / 2F), pitch / 3.0F, j, branchCount, 1.0D, bitSet, checker);
				this.createTunnel(ctx, config, access, biomePos, random.nextLong(), aquifer, posX, posY, posZ, horizMult, vertMult, random.nextFloat() * 0.5F + 0.5F, yaw + ((float)Math.PI / 2F), pitch / 3.0F, j, branchCount, 1.0D, bitSet, checker);
				return;
			}

			if (random.nextInt(4) != 0) {
				if (!canReach(access.getPos(), posX, posZ, j, branchCount, thickness)) {
					return;
				}

				this.carveEllipsoid(ctx, config, access, biomePos, seed, aquifer, posX, posY, posZ, d0 * horizMult, d1 * vertMult, bitSet, checker);
			}
		}

	}

	//make our own list of replaceables since otherwise it breaks structures like the yeti cave
	@Override
	protected boolean canReplaceBlock(BlockState state, BlockState aboveState) {
		return (this.replaceableBlocks.contains(state.getBlock()) || state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.DIRT) || state.is(TFBlocks.TROLLSTEINN.get())) && !aboveState.getFluidState().is(FluidTags.WATER);
	}

	private static boolean shouldSkip(double posX, double posY, double posZ, double minY) {
		if (posY <= minY) {
			return true;
		} else {
			return posX * posX + posY * posY + posZ * posZ >= 1.0D;
		}
	}
}
