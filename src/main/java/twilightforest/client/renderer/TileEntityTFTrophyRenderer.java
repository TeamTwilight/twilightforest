package twilightforest.client.renderer;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
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
import javax.annotation.ParametersAreNonnullByDefault;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;

public class TileEntityTFTrophyRenderer extends TileEntitySpecialRenderer<TileEntityTFTrophy> {
	public static class DummyTile extends TileEntityTFTrophy {}

	private ModelTFHydraHead hydraHeadModel;
	private static final ResourceLocation textureLocHydra = new ResourceLocation(TwilightForestMod.MODEL_DIR + "hydra4.png");
	private ModelTFNaga nagaHeadModel;
	private static final ResourceLocation textureLocNaga = new ResourceLocation(TwilightForestMod.MODEL_DIR + "nagahead.png");
	private ModelTFLich lichModel;
	private static final ResourceLocation textureLocLich = new ResourceLocation(TwilightForestMod.MODEL_DIR + "twilightlich64.png");
	private ModelTFTowerBoss urGhastModel;
	private static final ResourceLocation textureLocUrGhast = new ResourceLocation(TwilightForestMod.MODEL_DIR + "towerboss.png");
	private ModelTFSnowQueen snowQueenModel;
	private static final ResourceLocation textureLocSnowQueen = new ResourceLocation(TwilightForestMod.MODEL_DIR + "snowqueen.png");
	private ModelTFMinoshroom minoshroomModel;
	private static final ResourceLocation textureLocMinoshroom = new ResourceLocation(TwilightForestMod.MODEL_DIR + "minoshroomtaur.png");
	private ModelTFKnightPhantom2 knightPhantomModel;
	private static final ResourceLocation textureLocKnightPhantom = new ResourceLocation(TwilightForestMod.MODEL_DIR + "phantomskeleton.png");
	private ModelTFPhantomArmor knightPhantomArmorModel;
	private static final ResourceLocation textureLocKnightPhantomArmor = new ResourceLocation(TwilightForestMod.ARMOR_DIR + "phantom_1.png");
	private ModelTFQuestRam questRamModel;
	private static final ResourceLocation textureLocQuestRam = new ResourceLocation(TwilightForestMod.MODEL_DIR + "questram.png");
	private static final ResourceLocation textureLocQuestRamLines = new ResourceLocation(TwilightForestMod.MODEL_DIR + "questram_lines.png");

	public TileEntityTFTrophyRenderer() {
		hydraHeadModel = new ModelTFHydraHead();
		nagaHeadModel = new ModelTFNaga();
		lichModel = new ModelTFLich();
		urGhastModel = new ModelTFTowerBoss();
		snowQueenModel = new ModelTFSnowQueen();
		minoshroomModel = new ModelTFMinoshroom();
		knightPhantomModel = new ModelTFKnightPhantom2();
		knightPhantomArmorModel = new ModelTFPhantomArmor(EntityEquipmentSlot.HEAD, 0.5F);
		questRamModel = new ModelTFQuestRam();
	}

	@MethodsReturnNonnullByDefault
	@ParametersAreNonnullByDefault
	public class BakedModel implements IBakedModel {
		private class Overrides extends ItemOverrideList {
			public Overrides() {
				super(Collections.EMPTY_LIST);
			}

			@Override
			public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
				TileEntityTFTrophyRenderer.this.stack = stack;
				return BakedModel.this;
			}
		}

		@Override
		public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
			return Collections.EMPTY_LIST;
		}

		@Override
		public boolean isAmbientOcclusion() {
			return true;
		}

		@Override
		public boolean isGui3d() {
			return true;
		}

		@Override
		public boolean isBuiltInRenderer() {
			return true;
		}

		@Override
		public TextureAtlasSprite getParticleTexture() {
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/soul_sand");
		}

		@Override
		public ItemOverrideList getOverrides() {
			return new Overrides();
		}

		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
			TileEntityTFTrophyRenderer.this.transform = cameraTransformType;
			return Pair.of(this, null);
		}
	}

	public final BakedModel baked = new BakedModel();

	private ItemStack stack;
	private ItemCameraTransforms.TransformType transform;
	private IBakedModel model;

	private final IBakedModel[] trophyModels = new IBakedModel[BossVariant.TrophyType.values().length];

	@Override
	public void render(@Nullable TileEntityTFTrophy trophy, double x, double y, double z, float partialTime, int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();

		if (trophy == null) {
			if (transform == ItemCameraTransforms.TransformType.GUI) {
				BossVariant variant = BossVariant.values()[stack.getMetadata() % BossVariant.values().length];
				BossVariant.TrophyType trophyType = variant.getTrophyType();
				int trophyVariant = trophyType.ordinal();

				model = trophyModels[trophyVariant] != null ? trophyModels[trophyVariant] : (trophyModels[trophyVariant] = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(new ModelResourceLocation(TwilightForestMod.ID + ":" + trophyType.getModelName(), "inventory")));

				GlStateManager.disableLighting();
				GlStateManager.translate(0.5F, 0.5F, -1.5F);
				IBakedModel bakedModel = ForgeHooksClient.handleCameraTransforms(model, transform, transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND);
				Minecraft.getMinecraft().getRenderItem().renderItem(stack, bakedModel);
				GlStateManager.enableLighting();
				GlStateManager.translate(-0.5F, 0.0F, 1.5F);

				//if (variant == BossVariant.QUEST_RAM)
				//	GlStateManager.translate(0.0F,0.0625F,0.0F);

				GlStateManager.rotate(30, 1F, 0F, 0F);
			}

			if (transform == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND
					|| transform == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND) {
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				GlStateManager.rotate(45, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(45, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(0.40625F, 1.171875F, 0.0F);
			}

			if (transform == ItemCameraTransforms.TransformType.GROUND) {
				GlStateManager.translate(0.25F, 0.3F, 0.25F);
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
			}

			if (transform == ItemCameraTransforms.TransformType.HEAD) {
				if (BossVariant.values()[stack.getMetadata() % BossVariant.values().length] == BossVariant.QUEST_RAM) {
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

		switch (trophy != null ? BossVariant.values()[trophy.getSkullType()] : BossVariant.values()[stack.getMetadata() % BossVariant.values().length]) {
			case HYDRA:
				if (trophy == null)
					GlStateManager.translate(0.0F, -0.25F, transform == ItemCameraTransforms.TransformType.HEAD ? -0.125F : 0.0F);
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


	private void renderUrGhastHead(TileEntityTFTrophy trophy, float rotation, boolean onGround, float partialTime) {
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
		else if (stack == null)
			GlStateManager.scale(0.65f, 0.65f, 0.65f);
		else
			GlStateManager.scale(0.5f, 0.5f, 0.5f);

		if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
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
