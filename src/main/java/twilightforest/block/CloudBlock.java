package twilightforest.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.tuple.Pair;
import twilightforest.TFConfig;
import twilightforest.init.TFParticleType;
import twilightforest.network.ParticlePacket;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class CloudBlock extends Block {
    @Nullable
    protected final Biome.Precipitation precipitation;

    public CloudBlock(Properties properties, @Nullable Biome.Precipitation precipitation) {
        super(properties);
        this.precipitation = precipitation;
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.causeFallDamage(fallDistance, 0.1F, level.damageSources().fall());
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
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
            if (rainLevel > 0.0F) return Pair.of(level.getBiome(pos).value().getPrecipitationAt(pos), rainLevel);
            else return Pair.of(Biome.Precipitation.NONE, 0.0F);
        } else return Pair.of(this.getPrecipitation(), 1.0F);
    }

    /**
     * Simulate weather the way it's done in the ServerLevel class, but only for the block below our cloud
     */
    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1) || TFConfig.COMMON_CONFIG.cloudBlockPrecipitationDistanceCommon.get() == 0) return;

        Pair<Biome.Precipitation, Float> pair = this.getCurrentPrecipitation(pos, level, level.getRainLevel(1.0F));
        if (pair.getRight() > 0.0F) {
            Biome.Precipitation precipitation = pair.getLeft();
            if (precipitation == Biome.Precipitation.RAIN || precipitation == Biome.Precipitation.SNOW) {
                int highestRainyBlock = pos.getY() - 1;
                for (int y = pos.getY() - 1; y > pos.getY() - TFConfig.COMMON_CONFIG.cloudBlockPrecipitationDistanceCommon.get(); y--) {
                    if (!Heightmap.Types.MOTION_BLOCKING.isOpaque().test(level.getBlockState(pos.atY(y)))) highestRainyBlock = y - 1;
                    else break;
                }
                if (highestRainyBlock > level.getMinBuildHeight()) {
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

    public static boolean shouldSnow(LevelReader level, BlockPos pos) {
        if (pos.getY() >= level.getMinBuildHeight() && pos.getY() < level.getMaxBuildHeight() && level.getBrightness(LightLayer.BLOCK, pos) < 10) {
            BlockState blockstate = level.getBlockState(pos);
            return (blockstate.isAir() || blockstate.is(Blocks.SNOW)) && Blocks.SNOW.defaultBlockState().canSurvive(level, pos);
        }
        return false;
    }

    @Override
    public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2, LivingEntity living, int numberOfParticles) { // ServerSide
        ParticlePacket particlePacket = new ParticlePacket();
        int maxI = Mth.clamp((int)living.fallDistance * 2, 8, 40);

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

            particlePacket.queueParticle(TFParticleType.CLOUD_PUFF.get(),  false, x, y, z, xSpeed, ySpeed, zSpeed);
        }

        PacketDistributor.TRACKING_CHUNK.with(level.getChunkAt(pos)).send(particlePacket);

        return true;
    }

    @Override
    public boolean addRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) { // Client & Server Side
        if (level.isClientSide() && state.getRenderShape() != RenderShape.INVISIBLE) {
            CloudBlock.addEntityMovementParticles(level, pos, entity, false);
        }
        return true;
    }

    public static void addEntityMovementParticles(Level level, BlockPos pos, Entity entity, boolean jumping) {
        if (level.random.nextBoolean()) return;
        Vec3 deltaMovement = entity.getDeltaMovement();
        BlockPos blockpos1 = entity.blockPosition();
        double jumpMultiplier = jumping ? 2.0D : 1.0D;

        double x = entity.getX() + (level.random.nextDouble() - 0.5D) * (double) entity.dimensions.width * jumpMultiplier;
        double y = entity.getY() + 0.1D;
        double z = entity.getZ() + (level.random.nextDouble() - 0.5D) * (double) entity.dimensions.width * jumpMultiplier;

        if (blockpos1.getX() != pos.getX()) x = Mth.clamp(x, pos.getX(), (double) pos.getX() + 1.0D);
        if (blockpos1.getZ() != pos.getZ()) z = Mth.clamp(z, pos.getZ(), (double) pos.getZ() + 1.0D);

        level.addParticle(TFParticleType.CLOUD_PUFF.get(), x, y, z, deltaMovement.x * -0.5D, 0.015D * jumpMultiplier, deltaMovement.z * -0.5D);
    }

    @Override
    public void initializeClient(Consumer<IClientBlockExtensions> consumer) { // I'm sorry fabric devs
        consumer.accept(new IClientBlockExtensions() {
            @Override
            public boolean addHitEffects(BlockState state, Level level, HitResult target, ParticleEngine manager) {
                if (level.random.nextBoolean() && target instanceof BlockHitResult hitResult) { // No clue why the parameter isn't blockHitResult, this should be always true, but we check just in case
                    BlockPos pos = hitResult.getBlockPos();
                    BlockState blockstate = level.getBlockState(pos);
                    if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                        Direction side = hitResult.getDirection();

                        int posX = pos.getX();
                        int posY = pos.getY();
                        int posZ = pos.getZ();

                        AABB aabb = blockstate.getShape(level, pos).bounds();
                        double x = (double) posX + level.random.nextDouble() * (aabb.maxX - aabb.minX - (double) 0.2F) + (double) 0.1F + aabb.minX;
                        double y = (double) posY + level.random.nextDouble() * (aabb.maxY - aabb.minY - (double) 0.2F) + (double) 0.1F + aabb.minY;
                        double z = (double) posZ + level.random.nextDouble() * (aabb.maxZ - aabb.minZ - (double) 0.2F) + (double) 0.1F + aabb.minZ;

                        if (side == Direction.DOWN) y = (double) posY + aabb.minY - (double) 0.1F;
                        if (side == Direction.UP) y = (double) posY + aabb.maxY + (double) 0.1F;

                        if (side == Direction.NORTH) z = (double) posZ + aabb.minZ - (double) 0.1F;
                        if (side == Direction.SOUTH) z = (double) posZ + aabb.maxZ + (double) 0.1F;

                        if (side == Direction.WEST) x = (double) posX + aabb.minX - (double) 0.1F;
                        if (side == Direction.EAST) x = (double) posX + aabb.maxX + (double) 0.1F;

                        Particle particle = Minecraft.getInstance().particleEngine.createParticle(TFParticleType.CLOUD_PUFF.get(), x, y, z, (double) side.getStepX() * 0.01D, (double) side.getStepY() * 0.01D, (double) side.getStepZ() * 0.01D);
                        if (particle == null) return true;
                        manager.add(particle);
                    }
                }
                return true;
            }

            @Override
            public boolean addDestroyEffects(BlockState state, Level level, BlockPos pos, ParticleEngine manager) {
                state.getShape(level, pos).forAllBoxes((boxX, boxY, boxZ, boxX1, boxY1, boxZ1) -> {
                    double xSize = Math.min(1.0D, boxX1 - boxX);
                    double ySize = Math.min(1.0D, boxY1 - boxY);
                    double zSize = Math.min(1.0D, boxZ1 - boxZ);
                    
                    int xMax = Math.max(2, Mth.ceil(xSize / 0.25D));
                    int yMax = Math.max(2, Mth.ceil(ySize / 0.25D));
                    int zMax = Math.max(2, Mth.ceil(zSize / 0.25D));

                    for(int xSlice = 0; xSlice < xMax; ++xSlice) {
                        if (level.random.nextInt(3) == 1) continue;
                        for(int ySlice = 0; ySlice < yMax; ++ySlice) {
                            if (level.random.nextInt(3) == 1) continue;
                            for(int zSlice = 0; zSlice < zMax; ++zSlice) {
                                if (level.random.nextInt(3) == 1) continue;
                                
                                double speedX = ((double) xSlice + 0.5D) / (double) xMax;
                                double speedY = ((double) ySlice + 0.5D) / (double) yMax;
                                double speedZ = ((double) zSlice + 0.5D) / (double) zMax;

                                double x = speedX * xSize + boxX;
                                double y = speedY * ySize + boxY;
                                double z = speedZ * zSize + boxZ;

                                speedX = (speedX - 0.5D) * 0.05D;
                                speedY = (speedY - 0.5D) * 0.05D;
                                speedZ = (speedZ - 0.5D) * 0.05D;
                                
                                Particle particle = Minecraft.getInstance().particleEngine.createParticle(TFParticleType.CLOUD_PUFF.get(), (double) pos.getX() + x, (double) pos.getY() + y, (double) pos.getZ() + z, speedX, speedY, speedZ);
                                if (particle == null) return;
                                manager.add(particle);
                            }
                        }
                    }
                });
                return true;
            }
        });
    }
}
