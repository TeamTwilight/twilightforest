package twilightforest.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.entity.ModelTFFirefly;
import twilightforest.tileentity.critters.TileEntityTFFirefly;
import twilightforest.tileentity.critters.TileEntityTFFireflyTicking;

public class TileEntityTFFireflyRenderer<T extends TileEntityTFFirefly> extends TileEntityRenderer<T> {

	private final ModelTFFirefly fireflyModel = new ModelTFFirefly();
	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("firefly-tiny.png");

	public TileEntityTFFireflyRenderer(TileEntityRendererDispatcher dispatch) {
		super(dispatch);
	}

	@Override
	public void render(T te, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay) {
		int yaw = te != null ? ((TileEntityTFFireflyTicking) te).currentYaw : BugModelAnimationHelper.currentYaw;
		float glow = te != null ? ((TileEntityTFFireflyTicking) te).glowIntensity : BugModelAnimationHelper.glowIntensity;

		stack.push();
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
		//stack.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		RenderSystem.rotatef(rotX, 1F, 0F, 0F);
		RenderSystem.rotatef(rotZ, 0F, 0F, 1F);
		RenderSystem.rotatef(yaw, 0F, 1F, 0F);

		//this.bindTexture(textureLoc);
		IVertexBuilder builder = buffer.getBuffer(RenderType.getEntityCutout(textureLoc));
		stack.push();
		stack.scale(1f, -1f, -1f);

		GlStateManager.colorMask(true, true, true, true);

		// render the firefly body
		RenderSystem.disableBlend();
		fireflyModel.render(stack, builder, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 0.0625f);

//      GL11.glEnable(3042 /*GL_BLEND*/);
//      GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
//      GL11.glBlendFunc(770, 771);
//      GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);

		// render the firefly glow
		RenderSystem.enableBlend();
		RenderSystem.disableAlphaTest();
		RenderSystem.disableLighting();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, glow);
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		fireflyModel.glow.render(stack, builder, Float.floatToIntBits(glow), OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 0.0625f);
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableLighting();

		stack.pop();
		RenderSystem.color4f(1, 1, 1, 1);
		stack.pop();
	}
}
