package twilightforest.world.components;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.minecraft.world.level.material.Fluids;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;

import java.util.function.Function;

//Framework taken from CaveWorldCarver, everything worth knowing is documented for easier changes in the future
public class TFCavesCarver extends WorldCarver<CaveCarverConfiguration> {
	private final boolean isHighlands;
	private final BlockStateProvider wallBlocks;
	private final ImprovedNoise noise;

	public TFCavesCarver(Codec<CaveCarverConfiguration> codec, boolean isHighlands, BlockStateProvider wallBlocks) {
		super(codec);
		this.wallBlocks = wallBlocks;
		this.liquids = ImmutableSet.of(Fluids.WATER, Fluids.LAVA);
		this.isHighlands = isHighlands;

		// Since this object is constructed on game bootup instead of world creation, we can't use world seed
		// Making a different random one each bootup will only screw up determinism, and across separate sessions chunkgen will produce discontinuous noise edges
		// We only need the noise for one thing anyway, and that's placing dirt
		this.noise = new ImprovedNoise(new LegacyRandomSource(6972119253061020355L));
	}

	@Override
	public boolean isStartChunk(CaveCarverConfiguration config, RandomSource rand) {
		// Highland caves instead spawn when the troll caves structure is nearby, and with special location ruls
		return this.isHighlands || rand.nextFloat() <= config.probability;
	}

	@Override
	public boolean carve(CarvingContext ctx, CaveCarverConfiguration config, ChunkAccess access, Function<BlockPos, Holder<Biome>> biomePos, RandomSource random, Aquifer aquifer, ChunkPos accessPos, CarvingMask mask) {
		if (this.isHighlands && (Mth.clamp(LegacyLandmarkPlacements.manhattanDistanceFromLandmarkCenter(accessPos.x, accessPos.z), 0, 0b11) & 0b1) == 1)
			return false; // If highlands, enforces a binary grid (diagonal range of 4 chunks) of possible placements around the structure center, with center being one of the zero tiles

		int i = SectionPos.sectionToBlockCoord(this.getRange() * 2 - 1);

		// If highlands, only roll chance to generate even 1 cave. Otherwise, limited caves spawn for regular TF underground
		int caveCount = this.isHighlands ? random.nextInt(2) : random.nextInt(this.getCaveBound());

		for (int caveIndex = 0; caveIndex < caveCount; ++caveIndex) {
			double x = accessPos.getBlockX(random.nextInt(16));
			double y = config.y.sample(random, ctx);
			double z = accessPos.getBlockZ(random.nextInt(16));
			double horiz = config.horizontalRadiusMultiplier.sample(random);
			double vert = config.verticalRadiusMultiplier.sample(random);
			double floor = config.floorLevel.sample(random);
			CarveSkipChecker checker = (context, dX, dY, dZ, yPos) -> shouldSkip(dX, dY, dZ, floor);
			int tunnelCount = 1;
			if (this.isHighlands || random.nextInt(4) == 0) {
				double horizToVertRatio = config.yScale.sample(random);
				float radius = 1.0F + random.nextFloat() * 6.0F;
				this.createRoom(ctx, config, access, biomePos, aquifer, x, y, z, radius, horizToVertRatio, mask, checker);
				tunnelCount += random.nextInt(4);
			}

			for (int tunnelIndex = 0; tunnelIndex < tunnelCount; ++tunnelIndex) {
				float randomRadian = random.nextFloat() * ((float) Math.PI * 2F);
				float randomPitch = (random.nextFloat() - 0.5F) / 4.0F;
				float thickness = this.getThickness(random);
				int branchCount = i - random.nextInt(i / 4);

				this.createTunnel(ctx, config, access, biomePos, random.nextLong(), aquifer, x, y, z, horiz, vert, thickness, randomRadian, randomPitch, 0, branchCount, this.getYScale(), mask, checker);
			}
		}

		return true;
	}

