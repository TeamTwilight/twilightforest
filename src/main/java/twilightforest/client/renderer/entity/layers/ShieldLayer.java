package twilightforest.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.Lich;
import twilightforest.init.TFDataAttachments;

public class ShieldLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {

	public static final Identifier LOC = TwilightForestMod.prefix("item/shield");

	public static ContextKey<Integer> SHIELD_COUNT_KEY = new ContextKey<>(TwilightForestMod.prefix("shield_count"));

	public ShieldLayer(RenderLayerParent<S, M> renderer) {
		super(renderer);
	}

	@Override
	public void submit(PoseStack stack, SubmitNodeCollector collector, int light, S state, float netHeadYaw, float headPitch) {
		Integer count = state.getRenderData(SHIELD_COUNT_KEY);
		if (count != null && count > 0) {
			this.renderShields(stack, collector, state, count, state.lightCoords, state.outlineColor);
		}
	}

	public static int getShieldCount(LivingEntity entity) {
		return entity instanceof Lich lich
			? (lich.getTeleportInvisibility() > 0 ? 0 : lich.getShieldStrength())
			: entity.getData(TFDataAttachments.FORTIFICATION_SHIELDS).shieldsLeft();
	}

	private void renderShields(PoseStack stack, SubmitNodeCollector collector, S state, int count, int lightCoords, int outlineColor) {
		float age = state.ageInTicks;
		float rotateAngleY = age / -5.0F;
		float rotateAngleX = Mth.sin(age / 5.0F) / 4.0F;
		float rotateAngleZ = Mth.cos(age / 5.0F) / 4.0F;

		for (int c = 0; c < count; c++) {
			stack.pushPose();

			// perform the rotations, accounting for the fact that baked models are corner-based
			// Z gets extra 180 degrees to flip visual upside-down, since scaling y by -1 will cause back-faces to render instead
			stack.mulPose(Axis.ZP.rotationDegrees(180.0F + rotateAngleZ * (180.0F / Mth.PI)));
			stack.mulPose(Axis.YP.rotationDegrees(rotateAngleY * (180.0F / Mth.PI) + (c * (360.0F / count))));
			stack.mulPose(Axis.XP.rotationDegrees(rotateAngleX * (180.0F / Mth.PI)));
			stack.translate(-0.5F, -0.65F, -0.5F);

			// push the shields outwards from the center of rotation
			stack.translate(0.0F, 0.0F, -0.7F);

			BlockModel shieldModel = Minecraft.getInstance().getModelManager().getStandaloneModel(new StandaloneModelKey<>(LOC::toDebugFileName));

			if (shieldModel != null) {
				BlockModelRenderState modelState = new BlockModelRenderState();
				shieldModel.update(
					modelState,
					Blocks.AIR.defaultBlockState(),
					BlockDisplayContext.create(),
					42L
				);
				modelState.submit(
					stack,
					buffer.getBuffer(Sheets.translucentItemSheet()),
					model.getQuads(null, dir, Minecraft.getInstance().font.random, ModelData.EMPTY, Sheets.translucentItemSheet()),
					new int[0],
					LightCoordsUtil.FULL_BRIGHT,
					OverlayTexture.NO_OVERLAY
				);
			}

			stack.popPose();
		}
	}
}
