package twilightforest.client.renderer.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.item.BuiltInItemModel;
import twilightforest.enums.BossVariant;
import twilightforest.client.TFClientEvents;
import twilightforest.client.model.entity.ModelTFHydraHead;
import twilightforest.client.model.entity.ModelTFKnightPhantom2;
import twilightforest.client.model.entity.ModelTFLich;
import twilightforest.client.model.entity.ModelTFMinoshroom;
import twilightforest.client.model.entity.ModelTFNaga;
import twilightforest.client.model.armor.ModelTFPhantomArmor;
import twilightforest.client.model.entity.ModelTFQuestRam;
import twilightforest.client.model.entity.ModelTFSnowQueen;
import twilightforest.client.model.entity.ModelTFTowerBoss;
import twilightforest.tileentity.TileEntityTFTrophy;

import javax.annotation.Nullable;

public class TileEntityTFTrophyRenderer extends TileEntitySpecialRenderer<TileEntityTFTrophy> {

	public static class DummyTile extends TileEntityTFTrophy {}

	private final ModelTFHydraHead hydraHeadModel = new ModelTFHydraHead();
	private static final ResourceLocation textureLocHydra = TwilightForestMod.getModelTexture("hydra4.png");

	private final ModelTFNaga nagaHeadModel = new ModelTFNaga();
	private static final ResourceLocation textureLocNaga = TwilightForestMod.getModelTexture("nagahead.png");

	private final ModelTFLich lichModel = new ModelTFLich();
	private static final ResourceLocation textureLocLich = TwilightForestMod.getModelTexture("twilightlich64.png");

	private final ModelTFTowerBoss urGhastModel = new ModelTFTowerBoss();
	private static final ResourceLocation textureLocUrGhast = TwilightForestMod.getModelTexture("towerboss.png");

	private final ModelTFSnowQueen snowQueenModel = new ModelTFSnowQueen();
	private static final ResourceLocation textureLocSnowQueen = TwilightForestMod.getModelTexture("snowqueen.png");

	private final ModelTFMinoshroom minoshroomModel = new ModelTFMinoshroom();
	private static final ResourceLocation textureLocMinoshroom = TwilightForestMod.getModelTexture("minoshroomtaur.png");

	private final ModelTFKnightPhantom2 knightPhantomModel = new ModelTFKnightPhantom2();
	private static final ResourceLocation textureLocKnightPhantom = TwilightForestMod.getModelTexture("phantomskeleton.png");
	private final ModelTFPhantomArmor knightPhantomArmorModel = new ModelTFPhantomArmor(EntityEquipmentSlot.HEAD, 0.5F);
	private static final ResourceLocation textureLocKnightPhantomArmor = new ResourceLocation(TwilightForestMod.ARMOR_DIR + "phantom_1.png");

	private final ModelTFQuestRam questRamModel = new ModelTFQuestRam();
	private static final ResourceLocation textureLocQuestRam = TwilightForestMod.getModelTexture("questram.png");
	private static final ResourceLocation textureLocQuestRamLines = TwilightForestMod.getModelTexture("questram_lines.png");

	private final ModelResourceLocation itemModelLocation;

	public TileEntityTFTrophyRenderer() {
		this.itemModelLocation = null;
	}

