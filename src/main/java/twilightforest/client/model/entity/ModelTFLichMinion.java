package twilightforest.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import twilightforest.entity.boss.EntityTFLichMinion;

public class ModelTFLichMinion<T extends EntityTFLichMinion> extends ZombieModel<T> {

	private T entity;

	public ModelTFLichMinion() {
		super(0.0F, false);
	}

	/**
	 * Used for easily adding entity-dependent animations. The second and third float params here are the same second
	 * and third as in the setRotationAngles method.
	 */
	@Override
	public void setLivingAnimations(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		this.entity = entity;
	}

	@Override
	public void render(MatrixStack stack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float scale) {
		// make strong minions bigger FIXME: actually do this?
		if (entity.isStrong()) {
			super.render(stack, builder, light, overlay, red * 0.25F, green, blue * 0.25F, scale);
		} else {
			super.render(stack, builder, light, overlay, red * 0.5F, green, blue * 0.5F, scale);
		}
	}
}
