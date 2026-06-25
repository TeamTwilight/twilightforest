package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFParticleType;

import java.util.Random;

public record SpawnFallenLeafFromPacket(BlockPos pos, Vec3 motion) implements CustomPacketPayload {

	public static final Type<SpawnFallenLeafFromPacket> TYPE = new Type<>(TwilightForestMod.prefix("spawn_fallen_leaf"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SpawnFallenLeafFromPacket> STREAM_CODEC = CustomPacketPayload.codec(SpawnFallenLeafFromPacket::write, SpawnFallenLeafFromPacket::new);

	public SpawnFallenLeafFromPacket(RegistryFriendlyByteBuf buf) {
		this(buf.readBlockPos(), new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()));
	}

	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeBlockPos(this.pos());
		buf.writeDouble(this.motion().x());
		buf.writeDouble(this.motion().y());
		buf.writeDouble(this.motion().z());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(SpawnFallenLeafFromPacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			Level level = ctx.player().level();
			Random rand = new Random();
			int color = -1;
			// TODO: 26.1.2 - BlockTintSource API removed, use BlockColors.getColor instead
			int r = Mth.clamp(((color >> 16) & 0xFF) + rand.nextInt(0x22) - 0x11, 0x00, 0xFF);
			int g = Mth.clamp(((color >> 8) & 0xFF) + rand.nextInt(0x22) - 0x11, 0x00, 0xFF);
			int b = Mth.clamp((color & 0xFF) + rand.nextInt(0x22) - 0x11, 0x00, 0xFF);
			level.addParticle(ColorParticleOption.create(TFParticleType.FALLEN_LEAF.get(), r, g, b),
				message.pos().getX() + level.getRandom().nextFloat(),
				message.pos().getY(),
				message.pos().getZ() + level.getRandom().nextFloat(),
				(level.getRandom().nextFloat() * -0.5F) * message.motion().x(),
				level.getRandom().nextFloat() * 0.5F + 0.25F,
				(level.getRandom().nextFloat() * -0.5F) * message.motion().z()
			);
		});
	}
}
