package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.TFGhastModel;
import twilightforest.client.state.TFGhastRenderState;
import twilightforest.entity.monster.CarminiteGhastguard;

public class TFGhastRenderer<T extends CarminiteGhastguard, M extends TFGhastModel> extends MobRenderer<T, TFGhastRenderState, M> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("towerghast.png");
	private static final ResourceLocation LOOKING_TEXTURE = TwilightForestMod.getModelTexture("towerghast_openeyes.png");
	private static final ResourceLocation ATTACKING_TEXTURE = TwilightForestMod.getModelTexture("towerghast_fire.png");

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
	public ResourceLocation getTextureLocation(TFGhastRenderState state) {
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
