package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.world.WorldProviderTwilightForest;

public class PacketStructureProtection implements IMessage {
	private StructureBoundingBox sbb;

	public PacketStructureProtection() {
	}

	public PacketStructureProtection(StructureBoundingBox sbb) {
		this.sbb = sbb;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		sbb = new StructureBoundingBox(
				buf.readInt(), buf.readInt(), buf.readInt(),
				buf.readInt(), buf.readInt(), buf.readInt()
		);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(sbb.minX);
		buf.writeInt(sbb.minY);
		buf.writeInt(sbb.minZ);
		buf.writeInt(sbb.maxX);
		buf.writeInt(sbb.maxY);
		buf.writeInt(sbb.maxZ);
	}

	public static class Handler implements IMessageHandler<PacketStructureProtection, IMessage> {

		@Override
		public IMessage onMessage(PacketStructureProtection message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					World world = Minecraft.getMinecraft().world;

					// add weather box if needed
					if (world.provider instanceof WorldProviderTwilightForest) {
						TFWeatherRenderer weatherRenderer = (TFWeatherRenderer) world.provider.getWeatherRenderer();

						weatherRenderer.setProtectedBox(message.sbb);
					}
				}
			});

			return null;
		}
	}
}
