package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import twilightforest.TFMazeMapData;
import twilightforest.item.ItemTFMazeMap;

import java.io.IOException;

/**
 * Vanilla's SPacketMaps handler looks for and loads the vanilla MapData instances.
 * We rewrap the packet here in order to load our own MapData instances properly.
 */
public class PacketMazeMap implements IMessage {

	private SPacketMaps inner;

	public PacketMazeMap() {}

	public PacketMazeMap(SPacketMaps inner) {
		this.inner = inner;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		inner = new SPacketMaps();
		try {
			inner.readPacketData(new PacketBuffer(buf));
		} catch (IOException e) {
			throw new RuntimeException("Couldn't read inner SPacketMaps", e);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		try {
			inner.writePacketData(new PacketBuffer(buf));
		} catch (IOException e) {
			throw new RuntimeException("Couldn't write inner SPacketMaps", e);
		}
	}

	public static class Handler implements IMessageHandler<PacketMazeMap, IMessage> {
		@Override
		public IMessage onMessage(PacketMazeMap message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {

					MapItemRenderer mapItemRenderer = Minecraft.getMinecraft().entityRenderer.getMapItemRenderer();
					MapData mapData = ItemTFMazeMap.loadMapData(message.inner.getMapId(), Minecraft.getMinecraft().world);

					// Adapted from NetHandlerPlayClient#handleMaps
					if (mapData == null) {

						String s = ItemTFMazeMap.STR_ID + "_" + message.inner.getMapId();
						mapData = new TFMazeMapData(s);

						if (mapItemRenderer.getMapInstanceIfExists(s) != null) {
							MapData mapData1 = mapItemRenderer.getData(mapItemRenderer.getMapInstanceIfExists(s));
							if (mapData1 != null) {
								mapData = mapData1;
							}
						}

						Minecraft.getMinecraft().world.setData(s, mapData);
					}

					message.inner.setMapdataTo(mapData);
					mapItemRenderer.updateMapTexture(mapData);
				}
			});
			return null;
		}
	}
}
