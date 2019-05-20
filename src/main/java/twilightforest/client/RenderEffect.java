package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import org.lwjgl.opengl.GL11;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.client.renderer.entity.LayerShields;
import twilightforest.entity.boss.EntityTFLich;
import twilightforest.potions.PotionFrosted;

import java.util.Random;

public enum RenderEffect {

	ICE {

		private final Random random = new Random();

		@Override
		public boolean shouldRender(EntityLivingBase entity, boolean firstPerson) {
			return !firstPerson && entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(PotionFrosted.MODIFIER_UUID) != null;
		}

		@Override
		public void render(EntityLivingBase entity, RenderLivingBase<? extends EntityLivingBase> renderer,
		                   double x, double y, double z, float partialTicks, boolean firstPerson) {

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			random.setSeed(entity.getEntityId() * entity.getEntityId() * 3121 + entity.getEntityId() * 45238971);

			// number of cubes
			int numCubes = (int) (entity.height / 0.4F);

			// make cubes
			for (int i = 0; i < numCubes; i++) {
				GlStateManager.pushMatrix();
				float dx = (float) (x + random.nextGaussian() * 0.2F * entity.width);
				float dy = (float) (y + random.nextGaussian() * 0.2F * entity.height) + entity.height / 2F;
				float dz = (float) (z + random.nextGaussian() * 0.2F * entity.width);
				GlStateManager.translate(dx, dy, dz);
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				GlStateManager.rotate(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(random.nextFloat() * 360F, 0.0F, 0.0F, 1.0F);

				Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(Blocks.ICE.getDefaultState(), 1);
				GlStateManager.popMatrix();
			}

			GlStateManager.disableBlend();
		}

	}, SHIELDS {
		private final LayerRenderer<EntityLivingBase> layer = new LayerShields();

		@Override
		public boolean shouldRender(EntityLivingBase entity, boolean firstPerson) {
			if (entity instanceof EntityTFLich) return false;
			IShieldCapability cap = entity.getCapability(CapabilityList.SHIELDS, null);
			return cap != null && cap.shieldsLeft() > 0;
		}

		@Override
		public void render(EntityLivingBase entity, RenderLivingBase<? extends EntityLivingBase> renderer,
		                   double x, double y, double z, float partialTicks, boolean firstPerson) {

			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			GlStateManager.rotate(180, 1, 0, 0);
			GlStateManager.translate(0, 0.5F - entity.getEyeHeight(), 0);
			GlStateManager.enableBlend();
			GlStateManager.disableCull();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			layer.doRenderLayer(entity, 0, 0, partialTicks, 0, 0, 0, 0.0625F);
			GlStateManager.enableCull();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	};

	static final RenderEffect[] VALUES = values();

	public boolean shouldRender(EntityLivingBase entity, boolean firstPerson) {
		return false;
	}

	public void render(EntityLivingBase entity, RenderLivingBase<? extends EntityLivingBase> renderer,
	                   double x, double y, double z, float partialTicks, boolean firstPerson) {

	}
}
