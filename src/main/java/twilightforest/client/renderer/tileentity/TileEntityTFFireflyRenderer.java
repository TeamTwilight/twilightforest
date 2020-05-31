package twilightforest.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.entity.ModelTFFirefly;
import twilightforest.tileentity.critters.TileEntityTFFireflyTicking;

import javax.annotation.Nullable;

public class TileEntityTFFireflyRenderer extends TileEntityRenderer<TileEntityTFFireflyTicking> {

	private final ModelTFFirefly fireflyModel = new ModelTFFirefly();
	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("firefly-tiny.png");
	private static final RenderType GLOW_LAYER;
	static {
		RenderState.TransparencyState transparencyState = new RenderState.TransparencyState(TwilightForestMod.ID + ":firefly_glow", () -> {
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		}, () -> {
			RenderSystem.disableBlend();
			RenderSystem.defaultBlendFunc();
		});
		RenderState.AlphaState noAlphaTest = new RenderState.AlphaState(0);

		// [VanillaCopy] RenderState constants
		RenderState.DiffuseLightingState enableDiffuse = new RenderState.DiffuseLightingState(true);
		RenderState.CullState disableCull = new RenderState.CullState(false);
		RenderState.LightmapState enableLightmap = new RenderState.LightmapState(true);

		RenderType.State rendertype$state = RenderType.State.builder().texture(new RenderState.TextureState(textureLoc, false, false)).transparency(transparencyState).diffuseLighting(enableDiffuse).alpha(noAlphaTest).cull(disableCull).lightmap(enableLightmap).build(false);
		GLOW_LAYER = RenderType.of(TwilightForestMod.ID + ":firefly_glow", DefaultVertexFormats.POSITION_COLOR_TEXTURE_LIGHT, GL11.GL_QUADS, 256, true, true, rendertype$state);
	}

	public TileEntityTFFireflyRenderer(TileEntityRendererDispatcher dispatch) {
		super(dispatch);
	}

	@Override
	public void render(@Nullable TileEntityTFFireflyTicking te, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int light, int overlay) {
		int yaw = te != null ? te.currentYaw : BugModelAnimationHelper.currentYaw;
		float glow = te != null ? te.glowIntensity : BugModelAnimationHelper.glowIntensity;

		ms.push();
		Direction facing = te != null ? te.getBlockState().get(DirectionalBlock.FACING) : Direction.NORTH;

		float rotX = 90.0F;
		float rotZ = 0.0F;
		if (facing == Direction.SOUTH) {
			rotZ = 0F;
		} else if (facing == Direction.NORTH) {
			rotZ = 180F;
		} else if (facing == Direction.EAST) {
			rotZ = -90F;
		} else if (facing == Direction.WEST) {
			rotZ = 90F;
		} else if (facing == Direction.UP) {
			rotX = 0F;
		} else if (facing == Direction.DOWN) {
			rotX = 180F;
		}
		ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(rotX));
		ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rotZ));
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(yaw));

		ms.push();
		ms.scale(1f, -1f, -1f);

		IVertexBuilder builder = buffer.getBuffer(RenderType.getEntityCutout(textureLoc));
		fireflyModel.render(ms, builder, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

		builder = buffer.getBuffer(GLOW_LAYER);
		fireflyModel.glow.render(ms, builder, 0xF000F0, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, glow);

		ms.pop();
		ms.pop();
	}
}
