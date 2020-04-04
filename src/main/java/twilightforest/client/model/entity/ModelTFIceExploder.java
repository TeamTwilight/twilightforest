package twilightforest.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import twilightforest.entity.EntityTFIceMob;

public class ModelTFIceExploder<T extends EntityTFIceMob> extends BipedModel<T> {

	public ModelRenderer[] spikes = new ModelRenderer[16];

	public ModelTFIceExploder() {
		super(0.0F, 0.0F, 32, 32);
//		this.textureWidth = 32;
//		this.textureHeight = 32;

		float par1 = 0F;
		float par2 = 0F;
		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addCuboid(-4.0F, 0.0F, -4.0F, 8, 8, 8, par1);
		this.bipedHead.setRotationPoint(0.0F, 0.0F + par2, 0.0F);

		// delete all other body parts
		this.bipedHeadwear = new ModelRenderer(this, 0, 0);
		this.bipedBody = new ModelRenderer(this, 0, 0);
		this.bipedRightArm = new ModelRenderer(this, 0, 0);
		this.bipedLeftArm = new ModelRenderer(this, 0, 0);
		this.bipedRightLeg = new ModelRenderer(this, 0, 0);
		this.bipedLeftLeg = new ModelRenderer(this, 0, 0);

		// spikes

		for (int i = 0; i < spikes.length; i++) {
			this.spikes[i] = new ModelRenderer(this, 0, 16);

			int spikeLength = i % 2 == 0 ? 6 : 8;

			this.spikes[i].addCuboid(-1.0F, 6.0F, -1.0F, 2, spikeLength, 2, par1);
			this.spikes[i].setRotationPoint(0.0F, 4.0F + par2, 0.0F);

			ModelRenderer cube = new ModelRenderer(this, 8, 16);
			cube.addCuboid(-1.5F, -1.5F, -1.5F, 3, 3, 3);
			cube.setRotationPoint(0.0F, 8F, 0.0F);

			cube.rotateAngleZ = (float) (Math.PI / 4F);

			this.spikes[i].addChild(cube);
		}
	}

	@Override
	public void render(MatrixStack stack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float scale) {
		//setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.bipedHead.render(scale);

		for (int i = 0; i < spikes.length; i++) {

			if (entity.isAlive()) {
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				RenderSystem.color4f(1F, 1F, 1F, 0.6F);
			}

			this.spikes[i].render(scale);

			GlStateManager.disableBlend();
		}
	}

	/**
	 * Used for easily adding entity-dependent animations. The second and third float params here are the same second
	 * and third as in the setRotationAngles method.
	 */
	@Override
	public void setLivingAnimations(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		for (int i = 0; i < spikes.length; i++) {
			// rotate the spikes
			this.spikes[i].rotateAngleY = (entity.ticksExisted + partialTicks) / 5.0F;
			this.spikes[i].rotateAngleX = MathHelper.sin((entity.ticksExisted + partialTicks) / 5.0F) / 4.0F;
			this.spikes[i].rotateAngleZ = MathHelper.cos((entity.ticksExisted + partialTicks) / 5.0F) / 4.0F;

			this.spikes[i].rotateAngleX += i * 5;
			this.spikes[i].rotateAngleY += i * 2.5f;
			this.spikes[i].rotateAngleZ += i * 3;

			this.spikes[i].rotationPointX = MathHelper.cos((entity.ticksExisted + partialTicks) / (float) i) * 3F;
			this.spikes[i].rotationPointY = 5F + MathHelper.sin((entity.ticksExisted + partialTicks) / (float) i) * 3F;
			this.spikes[i].rotationPointZ = MathHelper.sin((entity.ticksExisted + partialTicks) / (float) i) * 3F;

			this.spikes[i].childModels.get(0).rotationPointY = 10 + MathHelper.sin((i + entity.ticksExisted + partialTicks) / i) * 3F;
		}
	}
}
