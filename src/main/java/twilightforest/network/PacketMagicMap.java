package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import twilightforest.TFMagicMapData;
import twilightforest.item.ItemTFMagicMap;

import java.io.IOException;

// Rewraps vanilla SPacketMaps to properly expose our custom decorations
public class PacketMagicMap implements IMessage {
	private int mapID;
	private byte[] featureData;
	private SPacketMaps inner;

	public PacketMagicMap() {
	}

	public PacketMagicMap(int mapID, TFMagicMapData mapData, SPacketMaps inner) {
		this.mapID = mapID;
		this.featureData = mapData.serializeFeatures();
		this.inner = inner;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer tmp = new PacketBuffer(buf);
		mapID = ByteBufUtils.readVarInt(buf, 5);
		featureData = tmp.readByteArray();

		inner = new SPacketMaps();
		try {
			inner.readPacketData(tmp);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't read inner SPacketMaps", e);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer tmp = new PacketBuffer(buf);
		ByteBufUtils.writeVarInt(buf, mapID, 5);
		tmp.writeByteArray(featureData);

		try {
			inner.writePacketData(tmp);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't write inner SPacketMaps", e);
		}
	}

	public static class Handler implements IMessageHandler<PacketMagicMap, IMessage> {
		@Override
		public IMessage onMessage(PacketMagicMap message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					TFMagicMapData mapData = ItemTFMagicMap.loadMapData(message.mapID, Minecraft.getMinecraft().world);
					message.inner.setMapdataTo(mapData);

					// Deserialize to tfDecorations
					mapData.deserializeFeatures(message.featureData);

					// Cheat and put tfDecorations into main collection so they are called by renderer
					for (TFMagicMapData.TFMapDecoration tfDecor : mapData.tfDecorations) {
						mapData.mapDecorations.put(tfDecor.toString(), tfDecor);
					}

					Minecraft.getMinecraft().entityRenderer.getMapItemRenderer().updateMapTexture(mapData);
				}
			});

			return null;
		}
	}

}
