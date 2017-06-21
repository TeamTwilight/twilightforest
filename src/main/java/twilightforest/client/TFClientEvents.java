package twilightforest.client;

import net.minecraft.block.BlockLeaves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.item.ItemTFBowBase;
import twilightforest.world.WorldProviderTwilightForest;

import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(Side.CLIENT)
public class TFClientEvents {

	private static final Random random = new Random();

	// Slowness potion uses an attribute modifier with specific UUID
	// We can detect whether an entity has slowness from the client by looking for this UUID
	private static final AttributeModifier SLOWNESS_POTION_MODIFIER =
			new AttributeModifier(UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890"), "doesntmatter", 0, 0);

	@SubscribeEvent
	public static void texStitch(TextureStitchEvent.Pre evt) {
		System.out.println("texture stitch");
		evt.getMap().registerSprite(new ResourceLocation(TwilightForestMod.ID, "items/snow_0"));
		evt.getMap().registerSprite(new ResourceLocation(TwilightForestMod.ID, "items/snow_1"));
		evt.getMap().registerSprite(new ResourceLocation(TwilightForestMod.ID, "items/snow_2"));
		evt.getMap().registerSprite(new ResourceLocation(TwilightForestMod.ID, "items/snow_3"));
	}

	/**
	 * Do ice effect on slowed monsters
	 */
	@SubscribeEvent
	public static void renderLivingPost(RenderLivingEvent.Post event) {
		boolean hasSlowness = event.getEntity().getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).hasModifier(SLOWNESS_POTION_MODIFIER);
		boolean showParticles = event.getEntity().getDataManager().get(EntityLivingBase.HIDE_PARTICLES);
		if (hasSlowness && showParticles) {
			//System.out.println("Rendering slowed entity");
			renderIcedEntity(event.getEntity(), event.getRenderer(), event.getX(), event.getY(), event.getZ());
		}
	}

	/**
	 * Alter FOV for our bows
	 */
	@SubscribeEvent
	public static void fovUpdate(FOVUpdateEvent event) {
		if (event.getEntity().isHandActive() && (event.getEntity().getHeldItem(event.getEntity().getActiveHand()).getItem() instanceof ItemTFBowBase)) {
			int i = event.getEntity().getItemInUseCount();
			float f1 = (float) i / 20.0F;

			if (f1 > 1.0F) {
				f1 = 1.0F;
			} else {
				f1 *= f1;
			}

			event.setNewfov(event.getNewfov() * (1.0F - f1 * 0.15F));
		}
	}

	/**
	 * Render an entity with the ice effect.
	 * This just displays a bunch of ice cubes around on their model
	 */
	private static void renderIcedEntity(EntityLivingBase entity, RenderLivingBase renderer, double x, double y, double z) {
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

	/**
	 * On the tick, we kill the vignette
	 */
	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		World world = mc.world;

		((BlockLeaves) TFBlocks.leaves).setGraphicsLevel(mc.gameSettings.fancyGraphics);
		((BlockLeaves) TFBlocks.leaves3).setGraphicsLevel(mc.gameSettings.fancyGraphics);
		((BlockLeaves) TFBlocks.magicLeaves).setGraphicsLevel(mc.gameSettings.fancyGraphics);

		// only fire if we're in the twilight forest
		if (world != null && (world.provider instanceof WorldProviderTwilightForest)) {
			// vignette
			if (mc.ingameGUI != null) {
				mc.ingameGUI.prevVignetteBrightness = 0.0F;
			}

		}
	}

}
