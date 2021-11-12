package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;
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
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float scale) {
		if (hasStrength) {
			super.renderToBuffer(stack, builder, light, overlay, red * 0.25F, green, blue * 0.25F, scale);
		} else {
			super.renderToBuffer(stack, builder, light, overlay, red * 0.5F, green, blue * 0.5F, scale);
		}
	}
}
