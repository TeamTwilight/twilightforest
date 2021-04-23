package twilightforest.client.model.entity.legacy;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import twilightforest.entity.boss.EntityTFNaga;
import twilightforest.entity.boss.EntityTFNagaSegment;

public class ModelTFNagaLegacy<T extends Entity> extends SegmentedModel<T> {

	private T entity;

	public ModelTFNagaLegacy() {
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-8F, -12F, -8F, 16, 16, 16, 0F);
		head.setRotationPoint(0F, 0F, 0F);

		body = new ModelRenderer(this, 0, 0);
		body.addBox(-8F, -16F, -8F, 16, 16, 16, 0F);
		body.setRotationPoint(0F, 0F, 0F);
	}

	@Override
	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(head, body);
	}

	@Override
	public void render(MatrixStack stack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float scale) {

		//setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

		if (entity instanceof EntityTFNaga) {
			head.render(stack, builder, light, overlay, red, green, blue, scale * 2);
		} else if (entity instanceof EntityTFNagaSegment) {
			body.render(stack, builder, light, overlay, red, green, blue, scale * 2);
		} else {
			head.render(stack, builder, light, overlay, red, green, blue, scale * 2);
		}
	}

	@Override
	public void setRotationAngles(T entity, float v, float v1, float v2, float v3, float v4) {
		this.entity = entity;
	}

	//fields
	public ModelRenderer head;
	public ModelRenderer body;
}
