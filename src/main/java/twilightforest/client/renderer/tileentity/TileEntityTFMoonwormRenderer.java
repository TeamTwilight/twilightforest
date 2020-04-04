package twilightforest.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.entity.ModelTFMoonworm;
import twilightforest.tileentity.critters.TileEntityTFMoonworm;
import twilightforest.tileentity.critters.TileEntityTFMoonwormTicking;

public class TileEntityTFMoonwormRenderer<T extends TileEntityTFMoonworm> extends TileEntityRenderer<T> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("moonworm.png");
	private final ModelTFMoonworm moonwormModel = new ModelTFMoonworm();

	public TileEntityTFMoonwormRenderer(TileEntityRendererDispatcher dispatch) {
		super(dispatch);
	}

	@Override
	public void render(T te, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay) {
		int yaw = te != null ? ((TileEntityTFMoonwormTicking) te).currentYaw : BugModelAnimationHelper.currentRotation;
		if (te == null) partialTicks = Minecraft.getInstance().getRenderPartialTicks();

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
		stack.translate(x + 0.5F, y + 0.5F, z + 0.5F);
		RenderSystem.rotatef(rotX, 1F, 0F, 0F);
		RenderSystem.rotatef(rotZ, 0F, 0F, 1F);
		RenderSystem.rotatef(yaw, 0F, 1F, 0F);

		this.bindTexture(textureLoc);

		stack.scale(1f, -1f, -1f);

		moonwormModel.setLivingAnimations((TileEntityTFMoonwormTicking) te, partialTicks);

		// render the firefly body
		moonwormModel.render(0.0625f);

		stack.pop();
	}
}
