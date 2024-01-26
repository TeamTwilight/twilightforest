package twilightforest.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.joml.Vector3f;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;

import java.util.Random;

//retired loading screen, see https://twitter.com/Drullkus/status/928466334744064000 for a video example.
//1.16+ load times have gotten a lot better, and you cant really see this screen anymore.
//it also breaks in one way or another every update, so we're gonna retire it until we find another use for it.
@OnlyIn(Dist.CLIENT)
public class LoadingScreen extends Screen {

	private boolean isEntering;
	private boolean contentNeedsAssignment = false;
	private long ticks = 0L;
	private long seed;
	private BackgroundThemes backgroundTheme;
	private ItemStack item;

	private static final Random random = new Random();
	private static final float backgroundScale = 32.0F;
	private static final TFConfig.Client.LoadingScreen LOADING_SCREEN = TFConfig.CLIENT_CONFIG.LOADING_SCREEN;

	LoadingScreen() {
	    super(NarratorChatListener.NO_TITLE);
	}

	void setEntering(boolean isEntering) {
		this.isEntering = isEntering;
	}

	@Override
	protected void init() {
		this.renderables.clear();
		this.assignContent();
	}

    @Override
    public boolean isPauseScreen() {
        return false;
    }

	@Override
	public void tick() {
		if (minecraft != null && minecraft.level != null && LOADING_SCREEN.cycleLoadingScreenFrequency.get() != 0 && ++ticks % LOADING_SCREEN.cycleLoadingScreenFrequency.get() == 0)
			assignContent();

		if (this.minecraft != null && this.minecraft.player != null) {
			BlockPos blockpos = this.minecraft.player.blockPosition();
			boolean flag1 = this.minecraft.level != null && this.minecraft.level.isOutsideBuildHeight(blockpos.getY());
			if (flag1 || this.minecraft.levelRenderer.isChunkCompiled(blockpos)) {
				this.onClose();
			}
		}
	}

	@Override
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		if (this.contentNeedsAssignment) {
			this.assignContent();
			this.contentNeedsAssignment = false;
		}

		Font fontRenderer = minecraft.font;
		Window resolution = minecraft.getWindow();


		drawBackground(resolution.getGuiScaledWidth(), resolution.getGuiScaledHeight());

		PoseStack stack = RenderSystem.getModelViewStack();
		RenderSystem.applyModelViewMatrix();
		drawBouncingWobblyItem(stack, partialTicks, resolution.getGuiScaledWidth(), resolution.getGuiScaledHeight());
		RenderSystem.applyModelViewMatrix();

		String loadTitle = I18n.get(TwilightForestMod.ID + ".loading.title." + (isEntering ? "enter" : "leave"));
		ms.pushPose();
		ms.translate(
				(resolution.getGuiScaledWidth() / 2f) - (fontRenderer.width(loadTitle) / 4f),
				(resolution.getGuiScaledHeight() / 3f),
				0f
		);
		ms.translate(-(fontRenderer.width(loadTitle) / 4f), 0f, 0f);
		fontRenderer.drawShadow(ms, loadTitle, 0, 0, 0xEEEEEE); //eeeeeeeeeeeeeeeeee
		ms.popPose();
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
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

	private void drawBouncingWobblyItem(PoseStack stack, float partialTicks, float width, float height) {
		float sineTicker = (TFClientEvents.sineTicker + partialTicks) * LOADING_SCREEN.frequency.get().floatValue();
		float sineTicker2 = (TFClientEvents.sineTicker + 314f + partialTicks) * LOADING_SCREEN.frequency.get().floatValue();

		stack.pushPose();

		// Shove it!
		stack.translate(width - ((width / 30f) * LOADING_SCREEN.scale.get().floatValue()), height - (height / 10f), 0f); // Bottom right Corner

		if (LOADING_SCREEN.enable.get()) {
			// Wobble it!
			stack.mulPose(Vector3f.ZP.rotationDegrees(Mth.sin(sineTicker / LOADING_SCREEN.tiltRange.get().floatValue()) * LOADING_SCREEN.tiltConstant.get().floatValue()));

			// Bounce it!
			stack.scale(((Mth.sin(((sineTicker2 + 180F) / LOADING_SCREEN.tiltRange.get().floatValue()) * 2F) / LOADING_SCREEN.scaleDeviation.get().floatValue()) + 2F) * (LOADING_SCREEN.scale.get().floatValue() / 2F), ((Mth.sin(((sineTicker + 180F) / LOADING_SCREEN.tiltRange.get().floatValue()) * 2F) / LOADING_SCREEN.scaleDeviation.get().floatValue()) + 2F) * (LOADING_SCREEN.scale.get().floatValue() / 2F), 1F);
		}

		// Shift it!
		stack.translate(-8f, -16.5f, 0f);

		// Draw it!
		minecraft.getItemRenderer().renderAndDecorateItem(item, 0, 0);

		// Pop it!
		stack.popPose();
		// Bop it!
	}

