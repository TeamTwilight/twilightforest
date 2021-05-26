package twilightforest.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class GuiTwilightForestLoading extends Screen {

	private boolean isEntering;
	private boolean contentNeedsAssignment = false;
	private long lastWorldUpdateTick = 0L;
	private long seed;
	private BackgroundThemes backgroundTheme;
	private ItemStack item;

	private static final Random random = new Random();
	private static final float backgroundScale = 32.0F;
	private static final TFConfig.Client.LoadingScreen LOADING_SCREEN = TFConfig.CLIENT_CONFIG.LOADING_SCREEN;

	GuiTwilightForestLoading() {
	    super(NarratorChatListener.EMPTY);
	}

	void setEntering(boolean isEntering) {
		this.isEntering = isEntering;
	}

	@Override
	protected void init() {
		this.buttons.clear();
		this.assignContent();
	}

//	@Override
//	protected void keyTyped(char typedChar, int keyCode) {}

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		if (this.contentNeedsAssignment) {
			this.assignContent();
			this.contentNeedsAssignment = false;
		}

		if (minecraft.world != null && LOADING_SCREEN.cycleLoadingScreenFrequency.get() != 0) {
			if (lastWorldUpdateTick != minecraft.world.getGameTime() % 240000) {

				lastWorldUpdateTick = minecraft.world.getGameTime() % 240000;

				if (lastWorldUpdateTick % LOADING_SCREEN.cycleLoadingScreenFrequency.get() == 0) {
					assignContent();
				}
			}
		}

		FontRenderer fontRenderer = minecraft.fontRenderer;
		MainWindow resolution = minecraft.getMainWindow();

		drawBackground(resolution.getScaledWidth(), resolution.getScaledHeight());

		drawBouncingWobblyItem(partialTicks, resolution.getScaledWidth(), resolution.getScaledHeight());

		String loadTitle = I18n.format(TwilightForestMod.ID + ".loading.title." + (isEntering ? "enter" : "leave"));
		ms.push();
		ms.translate(
				(resolution.getScaledWidth() / 2f) - (fontRenderer.getStringWidth(loadTitle) / 4f),
				(resolution.getScaledHeight() / 3f),
				0f
		);
		ms.translate(-(fontRenderer.getStringWidth(loadTitle) / 4f), 0f, 0f);
		fontRenderer.drawStringWithShadow(ms, loadTitle, 0, 0, 0xEEEEEE); //eeeeeeeeeeeeeeeeee
		ms.pop();
		RenderSystem.color4f(1F, 1F, 1F, 1F);
	}

	private void assignContent() {
		backgroundTheme = BackgroundThemes.values()[random.nextInt(BackgroundThemes.values().length)];
		item = LOADING_SCREEN.getLoadingScreenIcons().get(random.nextInt(LOADING_SCREEN.getLoadingScreenIcons().size()));
		seed = random.nextLong();
	}

	private void drawBackground(float width, float height) {
		random.setSeed(seed);

		backgroundTheme.renderBackground(width, height);
		backgroundTheme.postRenderBackground(width, height);
	}

	private void drawBouncingWobblyItem(float partialTicks, float width, float height) {
		float sineTicker = (TFClientEvents.sineTicker + partialTicks) * LOADING_SCREEN.frequency.get().floatValue();
		float sineTicker2 = (TFClientEvents.sineTicker + 314f + partialTicks) * LOADING_SCREEN.frequency.get().floatValue();
		RenderSystem.pushMatrix();

		// Shove it!
		RenderSystem.translatef(width - ((width / 30f) * LOADING_SCREEN.scale.get().floatValue()), height - (height / 10f), 0f); // Bottom right Corner

		if (LOADING_SCREEN.enable.get()) {
			// Wobble it!
			RenderSystem.rotatef(MathHelper.sin(sineTicker / LOADING_SCREEN.tiltRange.get().floatValue()) * LOADING_SCREEN.tiltConstant.get().floatValue(), 0f, 0f, 1f);

			// Bounce it!
			RenderSystem.scalef(((MathHelper.sin(((sineTicker2 + 180F) / LOADING_SCREEN.tiltRange.get().floatValue()) * 2F) / LOADING_SCREEN.scaleDeviation.get().floatValue()) + 2F) * (LOADING_SCREEN.scale.get().floatValue() / 2F), ((MathHelper.sin(((sineTicker + 180F) / LOADING_SCREEN.tiltRange.get().floatValue()) * 2F) / LOADING_SCREEN.scaleDeviation.get().floatValue()) + 2F) * (LOADING_SCREEN.scale.get().floatValue() / 2F), 1F);
		}

		// Shift it!
		RenderSystem.translatef(-8f, -16.5f, 0f);

		// Draw it!
		minecraft.getItemRenderer().renderItemAndEffectIntoGUI(item, 0, 0);

		// Pop it!
		RenderSystem.popMatrix();
		// Bop it!
	}

	public enum BackgroundThemes {
		LABYRINTH(
				TwilightForestMod.prefix("textures/block/maze_stone_brick.png"),
				TwilightForestMod.prefix("textures/block/maze_stone_brick.png"),
				//TwilightForestMod.prefix("textures/block/maze_stone_mossy.png"     ),
				TwilightForestMod.prefix("textures/block/maze_stone_cracked.png")
		) {
			private final ResourceLocation mazestoneDecor = TwilightForestMod.prefix("textures/block/maze_stone_decorative.png");

			@Override
			void postRenderBackground(float width, float height) {
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();
				Minecraft.getInstance().getTextureManager().bindTexture(mazestoneDecor);

				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
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

				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
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
		STRONGHOLD(
				TwilightForestMod.prefix("textures/block/underbrick.png"),
				TwilightForestMod.prefix("textures/block/underbrick_mossy.png"),
				TwilightForestMod.prefix("textures/block/underbrick_cracked.png")
		),
		DARKTOWER(
				TwilightForestMod.prefix("textures/block/tower_wood.png"),
				TwilightForestMod.prefix("textures/block/tower_wood.png"),
				TwilightForestMod.prefix("textures/block/tower_wood_mossy.png"),
				TwilightForestMod.prefix("textures/block/tower_wood_cracked.png"),
				TwilightForestMod.prefix("textures/block/tower_wood_cracked_alt.png")
		) {
			private final ResourceLocation towerwoodEncased = TwilightForestMod.prefix("textures/block/tower_wood_encased.png");

			private final float stretch = 0.985F;
            private final float depth = 1.15F;

			@Override
			void renderBackground(float width, float height) {
				final float headerDepthHeight = (backgroundScale / stretch) * depth;
				final float footerDepthHeight = height - headerDepthHeight;

				RenderSystem.disableLighting();
				RenderSystem.disableFog();
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();
				RenderSystem.color4f(0.9F, 0.9F, 0.9F, 1.0F);

				for (float x = backgroundScale; x < width + backgroundScale; x += backgroundScale) {
					for (float y = backgroundScale + headerDepthHeight; y < footerDepthHeight + backgroundScale; y += backgroundScale) {
						Minecraft.getInstance().getTextureManager().bindTexture(this.getBackgroundMaterials()[random.nextInt(this.getBackgroundMaterials().length)]);
						buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
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
				Minecraft.getInstance().getTextureManager().bindTexture(towerwoodEncased);

                float offset = 0.4F;
                final float textureHeaderXMin = stretch * offset;
				final float textureHeaderXMax = ((width / backgroundScale) * stretch) + offset;

				final float headerBottom = backgroundScale / stretch;
				final float headerDepthHeight = headerBottom * depth;

				final float footerTop = height - headerBottom;
				final float footerDepthHeight = height - headerDepthHeight;

				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
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

				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
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

				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
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

				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
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
		FINALCASTLE(
				TwilightForestMod.prefix("textures/block/castle_brick.png"),
				TwilightForestMod.prefix("textures/block/castle_brick.png"),
				TwilightForestMod.prefix("textures/block/castle_brick.png"),
				TwilightForestMod.prefix("textures/block/castle_brick.png"),
				TwilightForestMod.prefix("textures/block/castle_brick.png"),
				//TwilightForestMod.prefix("textures/block/castle_brick_mossy.png"   ), // Jeez this one does not fit at ALL. Out!
				TwilightForestMod.prefix("textures/block/castle_brick_cracked.png"),
				TwilightForestMod.prefix("textures/block/castle_brick_worn.png")
		) {
			private final ResourceLocation[] magic = new ResourceLocation[]{
					TwilightForestMod.prefix("textures/block/castleblock_magic_0.png"),
					TwilightForestMod.prefix("textures/block/castleblock_magic_1.png"),
					TwilightForestMod.prefix("textures/block/castleblock_magic_2.png"),
					TwilightForestMod.prefix("textures/block/castleblock_magic_3.png"),
					TwilightForestMod.prefix("textures/block/castleblock_magic_4.png"),
					TwilightForestMod.prefix("textures/block/castleblock_magic_5.png"),
					TwilightForestMod.prefix("textures/block/castleblock_magic_6.png"),
					TwilightForestMod.prefix("textures/block/castleblock_magic_7.png")
			};

			private final int[] colors = new int[]{0xFF00FF, 0x00FFFF, 0xFFFF00, 0x4B0082};

			@Override
			void postRenderBackground(float width, float height) {
				RenderSystem.disableLighting();
				RenderSystem.disableFog();
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();

				int color = this.colors[random.nextInt(this.colors.length)];

				int r = color >> 16 & 255;
				int g = color >> 8 & 255;
				int b = color & 255;

				for (float x = backgroundScale; x < width + backgroundScale; x += backgroundScale) {
					Minecraft.getInstance().getTextureManager().bindTexture(this.magic[random.nextInt(this.magic.length)]);
					buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
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
					Minecraft.getInstance().getTextureManager().bindTexture(this.magic[random.nextInt(this.magic.length)]);
					buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
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

		BackgroundThemes(ResourceLocation... backgroundMaterials) {
			this.backgroundMaterials = backgroundMaterials;
		}

		ResourceLocation[] getBackgroundMaterials() {
			return backgroundMaterials;
		}

		void renderBackground(float width, float height) {
			RenderSystem.disableLighting();
			RenderSystem.disableFog();
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			RenderSystem.color4f(0.9F, 0.9F, 0.9F, 1.0F);

			for (float x = backgroundScale; x < width + backgroundScale; x += backgroundScale) {
				for (float y = backgroundScale; y < height + backgroundScale; y += backgroundScale) {
					Minecraft.getInstance().getTextureManager().bindTexture(this.getBackgroundMaterials()[random.nextInt(this.getBackgroundMaterials().length)]);
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
