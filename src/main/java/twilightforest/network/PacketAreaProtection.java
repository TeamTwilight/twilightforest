package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraftforge.fml.network.NetworkEvent;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.EntityTFProtectionBox;

import java.util.function.Supplier;

public class PacketAreaProtection {

	private final MutableBoundingBox sbb;
	private final BlockPos pos;

	public PacketAreaProtection(MutableBoundingBox sbb, BlockPos pos) {
		this.sbb = sbb;
		this.pos = pos;
	}

	public PacketAreaProtection(PacketBuffer buf) {
		sbb = new MutableBoundingBox(
				buf.readInt(), buf.readInt(), buf.readInt(),
				buf.readInt(), buf.readInt(), buf.readInt()
		);
		pos = buf.readBlockPos();
	}

	public void encode(PacketBuffer buf) {
		buf.writeInt(sbb.minX);
		buf.writeInt(sbb.minY);
		buf.writeInt(sbb.minZ);
		buf.writeInt(sbb.maxX);
		buf.writeInt(sbb.maxY);
		buf.writeInt(sbb.maxZ);
		buf.writeLong(pos.toLong());
	}

	public static class Handler {

		public static boolean onMessage(PacketAreaProtection message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(new Runnable() {
				@Override
				public void run() {

					ClientWorld world = Minecraft.getInstance().world;
					addProtectionBox(world, message.sbb);

					for (int i = 0; i < 20; i++) {

						double vx = world.rand.nextGaussian() * 0.02D;
						double vy = world.rand.nextGaussian() * 0.02D;
						double vz = world.rand.nextGaussian() * 0.02D;

						double x = message.pos.getX() + 0.5D + world.rand.nextFloat() - world.rand.nextFloat();
						double y = message.pos.getY() + 0.5D + world.rand.nextFloat() - world.rand.nextFloat();
						double z = message.pos.getZ() + 0.5D + world.rand.nextFloat() - world.rand.nextFloat();

						world.addParticle(TFParticleType.PROTECTION.get(), x, y, z, vx, vy, vz);
					}
				}
			});
			return true;
		}

		static void addProtectionBox(ClientWorld world, MutableBoundingBox sbb) {

			for (Entity entity : world.getAllEntities()) {
				if (entity instanceof EntityTFProtectionBox) {
					EntityTFProtectionBox protectionBox = (EntityTFProtectionBox) entity;
					if (protectionBox.matches(sbb)) {
						protectionBox.resetLifetime();
						return;
					}
				}
			}

			world.addEntity(new EntityTFProtectionBox(world, sbb));
		}
	}
}
