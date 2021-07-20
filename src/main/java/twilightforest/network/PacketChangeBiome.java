package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketChangeBiome {
	private final BlockPos pos;
	private final ResourceLocation biomeId;

	public PacketChangeBiome(BlockPos pos, ResourceLocation id) {
		this.pos = pos;
		this.biomeId = id;
	}

	public PacketChangeBiome(PacketBuffer buf) {
		pos = new BlockPos(buf.readInt(), 0, buf.readInt());
		biomeId = buf.readResourceLocation();
	}

	public void encode(PacketBuffer buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getZ());
		buf.writeResourceLocation(biomeId);
	}

	public static class Handler {

		public static boolean onMessage(PacketChangeBiome message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(new Runnable() {
				@Override
				public void run() {
					final int WIDTH_BITS = (int) Math.round(Math.log(16.0D) / Math.log(2.0D)) - 2;
					final int HEIGHT_BITS = (int) Math.round(Math.log(256.0D) / Math.log(2.0D)) - 2;
					final int HORIZONTAL_MASK = (1 << WIDTH_BITS) - 1;
					final int VERTICAL_MASK = (1 << HEIGHT_BITS) - 1;

					ClientWorld world = Minecraft.getInstance().world;
					Chunk chunkAt = (Chunk) world.getChunk(message.pos);

					Biome targetBiome = world.func_241828_r().getRegistry(Registry.BIOME_KEY).getOrDefault(message.biomeId);

					for (int dy = 0; dy < 255; dy += 4) {
						int x = (message.pos.getX() >> 2) & HORIZONTAL_MASK;
						int y = MathHelper.clamp(dy >> 2, 0, VERTICAL_MASK);
						int z = (message.pos.getZ() >> 2) & HORIZONTAL_MASK;
						chunkAt.getBiomes().biomes[y << WIDTH_BITS + WIDTH_BITS | z << WIDTH_BITS | x] = targetBiome;
					}

					world.onChunkLoaded(message.pos.getX() >> 4, message.pos.getZ() >> 4);
					for (int k = 0; k < 16; ++k)
						world.markSurroundingsForRerender(message.pos.getX() >> 4, k, message.pos.getZ() >> 4);

				}
			});

			return true;
		}
	}
}
