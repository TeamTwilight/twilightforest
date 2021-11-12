package twilightforest.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.LevelRenderer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.IWeatherRenderHandler;
import twilightforest.TwilightForestMod;
import twilightforest.world.registration.TFGenerationSettings;
import twilightforest.world.registration.biomes.BiomeKeys;

import java.util.Random;

/**
 * Copypasta of EntityRenderer.renderRainSnow() hacked to include progression environmental effects
 */
public class TFWeatherRenderer implements IWeatherRenderHandler {

	private static final ResourceLocation RAIN_TEXTURES = new ResourceLocation("textures/environment/rain.png");
	private static final ResourceLocation SNOW_TEXTURES = new ResourceLocation("textures/environment/snow.png");

	private static final ResourceLocation SPARKLES_TEXTURE = TwilightForestMod.getEnvTexture("sparkles.png");

	private final float[] rainxs = new float[1024];
	private final float[] rainzs = new float[1024];

	private int rendererUpdateCount;
	private BoundingBox protectedBox;

	private final Random random = new Random();

	public TFWeatherRenderer() {
		for (int i = 0; i < 32; ++i) {
			for (int j = 0; j < 32; ++j) {
				float f  = j - 16;
				float f1 = i - 16;
				float f2 = Mth.sqrt(f * f + f1 * f1);
				this.rainxs[i << 5 | j] = -f1 / f2;
				this.rainzs[i << 5 | j] =   f / f2;
			}
		}
	}

	public void tick() {
		++this.rendererUpdateCount;
	}

	//Helpful tip: x, y, and z relate to the looking entity's position.
	@Override
	public void render(int ticks, float partialTicks, ClientLevel world, Minecraft mc, LightTexture lightmap, double xIn, double yIn, double zIn) {
		// do normal weather rendering
		renderNormalWeather(lightmap, world, mc, partialTicks, xIn, yIn, zIn);

		if (TFGenerationSettings.isProgressionEnforced(world) && !mc.player.isCreative() && !mc.player.isSpectator()) {
			// locked biome weather effects
			renderLockedBiome(partialTicks, world, mc, lightmap, xIn, yIn, zIn);

			// locked structures
			renderLockedStructure(partialTicks, mc, lightmap, xIn, yIn, zIn);
		}
	}

