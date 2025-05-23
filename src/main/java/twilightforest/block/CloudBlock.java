package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.tuple.Pair;
import twilightforest.config.TFConfig;
import twilightforest.init.TFParticleType;
import twilightforest.network.ParticlePacket;

import javax.annotation.Nullable;

public class CloudBlock extends Block {
	@Nullable
	protected final Biome.Precipitation precipitation;

	public CloudBlock(@Nullable Biome.Precipitation precipitation, Properties properties) {
		super(properties);
		this.precipitation = precipitation;
	}

	public static boolean shouldSnow(LevelReader level, BlockPos pos) {
		if (!level.isOutsideBuildHeight(pos) && level.getBrightness(LightLayer.BLOCK, pos) < 10) {
			BlockState blockstate = level.getBlockState(pos);
			return (blockstate.isAir() || blockstate.is(Blocks.SNOW)) && Blocks.SNOW.defaultBlockState().canSurvive(level, pos);
		}
		return false;
	}

	public static void addEntityMovementParticles(Level level, BlockPos pos, Entity entity, boolean jumping) {
		if (level.getRandom().nextBoolean()) return;
		Vec3 deltaMovement = entity.getDeltaMovement();
		BlockPos blockpos1 = entity.blockPosition();
		double jumpMultiplier = jumping ? 2.0D : 1.0D;

		double x = entity.getX() + (level.getRandom().nextDouble() - 0.5D) * (double) entity.dimensions.width() * jumpMultiplier;
		double y = entity.getY() + 0.1D;
		double z = entity.getZ() + (level.getRandom().nextDouble() - 0.5D) * (double) entity.dimensions.width() * jumpMultiplier;

		if (blockpos1.getX() != pos.getX()) x = Mth.clamp(x, pos.getX(), (double) pos.getX() + 1.0D);
		if (blockpos1.getZ() != pos.getZ()) z = Mth.clamp(z, pos.getZ(), (double) pos.getZ() + 1.0D);

		level.addParticle(TFParticleType.CLOUD_PUFF.get(), x, y, z, deltaMovement.x * -0.5D, 0.015D * jumpMultiplier, deltaMovement.z * -0.5D);
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		entity.causeFallDamage(fallDistance, 0.1F, level.damageSources().fall());
	}

	@Override
	public int getLightBlock(BlockState state) {
		return 1;
	}

	/**
	 * Returns one of the 3 options from the enum:
	 * NONE: If the cloud has no precipitation,
	 * RAIN: If the cloud always rains and
	 * SNOW: If the cloud always snows.
	 * Additionally, if the method returns null, the precipitation is instead dynamic, equal to the current weather at the block's position.
	 */
	@Nullable
	public Biome.Precipitation getPrecipitation() {
		return this.precipitation;
	}

	/**
	 * This method is used to get the appropriate precipitation and rain level depending on the circumstances.
	 * If the block has a non-null precipitation, it will return it, along with a rain level of 1.0F,
	 * otherwise, it will return the level's current weather at that position, along with the current rain level.
	 */
	public Pair<Biome.Precipitation, Float> getCurrentPrecipitation(BlockPos pos, Level level, float rainLevel) {
		if (this.getPrecipitation() == null) {
			if (rainLevel > 0.0F) return Pair.of(level.getBiome(pos).value().getPrecipitationAt(pos, level.getSeaLevel()), rainLevel);
			else return Pair.of(Biome.Precipitation.NONE, 0.0F);
		} else return Pair.of(this.getPrecipitation(), 1.0F);
	}

	/**
	 * Simulate weather the way it's done in the ServerLevel class, but only for the block below our cloud
	 */
	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!level.isAreaLoaded(pos, 1) || TFConfig.commonCloudBlockPrecipitationDistance == 0) return;

		Pair<Biome.Precipitation, Float> pair = this.getCurrentPrecipitation(pos, level, level.getRainLevel(1.0F));
		if (pair.getRight() > 0.0F) {
			Biome.Precipitation precipitation = pair.getLeft();
			if (precipitation == Biome.Precipitation.RAIN || precipitation == Biome.Precipitation.SNOW) {
				int highestRainyBlock = pos.getY() - 1;
				for (int y = pos.getY() - 1; y > pos.getY() - TFConfig.commonCloudBlockPrecipitationDistance; y--) {
					if (!Heightmap.Types.MOTION_BLOCKING.isOpaque().test(level.getBlockState(pos.atY(y)))) highestRainyBlock = y - 1;
					else break;
				}
				if (highestRainyBlock > level.getMinY()) {
					if (precipitation == Biome.Precipitation.SNOW) {
						int snowHeight = level.getGameRules().getInt(GameRules.RULE_SNOW_ACCUMULATION_HEIGHT);
						BlockPos snowOnPos = pos.atY(highestRainyBlock + 1); // We check the position above our last block
						if (snowHeight > 0 && CloudBlock.shouldSnow(level, snowOnPos)) {
							BlockState snowOnState = level.getBlockState(snowOnPos);
							if (snowOnState.is(Blocks.SNOW)) {
								int k = snowOnState.getValue(SnowLayerBlock.LAYERS);
								if (k < Math.min(snowHeight, 8)) {
									BlockState snowLayerState = snowOnState.setValue(SnowLayerBlock.LAYERS, k + 1);
									Block.pushEntitiesUp(snowOnState, snowLayerState, level, snowOnPos);
									level.setBlockAndUpdate(snowOnPos, snowLayerState);
								}
							} else level.setBlockAndUpdate(snowOnPos, Blocks.SNOW.defaultBlockState());
						}
					}

					BlockPos rainOnPos = pos.atY(highestRainyBlock);
					BlockState rainOnState = level.getBlockState(rainOnPos);
					rainOnState.getBlock().handlePrecipitation(rainOnState, level, rainOnPos, precipitation);
				}
			}
		}
	}

	@Override
	public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2, LivingEntity living, int numberOfParticles) { // ServerSide
		ParticlePacket particlePacket = new ParticlePacket();
		int maxI = Mth.clamp((int) living.fallDistance * 2, 8, 40);

		double bbWidth = living.getBbWidth();

		double y = living.getY() + 0.1D;
		double ySpeed = 0.0005D * maxI;

		for (int i = 0; i < maxI; i++) {
			double xSpd = (living.getRandom().nextDouble() - 0.5D) * bbWidth * 2.5D;
			double zSpd = (living.getRandom().nextDouble() - 0.5D) * bbWidth * 2.5D;

			double x = living.getX() + xSpd;
			double z = living.getZ() + zSpd;

			double xSpeed = xSpd * 0.0035D * maxI;
			double zSpeed = zSpd * 0.0035D * maxI;

			particlePacket.queueParticle(TFParticleType.CLOUD_PUFF.get(), x, y, z, xSpeed, ySpeed, zSpeed);
		}

		PacketDistributor.sendToPlayersTrackingChunk(level, new ChunkPos(pos), particlePacket);

		return true;
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (this.canSpawnCloudParticles(entity, level.getRandom())) {
			addEntityMovementParticles(level, pos, entity, false);
		}
	}

	public boolean canSpawnCloudParticles(Entity entity, RandomSource random) {
		if (entity.getDeltaMovement().x() == 0.0D && entity.getDeltaMovement().z() == 0.0D && random.nextInt(20) != 0)
			return false;
		return entity.tickCount % 2 == 0 && !entity.isSpectator();
	}

	@Override
	public boolean addRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) { // Client & Server Side
		if (level.isClientSide() && state.getRenderShape() != RenderShape.INVISIBLE) {
			addEntityMovementParticles(level, pos, entity, false);
		}
		return true;
	}
}
