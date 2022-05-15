package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChangeBiomePacket {
	private final BlockPos pos;
	private final ResourceKey<Biome> biomeId;

	public ChangeBiomePacket(BlockPos pos, ResourceKey<Biome> id) {
		this.pos = pos;
		this.biomeId = id;
	}

	public ChangeBiomePacket(FriendlyByteBuf buf) {
		pos = new BlockPos(buf.readInt(), 0, buf.readInt());
		biomeId = ResourceKey.create(Registry.BIOME_REGISTRY, buf.readResourceLocation());
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getZ());
		buf.writeResourceLocation(biomeId.location());
	}

	public static class Handler {

		public static boolean onMessage(ChangeBiomePacket message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(new Runnable() {
				@Override
				public void run() {
					ClientLevel world = Minecraft.getInstance().level;
					LevelChunk chunkAt = (LevelChunk) world.getChunk(message.pos);

					Holder<Biome> biome = world.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY).getHolderOrThrow(message.biomeId);

					int minY = QuartPos.fromBlock(world.getMinBuildHeight());
					int maxY = minY + QuartPos.fromBlock(world.getHeight()) - 1;

					int x = QuartPos.fromBlock(message.pos.getX());
					int z = QuartPos.fromBlock(message.pos.getZ());


					for (LevelChunkSection section : chunkAt.getSections()) {
						for (int dy = minY; dy < maxY; dy++) { // TODO: This probably isn't correct and isn't good for performance.
							int y = Mth.clamp(QuartPos.fromBlock(dy), minY, maxY);
							section.getBiomes().set(x & 3, y & 3, z & 3, biome);
						}
					}
					world.onChunkLoaded(new ChunkPos(message.pos));
				}
			});

			return true;
		}
	}
}
