package twilightforest.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.FireflyModel;
import twilightforest.block.entity.FireflyBlockEntity;

import javax.annotation.Nullable;

public class FireflyTileEntityRenderer implements BlockEntityRenderer<FireflyBlockEntity> {

	private final FireflyModel fireflyModel;
	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("firefly-tiny.png");
	//private static final RenderType GLOW_LAYER;
	static {
		RenderStateShard.TransparencyStateShard transparencyState = new RenderStateShard.TransparencyStateShard(TwilightForestMod.ID + ":firefly_glow", () -> {
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		}, () -> {
			RenderSystem.disableBlend();
			RenderSystem.defaultBlendFunc();
		});
		//RenderStateShard.AlphaStateShard noAlphaTest = new RenderStateShard.AlphaStateShard(0);

		// [VanillaCopy] RenderState constants
		//RenderStateShard.DiffuseLightingStateShard enableDiffuse = new RenderStateShard.DiffuseLightingStateShard(true);
		//RenderStateShard.CullStateShard disableCull = new RenderStateShard.CullStateShard(false);
		//RenderStateShard.LightmapStateShard enableLightmap = new RenderStateShard.LightmapStateShard(true);

		//RenderType.CompositeState rendertype$state = RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(textureLoc, false, false)).setTransparencyState(transparencyState)/*FIXME .setDiffuseLightingState(enableDiffuse).setAlphaState(noAlphaTest)*/.setCullState(disableCull).setLightmapState(enableLightmap).createCompositeState(false);
		//GLOW_LAYER = RenderType.create(TwilightForestMod.ID + ":firefly_glow", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, true, rendertype$state);
	}

	public FireflyTileEntityRenderer(BlockEntityRendererProvider.Context renderer) {
		this.fireflyModel = new FireflyModel(renderer.bakeLayer(TFModelLayers.FIREFLY));
	}

	@Override
	public void render(@Nullable FireflyBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		int yaw = te != null ? te.currentYaw : BugModelAnimationHelper.currentYaw;
		float glow = te != null ? te.glowIntensity : BugModelAnimationHelper.glowIntensity;

		ms.pushPose();
		Direction facing = te != null ? te.getBlockState().getValue(DirectionalBlock.FACING) : Direction.NORTH;

		ms.translate(0.5F, 0.5F, 0.5F);
		ms.mulPose(facing.getRotation());
		ms.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
		ms.mulPose(Vector3f.YP.rotationDegrees(180.0F));
		ms.mulPose(Vector3f.YN.rotationDegrees(yaw));

		ms.pushPose();

		VertexConsumer builder = buffer.getBuffer(RenderType.entityCutout(textureLoc));
		fireflyModel.renderToBuffer(ms, builder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		//builder = buffer.getBuffer(GLOW_LAYER);
		//fireflyModel.glow.render(ms, builder, 0xF000F0, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, glow);

		ms.popPose();
		ms.popPose();
	}
}
