package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.network.PacketDistributor;
import twilightforest.TFSounds;
import twilightforest.network.ChangeBiomePacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.util.WorldUtil;
import twilightforest.world.registration.biomes.BiomeKeys;

import java.util.Random;

public class TransLogCoreBlock extends SpecialMagicLogBlock {

	public TransLogCoreBlock(Properties props) {
		super(props);
	}

	/**
	 * The tree of transformation transforms the biome in the area near it into the enchanted forest biome.
	 * TODO: also change entities
	 */
	@Override
	void performTreeEffect(Level world, BlockPos pos, Random rand) {
		ResourceKey<Biome> target = BiomeKeys.ENCHANTED_FOREST;
		Holder<Biome> biome = world.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY).getHolderOrThrow(target);
		for (int i = 0; i < 16; i++) {
			BlockPos dPos = WorldUtil.randomOffset(rand, pos, 16, 0, 16);
			if (dPos.distSqr(pos) > 256.0)
				continue;

			if (world.getBiome(dPos).is(target))
				continue;

			int minY = QuartPos.fromBlock(world.getMinBuildHeight());
			int maxY = minY + QuartPos.fromBlock(world.getHeight()) - 1;

			int x = QuartPos.fromBlock(dPos.getX());
			int z = QuartPos.fromBlock(dPos.getZ());

			LevelChunk chunkAt = world.getChunk(dPos.getX() >> 4, dPos.getZ() >> 4);
			for (LevelChunkSection section : chunkAt.getSections()) {
				for (int dy = minY; dy < maxY; dy++) { // TODO: This probably isn't correct and isn't good for performance.
					int y = Mth.clamp(QuartPos.fromBlock(dy), minY, maxY);
					if (section.getBiomes().get(x & 3, y & 3, z & 3).is(target))
						continue;
					section.getBiomes().set(x & 3, y & 3, z & 3, biome);
				}
			}

			if (world instanceof ServerLevel) {
				sendChangedBiome(chunkAt, dPos, target);
			}
			break;
		}
	}

	/**
	 * Send a tiny update packet to the client to inform it of the changed biome
	 */
	private void sendChangedBiome(LevelChunk chunk, BlockPos pos, ResourceKey<Biome> biome) {
		ChangeBiomePacket message = new ChangeBiomePacket(pos, biome);
		TFPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), message);
	}

	@Override
	protected void playSound(Level level, BlockPos pos, Random rand) {
		level.playSound(null, pos, TFSounds.TRANSFORMATION_CORE, SoundSource.BLOCKS, 0.1F, rand.nextFloat() * 2F);
	}
}
