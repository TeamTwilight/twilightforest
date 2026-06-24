package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.TFGhastModel;
import twilightforest.client.state.entity.TFGhastRenderState;
import twilightforest.entity.monster.CarminiteGhastguard;

public class TFGhastRenderer<T extends CarminiteGhastguard, M extends TFGhastModel<TFGhastRenderState>> extends MobRenderer<T, TFGhastRenderState, M> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("towerghast.png");
	private static final Identifier LOOKING_TEXTURE = TwilightForestMod.getModelTexture("towerghast_openeyes.png");
	private static final Identifier ATTACKING_TEXTURE = TwilightForestMod.getModelTexture("towerghast_fire.png");

	public TFGhastRenderer(EntityRendererProvider.Context context, M model, float shadowSize) {
		super(context, model, shadowSize);
	}

	@Override
	public TFGhastRenderState createRenderState() {
		return new TFGhastRenderState();
	}

	@Override
	public void extractRenderState(T entity, TFGhastRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isCharging = entity.isCharging();
		state.attackTimer = Mth.lerp(partialTick, entity.getPrevAttackTimer(), entity.getAttackTimer());
		state.attackState = entity.getAttackStatus();
	}

	@Override
	public Identifier getTextureLocation(TFGhastRenderState state) {
		if (state.isCharging || state.deathTime > 0) {
			return ATTACKING_TEXTURE;
		}

		return switch (state.attackState) {
			case 1 -> LOOKING_TEXTURE;
			case 2 -> ATTACKING_TEXTURE;
			default -> TEXTURE;
		};
	}
}
