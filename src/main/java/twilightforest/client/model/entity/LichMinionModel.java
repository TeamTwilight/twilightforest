package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.FastColor;
import net.minecraft.world.effect.MobEffects;
import twilightforest.entity.monster.LichMinion;

public class LichMinionModel extends ZombieModel<LichMinion> {

	private boolean hasStrength;

	public LichMinionModel(ModelPart root) {
		super(root);
	}

	@Override
	public void prepareMobModel(LichMinion entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		this.hasStrength = entity.getEffect(MobEffects.DAMAGE_BOOST) != null;
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, int color) {
		if (hasStrength) {
			super.renderToBuffer(stack, builder, light, overlay, FastColor.ARGB32.color(FastColor.ARGB32.alpha(color), (int) (FastColor.ARGB32.red(color) * 0.25F), FastColor.ARGB32.green(color), (int) (FastColor.ARGB32.blue(color) * 0.25F)));
		} else {
			super.renderToBuffer(stack, builder, light, overlay, FastColor.ARGB32.color(FastColor.ARGB32.alpha(color), (int) (FastColor.ARGB32.red(color) * 0.5F), FastColor.ARGB32.green(color), (int) (FastColor.ARGB32.blue(color) * 0.5F)));
		}
	}
}