	public TileEntityTFTrophyRenderer(ModelResourceLocation itemModelLocation) {
		this.itemModelLocation = itemModelLocation;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onModelBake(ModelBakeEvent event) {
		event.getModelRegistry().putObject(itemModelLocation, new BakedModel());
	}

	private class BakedModel extends BuiltInItemModel {

		BakedModel() {
			super("minecraft:blocks/soul_sand");
		}

		@Override
		protected void setItemStack(ItemStack stack) {
			TileEntityTFTrophyRenderer.this.stack = stack;
		}

		@Override
		protected void setTransform(ItemCameraTransforms.TransformType transform) {
			TileEntityTFTrophyRenderer.this.transform = transform;
		}
	}

	private ItemStack stack = ItemStack.EMPTY;
	private ItemCameraTransforms.TransformType transform = ItemCameraTransforms.TransformType.NONE;

	@Override
	public void render(@Nullable TileEntityTFTrophy trophy, double x, double y, double z, float partialTime, int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();

		if (trophy == null) {
			if (transform == ItemCameraTransforms.TransformType.GUI) {
				String modelName = BossVariant.getVariant(stack.getMetadata()).getTrophyType().getModelName();
				ModelResourceLocation trophyModelLocation = new ModelResourceLocation(TwilightForestMod.ID + ":" + modelName, "inventory");
				IBakedModel trophyModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(trophyModelLocation);

				GlStateManager.disableLighting();
				GlStateManager.translate(0.5F, 0.5F, -1.5F);
				Minecraft.getMinecraft().getRenderItem().renderItem(stack, ForgeHooksClient.handleCameraTransforms(trophyModel, transform, false));
				GlStateManager.enableLighting();
				GlStateManager.translate(-0.5F, 0.0F, 1.5F);

				//if (variant == BossVariant.QUEST_RAM)
				//	GlStateManager.translate(0.0F,0.0625F,0.0F);

				GlStateManager.rotate(30, 1F, 0F, 0F);

			} else if (transform == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND
					|| transform == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND) {
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				GlStateManager.rotate(45, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(45, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(0.40625F, 1.171875F, 0.0F);

			} else if (transform == ItemCameraTransforms.TransformType.GROUND) {
				GlStateManager.translate(0.25F, 0.3F, 0.25F);
				GlStateManager.scale(0.5F, 0.5F, 0.5F);

			} else if (transform == ItemCameraTransforms.TransformType.HEAD) {
				if (BossVariant.getVariant(stack.getMetadata()) == BossVariant.QUEST_RAM) {
					GlStateManager.scale(3F, 3F, 3F);
					GlStateManager.translate(-0.33F, -0.13F, -0.33F);
				} else {
					GlStateManager.scale(2.0F, 2.0F, 2.0F);
					GlStateManager.translate(-0.25F, 0.0F, -0.25F);
				}
			}
		}

		//int meta = trophy != null ? trophy.getBlockMetadata() & 7 : stack.getMetadata() & 7;

		float rotation = trophy != null ? (float) (trophy.getSkullRotation() * 360) / 16.0F : 0.0F;
		boolean onGround = true;

		// wall mounted?
		if (trophy != null && trophy.getBlockMetadata() != 1) {
			switch (trophy.getBlockMetadata() & 7) {
				case 2:
					onGround = false;
					break;
				case 3:
					onGround = false;
					rotation = 180.0F;
					break;
				case 4:
					onGround = false;
					rotation = 270.0F;
					break;
				case 5:
				default:
					onGround = false;
					rotation = 90.0F;
			}
		} else if (trophy == null && transform == ItemCameraTransforms.TransformType.GUI) {
			rotation = TFConfig.rotateTrophyHeadsGui ? TFClientEvents.rotationTicker : 135;
		}

		GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

		switch (BossVariant.getVariant(trophy != null ? trophy.getSkullType() : stack.getMetadata())) {
			case HYDRA:
				if (trophy == null) {
					GlStateManager.translate(0.0F, -0.25F, transform == ItemCameraTransforms.TransformType.HEAD ? -0.125F : 0.0F);
				}
				renderHydraHead(rotation, onGround && trophy != null);
				break;
			case NAGA:
				renderNagaHead(rotation, onGround);
				break;
			case LICH:
				renderLichHead(rotation, onGround);
				break;
			case UR_GHAST:
				if (trophy == null) GlStateManager.translate(0.0F, -0.5F, 0.0F);
				renderUrGhastHead(trophy, rotation, onGround, partialTime);
				break;
			case SNOW_QUEEN:
				renderSnowQueenHead(rotation, onGround);
				break;
			case MINOSHROOM:
				renderMinoshroomHead(rotation, onGround);
				break;
			case KNIGHT_PHANTOM:
				renderKnightPhantomHead(rotation, onGround);
				break;
			case QUEST_RAM:
				renderQuestRamHead(rotation, onGround);
				break;
			default:
				break;
		}

		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

	/**
	 * Render a hydra head
	 */
	private void renderHydraHead(float rotation, boolean onGround) {
		GlStateManager.scale(0.25f, 0.25f, 0.25f);

		this.bindTexture(textureLocHydra);

		GlStateManager.scale(1f, -1f, -1f);

		// we seem to be getting a 180 degree rotation here
		GlStateManager.rotate(rotation, 0F, 1F, 0F);
		GlStateManager.rotate(180F, 0F, 1F, 0F);

		GlStateManager.translate(0, onGround ? 1F : -0F, 1.5F);

		// open mouth?
		hydraHeadModel.openMouthForTrophy(onGround ? 0F : 0.25F);

		// render the hydra head
		hydraHeadModel.render(null, 0.0F, 0.0F, 0.0F, rotation, 0.0F, 0.0625F);
	}

	private void renderNagaHead(float rotation, boolean onGround) {
		GlStateManager.translate(0, -0.125F, 0);

		GlStateManager.scale(0.25f, 0.25f, 0.25f);

		this.bindTexture(textureLocNaga);

		GlStateManager.scale(1f, -1f, -1f);

		// we seem to be getting a 180 degree rotation here
		GlStateManager.rotate(rotation, 0F, 1F, 0F);
		GlStateManager.rotate(180F, 0F, 1F, 0F);

		GlStateManager.translate(0, onGround ? 1F : -0F, onGround ? 0F : 1F);

		// render the naga head
		nagaHeadModel.render(null, 0.0F, 0.0F, 0.0F, rotation, 0.0F, 0.0625F);
	}


	private void renderLichHead(float rotation, boolean onGround) {
		GlStateManager.translate(0, 1, 0);

		//GlStateManager.scale(0.5f, 0.5f, 0.5f);

		this.bindTexture(textureLocLich);

		GlStateManager.scale(1f, -1f, -1f);

		// we seem to be getting a 180 degree rotation here
		GlStateManager.rotate(rotation, 0F, 1F, 0F);
		GlStateManager.rotate(180F, 0F, 1F, 0F);

		GlStateManager.translate(0, onGround ? 1.75F : 1.5F, onGround ? 0F : 0.24F);

		// render the lich head
		lichModel.bipedHead.render(0.0625F);
		lichModel.bipedHeadwear.render(0.0625F);
	}


	private void renderUrGhastHead(@Nullable TileEntityTFTrophy trophy, float rotation, boolean onGround, float partialTime) {
		GlStateManager.translate(0, 1, 0);

		GlStateManager.scale(0.5f, 0.5f, 0.5f);

		this.bindTexture(textureLocUrGhast);

		GlStateManager.scale(1f, -1f, -1f);

		// we seem to be getting a 180 degree rotation here
		GlStateManager.rotate(rotation, 0F, 1F, 0F);
		GlStateManager.rotate(180F, 0F, 1F, 0F);

		GlStateManager.translate(0, onGround ? 1F : 1F, onGround ? 0F : 0F);

		// render the head
		urGhastModel.render(null, 0.0F, 0, trophy != null ? trophy.ticksExisted + partialTime : TFClientEvents.sineTicker + partialTime, 0, 0.0F, 0.0625F);
	}

	private void renderSnowQueenHead(float rotation, boolean onGround) {
		GlStateManager.translate(0, 1, 0);

		//GlStateManager.scale(0.5f, 0.5f, 0.5f);

		this.bindTexture(textureLocSnowQueen);

		GlStateManager.scale(1f, -1f, -1f);

		// we seem to be getting a 180 degree rotation here
		GlStateManager.rotate(rotation, 0F, 1F, 0F);
		GlStateManager.rotate(180F, 0F, 1F, 0F);

		GlStateManager.translate(0, onGround ? 1.5F : 1.25F, onGround ? 0F : 0.24F);

		// render the head
		snowQueenModel.bipedHead.render(0.0625F);
		snowQueenModel.bipedHeadwear.render(0.0625F);
	}

	private void renderMinoshroomHead(float rotation, boolean onGround) {
		GlStateManager.translate(0, 1, 0);

		//GlStateManager.scale(0.5f, 0.5f, 0.5f);

		this.bindTexture(textureLocMinoshroom);

		GlStateManager.scale(1f, -1f, -1f);

		// we seem to be getting a 180 degree rotation here
		GlStateManager.rotate(rotation, 0F, 1F, 0F);
		GlStateManager.rotate(180F, 0F, 1F, 0F);

		GlStateManager.translate(0, onGround ? 1.875F : 1.625F, onGround ? 0.5625F : 0.8125F);

		// render the head
		minoshroomModel.bipedHead.render(0.0625F);
	}

	private void renderKnightPhantomHead(float rotation, boolean onGround) {
		GlStateManager.translate(0, 1, 0);

		GlStateManager.scale(1f, -1f, -1f);

		// we seem to be getting a 180 degree rotation here
		GlStateManager.rotate(rotation, 0F, 1F, 0F);
		GlStateManager.rotate(180F, 0F, 1F, 0F);

		GlStateManager.translate(0, onGround ? 1.5F : 1.25F, onGround ? 0.0F : 0.25F);

		GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);

		// render the head
		this.bindTexture(textureLocKnightPhantomArmor);
		knightPhantomArmorModel.bipedHead.render(0.0625F);

		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);

		this.bindTexture(textureLocKnightPhantom);
		knightPhantomModel.bipedHead.render(0.0625F);
	}

	private void renderQuestRamHead(float rotation, boolean onGround) {
		if (transform == ItemCameraTransforms.TransformType.GUI)
			GlStateManager.scale(0.55f, 0.55f, 0.55f);
		else if (stack.isEmpty())
			GlStateManager.scale(0.65f, 0.65f, 0.65f);
		else
			GlStateManager.scale(0.5f, 0.5f, 0.5f);

		if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND
				|| transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
			GlStateManager.translate(0.0F, 0.5F, 0.0F);
		}

		this.bindTexture(textureLocQuestRam);

		GlStateManager.scale(1f, -1f, -1f);

		GlStateManager.rotate(rotation, 0F, 1F, 0F);
		GlStateManager.rotate(180F, 0F, 1F, 0F);

		GlStateManager.translate(0F, onGround ? 1.30F : 1.03F, onGround ? 0.765625F : 1.085F);

		// render the head
		questRamModel.head.render(0.0625F);

		GlStateManager.disableLighting();
		this.bindTexture(textureLocQuestRamLines);
		float var4 = 1.0F;
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GlStateManager.scale(1.025f, 1.025f, 1.025f);
		char var5 = 61680;
		int var6 = var5 % 65536;
		int var7 = var5 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var6 / 1.0F, (float) var7 / 1.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, var4);
		questRamModel.head.render(0.0625F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableAlpha();
		GlStateManager.enableLighting();
	}
}
