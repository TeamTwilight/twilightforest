package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.RisingZombieModel;
import twilightforest.client.state.RisingZombieRenderState;
import twilightforest.entity.monster.RisingZombie;

public class RisingZombieRenderer extends HumanoidMobRenderer<RisingZombie, RisingZombieRenderState, RisingZombieModel> {

	private static final ResourceLocation ZOMBIE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/zombie/zombie.png");

	public RisingZombieRenderer(EntityRendererProvider.Context context) {
		super(context, new RisingZombieModel(context.bakeLayer(TFModelLayers.RISING_ZOMBIE)), 0.5F);
		this.addLayer(new HumanoidArmorLayer<>(this, new RisingZombieModel(context.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new RisingZombieModel(context.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), new RisingZombieModel(context.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new RisingZombieModel(context.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), context.getEquipmentRenderer()));
	}

	@Override
	protected float getShadowRadius(RisingZombieRenderState entity) {
		return 0.5F * (entity.risingTicks / 130.0F);
	}

	@Override
	public RisingZombieRenderState createRenderState() {
		return new RisingZombieRenderState();
	}

	@Override
	public void extractRenderState(RisingZombie entity, RisingZombieRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.isAggressive = entity.isAggressive();
		state.risingTicks = entity.getRisingTicks();
	}

	@Override
	protected @Nullable RenderType getRenderType(RisingZombieRenderState state, boolean bodyVisible, boolean translucent, boolean glowing) {
		// Render normally for spectators instead of very translucent
		if (translucent) return this.model.renderType(this.getTextureLocation(state));
		return super.getRenderType(state, bodyVisible, translucent, glowing);
	}

	@Override
	protected void setupRotations(RisingZombieRenderState state, PoseStack stack, float bodyRot, float scale) {
		super.setupRotations(state, stack, bodyRot, scale);
		var tick = state.risingTicks;
		stack.translate(0.0F, -(80.0F - Math.min(80.0F, tick)) / 80.0F, 0.0F);
		stack.translate(0.0F, -(40.0F - Math.min(40.0F, Math.max(0.0F, tick - 80.0F))) / 40.0F, 0.0F);
		final float yOff = 1.0F;
		stack.translate(0.0F, yOff, 0.0F);
		stack.mulPose(Axis.XP.rotationDegrees(120.0F * (80.0F - Math.min(80.0F, tick)) / 80.0F));
		stack.mulPose(Axis.XP.rotationDegrees(-30.0F * (40.0F - Math.min(40.0F, Math.max(0.0F, tick - 80.0F))) / 40.0F));
		stack.translate(0.0F, -yOff, 0.0F);

	}

	@Override
	protected float getFlipDegrees() {
		return 0.0F;
	}

	@Override
	public ResourceLocation getTextureLocation(RisingZombieRenderState state) {
		return ZOMBIE_LOCATION;
	}
}
