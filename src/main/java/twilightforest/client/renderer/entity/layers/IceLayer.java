package twilightforest.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.block.Blocks;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.DeathTomeModel;
import twilightforest.potions.FrostedEffect;

public class IceLayer<S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {
	private final RandomSource random = RandomSource.create();
	private final BlockDisplayContext displayContext = BlockDisplayContext.create();
	private final BlockModelRenderState iceModel = new BlockModelRenderState();

	public static final ContextKey<Double> FROST_COUNT_KEY = new ContextKey<>(TwilightForestMod.prefix("frost_count"));
	public static final ContextKey<Integer> FROST_ID_KEY = new ContextKey<>(TwilightForestMod.prefix("frost_id"));

	public IceLayer(RenderLayerParent<S, M> renderer) {
		super(renderer);
	}

	@Override
	public void submit(PoseStack stack, SubmitNodeCollector submitNodeCollector, int light, S state, float netHeadYaw, float headPitch) {
		Double count = state.getRenderData(FROST_COUNT_KEY);
		if (count == null)
			return;
		Integer id = state.getRenderData(FROST_ID_KEY);
		if (id == null)
			return;

		Minecraft.getInstance().getBlockModelResolver().update(iceModel, Blocks.ICE.defaultBlockState(), displayContext);

		random.setSeed(id * id * 3121L + id * 45238971L);

		int numCubes = (int) (state.boundingBoxHeight / 0.4F) + (int) (count / FrostedEffect.FROST_MULTIPLIER) + 1; //Number of cubes, adds more cubes based on the level of the effect

		float specialOffset = getParentModel() instanceof DeathTomeModel ? 1.0F : 0.0F;

		for (int i = 0; i < numCubes; i++) { //Render cubes
			stack.pushPose();
			float dx = ((random.nextFloat() * (state.boundingBoxWidth * 2.0F)) - state.boundingBoxWidth) * 0.1F;
			float dy = Math.max(1.5F - (random.nextFloat()) * (state.boundingBoxHeight - specialOffset), -0.1F) - specialOffset; //Gotta limit the height because otherwise frozen giants make blocks spawn like 10 blocks above them
			float dz = ((random.nextFloat() * (state.boundingBoxWidth * 2.0F)) - state.boundingBoxWidth) * 0.1F;
			stack.translate(dx, dy, dz);
			stack.scale(0.5F, 0.5F, 0.5F);
			stack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360F));
			stack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360F));
			stack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360F));
			stack.translate(-0.5F, -0.5F, -0.5F);

			iceModel.submit(stack, submitNodeCollector, light, OverlayTexture.NO_OVERLAY, 0);

			stack.popPose();
		}
	}
}