	// [VanillaCopy] exact of WorldRenderer.renderRainSnow
	private void renderNormalWeather(LightTexture lightmap, ClientLevel world, Minecraft mc, float ticks, double x, double y, double z) {

		float f = Minecraft.getInstance().level.getRainLevel(ticks);
		if (!(f <= 0.0F)) {
			lightmap.turnOnLightLayer();
			Level level = Minecraft.getInstance().level;
			int i = Mth.floor(x);
			int j = Mth.floor(y);
			int k = Mth.floor(z);
			Tesselator tesselator = Tesselator.getInstance();
			BufferBuilder bufferbuilder = tesselator.getBuilder();
			RenderSystem.disableCull();
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();
			int l = 5;
			if (Minecraft.useFancyGraphics()) {
				l = 10;
			}

			RenderSystem.depthMask(Minecraft.useShaderTransparency());
			int i1 = -1;
			float f1 = (float)rendererUpdateCount + ticks;
			RenderSystem.setShader(GameRenderer::getParticleShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

			for(int j1 = k - l; j1 <= k + l; ++j1) {
				for(int k1 = i - l; k1 <= i + l; ++k1) {
					int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
					double d0 = (double)this.rainxs[l1] * 0.5D;
					double d1 = (double)this.rainzs[l1] * 0.5D;
					blockpos$mutableblockpos.set(k1, 0, j1);
					Biome biome = level.getBiome(blockpos$mutableblockpos);
					if (biome.getPrecipitation() != Biome.Precipitation.NONE) {
						int i2 = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos$mutableblockpos).getY();
						int j2 = j - l;
						int k2 = j + l;
						if (j2 < i2) {
							j2 = i2;
						}

						if (k2 < i2) {
							k2 = i2;
						}

						int l2 = i2;
						if (i2 < j) {
							l2 = j;
						}

						if (j2 != k2) {
							Random random = new Random((long) k1 * k1 * 3121 + k1 * 45238971L ^ (long) j1 * j1 * 418711 + j1 * 13761L);
							blockpos$mutableblockpos.set(k1, j2, j1);
							float f2 = biome.getTemperature(blockpos$mutableblockpos);
							if (f2 >= 0.15F) {
								if (i1 != 0) {
									if (i1 >= 0) {
										tesselator.end();
									}

									i1 = 0;
									RenderSystem.setShaderTexture(0, RAIN_TEXTURES);
									bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
								}

								int i3 = rendererUpdateCount + k1 * k1 * 3121 + k1 * 45238971 + j1 * j1 * 418711 + j1 * 13761 & 31;
								float f3 = -((float)i3 + ticks) / 32.0F * (3.0F + random.nextFloat());
								double d2 = (double)k1 + 0.5D - x;
								double d4 = (double)j1 + 0.5D - z;
								float f4 = (float)Math.sqrt(d2 * d2 + d4 * d4) / (float)l;
								float f5 = ((1.0F - f4 * f4) * 0.5F + 0.5F) * f;
								blockpos$mutableblockpos.set(k1, l2, j1);
								int j3 = LevelRenderer.getLightColor(level, blockpos$mutableblockpos);
								bufferbuilder.vertex((double)k1 - x - d0 + 0.5D, (double)k2 - y, (double)j1 - z - d1 + 0.5D).uv(0.0F, (float)j2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).uv2(j3).endVertex();
								bufferbuilder.vertex((double)k1 - x + d0 + 0.5D, (double)k2 - y, (double)j1 - z + d1 + 0.5D).uv(1.0F, (float)j2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).uv2(j3).endVertex();
								bufferbuilder.vertex((double)k1 - x + d0 + 0.5D, (double)j2 - y, (double)j1 - z + d1 + 0.5D).uv(1.0F, (float)k2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).uv2(j3).endVertex();
								bufferbuilder.vertex((double)k1 - x - d0 + 0.5D, (double)j2 - y, (double)j1 - z - d1 + 0.5D).uv(0.0F, (float)k2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).uv2(j3).endVertex();
							} else {
								if (i1 != 1) {
									if (i1 >= 0) {
										tesselator.end();
									}

									i1 = 1;
									RenderSystem.setShaderTexture(0, SNOW_TEXTURES);
									bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
								}

								float f6 = -((float)(rendererUpdateCount & 511) + ticks) / 512.0F;
								float f7 = (float)(random.nextDouble() + (double)f1 * 0.01D * (double)((float)random.nextGaussian()));
								float f8 = (float)(random.nextDouble() + (double)(f1 * (float)random.nextGaussian()) * 0.001D);
								double d3 = (double)k1 + 0.5D - x;
								double d5 = (double)j1 + 0.5D - z;
								float f9 = (float)Math.sqrt(d3 * d3 + d5 * d5) / (float)l;
								float f10 = ((1.0F - f9 * f9) * 0.3F + 0.5F) * f;
								blockpos$mutableblockpos.set(k1, l2, j1);
								int k3 = LevelRenderer.getLightColor(level, blockpos$mutableblockpos);
								int l3 = k3 >> 16 & '\uffff';
								int i4 = k3 & '\uffff';
								int j4 = (l3 * 3 + 240) / 4;
								int k4 = (i4 * 3 + 240) / 4;
								bufferbuilder.vertex((double)k1 - x - d0 + 0.5D, (double)k2 - y, (double)j1 - z - d1 + 0.5D).uv(0.0F + f7, (float)j2 * 0.25F + f6 + f8).color(1.0F, 1.0F, 1.0F, f10).uv2(k4, j4).endVertex();
								bufferbuilder.vertex((double)k1 - x + d0 + 0.5D, (double)k2 - y, (double)j1 - z + d1 + 0.5D).uv(1.0F + f7, (float)j2 * 0.25F + f6 + f8).color(1.0F, 1.0F, 1.0F, f10).uv2(k4, j4).endVertex();
								bufferbuilder.vertex((double)k1 - x + d0 + 0.5D, (double)j2 - y, (double)j1 - z + d1 + 0.5D).uv(1.0F + f7, (float)k2 * 0.25F + f6 + f8).color(1.0F, 1.0F, 1.0F, f10).uv2(k4, j4).endVertex();
								bufferbuilder.vertex((double)k1 - x - d0 + 0.5D, (double)j2 - y, (double)j1 - z - d1 + 0.5D).uv(0.0F + f7, (float)k2 * 0.25F + f6 + f8).color(1.0F, 1.0F, 1.0F, f10).uv2(k4, j4).endVertex();
							}
						}
					}
				}
			}

			if (i1 >= 0) {
				tesselator.end();
			}

			RenderSystem.enableCull();
			RenderSystem.disableBlend();
			lightmap.turnOffLightLayer();
		}
	}

