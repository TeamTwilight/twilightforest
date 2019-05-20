package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.TFParticleType;

import java.util.Random;

public class PacketSpawnEntityParticles implements IMessage {

	private TFParticleType type;
	private double x, y, z;
	private float width, height;
	private int count;

	public PacketSpawnEntityParticles() {}

	public PacketSpawnEntityParticles(TFParticleType type, Entity entity, int count) {
		this.type = type;
		this.x = entity.posX;
		this.y = entity.posY;
		this.z = entity.posZ;
		this.width = entity.width;
		this.height = entity.height;
		this.count = count;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		type = TFParticleType.values()[buf.readInt()];
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		width = buf.readFloat();
		height = buf.readFloat();
		count = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(type.ordinal());
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeFloat(width);
		buf.writeFloat(height);
		buf.writeInt(count);
	}

	public static class Handler implements IMessageHandler<PacketSpawnEntityParticles, IMessage> {

		private final Random random = new Random();

		@Override
		public IMessage onMessage(PacketSpawnEntityParticles message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				for (int i = 0; i < message.count; i++) {
					double x = message.x + random.nextFloat() * message.width * 2.0 - message.width;
					double y = message.y + random.nextFloat() * message.height;
					double z = message.z + random.nextFloat() * message.width * 2.0 - message.width;
					TwilightForestMod.proxy.spawnParticle(message.type, x, y, z, 0.0, 0.0, 0.0);
				}
			});
			return null;
		}
	}
}
