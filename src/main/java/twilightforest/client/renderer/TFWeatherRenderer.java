package twilightforest.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.client.IWeatherRenderHandler;
import twilightforest.TwilightForestMod;
import twilightforest.world.TFGenerationSettings;
import twilightforest.worldgen.biomes.BiomeKeys;

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
	private MutableBoundingBox protectedBox;

	private final Random random = new Random();

	public TFWeatherRenderer() {
		for (int i = 0; i < 32; ++i) {
			for (int j = 0; j < 32; ++j) {
				float f  = j - 16;
				float f1 = i - 16;
				float f2 = MathHelper.sqrt(f * f + f1 * f1);
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
	public void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc, LightTexture lightmap, double xIn, double yIn, double zIn) {
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
	private void renderNormalWeather(LightTexture lightmapIn, ClientWorld world, Minecraft mc, float partialTicks, double xIn, double yIn, double zIn) {

		float f = world.getRainStrength(partialTicks);
		if (!(f <= 0.0F)) {
			lightmapIn.enableLightmap();
			int i = MathHelper.floor(xIn);
			int j = MathHelper.floor(yIn);
			int k = MathHelper.floor(zIn);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			RenderSystem.enableAlphaTest();
			RenderSystem.disableCull();
			RenderSystem.normal3f(0.0F, 1.0F, 0.0F);
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.defaultAlphaFunc();
			RenderSystem.enableDepthTest();
			int l = 5;
			if (Minecraft.isFancyGraphicsEnabled()) {
				l = 10;
			}

			RenderSystem.depthMask(Minecraft.isFabulousGraphicsEnabled());
			int i1 = -1;
			float f1 = (float) rendererUpdateCount + partialTicks;
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

			for (int j1 = k - l; j1 <= k + l; ++j1) {
				for (int k1 = i - l; k1 <= i + l; ++k1) {
					int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
					double d0 = (double) rainxs[l1] * 0.5D;
					double d1 = (double) rainzs[l1] * 0.5D;
					blockpos$mutable.setPos(k1, 0, j1);
					Biome biome = world.getBiome(blockpos$mutable);
					if (biome.getPrecipitation() != Biome.RainType.NONE) {
						int i2 = world.getHeight(Heightmap.Type.MOTION_BLOCKING, blockpos$mutable).getY();
						int j2 = j - l;
						int k2 = j + l;
						if (j2 < i2) {
							j2 = i2;
						}

						if (k2 < i2) {
							k2 = i2;
						}

						int l2 = Math.max(i2, j);

						if (j2 != k2) {
							Random random = new Random((long) k1 * k1 * 3121 + k1 * 45238971L ^ (long) j1 * j1 * 418711 + j1 * 13761L);
							blockpos$mutable.setPos(k1, j2, j1);
							float f2 = biome.getTemperature(blockpos$mutable);
							if (f2 >= 0.15F) {
								if (i1 != 0) {
									if (i1 >= 0) {
										tessellator.draw();
									}

									i1 = 0;
									mc.getTextureManager().bindTexture(RAIN_TEXTURES);
									bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
								}

								int i3 = rendererUpdateCount + k1 * k1 * 3121 + k1 * 45238971 + j1 * j1 * 418711 + j1 * 13761 & 31;
								float f3 = -((float) i3 + partialTicks) / 32.0F * (3.0F + random.nextFloat());
								double d2 = (double) ((float) k1 + 0.5F) - xIn;
								double d4 = (double) ((float) j1 + 0.5F) - zIn;
								float f4 = MathHelper.sqrt(d2 * d2 + d4 * d4) / (float) l;
								float f5 = ((1.0F - f4 * f4) * 0.5F + 0.5F) * f;
								blockpos$mutable.setPos(k1, l2, j1);
								int j3 = WorldRenderer.getCombinedLight(world, blockpos$mutable);
								bufferbuilder.pos((double) k1 - xIn - d0 + 0.5D, (double) k2 - yIn, (double) j1 - zIn - d1 + 0.5D).tex(0.0F, (float) j2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).lightmap(j3).endVertex();
								bufferbuilder.pos((double) k1 - xIn + d0 + 0.5D, (double) k2 - yIn, (double) j1 - zIn + d1 + 0.5D).tex(1.0F, (float) j2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).lightmap(j3).endVertex();
								bufferbuilder.pos((double) k1 - xIn + d0 + 0.5D, (double) j2 - yIn, (double) j1 - zIn + d1 + 0.5D).tex(1.0F, (float) k2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).lightmap(j3).endVertex();
								bufferbuilder.pos((double) k1 - xIn - d0 + 0.5D, (double) j2 - yIn, (double) j1 - zIn - d1 + 0.5D).tex(0.0F, (float) k2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).lightmap(j3).endVertex();
							} else {
								if (i1 != 1) {
									if (i1 >= 0) {
										tessellator.draw();
									}

									i1 = 1;
									mc.getTextureManager().bindTexture(SNOW_TEXTURES);
									bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
								}

								float f6 = -((float) (rendererUpdateCount & 511) + partialTicks) / 512.0F;
								float f7 = (float) (random.nextDouble() + (double) f1 * 0.01D * (double) ((float) random.nextGaussian()));
								float f8 = (float) (random.nextDouble() + (double) (f1 * (float) random.nextGaussian()) * 0.001D);
								double d3 = (double) ((float) k1 + 0.5F) - xIn;
								double d5 = (double) ((float) j1 + 0.5F) - zIn;
								float f9 = MathHelper.sqrt(d3 * d3 + d5 * d5) / (float) l;
								float f10 = ((1.0F - f9 * f9) * 0.3F + 0.5F) * f;
								blockpos$mutable.setPos(k1, l2, j1);
								int k3 = WorldRenderer.getCombinedLight(world, blockpos$mutable);
								int l3 = k3 >> 16 & '\uffff';
								int i4 = (k3 & '\uffff') * 3;
								int j4 = (l3 * 3 + 240) / 4;
								int k4 = (i4 * 3 + 240) / 4;
								bufferbuilder.pos((double) k1 - xIn - d0 + 0.5D, (double) k2 - yIn, (double) j1 - zIn - d1 + 0.5D).tex(0.0F + f7, (float) j2 * 0.25F + f6 + f8).color(1.0F, 1.0F, 1.0F, f10).lightmap(k4, j4).endVertex();
								bufferbuilder.pos((double) k1 - xIn + d0 + 0.5D, (double) k2 - yIn, (double) j1 - zIn + d1 + 0.5D).tex(1.0F + f7, (float) j2 * 0.25F + f6 + f8).color(1.0F, 1.0F, 1.0F, f10).lightmap(k4, j4).endVertex();
								bufferbuilder.pos((double) k1 - xIn + d0 + 0.5D, (double) j2 - yIn, (double) j1 - zIn + d1 + 0.5D).tex(1.0F + f7, (float) k2 * 0.25F + f6 + f8).color(1.0F, 1.0F, 1.0F, f10).lightmap(k4, j4).endVertex();
								bufferbuilder.pos((double) k1 - xIn - d0 + 0.5D, (double) j2 - yIn, (double) j1 - zIn - d1 + 0.5D).tex(0.0F + f7, (float) k2 * 0.25F + f6 + f8).color(1.0F, 1.0F, 1.0F, f10).lightmap(k4, j4).endVertex();
							}
						}
					}
				}
			}

			if (i1 >= 0) {
				tessellator.draw();
			}

			RenderSystem.enableCull();
			RenderSystem.disableBlend();
			RenderSystem.defaultAlphaFunc();
			RenderSystem.disableAlphaTest();
			lightmapIn.disableLightmap();
		}
	}

	// [VanillaCopy] inside of EntityRenderer.renderRainSnow, edits noted
	private void renderLockedBiome(float partialTicks, ClientWorld wc, Minecraft mc, LightTexture lightmap, double xIn, double yIn, double zIn) {
		// check nearby for locked biome
		if (isNearLockedBiome(wc, mc.getRenderViewEntity())) {

			lightmap.enableLightmap();
			Entity entity = mc.getRenderViewEntity();
			World world = mc.world;

			int x0 = MathHelper.floor(xIn);
			int y0 = MathHelper.floor(yIn);
			int z0 = MathHelper.floor(zIn);

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();

			RenderSystem.disableCull();
			RenderSystem.normal3f(0.0F, 1.0F, 0.0F);
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.defaultAlphaFunc();
			RenderSystem.enableDepthTest();

			int range = 5;

			if (Minecraft.isFancyGraphicsEnabled()) {
				range = 10;
			}

			RenderSystem.depthMask(Minecraft.isFabulousGraphicsEnabled());

			RenderType currentType = null;
			float combinedTicks = this.rendererUpdateCount + partialTicks;
			//bufferbuilder.setTranslation(-dx, -dy, -dz);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();

			for (int z = z0 - range; z <= z0 + range; ++z) {
				for (int x = x0 - range; x <= x0 + range; ++x) {

					int idx = (z - z0 + 16) * 32 + x - x0 + 16;
					double rx = this.rainxs[idx] * 0.5D;
					double ry = this.rainzs[idx] * 0.5D;

					blockpos$mutableblockpos.setPos(x, 0, z);
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
									tessellator.draw();
								}
								currentType = nextType;
								mc.getTextureManager().bindTexture(nextType.getTextureLocation());
								bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
							}

							// TF - replicate for each render type with own changes
							switch (currentType) {
								case BLIZZARD:
								case BIG_RAIN: {
									float d5 = -((this.rendererUpdateCount + x * x * 3121 + x * 45238971 + z * z * 418711 + z * 13761 & 31) + partialTicks) / 32.0F * (3.0F + random.nextFloat());
									double d6 = x + 0.5F - xIn;
									double d7 = z + 0.5F - zIn;
									float f3 = MathHelper.sqrt(d6 * d6 + d7 * d7) / range;
									float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F);
									blockpos$mutableblockpos.setPos(x, y, z);
									int j3 = WorldRenderer.getCombinedLight(world, blockpos$mutableblockpos);
									int k3 = j3 >> 16 & 65535;
									int l3 = j3 & 65535;
									bufferbuilder.pos(x - xIn - rx + 0.5D, maxY - yIn, z - zIn - ry + 0.5D).tex(0.0F, minY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
									bufferbuilder.pos(x - xIn + rx + 0.5D, maxY - yIn, z - zIn + ry + 0.5D).tex(1.0F, minY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
									bufferbuilder.pos(x - xIn + rx + 0.5D, minY - yIn, z - zIn + ry + 0.5D).tex(1.0F, maxY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
									bufferbuilder.pos(x - xIn - rx + 0.5D, minY - yIn, z - zIn - ry + 0.5D).tex(0.0F, maxY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
								} break;
								case MOSQUITO: {
									float d8 = 0; // TF - no wiggle
									float d9 = random.nextFloat() + combinedTicks * 0.01F * (float) random.nextGaussian();
									float d10 = random.nextFloat() + (combinedTicks * (float) random.nextGaussian()) * 0.001F;
									double d11 = x + 0.5F - xIn;
									double d12 = z + 0.5F - zIn;
									float f6 = MathHelper.sqrt(d11 * d11 + d12 * d12) / range;
									float r = random.nextFloat() * 0.3F; // TF - random color
									float g = random.nextFloat() * 0.3F;
									float b = random.nextFloat() * 0.3F;
									float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F);
									int i4 = 15 << 20 | 15 << 4; // TF - fullbright
									int j4 = i4 >> 16 & 65535;
									int k4 = i4 & 65535;
									bufferbuilder.pos(x - xIn - rx + 0.5D, maxY - yIn, z - zIn - ry + 0.5D).tex(0.0F + d9, minY * 0.25F + d8 + d10).color(r, g, b, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos(x - xIn + rx + 0.5D, maxY - yIn, z - zIn + ry + 0.5D).tex(1.0F + d9, minY * 0.25F + d8 + d10).color(r, g, b, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos(x - xIn + rx + 0.5D, minY - yIn, z - zIn + ry + 0.5D).tex(1.0F + d9, maxY * 0.25F + d8 + d10).color(r, g, b, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos(x - xIn - rx + 0.5D, minY - yIn, z - zIn - ry + 0.5D).tex(0.0F + d9, maxY * 0.25F + d8 + d10).color(r, g, b, f5).lightmap(j4, k4).endVertex();
								} break;
								case ASHES: {
									float d8 = -((this.rendererUpdateCount & 511) + partialTicks) / 512.0F;
									float d9 = random.nextFloat() + combinedTicks * 0.01F * (float) random.nextGaussian();
									float d10 = random.nextFloat() + (combinedTicks * (float) random.nextGaussian()) * 0.001F;
									double d11 = x + 0.5F - xIn;
									double d12 = z + 0.5F - zIn;
									float f6 = MathHelper.sqrt(d11 * d11 + d12 * d12) / range;
									float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F);
									int i4 = 15 << 20 | 15 << 4; // TF - fullbright
									int j4 = i4 >> 16 & 65535;
									int k4 = i4 & 65535;
									float color = random.nextFloat() * 0.2F + 0.8F; // TF - random color
									bufferbuilder.pos(x - xIn - rx + 0.5D, maxY - yIn, z - zIn - ry + 0.5D).tex(0.0F + d9, minY * 0.25F + d8 + d10).color(color, color, color, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos(x - xIn + rx + 0.5D, maxY - yIn, z - zIn + ry + 0.5D).tex(1.0F + d9, minY * 0.25F + d8 + d10).color(color, color, color, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos(x - xIn + rx + 0.5D, minY - yIn, z - zIn + ry + 0.5D).tex(1.0F + d9, maxY * 0.25F + d8 + d10).color(color, color, color, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos(x - xIn - rx + 0.5D, minY - yIn, z - zIn - ry + 0.5D).tex(0.0F + d9, maxY * 0.25F + d8 + d10).color(color, color, color, f5).lightmap(j4, k4).endVertex();
								} break;
								case DARK_STREAM: {
									float d8 = -((this.rendererUpdateCount & 511) + partialTicks) / 512.0F;
									float d9 = 0; // TF - no u wiggle
									float d10 = random.nextFloat() + (combinedTicks * (float) random.nextGaussian()) * 0.001F;
									double d11 = x + 0.5F - xIn;
									double d12 = z + 0.5F - zIn;
									float f6 = MathHelper.sqrt(d11 * d11 + d12 * d12) / range;
									float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * random.nextFloat(); // TF - random alpha multiplier
									int i4 = 15 << 20 | 15 << 4; // TF - fullbright
									int j4 = i4 >> 16 & 65535;
									int k4 = i4 & 65535;
									bufferbuilder.pos(x - xIn - rx + 0.5D, maxY - yIn, z - zIn - ry + 0.5D).tex(0.0F + d9, minY * 0.25F + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos(x - xIn + rx + 0.5D, maxY - yIn, z - zIn + ry + 0.5D).tex(1.0F + d9, minY * 0.25F + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos(x - xIn + rx + 0.5D, minY - yIn, z - zIn + ry + 0.5D).tex(1.0F + d9, maxY * 0.25F + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos(x - xIn - rx + 0.5D, minY - yIn, z - zIn - ry + 0.5D).tex(0.0F + d9, maxY * 0.25F + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
								} break;
							}
						}
					}
				}
			}

			if (currentType != null) {
				tessellator.draw();
			}

			RenderSystem.enableCull();
			RenderSystem.disableBlend();
			RenderSystem.defaultAlphaFunc();
			RenderSystem.disableAlphaTest();
			lightmap.disableLightmap();
		}
	}

	// [VanillaCopy] inside of EntityRenderer.renderRainSnow, edits noted
	private void renderLockedStructure(float partialTicks, Minecraft mc, LightTexture lightmap, double xIn, double yIn, double zIn) {
		// draw locked structure thing
		if (isNearLockedStructure(xIn, zIn)) {
			lightmap.enableLightmap();
			int i = MathHelper.floor(xIn);
			int j = MathHelper.floor(yIn);
			int k = MathHelper.floor(zIn);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			RenderSystem.disableCull();
			RenderSystem.normal3f(0.0F, 1.0F, 0.0F);
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.defaultAlphaFunc();
			RenderSystem.enableDepthTest();
			int i1 = 5;

			if (Minecraft.isFancyGraphicsEnabled()) {
				i1 = 10;
			}

			RenderSystem.depthMask(Minecraft.isFabulousGraphicsEnabled());
			int j1 = -1;
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();

			for (int k1 = k - i1; k1 <= k + i1; ++k1) {
				for (int l1 = i - i1; l1 <= i + i1; ++l1) {
					int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
					double d3 = this.rainxs[i2] * 0.5D;
					double d4 = this.rainzs[i2] * 0.5D;

					// TF - replace biome check with box check
					if (this.protectedBox != null && this.protectedBox.intersectsWith(l1, k1, l1, k1)) {
						int structureMin = this.protectedBox.minY - 4;
						int structureMax = this.protectedBox.maxY + 4;
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
							blockpos$mutableblockpos.setPos(l1, k2, k1);

							// TF - unwrap temperature check for snow, only one branch. Use our own texture
							if (j1 != 0) {
								if (j1 >= 0) {
									tessellator.draw();
								}

								j1 = 0;
								mc.getTextureManager().bindTexture(SPARKLES_TEXTURE);
								bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
							}

							float d5 = -((this.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 31) + partialTicks) / 32.0F * (3.0F + random.nextFloat());
							double d6 = l1 + 0.5F - xIn;
							double d7 = k1 + 0.5F - zIn;
							float f3 = MathHelper.sqrt(d6 * d6 + d7 * d7) / i1;
							// TF - "f" was rain strength for alpha
							float f = random.nextFloat();
							float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * f;
							int j3 = 15 << 20 | 15 << 4; // TF - fullbright
							int k3 = j3 >> 16 & 65535;
							int l3 = j3 & 65535;
							bufferbuilder.pos(l1 - xIn - d3 + 0.5D, l2 - yIn, k1 - zIn - d4 + 0.5D).tex(0.0F, k2 * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
							bufferbuilder.pos(l1 - xIn + d3 + 0.5D, l2 - yIn, k1 - zIn + d4 + 0.5D).tex(1.0F, k2 * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
							bufferbuilder.pos(l1 - xIn + d3 + 0.5D, k2 - yIn, k1 - zIn + d4 + 0.5D).tex(1.0F, l2 * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
							bufferbuilder.pos(l1 - xIn - d3 + 0.5D, k2 - yIn, k1 - zIn - d4 + 0.5D).tex(0.0F, l2 * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
						}
					}
				}
			}

			if (j1 >= 0) {
				tessellator.draw();
			}

			RenderSystem.enableCull();
			RenderSystem.disableBlend();
			RenderSystem.defaultAlphaFunc();
			RenderSystem.disableAlphaTest();
			lightmap.disableLightmap();
		}
	}

	private boolean isNearLockedBiome(World world, Entity viewEntity) {
		BlockPos.Mutable pos = new BlockPos.Mutable();
		final int range = 15;
		int px = MathHelper.floor(viewEntity.getPosX());
		int pz = MathHelper.floor(viewEntity.getPosZ());

		for (int z = pz - range; z <= pz + range; ++z) {
			for (int x = px - range; x <= px + range; ++x) {
				Biome biome = world.getBiome(pos.setPos(x, 0, z));
				if (!TFGenerationSettings.isBiomeSafeFor(biome, viewEntity)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean isNearLockedStructure(double xIn, double zIn) {
		final int range = 15;
		int px = MathHelper.floor(xIn);
		int pz = MathHelper.floor(zIn);

		return this.protectedBox != null && this.protectedBox.intersectsWith(px - range, pz - range, px + range, pz + range);
	}

	public void setProtectedBox(MutableBoundingBox protectedBox) {
		this.protectedBox = protectedBox;
	}

	private RenderType getRenderType(Biome b) {
		if (Minecraft.getInstance().world == null)
			return null;
		ResourceLocation biome = Minecraft.getInstance().world.func_241828_r().getRegistry(Registry.BIOME_KEY).getKey(b);
		if (BiomeKeys.SNOWY_FOREST.getLocation().equals(biome) || BiomeKeys.GLACIER.getLocation().equals(biome)) {
			return RenderType.BLIZZARD;
		} else if (BiomeKeys.SWAMP.getLocation().equals(biome)) {
			return RenderType.MOSQUITO;
		} else if (BiomeKeys.FIRE_SWAMP.getLocation().equals(biome)) {
			return RenderType.ASHES;
		} else if (BiomeKeys.DARK_FOREST.getLocation().equals(biome) || BiomeKeys.DARK_FOREST_CENTER.getLocation().equals(biome)) {
			return random.nextInt(2) == 0 ? RenderType.DARK_STREAM : null;
		} else if (BiomeKeys.HIGHLANDS.getLocation().equals(biome) || BiomeKeys.THORNLANDS.getLocation().equals(biome) || BiomeKeys.FINAL_PLATEAU.getLocation().equals(biome)) {
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