	// [VanillaCopy] inside of EntityRenderer.renderRainSnow, edits noted
	private void renderLockedBiome(float partialTicks, ClientLevel wc, Minecraft mc, LightTexture lightmap, double xIn, double yIn, double zIn) {
		// check nearby for locked biome
		if (isNearLockedBiome(wc, mc.getCameraEntity())) {

			lightmap.turnOnLightLayer();
			Entity entity = mc.getCameraEntity();
			Level world = mc.level;

			int x0 = Mth.floor(xIn);
			int y0 = Mth.floor(yIn);
			int z0 = Mth.floor(zIn);

			Tesselator tessellator = Tesselator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuilder();

			RenderSystem.disableCull();
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();

			int range = 5;

			if (Minecraft.useFancyGraphics()) {
				range = 10;
			}

			RenderSystem.depthMask(Minecraft.useShaderTransparency());

			RenderType currentType = null;
			float combinedTicks = this.rendererUpdateCount + partialTicks;
			//bufferbuilder.setTranslation(-dx, -dy, -dz);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

			for (int z = z0 - range; z <= z0 + range; ++z) {
				for (int x = x0 - range; x <= x0 + range; ++x) {

					int idx = (z - z0 + 16) * 32 + x - x0 + 16;
					double rx = this.rainxs[idx] * 0.5D;
					double ry = this.rainzs[idx] * 0.5D;

					blockpos$mutableblockpos.set(x, 0, z);
					Biome biome = world.getBiome(blockpos$mutableblockpos);

					// TF - check for our own biomes
					if (!TFGenerationSettings.isBiomeSafeFor(biome, entity)) {

						int groundY = 0; // TF - extend through full height
						int minY = y0 - range;
						int maxY = y0 + range;

						if (minY < groundY) {
							minY = groundY;
						}

						if (maxY < groundY) {
							maxY = groundY;
						}

						int y = Math.max(groundY, y0);

						if (minY != maxY) {

							random.setSeed((long) x * x * 3121 + x * 45238971L ^ (long) z * z * 418711 + z * 13761L);

							// TF - replace temperature check with biome check
							RenderType nextType = getRenderType(biome);
							if (nextType == null) {
								continue;
							}

							// TF - share this logic and use an enum instead of magic numbers
							if (currentType != nextType) {
								if (currentType != null) {
									tessellator.end();
								}
								currentType = nextType;
								RenderSystem._setShaderTexture(0, nextType.getTextureLocation());
								bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
							}

							// TF - replicate for each render type with own changes
							switch (currentType) {
								case BLIZZARD, BIG_RAIN -> {
									float d5 = -((this.rendererUpdateCount + x * x * 3121 + x * 45238971 + z * z * 418711 + z * 13761 & 31) + partialTicks) / 32.0F * (3.0F + random.nextFloat());
									double d6 = x + 0.5F - xIn;
									double d7 = z + 0.5F - zIn;
									float f3 = Mth.sqrt((float) (d6 * d6 + d7 * d7)) / range;
									float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F);
									blockpos$mutableblockpos.set(x, y, z);
									int j3 = LevelRenderer.getLightColor(world, blockpos$mutableblockpos);
									int k3 = j3 >> 16 & 65535;
									int l3 = j3 & 65535;
									bufferbuilder.vertex(x - xIn - rx + 0.5D, maxY - yIn, z - zIn - ry + 0.5D).uv(0.0F, minY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).uv2(k3, l3).endVertex();
									bufferbuilder.vertex(x - xIn + rx + 0.5D, maxY - yIn, z - zIn + ry + 0.5D).uv(1.0F, minY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).uv2(k3, l3).endVertex();
									bufferbuilder.vertex(x - xIn + rx + 0.5D, minY - yIn, z - zIn + ry + 0.5D).uv(1.0F, maxY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).uv2(k3, l3).endVertex();
									bufferbuilder.vertex(x - xIn - rx + 0.5D, minY - yIn, z - zIn - ry + 0.5D).uv(0.0F, maxY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).uv2(k3, l3).endVertex();
								}
								case MOSQUITO -> {
									float d8 = 0; // TF - no wiggle
									float d9 = random.nextFloat() + combinedTicks * 0.01F * (float) random.nextGaussian();
									float d10 = random.nextFloat() + (combinedTicks * (float) random.nextGaussian()) * 0.001F;
									double d11 = x + 0.5F - xIn;
									double d12 = z + 0.5F - zIn;
									float f6 = Mth.sqrt((float) (d11 * d11 + d12 * d12)) / range;
									float r = random.nextFloat() * 0.3F; // TF - random color
									float g = random.nextFloat() * 0.3F;
									float b = random.nextFloat() * 0.3F;
									float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F);
									int i4 = 15 << 20 | 15 << 4; // TF - fullbright
									int j4 = i4 >> 16 & 65535;
									int k4 = i4 & 65535;
									bufferbuilder.vertex(x - xIn - rx + 0.5D, maxY - yIn, z - zIn - ry + 0.5D).uv(0.0F + d9, minY * 0.25F + d8 + d10).color(r, g, b, f5).uv2(j4, k4).endVertex();
									bufferbuilder.vertex(x - xIn + rx + 0.5D, maxY - yIn, z - zIn + ry + 0.5D).uv(1.0F + d9, minY * 0.25F + d8 + d10).color(r, g, b, f5).uv2(j4, k4).endVertex();
									bufferbuilder.vertex(x - xIn + rx + 0.5D, minY - yIn, z - zIn + ry + 0.5D).uv(1.0F + d9, maxY * 0.25F + d8 + d10).color(r, g, b, f5).uv2(j4, k4).endVertex();
									bufferbuilder.vertex(x - xIn - rx + 0.5D, minY - yIn, z - zIn - ry + 0.5D).uv(0.0F + d9, maxY * 0.25F + d8 + d10).color(r, g, b, f5).uv2(j4, k4).endVertex();
								}
								case ASHES -> {
									float d8 = -((this.rendererUpdateCount & 511) + partialTicks) / 512.0F;
									float d9 = random.nextFloat() + combinedTicks * 0.01F * (float) random.nextGaussian();
									float d10 = random.nextFloat() + (combinedTicks * (float) random.nextGaussian()) * 0.001F;
									double d11 = x + 0.5F - xIn;
									double d12 = z + 0.5F - zIn;
									float f6 = Mth.sqrt((float) (d11 * d11 + d12 * d12)) / range;
									float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F);
									int i4 = 15 << 20 | 15 << 4; // TF - fullbright
									int j4 = i4 >> 16 & 65535;
									int k4 = i4 & 65535;
									float color = random.nextFloat() * 0.2F + 0.8F; // TF - random color
									bufferbuilder.vertex(x - xIn - rx + 0.5D, maxY - yIn, z - zIn - ry + 0.5D).uv(0.0F + d9, minY * 0.25F + d8 + d10).color(color, color, color, f5).uv2(j4, k4).endVertex();
									bufferbuilder.vertex(x - xIn + rx + 0.5D, maxY - yIn, z - zIn + ry + 0.5D).uv(1.0F + d9, minY * 0.25F + d8 + d10).color(color, color, color, f5).uv2(j4, k4).endVertex();
									bufferbuilder.vertex(x - xIn + rx + 0.5D, minY - yIn, z - zIn + ry + 0.5D).uv(1.0F + d9, maxY * 0.25F + d8 + d10).color(color, color, color, f5).uv2(j4, k4).endVertex();
									bufferbuilder.vertex(x - xIn - rx + 0.5D, minY - yIn, z - zIn - ry + 0.5D).uv(0.0F + d9, maxY * 0.25F + d8 + d10).color(color, color, color, f5).uv2(j4, k4).endVertex();
								}
								case DARK_STREAM -> {
									float d8 = -((this.rendererUpdateCount & 511) + partialTicks) / 512.0F;
									float d9 = 0; // TF - no u wiggle
									float d10 = random.nextFloat() + (combinedTicks * (float) random.nextGaussian()) * 0.001F;
									double d11 = x + 0.5F - xIn;
									double d12 = z + 0.5F - zIn;
									float f6 = Mth.sqrt((float) (d11 * d11 + d12 * d12)) / range;
									float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * random.nextFloat(); // TF - random alpha multiplier
									int i4 = 15 << 20 | 15 << 4; // TF - fullbright
									int j4 = i4 >> 16 & 65535;
									int k4 = i4 & 65535;
									bufferbuilder.vertex(x - xIn - rx + 0.5D, maxY - yIn, z - zIn - ry + 0.5D).uv(0.0F + d9, minY * 0.25F + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).uv2(j4, k4).endVertex();
									bufferbuilder.vertex(x - xIn + rx + 0.5D, maxY - yIn, z - zIn + ry + 0.5D).uv(1.0F + d9, minY * 0.25F + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).uv2(j4, k4).endVertex();
									bufferbuilder.vertex(x - xIn + rx + 0.5D, minY - yIn, z - zIn + ry + 0.5D).uv(1.0F + d9, maxY * 0.25F + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).uv2(j4, k4).endVertex();
									bufferbuilder.vertex(x - xIn - rx + 0.5D, minY - yIn, z - zIn - ry + 0.5D).uv(0.0F + d9, maxY * 0.25F + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).uv2(j4, k4).endVertex();
								}
							}
						}
					}
				}
			}

			if (currentType != null) {
				tessellator.end();
			}

			RenderSystem.enableCull();
			RenderSystem.disableBlend();
			lightmap.turnOffLightLayer();
		}
	}

	// [VanillaCopy] inside of EntityRenderer.renderRainSnow, edits noted
	private void renderLockedStructure(float partialTicks, Minecraft mc, LightTexture lightmap, double xIn, double yIn, double zIn) {
		// draw locked structure thing
		if (isNearLockedStructure(xIn, zIn)) {
			lightmap.turnOnLightLayer();
			int i = Mth.floor(xIn);
			int j = Mth.floor(yIn);
			int k = Mth.floor(zIn);
			Tesselator tessellator = Tesselator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuilder();
			RenderSystem.disableCull();
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();
			int i1 = 5;

			if (Minecraft.useFancyGraphics()) {
				i1 = 10;
			}

			RenderSystem.depthMask(Minecraft.useShaderTransparency());
			int j1 = -1;
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

			for (int k1 = k - i1; k1 <= k + i1; ++k1) {
				for (int l1 = i - i1; l1 <= i + i1; ++l1) {
					int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
					double d3 = this.rainxs[i2] * 0.5D;
					double d4 = this.rainzs[i2] * 0.5D;

					// TF - replace biome check with box check
					if (this.protectedBox != null && this.protectedBox.intersects(l1, k1, l1, k1)) {
						int structureMin = this.protectedBox.minY() - 4;
						int structureMax = this.protectedBox.maxY() + 4;
						int k2 = j - i1;
						int l2 = j + i1 * 2;

						if (k2 < structureMin) {
							k2 = structureMin;
						}

						if (l2 < structureMin) {
							l2 = structureMin;
						}

						if (k2 > structureMax) {
							k2 = structureMax;
						}

						if (l2 > structureMax) {
							l2 = structureMax;
						}

						if (k2 != l2) {
							Random random = new Random((long) l1 * l1 * 3121 + l1 * 45238971L ^ (long) k1 * k1 * 418711 + k1 * 13761L);
							blockpos$mutableblockpos.set(l1, k2, k1);

							// TF - unwrap temperature check for snow, only one branch. Use our own texture
							if (j1 != 0) {
								if (j1 >= 0) {
									tessellator.end();
								}

								j1 = 0;
								RenderSystem._setShaderTexture(0, SPARKLES_TEXTURE);
								bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
							}

							float d5 = -((this.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 31) + partialTicks) / 32.0F * (3.0F + random.nextFloat());
							double d6 = l1 + 0.5F - xIn;
							double d7 = k1 + 0.5F - zIn;
							float f3 = Mth.sqrt((float) (d6 * d6 + d7 * d7)) / i1;
							// TF - "f" was rain strength for alpha
							float f = random.nextFloat();
							float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * f;
							int j3 = 15 << 20 | 15 << 4; // TF - fullbright
							int k3 = j3 >> 16 & 65535;
							int l3 = j3 & 65535;
							bufferbuilder.vertex(l1 - xIn - d3 + 0.5D, l2 - yIn, k1 - zIn - d4 + 0.5D).uv(0.0F, k2 * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).uv2(k3, l3).endVertex();
							bufferbuilder.vertex(l1 - xIn + d3 + 0.5D, l2 - yIn, k1 - zIn + d4 + 0.5D).uv(1.0F, k2 * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).uv2(k3, l3).endVertex();
							bufferbuilder.vertex(l1 - xIn + d3 + 0.5D, k2 - yIn, k1 - zIn + d4 + 0.5D).uv(1.0F, l2 * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).uv2(k3, l3).endVertex();
							bufferbuilder.vertex(l1 - xIn - d3 + 0.5D, k2 - yIn, k1 - zIn - d4 + 0.5D).uv(0.0F, l2 * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).uv2(k3, l3).endVertex();
						}
					}
				}
			}

			if (j1 >= 0) {
				tessellator.end();
			}

			RenderSystem.enableCull();
			RenderSystem.disableBlend();
			lightmap.turnOffLightLayer();
		}
	}

	private boolean isNearLockedBiome(Level world, Entity viewEntity) {
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		final int range = 15;
		int px = Mth.floor(viewEntity.getX());
		int pz = Mth.floor(viewEntity.getZ());

		for (int z = pz - range; z <= pz + range; ++z) {
			for (int x = px - range; x <= px + range; ++x) {
				Biome biome = world.getBiome(pos.set(x, 0, z));
				if (!TFGenerationSettings.isBiomeSafeFor(biome, viewEntity)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean isNearLockedStructure(double xIn, double zIn) {
		final int range = 15;
		int px = Mth.floor(xIn);
		int pz = Mth.floor(zIn);

		return this.protectedBox != null && this.protectedBox.intersects(px - range, pz - range, px + range, pz + range);
	}

	public void setProtectedBox(BoundingBox protectedBox) {
		this.protectedBox = protectedBox;
	}

	private RenderType getRenderType(Biome b) {
		if (Minecraft.getInstance().level == null)
			return null;
		ResourceLocation biome = Minecraft.getInstance().level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(b);
		if (BiomeKeys.SNOWY_FOREST.location().equals(biome) || BiomeKeys.GLACIER.location().equals(biome)) {
			return RenderType.BLIZZARD;
		} else if (BiomeKeys.SWAMP.location().equals(biome)) {
			return RenderType.MOSQUITO;
		} else if (BiomeKeys.FIRE_SWAMP.location().equals(biome)) {
			return RenderType.ASHES;
		} else if (BiomeKeys.DARK_FOREST.location().equals(biome) || BiomeKeys.DARK_FOREST_CENTER.location().equals(biome)) {
			return random.nextInt(2) == 0 ? RenderType.DARK_STREAM : null;
		} else if (BiomeKeys.HIGHLANDS.location().equals(biome) || BiomeKeys.THORNLANDS.location().equals(biome) || BiomeKeys.FINAL_PLATEAU.location().equals(biome)) {
			return RenderType.BIG_RAIN;
		}
		return null;
	}

	private enum RenderType {

		BLIZZARD("blizzard.png"),
		MOSQUITO("mosquitoes.png"),
		ASHES("ashes.png"),
		DARK_STREAM("darkstream.png"),
		BIG_RAIN("bigrain.png");

		RenderType(String textureName) {
			this.textureLocation = TwilightForestMod.getEnvTexture(textureName);
		}

		private final ResourceLocation textureLocation;

		public ResourceLocation getTextureLocation() {
			return textureLocation;
		}
	}
}
