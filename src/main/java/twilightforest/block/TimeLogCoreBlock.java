package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.TFConfig;
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
		return !TFConfig.COMMON_CONFIG.MAGIC_TREES.disableTime.get();
	}

	/**
	 * The tree of time adds extra ticks to blocks, so that they have twice the normal chance to get a random tick
	 */
	@Override
	@SuppressWarnings("unchecked")
	// Vanilla also makes this dirty cast on block entity tickers, poor mojank design.
	void performTreeEffect(ServerLevel level, BlockPos pos, RandomSource rand) {
		int numticks = 8 * 3 * this.tickRate();

		for (int i = 0; i < numticks; i++) {

			BlockPos dPos = WorldUtil.randomOffset(rand, pos, TFConfig.COMMON_CONFIG.MAGIC_TREES.timeRange.get());

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

				if (worked) {
					Vec3 xyz = Vec3.atCenterOf(dPos);
					for (ServerPlayer serverplayer : level.players()) { // This is just particle math, we send a particle packet to every player in range
						if (serverplayer.distanceToSqr(xyz) < 4096.0D) {
							ParticlePacket particlePacket = new ParticlePacket();
							double yOffset = state.getBlock().getOcclusionShape(state, level, dPos).max(Direction.Axis.Y);
							particlePacket.queueParticle(TFParticleType.LOG_CORE_PARTICLE.get(), false, xyz.add(0.0, yOffset - 0.5, 0.0), new Vec3(0.953, 0.698, 0.0));
							PacketDistributor.PLAYER.with(serverplayer).send(particlePacket);
						}
					}
				}
			}
		}
	}

	@Override
	protected void playSound(Level level, BlockPos pos, RandomSource rand) {
		level.playSound(null, pos, TFSounds.TIME_CORE.get(), SoundSource.BLOCKS, 0.35F, 0.5F);
	}
}
