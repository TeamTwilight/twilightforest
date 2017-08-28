package twilightforest.client.renderer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.block.enums.BossVariant;
import twilightforest.client.model.ModelTFHydraHead;
import twilightforest.client.model.ModelTFLich;
import twilightforest.client.model.ModelTFNaga;
import twilightforest.client.model.ModelTFSnowQueen;
import twilightforest.client.model.ModelTFTowerBoss;
import twilightforest.tileentity.TileEntityTFTrophy;


public class TileEntityTFTrophyRenderer extends TileEntitySpecialRenderer<TileEntityTFTrophy> {

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

	public TileEntityTFTrophyRenderer() {
		hydraHeadModel = new ModelTFHydraHead();
		nagaHeadModel = new ModelTFNaga();
		lichModel = new ModelTFLich();
		urGhastModel = new ModelTFTowerBoss();
		snowQueenModel = new ModelTFSnowQueen();
	}


	@Override
	public void render(TileEntityTFTrophy trophy, double x, double y, double z, float partialTime, int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();

		int meta = trophy.getBlockMetadata() & 7;

		float rotation = (float) (trophy.getSkullRotation() * 360) / 16.0F;
		boolean onGround = true;

		// wall mounted?
		if (meta != 1) {
			switch (meta) {
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
		}


		GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);


		switch (BossVariant.values()[trophy.getSkullType()]) {
			case HYDRA:
				renderHydraHead(rotation, onGround);
				break;
			case NAGA:
				renderNagaHead(rotation, onGround);
				break;
			case LICH:
				renderLichHead(rotation, onGround);
				break;
			case UR_GHAST:
				renderUrGhastHead(trophy, rotation, onGround, partialTime);
				break;
			case SNOW_QUEEN:
				renderSnowQueenHead(rotation, onGround);
				break;
			default:
				break;
		}

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
		hydraHeadModel.render((Entity) null, 0.0F, 0.0F, 0.0F, rotation, 0.0F, 0.0625F);
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
		nagaHeadModel.render((Entity) null, 0.0F, 0.0F, 0.0F, rotation, 0.0F, 0.0625F);
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

		// render the naga head
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

		// render the naga head
		urGhastModel.render((Entity) null, 0.0F, 0, trophy.ticksExisted + partialTime, 0, 0.0F, 0.0625F);
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

		// render the naga head
		snowQueenModel.bipedHead.render(0.0625F);
		snowQueenModel.bipedHeadwear.render(0.0625F);
	}

}
