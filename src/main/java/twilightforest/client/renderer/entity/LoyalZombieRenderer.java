package twilightforest.client.renderer.entity;

import net.minecraft.client.model.monster.zombie.AbstractZombieModel;
import net.minecraft.client.model.monster.zombie.BabyZombieModel;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import twilightforest.client.model.TFModelLayers;
import twilightforest.entity.monster.LoyalZombie;

public class LoyalZombieRenderer extends HumanoidMobRenderer<LoyalZombie, ZombieRenderState, AbstractZombieModel<ZombieRenderState>> {
	private static final Identifier ZOMBIE_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/zombie.png");
	private static final Identifier BABY_ZOMBIE_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/zombie_baby.png");

	public LoyalZombieRenderer(EntityRendererProvider.Context context) {
		super(context, new ZombieModel<>(context.bakeLayer(TFModelLayers.LOYAL_ZOMBIE)), new BabyZombieModel<>(context.bakeLayer(TFModelLayers.LOYAL_ZOMBIE_BABY)), 0.5F);
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
	public Identifier getTextureLocation(ZombieRenderState state) {
		return state.isBaby ? BABY_ZOMBIE_LOCATION : ZOMBIE_LOCATION;
	}
}