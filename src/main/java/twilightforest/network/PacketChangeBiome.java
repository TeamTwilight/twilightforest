package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketChangeBiome {
	private final BlockPos pos;
	private final int biomeId;

	public PacketChangeBiome(BlockPos pos, Biome biome) {
		this.pos = pos;
		this.biomeId = WorldGenRegistries.BIOME.getId(biome);
	}

	public PacketChangeBiome(PacketBuffer buf) {
		pos = new BlockPos(buf.readInt(), 0, buf.readInt());
		biomeId = buf.readVarInt();
	}

	public void encode(PacketBuffer buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getZ());
		buf.writeVarInt(biomeId);
	}

	public static class Handler {

		public static boolean onMessage(PacketChangeBiome message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(new Runnable() {
				@Override
				public void run() {
					World world = Minecraft.getInstance().world;
					Chunk chunkAt = (Chunk) world.getChunk(message.pos);

//					chunkAt.getBiomeArray()[(message.pos.getZ() & 15) << 4 | (message.pos.getX() & 15)] = message.biomeId;

//					world.markBlockRangeForRenderUpdate(message.pos, message.pos.up(255));
				}
			});

			return true;
		}
	}
}
