package twilightforest.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.TextureTransform;
import org.joml.Matrix4f;
import twilightforest.TwilightForestMod;
import twilightforest.client.renderer.block.RedThreadRenderer;
import twilightforest.client.renderer.entity.LichRenderer;

//TODO check all of these in-game, im bad at writing these
public class TFRenderTypes {

	public static final RenderType RED_THREAD = RenderType.create("twilightforest:red_thread", RenderSetup.builder(TFRenderPipelines.RED_THREAD)
		.withTexture("Sampler0", RedThreadRenderer.TEXTURE)
		.affectsCrumbling()
		.useLightmap()
		.useOverlay()
		.setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
		.createRenderSetup());

	public static final RenderType PROTECTION_BOX = RenderType.create("twilightforest:protection_box", RenderSetup.builder(TFRenderPipelines.PROTECTION_BOX)
		.withTexture("Sampler0", TwilightForestMod.getModelTexture("protectionbox.png"))
		.setTextureTransform(new ProtectionBoxTextureTransform())
		.useLightmap()
		.useOverlay()
		.sortOnUpload()
		.createRenderSetup());

	public static final RenderType SHADOW_CLONE = RenderType.create("twilightforest:shadow_clone", RenderSetup.builder(TFRenderPipelines.SHADOW_CLONE)
		.withTexture("Sampler0", LichRenderer.TEXTURE)
		.useLightmap()
		.useOverlay()
		.affectsCrumbling()
		.sortOnUpload()
		.setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
		.createRenderSetup());

	public static final class ProtectionBoxTextureTransform extends TextureTransform {
		public ProtectionBoxTextureTransform() {
			super("protection_offset_texturing", () -> {
				float tick = (float) (Minecraft.getInstance().getCameraEntity() != null ? Minecraft.getInstance().getCameraEntity().tickCount : 0) + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
				return new Matrix4f().translation((-tick * 0.06F) % 1.0F, (-tick * 0.035F) % 1.0F, 0.0F).scale(0.5F);
			});
		}
	}
}
