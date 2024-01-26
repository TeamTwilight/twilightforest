package twilightforest.network;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.List;

public class ParticlePacket implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("particle_queue");

	private final List<QueuedParticle> queuedParticles = new ArrayList<>();

	public ParticlePacket() {
	}

	public ParticlePacket(FriendlyByteBuf buf) {
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			ParticleType<?> type = BuiltInRegistries.PARTICLE_TYPE.byId(buf.readInt());
			if (type == null)
				break; // Fail silently and end execution entirely. Due to Type serialization we now have completely unknown data in the pipeline without any way to safely read it all
			this.queuedParticles.add(new QueuedParticle(readParticle(type, buf), buf.readBoolean(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble()));
		}
	}

	private <T extends ParticleOptions> T readParticle(ParticleType<T> particleType, FriendlyByteBuf buf) {
		return particleType.getDeserializer().fromNetwork(particleType, buf);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.queuedParticles.size());
		for (QueuedParticle queuedParticle : this.queuedParticles) {
			int d = BuiltInRegistries.PARTICLE_TYPE.getId(queuedParticle.particleOptions.getType());
			buf.writeInt(d);
			queuedParticle.particleOptions.writeToNetwork(buf);
			buf.writeBoolean(queuedParticle.b);
			buf.writeDouble(queuedParticle.x);
			buf.writeDouble(queuedParticle.y);
			buf.writeDouble(queuedParticle.z);
			buf.writeDouble(queuedParticle.x2);
			buf.writeDouble(queuedParticle.y2);
			buf.writeDouble(queuedParticle.z2);
		}
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public void queueParticle(ParticleOptions particleOptions, boolean b, double x, double y, double z, double x2, double y2, double z2) {
		this.queuedParticles.add(new QueuedParticle(particleOptions, b, x, y, z, x2, y2, z2));
	}

	public void queueParticle(ParticleOptions particleOptions, boolean b, Vec3 xyz, Vec3 xyz2) {
		this.queuedParticles.add(new QueuedParticle(particleOptions, b, xyz.x, xyz.y, xyz.z, xyz2.x, xyz2.y, xyz2.z));
	}

	private record QueuedParticle(ParticleOptions particleOptions, boolean b, double x, double y, double z, double x2,
								  double y2, double z2) {
	}

	public static void handle(ParticlePacket message, PlayPayloadContext ctx) {
		ctx.workHandler().execute(() -> {
			for (QueuedParticle queuedParticle : message.queuedParticles) {
				ctx.level().orElseThrow().addParticle(queuedParticle.particleOptions, queuedParticle.b, queuedParticle.x, queuedParticle.y, queuedParticle.z, queuedParticle.x2, queuedParticle.y2, queuedParticle.z2);
			}
		});
	}
}
