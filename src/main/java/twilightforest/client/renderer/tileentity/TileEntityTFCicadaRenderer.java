package twilightforest.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.entity.ModelTFCicada;
import twilightforest.tileentity.critters.TileEntityTFCicada;
import twilightforest.tileentity.critters.TileEntityTFCicadaTicking;

public class TileEntityTFCicadaRenderer<T extends TileEntityTFCicada> extends TileEntityRenderer<T> {

	private final ModelTFCicada cicadaModel = new ModelTFCicada();
	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("cicada-model.png");

	public TileEntityTFCicadaRenderer(TileEntityRendererDispatcher dispatch) {
		super(dispatch);
	}

	@Override
	public void render(T te, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay) {
		int yaw = te != null ? ((TileEntityTFCicadaTicking) te).currentYaw : BugModelAnimationHelper.currentYaw;

		stack.push();
		Direction facing = Direction.byIndex(te != null ? te.getBlockMetadata() : 0);

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
		stack.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		RenderSystem.rotatef(rotX, 1F, 0F, 0F);
		RenderSystem.rotatef(rotZ, 0F, 0F, 1F);
		RenderSystem.rotatef(yaw, 0F, 1F, 0F);

		this.bindTexture(textureLoc);
		stack.push();
		stack.scale(-1f, -1f, -1f);
		cicadaModel.render(0.0625f);
		stack.pop();
		RenderSystem.color4f(1, 1, 1, 1);
		stack.pop();
	}
}
