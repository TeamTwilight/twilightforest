package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;
import twilightforest.network.ParticlePacket;
import twilightforest.util.WorldUtil;

public class TimeLogCoreBlock extends SpecialMagicLogBlock {

	public TimeLogCoreBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean doesCoreFunction() {
		return !TFConfig.disableTimeCore;
	}

	/**
	 * The tree of time adds extra ticks to blocks, so that they have twice the normal chance to get a random tick
	 */
	@Override
	@SuppressWarnings("unchecked")
	// Vanilla also makes this dirty cast on block entity tickers, poor mojank design.
	void performTreeEffect(ServerLevel level, BlockPos pos, RandomSource rand) {
		int numticks = 8 * 3 * this.tickRate();
		// One packet per run instead of one per affected block (that was up to ~480 packets/s per core).
		ParticlePacket particlePacket = new ParticlePacket();
		int queuedParticles = 0;

		for (int i = 0; i < numticks; i++) {

			BlockPos dPos = WorldUtil.randomOffset(rand, pos, TFConfig.timeCoreRange);
			if (!level.isLoaded(dPos))  // don't load chunks (or tick blocks in unloaded ones) at large configured ranges
				continue;

			BlockState state = level.getBlockState(dPos);

			if (!state.is(BlockTagGenerator.TIME_CORE_EXCLUDED)) {
				boolean worked = false;

				if (state.isRandomlyTicking()) {
					state.randomTick(level, dPos, rand);
					worked = true;
				}

				BlockEntity entity = level.getBlockEntity(dPos);
				if (entity != null) {
					BlockEntityTicker<BlockEntity> ticker = state.getTicker(level, (BlockEntityType<BlockEntity>) entity.getType());
					if (ticker != null) {
						ticker.tick(level, dPos, state, entity);
						worked = true;
					}
				}

				if (worked && queuedParticles < MAX_PARTICLES_PER_RUN) {
					Vec3 xyz = Vec3.atCenterOf(dPos);
					double yOffset = state.getOcclusionShape(level, dPos).max(Direction.Axis.Y);
					particlePacket.queueParticle(TFParticleType.LOG_CORE_PARTICLE.get(), false, xyz.add(0.0, yOffset - 0.5, 0.0), new Vec3(0.953, 0.698, 0.0));
					queuedParticles++;
				}
			}
		}

		if (queuedParticles > 0) {
			Vec3 center = Vec3.atCenterOf(pos);
			// Previously each particle went to players within 64 of that block; widen by the core's range to cover the same players.
			PacketDistributor.sendToPlayersNear(level, null, center.x(), center.y(), center.z(), 64.0D + TFConfig.timeCoreRange, particlePacket);
		}
	}

	private static final int MAX_PARTICLES_PER_RUN = 128;

	@Override
	protected void playSound(Level level, BlockPos pos, RandomSource rand) {
		level.playSound(null, pos, TFSounds.TIME_CORE.get(), SoundSource.BLOCKS, 0.35F, 0.5F);
	}
}
