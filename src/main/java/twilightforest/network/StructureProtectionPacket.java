package twilightforest.network;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.client.DimensionSpecialEffectsManager;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.client.TwilightForestRenderInfo;
import twilightforest.client.renderer.TFWeatherRenderer;

public record StructureProtectionPacket(BoundingBox box) implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("add_protection_renderer");

	public StructureProtectionPacket(FriendlyByteBuf buf) {
		this(new BoundingBox(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt()));
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.box().minX());
		buf.writeInt(this.box().minY());
		buf.writeInt(this.box().minZ());
		buf.writeInt(this.box().maxX());
		buf.writeInt(this.box().maxY());
		buf.writeInt(this.box().maxZ());
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(StructureProtectionPacket message, PlayPayloadContext ctx) {
		ctx.workHandler().execute(() -> {
			DimensionSpecialEffects info = DimensionSpecialEffectsManager.getForType(TwilightForestMod.prefix("renderer"));

			// add weather box if needed
			if (info instanceof TwilightForestRenderInfo) {
				TFWeatherRenderer.setProtectedBox(message.box());
			}
		});
	}
}
