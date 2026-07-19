package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.util.ARGB;
import twilightforest.client.state.entity.LichMinionRenderState;

public class LichMinionModel extends ZombieModel<LichMinionRenderState> {

	private boolean hasStrength;

	public LichMinionModel(ModelPart root) {
		super(root);
	}

	@Override
	public void setupAnim(LichMinionRenderState state) {
		this.hasStrength = state.hasStrength;
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, int color) {
		if (this.hasStrength) {
			super.renderToBuffer(stack, builder, light, overlay, ARGB.color(ARGB.alpha(color), (int) (ARGB.red(color) * 0.25F), ARGB.green(color), (int) (ARGB.blue(color) * 0.25F)));
		} else {
			super.renderToBuffer(stack, builder, light, overlay, ARGB.color(ARGB.alpha(color), (int) (ARGB.red(color) * 0.5F), ARGB.green(color), (int) (ARGB.blue(color) * 0.5F)));
		}
	}
}