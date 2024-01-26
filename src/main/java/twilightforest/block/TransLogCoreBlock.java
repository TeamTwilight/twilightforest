package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.TFConfig;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;
import twilightforest.network.ParticlePacket;
import twilightforest.util.WorldUtil;

import java.util.List;

public class TransLogCoreBlock extends SpecialMagicLogBlock {

	public TransLogCoreBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean doesCoreFunction() {
		return !TFConfig.COMMON_CONFIG.MAGIC_TREES.disableTransformation.get();
	}

	/**
	 * The tree of transformation transforms the biome in the area near it into the enchanted forest biome.
	 */
	@Override
	void performTreeEffect(ServerLevel level, BlockPos pos, RandomSource rand) {
		ResourceKey<Biome> target = TFBiomes.ENCHANTED_FOREST;
		Holder<Biome> biome = level.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(target);
		int range = TFConfig.COMMON_CONFIG.MAGIC_TREES.transformationRange.get();
		for (int i = 0; i < 16; i++) {
			BlockPos dPos = WorldUtil.randomOffset(rand, pos, range, 0, range);
			if (dPos.distSqr(pos) > 256.0)
				continue;

			if (level.getBiome(dPos).is(target))
				continue;

			int minY = QuartPos.fromBlock(level.getMinBuildHeight());
			int maxY = minY + QuartPos.fromBlock(level.getHeight()) - 1;

			int x = QuartPos.fromBlock(dPos.getX());
			int z = QuartPos.fromBlock(dPos.getZ());

			LevelChunk chunkAt = level.getChunk(dPos.getX() >> 4, dPos.getZ() >> 4);
			for (LevelChunkSection section : chunkAt.getSections()) {
				for (int sy = 0; sy < 16; sy += 4) {
					int y = Mth.clamp(QuartPos.fromBlock(chunkAt.getMinSection() + sy), minY, maxY);
					if (section.getBiomes().get(x & 3, y & 3, z & 3).is(target))
						continue;
					if (section.getBiomes() instanceof PalettedContainer<Holder<Biome>> container)
						container.set(x & 3, y & 3, z & 3, biome);
				}
			}

			if (!chunkAt.isUnsaved()) chunkAt.setUnsaved(true);
			level.getChunkSource().chunkMap.resendBiomesForChunks(List.of(chunkAt));

			Vec3 xyz = Vec3.atCenterOf(dPos);

			for (ServerPlayer serverplayer : level.players()) { // This is just particle math, we send a particle packet to every player in range
				if (serverplayer.distanceToSqr(xyz) < 4096.0D) {
					ParticlePacket particlePacket = new ParticlePacket();
					for (int j = 0; j < 9; j++) {
						float angle = rand.nextFloat() * 360.0F;
						Vec3 offset = new Vec3(Math.cos(angle), 0.0D, Math.sin(angle)).scale(2.0D);
						particlePacket.queueParticle(TFParticleType.TRANSFORMATION_PARTICLE.get(), false, xyz.add(offset), Vec3.ZERO.subtract(offset));
					}
					PacketDistributor.PLAYER.with(serverplayer).send(particlePacket);
				}
			}
			break;
		}
	}

	@Override
	protected void playSound(Level level, BlockPos pos, RandomSource rand) {
		level.playSound(null, pos, TFSounds.TRANSFORMATION_CORE.get(), SoundSource.BLOCKS, 0.1F, rand.nextFloat() * 2F);
	}
}
