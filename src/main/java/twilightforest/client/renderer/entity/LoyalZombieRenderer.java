package twilightforest.client.renderer.entity;

import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import twilightforest.client.model.TFModelLayers;
import twilightforest.entity.monster.LoyalZombie;

public class LoyalZombieRenderer extends HumanoidMobRenderer<LoyalZombie, ZombieRenderState, AbstractZombieModel<ZombieRenderState>> {

	private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/zombie/zombie.png");

	public LoyalZombieRenderer(EntityRendererProvider.Context context) {
		super(context, new ZombieModel<>(context.bakeLayer(TFModelLayers.LOYAL_ZOMBIE)), 0.5F);
	}

	@Override
	protected int getModelTint(ZombieRenderState state) {
		return ARGB.colorFromFloat(1.0F, 0.25F, 1.0F, 0.25F);
	}

	@Override
	public ZombieRenderState createRenderState() {
		return new ZombieRenderState();
	}

	@Override
	public void extractRenderState(LoyalZombie entity, ZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isAggressive = entity.isAggressive();
	}

	@Override
	public ResourceLocation getTextureLocation(ZombieRenderState state) {
		return TEXTURE;
	}
}