	@Override
	protected boolean carveBlock(CarvingContext ctx, CaveCarverConfiguration config, ChunkAccess access, Function<BlockPos, Holder<Biome>> biomePos, CarvingMask mask, BlockPos.MutableBlockPos posMutable, BlockPos.MutableBlockPos posUp, Aquifer aquifer, MutableBoolean isSurface) {
		BlockPos pos = posMutable.immutable();
		BlockState stateBeforeReplacement = access.getBlockState(pos);
		if (stateBeforeReplacement.is(Blocks.GRASS_BLOCK) || stateBeforeReplacement.is(Blocks.MYCELIUM) || stateBeforeReplacement.is(Blocks.PODZOL) || stateBeforeReplacement.is(Blocks.DIRT_PATH)) {
			isSurface.setTrue();
		}

		//We dont want caves to go so far down you can see bedrock, so lets stop them right before
		if (pos.getY() < access.getMinY() + 6) return false;

		if (!this.canReplaceBlock(config, stateBeforeReplacement) && !isDebugEnabled(config)) {
			return false;
		} else {
			BlockPos chunkOrigin = access.getPos().getWorldPosition();
			for (Direction facing : Direction.values()) {
				BlockPos relative = pos.relative(facing);
				if (isInsideChunk(relative, chunkOrigin) && access.getFluidState(relative).is(FluidTags.WATER)) {
					return false; // If replacing this block will expose any neighboring water, then skip the current position param
				}
			}

			BlockState blockStateToPlace = this.getCarveState(ctx, config, pos, aquifer);
			if (blockStateToPlace != null) {
				RandomSource randomFromPos = ctx.randomState().oreRandom().at(pos);

				if (!access.getFluidState(pos.above(2)).isEmpty()) // Sand doesn't quite generate until after the carvers, so we must look for liquid above possible sand instead
					blockStateToPlace = randomFromPos.nextBoolean() ? Blocks.ROOTED_DIRT.defaultBlockState() : Blocks.COARSE_DIRT.defaultBlockState(); // normal dirt will get replaced with sand, special ones are required

				boolean blockPlaced = access.setBlockState(pos, blockStateToPlace, false) != null;

				if (aquifer.shouldScheduleFluidUpdate() && !blockStateToPlace.getFluidState().isEmpty()) {
					access.markPosForPostprocessing(pos);
				}

				if (isSurface.isTrue()) {
					BlockPos posDown = pos.relative(Direction.DOWN);
					if (access.getBlockState(posDown).is(Blocks.DIRT)) {
						ctx.topMaterial(biomePos, access, posDown, !blockStateToPlace.getFluidState().isEmpty()).ifPresent(state -> {
							access.setBlockState(posDown, state, false);
							if (!state.getFluidState().isEmpty()) {
								access.markPosForPostprocessing(posDown);
							}
						});
					}
				}


				if (blockPlaced) this.postCarveBlock(access, pos, config, randomFromPos, chunkOrigin);

				return blockPlaced;
			} else {
				return false;
			}
		}
	}

	private static boolean isInsideChunk(BlockPos relative, BlockPos chunkOrigin) {
		int deltaX = relative.getX() - chunkOrigin.getX();
		int deltaZ = relative.getZ() - chunkOrigin.getZ();
		return deltaX >= 0 && deltaZ >= 0 && deltaX <= 15 && deltaZ <= 15;
	}

	private void postCarveBlock(ChunkAccess access, BlockPos pos, CaveCarverConfiguration config, RandomSource rand, BlockPos chunkOrigin) {
		for (Direction facing : Direction.values()) {
			BlockPos directionalRelative = pos.relative(facing);
			if (!isInsideChunk(directionalRelative, chunkOrigin)) continue;

			// FIXME Half-way configurable, would prefer to eliminate the isHighlands check entirely
			//  The rand.nextInt rolls should have some way of being set into a custom config as well

			if (this.isHighlands) {
				if (rand.nextInt(4) == 0 && this.canReplaceBlock(config, access.getBlockState(directionalRelative))) {
					access.setBlockState(directionalRelative, this.wallBlocks.getState(rand, directionalRelative), false);
				}
			} else if (facing != Direction.DOWN && (facing == Direction.UP || access.getBlockState(directionalRelative.above()).isAir() || this.checkNoiseThreshold(directionalRelative, 0.25f, 0.5f))) { //here's the code for making dirt roofs. Enjoy :)
				// Dirt is never placed below, always on roof, and typically to the sides

				BlockState neighboringBlock = access.getBlockState(directionalRelative);

				if (neighboringBlock.is(BlockTags.BASE_STONE_OVERWORLD) || neighboringBlock.getFluidState().is(FluidTags.WATER)) {
					access.setBlockState(directionalRelative, this.wallBlocks.getState(rand, directionalRelative), false);
				}
			}
		}
	}

	@SuppressWarnings("SameParameterValue")
	private boolean checkNoiseThreshold(BlockPos pos, double posScalar, double threshold) {
		double noise = this.noise.noise(pos.getX() * posScalar, pos.getY() * posScalar, pos.getZ() * posScalar);

		// Noise outputs values between -1 to 1, we must normalize it using n * 0.5 + 0.5
		return noise * 0.5 + 0.5 > threshold;
	}

	protected int getCaveBound() {
		return 4;
	}

	protected float getThickness(RandomSource rand) {
		float f = rand.nextFloat() * 2.0F + rand.nextFloat();
		if (rand.nextInt(10) == 0) {
			f *= rand.nextFloat() * rand.nextFloat() * 3.0F + 1.0F;
		}

		return f;
	}

