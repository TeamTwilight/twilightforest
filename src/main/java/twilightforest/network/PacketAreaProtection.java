package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
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

	public PacketAreaProtection() {}

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

					World world = Minecraft.getMinecraft().world;
					addProtectionBox(world, message.sbb);

					for (int i = 0; i < 20; i++) {

						double vx = world.rand.nextGaussian() * 0.02D;
						double vy = world.rand.nextGaussian() * 0.02D;
						double vz = world.rand.nextGaussian() * 0.02D;

						double x = message.pos.getX() + 0.5D + world.rand.nextFloat() - world.rand.nextFloat();
						double y = message.pos.getY() + 0.5D + world.rand.nextFloat() - world.rand.nextFloat();
						double z = message.pos.getZ() + 0.5D + world.rand.nextFloat() - world.rand.nextFloat();

						TwilightForestMod.proxy.spawnParticle(TFParticleType.PROTECTION, x, y, z, vx, vy, vz);
					}
				}
			});
			return null;
		}

		static void addProtectionBox(World world, StructureBoundingBox sbb) {

			for (Entity entity : world.weatherEffects) {
				if (entity instanceof EntityTFProtectionBox) {
					EntityTFProtectionBox protectionBox = (EntityTFProtectionBox) entity;
					if (protectionBox.matches(sbb)) {
						protectionBox.resetLifetime();
						return;
					}
				}
			}

			world.addWeatherEffect(new EntityTFProtectionBox(world, sbb));
		}
	}
}
