package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.apache.commons.lang3.ArrayUtils;
import twilightforest.TwilightForestMod;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.entity.boss.EntityTFLich;

import java.util.Random;

public class LayerShields<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {

	public static final ModelResourceLocation LOC = new ModelResourceLocation(new ResourceLocation(TwilightForestMod.ID, "shield"), "inventory");
	private static final Direction[] DIRS = ArrayUtils.add(Direction.values(), null);
	private static final Random RAND = new Random();

	public LayerShields(IEntityRenderer<T, M> renderer) {
		super(renderer);
	}

	private int getShieldCount(T entity) {
		return entity instanceof EntityTFLich
						? ((EntityTFLich) entity).getShieldStrength()
						: entity.getCapability(CapabilityList.SHIELDS).map(IShieldCapability::shieldsLeft).orElse(0);
	}

	private void renderShields(MatrixStack stack, IRenderTypeBuffer buffer, T entity, float partialTicks) {
		float age = entity.ticksExisted + partialTicks;
		float rotateAngleY = age / 5.0F;
		float rotateAngleX = MathHelper.sin(age / 5.0F) / 4.0F;
		float rotateAngleZ = MathHelper.cos(age / 5.0F) / 4.0F;

		int count = getShieldCount(entity);
		for (int c = 0; c < count; c++) {
			stack.push();

			// shift to the torso
			stack.translate(-0.5, 0.5, -0.5);

			// invert Y
			stack.scale(1, -1, 1);

			// perform the rotations, accounting for the fact that baked models are corner-based
			stack.translate(0.5, 0.5, 0.5);
			stack.rotate(Vector3f.ZP.rotationDegrees(rotateAngleZ * (180F / (float) Math.PI)));
			stack.rotate(Vector3f.YP.rotationDegrees(rotateAngleY * (180F / (float) Math.PI) + (c * (360F / count))));
			stack.rotate(Vector3f.XP.rotationDegrees(rotateAngleX * (180F / (float) Math.PI)));
			stack.translate(-0.5, -0.5, -0.5);

			// push the shields outwards from the center of rotation
			stack.translate(0F, 0F, -0.7F);

			IBakedModel model = Minecraft.getInstance().getModelManager().getModel(LOC);
			for (Direction dir : DIRS) {
				RAND.setSeed(42L);
				Minecraft.getInstance().getItemRenderer().renderQuads(
						stack,
						buffer.getBuffer(Atlases.getTranslucentCullBlockType()),
						model.getQuads(null, dir, RAND, EmptyModelData.INSTANCE),
						ItemStack.EMPTY,
						0xF000F0,
						OverlayTexture.NO_OVERLAY
				);
			}

			stack.pop();
		}
	}

	@Override
	public void render(MatrixStack stack, IRenderTypeBuffer buffer, int light, T living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (getShieldCount(living) > 0) {
			renderShields(stack, buffer, living, partialTicks);
		}
	}
}
