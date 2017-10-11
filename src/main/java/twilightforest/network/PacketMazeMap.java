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
public class PacketMazeMap implements IMessage {
	private SPacketMaps inner;

	public PacketMazeMap() {
	}

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
					MapData mapData = ItemTFMazeMap.loadMapData(message.inner.getMapId(), Minecraft.getMinecraft().world);
					message.inner.setMapdataTo(mapData);
					Minecraft.getMinecraft().entityRenderer.getMapItemRenderer().updateMapTexture(mapData);
				}
			});
			return null;
		}
	}
}
