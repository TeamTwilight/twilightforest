package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.item.FilledMapItem;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SMapDataPacket;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.network.NetworkEvent;
import twilightforest.TFMazeMapData;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * Vanilla's SPacketMaps handler looks for and loads the vanilla MapData instances.
 * We rewrap the packet here in order to load our own MapData instances properly.
 */
public class PacketMazeMap {

	private final SMapDataPacket inner;

	public PacketMazeMap(SMapDataPacket inner) {
		this.inner = inner;
	}

	public PacketMazeMap(PacketBuffer buf) {
		inner = new SMapDataPacket();
		try {
			inner.readPacketData(buf);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't read inner SPacketMaps", e);
		}
	}

	public void encode(PacketBuffer buf) {
		try {
			inner.writePacketData(buf);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't write inner SPacketMaps", e);
		}
	}

	public static class Handler {
		public static boolean onMessage(PacketMazeMap message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(new Runnable() {
				@Override
				public void run() {
					// [VanillaCopy] ClientPlayNetHandler#handleMaps with our own mapdatas
					MapItemRenderer mapitemrenderer = Minecraft.getInstance().gameRenderer.getMapItemRenderer();
					String s = FilledMapItem.getMapName(message.inner.getMapId());
					TFMazeMapData mapdata = TFMazeMapData.getMazeMapData(Minecraft.getInstance().world, s);
					if (mapdata == null) {
						mapdata = new TFMazeMapData(s);
						if (mapitemrenderer.getMapInstanceIfExists(s) != null) {
							MapData mapdata1 = mapitemrenderer.getData(mapitemrenderer.getMapInstanceIfExists(s));
							if (mapdata1 instanceof TFMazeMapData) {
								mapdata = (TFMazeMapData) mapdata1;
							}
						}

						TFMazeMapData.registerMazeMapData(Minecraft.getInstance().world, mapdata);
					}

					message.inner.setMapdataTo(mapdata);
					mapitemrenderer.updateMapTexture(mapdata);
				}
			});
			return true;
		}
	}
}
