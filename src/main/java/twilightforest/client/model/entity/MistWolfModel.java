package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.monster.MistWolf;

public class MistWolfModel extends HostileWolfModel<MistWolf> {

	@Nullable
	private MistWolf wolf;

	public MistWolfModel(ModelPart root) {
		super(RenderType::entityTranslucent, root);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer consumer, int light, int overlay, int color) {
		if (this.wolf != null) {
			float brightness = this.wolf.level().getMaxLocalRawBrightness(this.wolf.blockPosition());
			float misty = Math.min(1.0F, brightness * 3.0F + 0.25F);
			float smoky = brightness * 2.0F + 0.6F;
			super.renderToBuffer(stack, consumer, light, overlay, FastColor.ARGB32.colorFromFloat(smoky, misty, misty, misty));
		} else {
			super.renderToBuffer(stack, consumer, light, overlay, color);
		}
		this.wolf = null;
	}

	@Override
	public void setupAnim(MistWolf entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.wolf = entity;
	}
}
