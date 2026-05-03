package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.RisingZombieModel;
import twilightforest.client.state.entity.RisingZombieRenderState;
import twilightforest.entity.monster.RisingZombie;

public class RisingZombieRenderer extends HumanoidMobRenderer<RisingZombie, RisingZombieRenderState, RisingZombieModel> {

	private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/entity/zombie/zombie.png");

	public RisingZombieRenderer(EntityRendererProvider.Context context) {
		super(context, new RisingZombieModel(context.bakeLayer(TFModelLayers.RISING_ZOMBIE)), 0.5F);
		this.addLayer(new HumanoidArmorLayer<>(this, ArmorModelSet.bake(ModelLayers.ZOMBIE_ARMOR, context.getModelSet(), RisingZombieModel::new), context.getEquipmentRenderer()));
	}

	@Override
	protected float getShadowRadius(RisingZombieRenderState state) {
		return 0.5F * (state.risingTicks / 130.0F);
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

	@Nullable
	@Override
	protected RenderType getRenderType(RisingZombieRenderState state, boolean bodyVisible, boolean translucent, boolean glowing) {
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
	public Identifier getTextureLocation(RisingZombieRenderState state) {
		return TEXTURE;
	}
}
