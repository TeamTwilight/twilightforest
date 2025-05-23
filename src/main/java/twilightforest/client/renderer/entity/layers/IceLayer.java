package twilightforest.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.data.ModelData;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.DeathTomeModel;
import twilightforest.potions.FrostedEffect;

public class IceLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
	private final RandomSource random = RandomSource.create();

	public static ContextKey<Double> FROST_COUNT_KEY = new ContextKey<>(TwilightForestMod.prefix("frost_count"));
	public static ContextKey<Integer> FROST_ID_KEY = new ContextKey<>(TwilightForestMod.prefix("frost_id"));

	public IceLayer(RenderLayerParent<S, M> renderer) {
		super(renderer);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, S state, float netHeadYaw, float headPitch) {
		Double count = state.getRenderData(FROST_COUNT_KEY);
		if (count == null || count <= 0.0D) return;
		Integer id = state.getRenderData(FROST_ID_KEY);
		if (id == null) return;

		this.random.setSeed(id * id * 3121L + id * 45238971L);

		int numCubes = (int) (state.boundingBoxHeight / 0.4F) + (int) (count / FrostedEffect.FROST_MULTIPLIER) + 1; //Number of cubes, adds more cubes based on the level of the effect

		float specialOffset = this.getParentModel() instanceof DeathTomeModel ? 1.0F : 0.0F;

		for (int i = 0; i < numCubes; i++) { //Render cubes
			stack.pushPose();
			float dx = ((this.random.nextFloat() * (state.boundingBoxWidth * 2.0F)) - state.boundingBoxWidth) * 0.1F;
			float dy = Math.max(1.5F - (this.random.nextFloat()) * (state.boundingBoxHeight - specialOffset), -0.1F) - specialOffset; //Gotta limit the height because otherwise frozen giants make blocks spawn like 10 blocks above them
			float dz = ((this.random.nextFloat() * (state.boundingBoxWidth * 2.0F)) - state.boundingBoxWidth) * 0.1F;
			stack.translate(dx, dy, dz);
			stack.scale(0.5F, 0.5F, 0.5F);
			stack.mulPose(Axis.XP.rotationDegrees(this.random.nextFloat() * 360F));
			stack.mulPose(Axis.YP.rotationDegrees(this.random.nextFloat() * 360F));
			stack.mulPose(Axis.ZP.rotationDegrees(this.random.nextFloat() * 360F));
			stack.translate(-0.5F, -0.5F, -0.5F);

			Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.ICE.defaultBlockState(), stack, buffer, light, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.translucentMovingBlock());
			stack.popPose();
		}
	}
}
