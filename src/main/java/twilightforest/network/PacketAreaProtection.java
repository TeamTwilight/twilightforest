package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.EntityTFProtectionBox;

public class PacketAreaProtection implements IMessage {
	private StructureBoundingBox sbb;
	private BlockPos pos;

	public PacketAreaProtection() {
	}

	public PacketAreaProtection(StructureBoundingBox sbb, BlockPos pos) {
		this.sbb = sbb;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		sbb = new StructureBoundingBox(
				buf.readInt(), buf.readInt(), buf.readInt(),
				buf.readInt(), buf.readInt(), buf.readInt()
		);
		pos = BlockPos.fromLong(buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(sbb.minX);
		buf.writeInt(sbb.minY);
		buf.writeInt(sbb.minZ);
		buf.writeInt(sbb.maxX);
		buf.writeInt(sbb.maxY);
		buf.writeInt(sbb.maxZ);
		buf.writeLong(pos.toLong());
	}

	public static class Handler implements IMessageHandler<PacketAreaProtection, IMessage> {

		@Override
		public IMessage onMessage(PacketAreaProtection message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					// make a box entity
					World world = Minecraft.getMinecraft().world;
					StructureBoundingBox sbb = message.sbb;
					EntityTFProtectionBox box = new EntityTFProtectionBox(world, sbb.minX, sbb.minY, sbb.minZ, sbb.maxX, sbb.maxY, sbb.maxZ);

					world.addWeatherEffect(box);

					for (int i = 0; i < 20; i++) {

						double d0 = world.rand.nextGaussian() * 0.02D;
						double d1 = world.rand.nextGaussian() * 0.02D;
						double d2 = world.rand.nextGaussian() * 0.02D;

						float dx = message.pos.getX() + 0.5F + world.rand.nextFloat() - world.rand.nextFloat();
						float dy = message.pos.getY() + 0.5F + world.rand.nextFloat() - world.rand.nextFloat();
						float dz = message.pos.getZ() + 0.5F + world.rand.nextFloat() - world.rand.nextFloat();

						TwilightForestMod.proxy.spawnParticle(world, TFParticleType.PROTECTION, dx, dy, dz, d0, d1, d2);

					}
				}
			});

			return null;
		}
	}
}
