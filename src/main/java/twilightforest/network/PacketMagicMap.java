package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SMapDataPacket;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.fml.network.NetworkEvent;
import twilightforest.TFMagicMapData;
import twilightforest.item.ItemTFMagicMap;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

// Rewraps vanilla SPacketMaps to properly expose our custom decorations
public class PacketMagicMap {
	private final byte[] featureData;
	private final SMapDataPacket inner;

	public PacketMagicMap(TFMagicMapData mapData, SMapDataPacket inner) {
		this.featureData = mapData.serializeFeatures();
		this.inner = inner;
	}

	public PacketMagicMap(PacketBuffer buf) {
		featureData = buf.readByteArray();

		inner = new SMapDataPacket();
		try {
			inner.readPacketData(buf);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't read inner SPacketMaps", e);
		}
	}

	public void encode(PacketBuffer buf) {
		buf.writeByteArray(featureData);

		try {
			inner.writePacketData(buf);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't write inner SPacketMaps", e);
		}
	}

	public static class Handler {
		public static boolean onMessage(PacketMagicMap message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(new Runnable() {
				@Override
				public void run() {
					// [VanillaCopy] ClientPlayNetHandler#handleMaps with our own mapdatas
					MapItemRenderer mapitemrenderer = Minecraft.getInstance().gameRenderer.getMapItemRenderer();
					String s = ItemTFMagicMap.getMapName(message.inner.getMapId());
					TFMagicMapData mapdata = TFMagicMapData.getMagicMapData(Minecraft.getInstance().world, s);
					if (mapdata == null) {
						mapdata = new TFMagicMapData(s);
						if (mapitemrenderer.getMapInstanceIfExists(s) != null) {
							MapData mapdata1 = mapitemrenderer.getData(mapitemrenderer.getMapInstanceIfExists(s));
							if (mapdata1 instanceof TFMagicMapData) {
								mapdata = (TFMagicMapData) mapdata1;
							}
						}

						TFMagicMapData.registerMagicMapData(Minecraft.getInstance().world, mapdata);
					}

					message.inner.setMapdataTo(mapdata);

					// TF - handle custom decorations
					{
						mapdata.deserializeFeatures(message.featureData);

						// Cheat and put tfDecorations into main collection so they are called by renderer
						// However, ensure they come before vanilla's markers, so player markers go above feature markers.
						Map<String, MapDecoration> saveVanilla = new LinkedHashMap<>(mapdata.mapDecorations);
						mapdata.mapDecorations.clear();

						for (TFMagicMapData.TFMapDecoration tfDecor : mapdata.tfDecorations) {
							mapdata.mapDecorations.put(tfDecor.toString(), tfDecor);
						}

						mapdata.mapDecorations.putAll(saveVanilla);
					}

					mapitemrenderer.updateMapTexture(mapdata);
				}
			});

			return true;
		}
	}

}