	public enum BackgroundThemes {
		LABYRINTH(
				TwilightForestMod.prefix("textures/block/mazestone_brick.png"),
				TwilightForestMod.prefix("textures/block/mazestone_brick.png"),
				//TwilightForestMod.prefix("textures/block/mossy_mazestone.png"     ),
				TwilightForestMod.prefix("textures/block/cracked_mazestone.png")
		) {
			private final ResourceLocation mazestoneDecor = TwilightForestMod.prefix("textures/block/decorative_mazestone.png");

			@Override
			void postRenderBackground(float width, float height) {
				Tesselator tessellator = Tesselator.getInstance();
				BufferBuilder buffer = tessellator.getBuilder();
				RenderSystem.setShaderTexture(0, mazestoneDecor);

				buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
				buffer.vertex(0, 24F, 0F)
						.uv(0F, 0.75F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.vertex(width, 24F, 0F)
						.uv(width / backgroundScale, 0.75F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.vertex(width, 8F, 0F)
						.uv(width / backgroundScale, 0.25F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.vertex(0, 8F, 0)
						.uv(0F, 0.25F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				tessellator.end();

				float halfScale = backgroundScale / 2F;
				float bottomGrid = height - (height % halfScale);

				buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
				buffer.vertex(0, bottomGrid, 0F)
						.uv(0F, 0.75F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.vertex(width, bottomGrid, 0F)
						.uv(width / backgroundScale, 0.75F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.vertex(width, bottomGrid - halfScale, 0F)
						.uv(width / backgroundScale, 0.25F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.vertex(0, bottomGrid - halfScale, 0)
						.uv(0F, 0.25F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				tessellator.end();
			}
		},
		STRONGHOLD(
				TwilightForestMod.prefix("textures/block/underbrick.png"),
				TwilightForestMod.prefix("textures/block/mossy_underbrick.png"),
				TwilightForestMod.prefix("textures/block/cracked_underbrick.png")
		),
		DARKTOWER(
				TwilightForestMod.prefix("textures/block/towerwood.png"),
				TwilightForestMod.prefix("textures/block/towerwood.png"),
				TwilightForestMod.prefix("textures/block/mossy_towerwood.png"),
				TwilightForestMod.prefix("textures/block/cracked_towerwood.png"),
				TwilightForestMod.prefix("textures/block/cracked_towerwood_alt.png")
		) {
			private final ResourceLocation towerwoodEncased = TwilightForestMod.prefix("textures/block/encased_towerwood.png");

			private final float stretch = 0.985F;
            private final float depth = 1.15F;

			@Override
			void renderBackground(float width, float height) {
				final float headerDepthHeight = (backgroundScale / stretch) * depth;
				final float footerDepthHeight = height - headerDepthHeight;

				Tesselator tessellator = Tesselator.getInstance();
				BufferBuilder buffer = tessellator.getBuilder();
				RenderSystem.setShaderColor(0.9F, 0.9F, 0.9F, 1.0F);

				for (float x = backgroundScale; x < width + backgroundScale; x += backgroundScale) {
					for (float y = backgroundScale + headerDepthHeight; y < footerDepthHeight + backgroundScale; y += backgroundScale) {
						RenderSystem.setShaderTexture(0, this.getBackgroundMaterials()[random.nextInt(this.getBackgroundMaterials().length)]);
						buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
						buffer.vertex(x - backgroundScale, y, 0)
								.uv(0, 1)
								.color(0.5f, 0.5f, 0.5f, 1f)
								.endVertex();
						buffer.vertex(x, y, 0)
								.uv(1, 1)
								.color(0.5f, 0.5f, 0.5f, 1f)
								.endVertex();
						buffer.vertex(x, y - backgroundScale, 0)
								.uv(1, 0)
								.color(0.5f, 0.5f, 0.5f, 1f)
								.endVertex();
						buffer.vertex(x - backgroundScale, y - backgroundScale, 0)
								.uv(0, 0)
								.color(0.5f, 0.5f, 0.5f, 1f)
								.endVertex();
						tessellator.end();
					}
				}
			}

			@Override
			void postRenderBackground(float width, float height) {
				Tesselator tessellator = Tesselator.getInstance();
				BufferBuilder buffer = tessellator.getBuilder();
				RenderSystem.setShaderTexture(0, towerwoodEncased);

                float offset = 0.4F;
                final float textureHeaderXMin = stretch * offset;
				final float textureHeaderXMax = ((width / backgroundScale) * stretch) + offset;

				final float headerBottom = backgroundScale / stretch;
				final float headerDepthHeight = headerBottom * depth;

				final float footerTop = height - headerBottom;
				final float footerDepthHeight = height - headerDepthHeight;

				buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
				// BOTTOM VERTEXES
				buffer.vertex(0F, headerBottom, 0F)
						.uv(textureHeaderXMin, 1F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.vertex(width, headerBottom, 0F)
						.uv(textureHeaderXMax, 1F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				// TOP VERTEXES
				buffer.vertex(width, 0F, 0F)
						.uv(textureHeaderXMax, 0F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.vertex(0F, 0F, 0F)
						.uv(textureHeaderXMin, 0F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				tessellator.end();

				buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
				// BOTTOM VERTEXES
				buffer.vertex(0F, headerDepthHeight, 0F)
						.uv(0F, 1F)
						.color(0.25F, 0.25F, 0.25F, 1F)
						.endVertex();
				buffer.vertex(width, headerDepthHeight, 0F)
						.uv((width / backgroundScale), 1F)
						.color(0.25F, 0.25F, 0.25F, 1F)
						.endVertex();
				// TOP VERTEXES
				buffer.vertex(width, headerBottom, 0F)
						.uv(textureHeaderXMax, 0F)
						.color(0.25F, 0.25F, 0.25F, 1F)
						.endVertex();
				buffer.vertex(0F, headerBottom, 0F)
						.uv(textureHeaderXMin, 0F)
						.color(0.25F, 0.25F, 0.25F, 1F)
						.endVertex();
				tessellator.end();

				buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
				// BOTTOM VERTEXES
				buffer.vertex(0F, height, 0F)
						.uv(textureHeaderXMin, 1F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.vertex(width, height, 0F)
						.uv(textureHeaderXMax, 1F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				// TOP VERTEXES
				buffer.vertex(width, footerTop, 0F)
						.uv(textureHeaderXMax, 0F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				buffer.vertex(0F, footerTop, 0F)
						.uv(textureHeaderXMin, 0F)
						.color(0.5F, 0.5F, 0.5F, 1F)
						.endVertex();
				tessellator.end();

				buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
				// BOTTOM VERTEXES
				buffer.vertex(0F, footerTop, 0F)
						.uv(textureHeaderXMin, 1F)
						.color(0.75F, 0.75F, 0.75F, 1F)
						.endVertex();
				buffer.vertex(width, footerTop, 0F)
						.uv(textureHeaderXMax, 1F)
						.color(0.75F, 0.75F, 0.75F, 1F)
						.endVertex();
				// TOP VERTEXES
				buffer.vertex(width, footerDepthHeight, 0F)
						.uv(width / backgroundScale, 0F)
						.color(0.75F, 0.75F, 0.75F, 1F)
						.endVertex();
				buffer.vertex(0F, footerDepthHeight, 0F)
						.uv(0F, 0F)
						.color(0.75F, 0.75F, 0.75F, 1F)
						.endVertex();
				tessellator.end();
			}
		},
		FINALCASTLE(
				TwilightForestMod.prefix("textures/block/castle_brick.png"),
				TwilightForestMod.prefix("textures/block/castle_brick.png"),
				TwilightForestMod.prefix("textures/block/castle_brick.png"),
				TwilightForestMod.prefix("textures/block/castle_brick.png"),
				TwilightForestMod.prefix("textures/block/castle_brick.png"),
				//TwilightForestMod.prefix("textures/block/mossy_castle_brick.png"   ), // Jeez this one does not fit at ALL. Out!
				TwilightForestMod.prefix("textures/block/cracked_castle_brick.png"),
				TwilightForestMod.prefix("textures/block/worn_castle_brick.png")
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
				Tesselator tessellator = Tesselator.getInstance();
				BufferBuilder buffer = tessellator.getBuilder();

				int color = this.colors[random.nextInt(this.colors.length)];

				int r = color >> 16 & 255;
				int g = color >> 8 & 255;
				int b = color & 255;

				for (float x = backgroundScale; x < width + backgroundScale; x += backgroundScale) {
					RenderSystem.setShaderTexture(0, this.magic[random.nextInt(this.magic.length)]);
					buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
					buffer.vertex(x - backgroundScale, backgroundScale + (backgroundScale / 2), 0)
							.uv(0, 1)
							.color(r, g, b, 255)
							.endVertex();
					buffer.vertex(x, backgroundScale + (backgroundScale / 2), 0)
							.uv(1, 1)
							.color(r, g, b, 255)
							.endVertex();
					buffer.vertex(x, backgroundScale / 2, 0)
							.uv(1, 0)
							.color(r, g, b, 255)
							.endVertex();
					buffer.vertex(x - backgroundScale, backgroundScale / 2, 0)
							.uv(0, 0)
							.color(r, g, b, 255)
							.endVertex();
					tessellator.end();
				}

				for (float x = backgroundScale; x < width + backgroundScale; x += backgroundScale) {
					RenderSystem.setShaderTexture(0, this.magic[random.nextInt(this.magic.length)]);
					buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
					buffer.vertex(x - backgroundScale, height - (backgroundScale / 2), 0)
							.uv(0, 1)
							.color(r, g, b, 255)
							.endVertex();
					buffer.vertex(x, height - (backgroundScale / 2), 0)
							.uv(1, 1)
							.color(r, g, b, 255)
							.endVertex();
					buffer.vertex(x, height - backgroundScale - (backgroundScale / 2), 0)
							.uv(1, 0)
							.color(r, g, b, 255)
							.endVertex();
					buffer.vertex(x - backgroundScale, height - backgroundScale - (backgroundScale / 2), 0)
							.uv(0, 0)
							.color(r, g, b, 255)
							.endVertex();
					tessellator.end();
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
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			Tesselator tessellator = Tesselator.getInstance();
			BufferBuilder buffer = tessellator.getBuilder();
			RenderSystem.setShaderColor(0.9F, 0.9F, 0.9F, 1.0F);

			for (float x = backgroundScale; x < width + backgroundScale; x += backgroundScale) {
				for (float y = backgroundScale; y < height + backgroundScale; y += backgroundScale) {
					RenderSystem.setShaderTexture(0, this.getBackgroundMaterials()[random.nextInt(this.getBackgroundMaterials().length)]);
					buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
					buffer.vertex(x - backgroundScale, y, 0)
							.uv(0, 1)
							.color(0.5f, 0.5f, 0.5f, 1f)
							.endVertex();
					buffer.vertex(x, y, 0)
							.uv(1, 1)
							.color(0.5f, 0.5f, 0.5f, 1f)
							.endVertex();
					buffer.vertex(x, y - backgroundScale, 0)
							.uv(1, 0)
							.color(0.5f, 0.5f, 0.5f, 1f)
							.endVertex();
					buffer.vertex(x - backgroundScale, y - backgroundScale, 0)
							.uv(0, 0)
							.color(0.5f, 0.5f, 0.5f, 1f)
							.endVertex();
					tessellator.end();
				}
			}
		}

		void postRenderBackground(float width, float height) {
		}
	}
}
