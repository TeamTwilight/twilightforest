package twilightforest.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import twilightforest.entity.EntityTFDeathTome;

// todo 1.15: update vanillacopying from BookModel
public class ModelTFDeathTome extends EntityModel<EntityTFDeathTome> {
	private ModelRenderer everything;

	private ModelRenderer book;
	private ModelRenderer loosePage1;
	private ModelRenderer loosePage2;
	private ModelRenderer loosePage3;
	private ModelRenderer loosePage4;

	public ModelTFDeathTome() {
		everything = (new ModelRenderer(this)).setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0, 0, 0);

		book = (new ModelRenderer(this)).setTextureOffset(0, 0).addCuboid(0.0F, 0.0F, 0.0F, 0, 0, 0);

		/* todo 1.15 BookModel copying
		book.addChild(coverRight);
		book.addChild(coverLeft);
		book.addChild(bookSpine);
		book.addChild(pagesRight);
		book.addChild(pagesLeft);
		book.addChild(flippingPageRight);
		book.addChild(flippingPageLeft);
		 */

		loosePage1 = (new ModelRenderer(this)).setTextureOffset(24, 10).addCuboid(0F, -4F, -8F, 5, 8, 0);
		loosePage2 = (new ModelRenderer(this)).setTextureOffset(24, 10).addCuboid(0F, -4F, 9F, 5, 8, 0);
		loosePage3 = (new ModelRenderer(this)).setTextureOffset(24, 10).addCuboid(0F, -4F, 11F, 5, 8, 0);
		loosePage4 = (new ModelRenderer(this)).setTextureOffset(24, 10).addCuboid(0F, -4F, 7F, 5, 8, 0);

		everything.addChild(book);
		everything.addChild(loosePage1);
		everything.addChild(loosePage2);
		everything.addChild(loosePage3);
		everything.addChild(loosePage4);
	}

	@Override
	public void render(MatrixStack stack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float scale) {
		this.everything.render(stack, builder, light, overlay);
	}

	@Override
	public void setAngles(EntityTFDeathTome entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch) {
		// todo 1.15 verify
		book.rotateAngleZ = -0.8726646259971647F;
		this.everything.rotateAngleY = customAngle / (180F / (float) Math.PI) + (float) Math.PI / 2.0F;
	}

	@Override
	public void setLivingAnimations(EntityTFDeathTome entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		float bounce = entity.ticksExisted + partialTicks;
		float open = 0.9f;
		float flipRight = 0.4f;
		float flipLeft = 0.6f;

		// hoveriness
		book.setRotationPoint(0, 4 + MathHelper.sin((bounce) * 0.3F) * 2.0F, 0);

		// book openness
		float openAngle = (MathHelper.sin(bounce * 0.4F) * 0.3F + 1.25F) * open;
		/* todo 1.15 bookmodel copying
		this.coverRight.rotateAngleY = (float) Math.PI + openAngle;
		this.coverLeft.rotateAngleY = -openAngle;
		this.pagesRight.rotateAngleY = openAngle;
		this.pagesLeft.rotateAngleY = -openAngle;
		this.flippingPageRight.rotateAngleY = openAngle - openAngle * 2.0F * flipRight;
		this.flippingPageLeft.rotateAngleY = openAngle - openAngle * 2.0F * flipLeft;
		this.pagesRight.rotationPointX = MathHelper.sin(openAngle);
		this.pagesLeft.rotationPointX = MathHelper.sin(openAngle);
		this.flippingPageRight.rotationPointX = MathHelper.sin(openAngle);
		this.flippingPageLeft.rotationPointX = MathHelper.sin(openAngle);
		 */


		// page rotations
		loosePage1.rotateAngleY = (bounce) / 4.0F;
		loosePage1.rotateAngleX = MathHelper.sin((bounce) / 5.0F) / 3.0F;
		loosePage1.rotateAngleZ = MathHelper.cos((bounce) / 5.0F) / 5.0F;

		loosePage2.rotateAngleY = (bounce) / 3.0F;
		loosePage2.rotateAngleX = MathHelper.sin((bounce) / 5.0F) / 3.0F;
		loosePage2.rotateAngleZ = MathHelper.cos((bounce) / 5.0F) / 4.0F + 2;

		loosePage3.rotateAngleY = (bounce) / 4.0F;
		loosePage3.rotateAngleX = -MathHelper.sin((bounce) / 5.0F) / 3.0F;
		loosePage3.rotateAngleZ = MathHelper.cos((bounce) / 5.0F) / 5.0F - 1.0F;

		loosePage4.rotateAngleY = (bounce) / 4.0F;
		loosePage4.rotateAngleX = -MathHelper.sin((bounce) / 2.0F) / 4.0F;
		loosePage4.rotateAngleZ = MathHelper.cos((bounce) / 7.0F) / 5.0F;
	}
}
