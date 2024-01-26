package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.data.LeafParticleData;

import java.util.Random;

public record SpawnFallenLeafFromPacket(BlockPos pos, Vec3 motion) implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("spawn_fallen_leaf");

	public SpawnFallenLeafFromPacket(FriendlyByteBuf buf) {
		this(buf.readBlockPos(), new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()));
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(this.pos());
		buf.writeDouble(this.motion().x());
		buf.writeDouble(this.motion().y());
		buf.writeDouble(this.motion().z());
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}


	public static void handle(SpawnFallenLeafFromPacket message, PlayPayloadContext ctx) {
		ctx.workHandler().execute(() -> {
			Level level = ctx.level().orElseThrow();
			Random rand = new Random();
			int color = Minecraft.getInstance().getBlockColors().getColor(Blocks.OAK_LEAVES.defaultBlockState(), level, message.pos(), 0);
			int r = Mth.clamp(((color >> 16) & 0xFF) + rand.nextInt(0x22) - 0x11, 0x00, 0xFF);
			int g = Mth.clamp(((color >> 8) & 0xFF) + rand.nextInt(0x22) - 0x11, 0x00, 0xFF);
			int b = Mth.clamp((color & 0xFF) + rand.nextInt(0x22) - 0x11, 0x00, 0xFF);
			level.addParticle(new LeafParticleData(r, g, b),
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
