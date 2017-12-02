package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;

import java.io.IOException;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class GuiTwilightForestLoading extends GuiScreen {

	private Minecraft client = FMLClientHandler.instance().getClient();
	private boolean isEntering;
	private boolean contentNeedsAssignment = false;
	private long lastWorldUpdateTick = 0L;
	private long seed;
	private static Random random = new Random();
	private BackgroundThemes backgroundTheme;
	private ItemStack item;
	private static final float backgroundScale = 32.0F;

	GuiTwilightForestLoading() {

	}

	void setEntering(boolean isEntering) {
		this.isEntering = isEntering;
	}

	@Override
	public void initGui() {
		this.buttonList.clear();
		this.assignContent();
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (this.contentNeedsAssignment) {
			this.assignContent();
			this.contentNeedsAssignment = false;
		}

		if (mc.world != null && TFConfig.loadingScreen.cycleLoadingScreenFrequency != 0) {
			if (lastWorldUpdateTick != mc.world.getTotalWorldTime() % 240000) {

				lastWorldUpdateTick = mc.world.getTotalWorldTime() % 240000;

				if (lastWorldUpdateTick % TFConfig.loadingScreen.cycleLoadingScreenFrequency == 0) {
					assignContent();
				}
			}
		}

		FontRenderer fontRenderer = mc.fontRenderer;
		ScaledResolution resolution = new ScaledResolution(client);

		drawBackground(resolution.getScaledWidth(), resolution.getScaledHeight());

		drawBouncingWobblyItem(partialTicks, resolution.getScaledWidth(), resolution.getScaledHeight());

		String loadTitle = I18n.translateToLocal(TwilightForestMod.ID + ".loading.title." + (isEntering ? "enter" : "leave"));
		GlStateManager.pushMatrix();
		GlStateManager.translate(
				((resolution.getScaledWidth()) / 2) - (fontRenderer.getStringWidth(loadTitle) / 4),
				(resolution.getScaledHeight() / 3),
				0
		);
		GlStateManager.translate(-(fontRenderer.getStringWidth(loadTitle) / 4), 0, 0);
		fontRenderer.drawStringWithShadow(loadTitle, 0, 0, 0xEEEEEE); //eeeeeeeeeeeeeeeeee
		GlStateManager.popMatrix();
		GlStateManager.color(1F, 1F, 1F, 1F);
	}

	private void assignContent() {
		backgroundTheme = BackgroundThemes.values()[random.nextInt(BackgroundThemes.values().length)];
		item = TFConfig.loadingScreen.getLoadingScreenIcons().get(random.nextInt(TFConfig.loadingScreen.getLoadingScreenIcons().size()));
		seed = random.nextLong();
	}

	private void drawBackground(float width, float height) {
		random.setSeed(seed);

		backgroundTheme.renderBackground(width, height);
		backgroundTheme.postRenderBackground(width, height);
	}

	private void drawBouncingWobblyItem(float partialTicks, float width, float height) {
		float sineTicker = (TFClientEvents.sineTicker + partialTicks) * TFConfig.loadingScreen.frequency;
		float sineTicker2 = (TFClientEvents.sineTicker + 314 + partialTicks) * TFConfig.loadingScreen.frequency;
		GlStateManager.pushMatrix();

		// Shove it!
		GlStateManager.translate(width - ((width / 30) * TFConfig.loadingScreen.scale), height - (height / 10), 0); // Bottom right Corner

		if (TFConfig.loadingScreen.enable) {
			// Wobble it!
			GlStateManager.rotate((float) Math.sin(sineTicker / TFConfig.loadingScreen.tiltRange) * TFConfig.loadingScreen.tiltConstant, 0, 0, 1);

			// Bounce it!
			GlStateManager.scale(((Math.sin(((sineTicker2 + 180F) / TFConfig.loadingScreen.tiltRange) * 2F) / TFConfig.loadingScreen.scaleDeviation) + 2F) * (TFConfig.loadingScreen.scale / 2), ((Math.sin(((sineTicker + 180F) / TFConfig.loadingScreen.tiltRange) * 2F) / TFConfig.loadingScreen.scaleDeviation) + 2F) * (TFConfig.loadingScreen.scale / 2), 1F);
		}

		// Shift it!
		GlStateManager.translate(-8, -16.5, 0);

		RenderHelper.enableGUIStandardItemLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0x20, 0x20);
		// Draw it!
		mc.getRenderItem().renderItemAndEffectIntoGUI(item, 0, 0);
		RenderHelper.disableStandardItemLighting();

		// Pop it!
		GlStateManager.popMatrix();
		// Bop it!
	}

	public enum BackgroundThemes {
		LABYRINTH(new ResourceLocation[]{
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/mazestone_brick.png"),
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/mazestone_brick.png"),
				//new ResourceLocation(TwilightForestMod.ID, "textures/blocks/mazestone_mossy.png"     ),
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/mazestone_cracked.png")
		}) {
			private final ResourceLocation mazestoneDecor = new ResourceLocation(TwilightForestMod.ID, "textures/blocks/mazestone_decorative.png");

			@Override
			void postRenderBackground(float width, float height) {
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();
				Minecraft.getMinecraft().getTextureManager().bindTexture(mazestoneDecor);

				buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				buffer.pos(0, 24F, 0F)
						.tex(0F, 0.75F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.pos(width, 24F, 0F)
						.tex(width / backgroundScale, 0.75F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.pos(width, 8F, 0F)
						.tex(width / backgroundScale, 0.25F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.pos(0, 8F, 0)
						.tex(0F, 0.25F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				tessellator.draw();

				float halfScale = backgroundScale / 2F;
				float bottomGrid = height - (height % halfScale);

				buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				buffer.pos(0, bottomGrid, 0F)
						.tex(0F, 0.75F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.pos(width, bottomGrid, 0F)
						.tex(width / backgroundScale, 0.75F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.pos(width, bottomGrid - halfScale, 0F)
						.tex(width / backgroundScale, 0.25F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.pos(0, bottomGrid - halfScale, 0)
						.tex(0F, 0.25F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				tessellator.draw();
			}
		},
		STRONGHOLD(new ResourceLocation[]{
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/knightbrick.png"),
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/knightbrick_mossy.png"),
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/knightbrick_cracked.png")
		}),
		DARKTOWER(new ResourceLocation[]{
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/towerwood_planks.png"),
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/towerwood_planks.png"),
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/towerwood_mossy.png"),
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/towerwood_cracked.png"),
				//new ResourceLocation(TwilightForestMod.ID, "textures/blocks/towerwood_infested.png"  ),
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/towerwood_alt.png")
		}) {
			private final ResourceLocation towerwoodEncased = new ResourceLocation(TwilightForestMod.ID, "textures/blocks/towerwood_encased.png");

			private final float stretch = 0.985F;
			private final float offset = 0.4F;
			private final float depth = 1.15F;

			@Override
			void renderBackground(float width, float height) {
				final float headerDepthHeight = (backgroundScale / stretch) * depth;
				final float footerDepthHeight = height - headerDepthHeight;

				GlStateManager.disableLighting();
				GlStateManager.disableFog();
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();
				GlStateManager.color(0.9F, 0.9F, 0.9F, 1.0F);

				for (float x = backgroundScale; x < width + backgroundScale; x += backgroundScale) {
					for (float y = backgroundScale + headerDepthHeight; y < footerDepthHeight + backgroundScale; y += backgroundScale) {
						Minecraft.getMinecraft().getTextureManager().bindTexture(this.getBackgroundMaterials()[random.nextInt(this.getBackgroundMaterials().length)]);
						buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
						buffer.pos(x - backgroundScale, y, 0)
								.tex(0, 1)
								.color(0.5f, 0.5f, 0.5f, 1f)
								.endVertex();
						buffer.pos(x, y, 0)
								.tex(1, 1)
								.color(0.5f, 0.5f, 0.5f, 1f)
								.endVertex();
						buffer.pos(x, y - backgroundScale, 0)
								.tex(1, 0)
								.color(0.5f, 0.5f, 0.5f, 1f)
								.endVertex();
						buffer.pos(x - backgroundScale, y - backgroundScale, 0)
								.tex(0, 0)
								.color(0.5f, 0.5f, 0.5f, 1f)
								.endVertex();
						tessellator.draw();
					}
				}
			}

			@Override
			void postRenderBackground(float width, float height) {
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();
				Minecraft.getMinecraft().getTextureManager().bindTexture(towerwoodEncased);

				final float textureHeaderXMin = stretch * offset;
				final float textureHeaderXMax = ((width / backgroundScale) * stretch) + offset;

				final float headerBottom = backgroundScale / stretch;
				final float headerDepthHeight = headerBottom * depth;

				final float footerTop = height - headerBottom;
				final float footerDepthHeight = height - headerDepthHeight;

				buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				// BOTTOM VERTEXES
				buffer.pos(0F, headerBottom, 0F)
						.tex(textureHeaderXMin, 1F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.pos(width, headerBottom, 0F)
						.tex(textureHeaderXMax, 1F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				// TOP VERTEXES
				buffer.pos(width, 0F, 0F)
						.tex(textureHeaderXMax, 0F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.pos(0F, 0F, 0F)
						.tex(textureHeaderXMin, 0F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				tessellator.draw();

				buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				// BOTTOM VERTEXES
				buffer.pos(0F, headerDepthHeight, 0F)
						.tex(0F, 1F)
						.color(0.25F, 0.25F, 0.25F, 1F)
						.endVertex();
				buffer.pos(width, headerDepthHeight, 0F)
						.tex((width / backgroundScale), 1F)
						.color(0.25F, 0.25F, 0.25F, 1F)
						.endVertex();
				// TOP VERTEXES
				buffer.pos(width, headerBottom, 0F)
						.tex(textureHeaderXMax, 0F)
						.color(0.25F, 0.25F, 0.25F, 1F)
						.endVertex();
				buffer.pos(0F, headerBottom, 0F)
						.tex(textureHeaderXMin, 0F)
						.color(0.25F, 0.25F, 0.25F, 1F)
						.endVertex();
				tessellator.draw();

				buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				// BOTTOM VERTEXES
				buffer.pos(0F, height, 0F)
						.tex(textureHeaderXMin, 1F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.pos(width, height, 0F)
						.tex(textureHeaderXMax, 1F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				// TOP VERTEXES
				buffer.pos(width, footerTop, 0F)
						.tex(textureHeaderXMax, 0F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.pos(0F, footerTop, 0F)
						.tex(textureHeaderXMin, 0F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				tessellator.draw();

				buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				// BOTTOM VERTEXES
				buffer.pos(0F, footerTop, 0F)
						.tex(textureHeaderXMin, 1F)
						.color(0.75F, 0.75F, 0.75F, 1F)
						.endVertex();
				buffer.pos(width, footerTop, 0F)
						.tex(textureHeaderXMax, 1F)
						.color(0.75F, 0.75F, 0.75F, 1F)
						.endVertex();
				// TOP VERTEXES
				buffer.pos(width, footerDepthHeight, 0F)
						.tex(width / backgroundScale, 0F)
						.color(0.75F, 0.75F, 0.75F, 1F)
						.endVertex();
				buffer.pos(0F, footerDepthHeight, 0F)
						.tex(0F, 0F)
						.color(0.75F, 0.75F, 0.75F, 1F)
						.endVertex();
				tessellator.draw();
			}
		},
		FINALCASTLE(new ResourceLocation[]{
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/castleblock_brick.png"),
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/castleblock_brick.png"),
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/castleblock_brick.png"),
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/castleblock_brick.png"),
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/castleblock_brick.png"),
				//new ResourceLocation(TwilightForestMod.ID, "textures/blocks/castleblock_mossy.png"   ), // Jeez this one does not fit at ALL. Out!
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/castleblock_cracked.png"),
				new ResourceLocation(TwilightForestMod.ID, "textures/blocks/castleblock_faded.png")
		}) {
			private final ResourceLocation[] magic = new ResourceLocation[]{
					new ResourceLocation(TwilightForestMod.ID, "textures/blocks/castleblock_magic_0.png"),
					new ResourceLocation(TwilightForestMod.ID, "textures/blocks/castleblock_magic_1.png"),
					new ResourceLocation(TwilightForestMod.ID, "textures/blocks/castleblock_magic_2.png"),
					new ResourceLocation(TwilightForestMod.ID, "textures/blocks/castleblock_magic_3.png"),
					new ResourceLocation(TwilightForestMod.ID, "textures/blocks/castleblock_magic_4.png"),
					new ResourceLocation(TwilightForestMod.ID, "textures/blocks/castleblock_magic_5.png"),
					new ResourceLocation(TwilightForestMod.ID, "textures/blocks/castleblock_magic_6.png"),
					new ResourceLocation(TwilightForestMod.ID, "textures/blocks/castleblock_magic_7.png")
			};

			private final int[] colors = new int[]{0xFF00FF, 0x00FFFF, 0xFFFF00, 0x4B0082};

			@Override
			void postRenderBackground(float width, float height) {
				GlStateManager.disableLighting();
				GlStateManager.disableFog();
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();

				int color = this.colors[random.nextInt(this.colors.length)];

				int r = color >> 16 & 255;
				int g = color >> 8 & 255;
				int b = color & 255;

				for (float x = backgroundScale; x < width + backgroundScale; x += backgroundScale) {
					Minecraft.getMinecraft().getTextureManager().bindTexture(this.magic[random.nextInt(this.magic.length)]);
					buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
					buffer.pos(x - backgroundScale, backgroundScale + (backgroundScale / 2), 0)
							.tex(0, 1)
							.color(r, g, b, 255)
							.endVertex();
					buffer.pos(x, backgroundScale + (backgroundScale / 2), 0)
							.tex(1, 1)
							.color(r, g, b, 255)
							.endVertex();
					buffer.pos(x, backgroundScale / 2, 0)
							.tex(1, 0)
							.color(r, g, b, 255)
							.endVertex();
					buffer.pos(x - backgroundScale, backgroundScale / 2, 0)
							.tex(0, 0)
							.color(r, g, b, 255)
							.endVertex();
					tessellator.draw();
				}

				for (float x = backgroundScale; x < width + backgroundScale; x += backgroundScale) {
					Minecraft.getMinecraft().getTextureManager().bindTexture(this.magic[random.nextInt(this.magic.length)]);
					buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
					buffer.pos(x - backgroundScale, height - (backgroundScale / 2), 0)
							.tex(0, 1)
							.color(r, g, b, 255)
							.endVertex();
					buffer.pos(x, height - (backgroundScale / 2), 0)
							.tex(1, 1)
							.color(r, g, b, 255)
							.endVertex();
					buffer.pos(x, height - backgroundScale - (backgroundScale / 2), 0)
							.tex(1, 0)
							.color(r, g, b, 255)
							.endVertex();
					buffer.pos(x - backgroundScale, height - backgroundScale - (backgroundScale / 2), 0)
							.tex(0, 0)
							.color(r, g, b, 255)
							.endVertex();
					tessellator.draw();
				}
			}
		};

		private final ResourceLocation[] backgroundMaterials;

		BackgroundThemes(ResourceLocation[] backgroundMaterials) {
			this.backgroundMaterials = backgroundMaterials;
		}

		ResourceLocation[] getBackgroundMaterials() {
			return backgroundMaterials;
		}

		void renderBackground(float width, float height) {
			GlStateManager.disableLighting();
			GlStateManager.disableFog();
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			GlStateManager.color(0.9F, 0.9F, 0.9F, 1.0F);

			for (float x = backgroundScale; x < width + backgroundScale; x += backgroundScale) {
				for (float y = backgroundScale; y < height + backgroundScale; y += backgroundScale) {
					Minecraft.getMinecraft().getTextureManager().bindTexture(this.getBackgroundMaterials()[random.nextInt(this.getBackgroundMaterials().length)]);
					buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
					buffer.pos(x - backgroundScale, y, 0)
							.tex(0, 1)
							.color(0.5f, 0.5f, 0.5f, 1f)
							.endVertex();
					buffer.pos(x, y, 0)
							.tex(1, 1)
							.color(0.5f, 0.5f, 0.5f, 1f)
							.endVertex();
					buffer.pos(x, y - backgroundScale, 0)
							.tex(1, 0)
							.color(0.5f, 0.5f, 0.5f, 1f)
							.endVertex();
					buffer.pos(x - backgroundScale, y - backgroundScale, 0)
							.tex(0, 0)
							.color(0.5f, 0.5f, 0.5f, 1f)
							.endVertex();
					tessellator.draw();
				}
			}
		}

		void postRenderBackground(float width, float height) {
		}
	}
}
