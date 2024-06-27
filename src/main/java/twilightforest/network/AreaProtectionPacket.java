package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ProtectionBox;
import twilightforest.init.TFParticleType;

import java.util.ArrayList;
import java.util.List;

public class AreaProtectionPacket implements CustomPacketPayload {

	public static final Type<AreaProtectionPacket> TYPE = new Type<>(TwilightForestMod.prefix("add_protection_box"));
	public static final StreamCodec<RegistryFriendlyByteBuf, AreaProtectionPacket> STREAM_CODEC = CustomPacketPayload.codec(AreaProtectionPacket::write, AreaProtectionPacket::new);

	private final List<BoundingBox> sbb;
	private final BlockPos pos;

	public AreaProtectionPacket(List<BoundingBox> sbb, BlockPos pos) {
		this.sbb = sbb;
		this.pos = pos;
	}

	public AreaProtectionPacket(FriendlyByteBuf buf) {
		this.sbb = new ArrayList<>();
		int len = buf.readInt();
		for (int i = 0; i < len; i++) {
			this.sbb.add(new BoundingBox(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt()));
		}
		this.pos = buf.readBlockPos();
	}

	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeInt(this.sbb.size());
		this.sbb.forEach(box -> {
			buf.writeInt(box.minX());
			buf.writeInt(box.minY());
			buf.writeInt(box.minZ());
			buf.writeInt(box.maxX());
			buf.writeInt(box.maxY());
			buf.writeInt(box.maxZ());
		});
		buf.writeBlockPos(this.pos);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	@SuppressWarnings("Convert2Lambda")
	public static void handle(AreaProtectionPacket message, IPayloadContext ctx) {
		//ensure this is only done on clients as this uses client only code
		if (ctx.flow().isClientbound()) {
			ctx.enqueueWork(new Runnable() {
				@Override
				public void run() {
					ClientLevel level = ctx.player().level() instanceof ClientLevel clientLevel ? clientLevel : Minecraft.getInstance().level;
					message.sbb.forEach(box -> {
						for (Entity entity : level.entitiesForRendering()) {
							if (entity instanceof ProtectionBox prot) {
								if (prot.lifeTime > 0 && prot.matches(box)) {
									prot.resetLifetime();
									return;
								}
							}
						}

						level.addEntity(new ProtectionBox(level, box));
					});

					for (int i = 0; i < 20; i++) {
						double vx = level.getRandom().nextGaussian() * 0.02D;
						double vy = level.getRandom().nextGaussian() * 0.02D;
						double vz = level.getRandom().nextGaussian() * 0.02D;

						double x = message.pos.getX() + 0.5D + level.getRandom().nextFloat() - level.getRandom().nextFloat();
						double y = message.pos.getY() + 0.5D + level.getRandom().nextFloat() - level.getRandom().nextFloat();
						double z = message.pos.getZ() + 0.5D + level.getRandom().nextFloat() - level.getRandom().nextFloat();

						level.addParticle(TFParticleType.PROTECTION.get(), x, y, z, vx, vy, vz);
					}
				}
			});
		}
	}
}
