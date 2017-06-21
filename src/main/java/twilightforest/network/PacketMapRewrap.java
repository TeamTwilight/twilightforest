package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import twilightforest.item.ItemTFMagicMap;
import twilightforest.item.ItemTFMazeMap;

import java.io.IOException;

/**
 * Vanilla's SPacketMaps handler looks for and loads the vanilla MapData instances.
 * We rewrap the packet here in order to load our own MapData instances properly.
 */
public class PacketMapRewrap implements IMessage {
	private boolean isMaze;
	private SPacketMaps inner;

	public PacketMapRewrap() {
	}

	public PacketMapRewrap(boolean isMaze, SPacketMaps inner) {
		this.isMaze = isMaze;
		this.inner = inner;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		inner = new SPacketMaps();
		PacketBuffer tmp = new PacketBuffer(buf);
		try {
			inner.readPacketData(tmp);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't read inner SPacketMaps", e);
		}

		isMaze = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer tmp = new PacketBuffer(buf);
		try {
			inner.writePacketData(tmp);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't write inner SPacketMaps", e);
		}

		buf.writeBoolean(isMaze);
	}

	public static class Handler implements IMessageHandler<PacketMapRewrap, IMessage> {
		@Override
		public IMessage onMessage(PacketMapRewrap message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					MapData mapData = message.isMaze
							? ItemTFMazeMap.loadMapData(message.inner.getMapId(), Minecraft.getMinecraft().world)
							: ItemTFMagicMap.loadMapData(message.inner.getMapId(), Minecraft.getMinecraft().world);
					message.inner.setMapdataTo(mapData);
					Minecraft.getMinecraft().entityRenderer.getMapItemRenderer().updateMapTexture(mapData);
				}
			});
			return null;
		}
	}
}
