package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraftforge.client.model.data.EmptyModelData;
import twilightforest.potions.PotionFrosted;

import java.util.Random;

public class LayerIce<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
	private final Random random = new Random();

	public LayerIce(IEntityRenderer<T, M> renderer) {
		super(renderer);
	}

	@Override
	public void render(MatrixStack stack, IRenderTypeBuffer buffer, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (entity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(PotionFrosted.MODIFIER_UUID) == null) {
			return;
		}

		random.setSeed(entity.getEntityId() * entity.getEntityId() * 3121 + entity.getEntityId() * 45238971);

		// number of cubes
		int numCubes = (int) (entity.getHeight() / 0.4F);

		// make cubes
		for (int i = 0; i < numCubes; i++) {
			stack.push();
			float dx = (float) (entity.getX() + random.nextGaussian() * 0.2F * entity.getWidth());
			float dy = (float) (entity.getY() + random.nextGaussian() * 0.2F * entity.getHeight()) + entity.getHeight() / 2F;
			float dz = (float) (entity.getZ() + random.nextGaussian() * 0.2F * entity.getWidth());
			stack.translate(dx, dy, dz);
			stack.scale(0.5F, 0.5F, 0.5F);
			stack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360F));
			stack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360F));
			stack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360F));

			Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(Blocks.ICE.getDefaultState(), stack, buffer, light, OverlayTexture.DEFAULT_UV, EmptyModelData.INSTANCE);
			stack.pop();
		}
	}
}