	protected double getYScale() {
		return 1.0D;
	}

	protected void createRoom(CarvingContext ctx, CaveCarverConfiguration config, ChunkAccess access, Function<BlockPos, Holder<Biome>> biomePos, Aquifer aquifer, double posX, double posY, double posZ, float radius, double horizToVertRatio, CarvingMask mask, CarveSkipChecker checker) {
		double d0 = 1.5D + (double) (Mth.sin(((float) Math.PI / 2F)) * radius);
		double d1 = d0 * horizToVertRatio;
		this.carveEllipsoid(ctx, config, access, biomePos, aquifer, posX, posY, posZ, d0, d1, mask, checker);
	}

	protected void createTunnel(CarvingContext ctx, CaveCarverConfiguration config, ChunkAccess access, Function<BlockPos, Holder<Biome>> biomePos, long seed, Aquifer aquifer, double posX, double posY, double posZ, double horizMult, double vertMult, float thickness, float yaw, float pitch, int branchIndex, int branchCount, double horizToVertRatio, CarvingMask mask, CarveSkipChecker checker) {
		RandomSource random = RandomSource.create(seed);
		int i = random.nextInt(branchCount / 2) + branchCount / 4;
		boolean flag = random.nextInt(6) == 0;
		float f = 0.0F;
		float f1 = 0.0F;

		for (int j = branchIndex; j < branchCount; ++j) {
			double horizontalRadius = 1.5D + (double) (Mth.sin((float) Math.PI * (float) j / (float) branchCount) * thickness);
			double verticalRadius = horizontalRadius * horizToVertRatio;
			float f2 = Mth.cos(pitch);
			posX += Mth.cos(yaw) * f2;

			float yShift = Mth.sin(pitch);
			// If posY nears bedrock, "slow" its descent if marching downwards
			posY += yShift > 0 || posY + yShift > access.getMinY() + 10 ? yShift : yShift * 0.25f;

			posZ += Mth.sin(yaw) * f2;
			pitch = pitch * (flag ? 0.92F : 0.7F);
			pitch = pitch + f1 * 0.1F;
			yaw += f * 0.1F;
			f1 = f1 * 0.9F;
			f = f * 0.75F;
			f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
			if (j == i && thickness > 1.0F) {
				this.createTunnel(ctx, config, access, biomePos, random.nextLong(), aquifer, posX, posY, posZ, horizMult, vertMult, random.nextFloat() * 0.5F + 0.5F, yaw - ((float) Math.PI / 2F), pitch / 3.0F, j, branchCount, 1.0D, mask, checker);
				this.createTunnel(ctx, config, access, biomePos, random.nextLong(), aquifer, posX, posY, posZ, horizMult, vertMult, random.nextFloat() * 0.5F + 0.5F, yaw + ((float) Math.PI / 2F), pitch / 3.0F, j, branchCount, 1.0D, mask, checker);
				return;
			}

			if (random.nextInt(4) != 0) {
				if (!canReach(access.getPos(), posX, posZ, j, branchCount, thickness)) {
					return;
				}

				// Additional size-boosting to make wider & taller spherical rooms
				boolean shouldEnlargeSphere = posY > access.getMinY() + 12 && random.nextInt(48) == 0;
				float sizeMultiplier = shouldEnlargeSphere
					? random.nextFloat() * random.nextFloat() * 2f + 1
					: 1;

				double sphereHRadius = Math.min(horizontalRadius * horizMult * sizeMultiplier, 10);
				double sphereVRadius = verticalRadius * vertMult * sizeMultiplier;
				// If side-boosting is applied, then squish the sphere's edge-steeped floor into a dish
				double sphereVRadiusLimited = shouldEnlargeSphere ? Math.min(sphereVRadius, sphereHRadius * 0.65f) : sphereVRadius;

				this.carveEllipsoid(ctx, config, access, biomePos, aquifer, posX, posY, posZ, sphereHRadius, sphereVRadiusLimited, mask, checker);
			}
		}

	}

	@Override
	protected boolean canReplaceBlock(CaveCarverConfiguration config, BlockState state) {
		return !state.is(BlockTags.ICE) && !state.getFluidState().is(FluidTags.WATER) && super.canReplaceBlock(config, state);
	}

	private static boolean shouldSkip(double posX, double posY, double posZ, double minY) {
		if (posY <= minY) {
			return true;
		} else {
			return posX * posX + posY * posY + posZ * posZ >= 1.0D;
		}
	}

	@Nullable
	@Override
	public BlockState getCarveState(CarvingContext context, CaveCarverConfiguration config, BlockPos pos, Aquifer aquifer) {
		return Blocks.CAVE_AIR.defaultBlockState();
	}
}
