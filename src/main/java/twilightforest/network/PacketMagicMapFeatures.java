package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import twilightforest.TFMagicMapData;
import twilightforest.item.ItemTFMagicMap;

public class PacketMagicMapFeatures implements IMessage {
	private int mapID;
	private byte[] featureData;

	public PacketMagicMapFeatures() {
	}

	public PacketMagicMapFeatures(int mapID, byte[] featureData) {
		this.mapID = mapID;
		this.featureData = featureData;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		mapID = ByteBufUtils.readVarInt(buf, 5);
		int len = ByteBufUtils.readVarInt(buf, 5);
		featureData = new byte[len];
		buf.readBytes(featureData);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, mapID, 5);
		ByteBufUtils.writeVarInt(buf, featureData.length, 5);
		buf.writeBytes(featureData);
	}

	public static class Handler implements IMessageHandler<PacketMagicMapFeatures, IMessage> {
		@Override
		public IMessage onMessage(PacketMagicMapFeatures message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					TFMagicMapData mapData = ItemTFMagicMap.loadMapData(message.mapID, Minecraft.getMinecraft().world);
					mapData.deserializeFeatures(message.featureData);
				}
			});

			return null;
		}
	}

}
