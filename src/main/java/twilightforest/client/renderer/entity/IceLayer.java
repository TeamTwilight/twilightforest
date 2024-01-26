package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.data.ModelData;
import twilightforest.client.model.entity.DeathTomeModel;
import twilightforest.potions.FrostedEffect;

public class IceLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private final RandomSource random = RandomSource.create();

	public IceLayer(RenderLayerParent<T, M> renderer) {
		super(renderer);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		AttributeInstance speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
		if (speed == null) return;

		AttributeModifier frost = speed.getModifier(FrostedEffect.MOVEMENT_SPEED_MODIFIER_UUID);
		if (frost == null) return;

		this.random.setSeed(entity.getId() * entity.getId() * 3121L + entity.getId() * 45238971L);

		int numCubes = (int) (entity.getBbHeight() / 0.4F) + (int) (frost.getAmount() / FrostedEffect.FROST_MULTIPLIER) + 1; //Number of cubes, adds more cubes based on the level of the effect

		float specialOffset = this.getParentModel() instanceof DeathTomeModel ? 1.0F : 0.0F;

		for (int i = 0; i < numCubes; i++) { //Render cubes
			stack.pushPose();
			float dx = ((this.random.nextFloat() * (entity.getBbWidth() * 2.0F)) - entity.getBbWidth()) * 0.1F;
			float dy = Math.max(1.5F - (this.random.nextFloat()) * (entity.getBbHeight() - specialOffset), -0.1F) - specialOffset; //Gotta limit the height because otherwise frozen giants make blocks spawn like 10 blocks above them
			float dz = ((this.random.nextFloat() * (entity.getBbWidth() * 2.0F)) - entity.getBbWidth()) * 0.1F;
			stack.translate(dx, dy, dz);
			stack.scale(0.5F, 0.5F, 0.5F);
			stack.mulPose(Axis.XP.rotationDegrees(this.random.nextFloat() * 360F));
			stack.mulPose(Axis.YP.rotationDegrees(this.random.nextFloat() * 360F));
			stack.mulPose(Axis.ZP.rotationDegrees(this.random.nextFloat() * 360F));
			stack.translate(-0.5F, -0.5F, -0.5F);

			Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.ICE.defaultBlockState(), stack, buffer, light, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.translucentMovingBlock());
			stack.popPose();
		}
	}
}
