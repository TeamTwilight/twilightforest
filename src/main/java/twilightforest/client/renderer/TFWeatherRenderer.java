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
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.client.IWeatherRenderHandler;
import twilightforest.TwilightForestMod;
import twilightforest.world.TFGenerationSettings;

import java.util.Random;

/**
 * Copypasta of EntityRenderer.renderRainSnow() hacked to include progression environmental effects
 */
public class TFWeatherRenderer implements IWeatherRenderHandler {

	private static final ResourceLocation RAIN_TEXTURES = new ResourceLocation("textures/environment/rain.png");
	private static final ResourceLocation SNOW_TEXTURES = new ResourceLocation("textures/environment/snow.png");

	private static final ResourceLocation SPARKLES_TEXTURE = TwilightForestMod.getEnvTexture("sparkles.png");

	private final float[] rainxs = new float[1024];
	private final float[] rainys = new float[1024];

	private int rendererUpdateCount;
	private MutableBoundingBox protectedBox;

	public TFWeatherRenderer() {
		for (int i = 0; i < 32; ++i) {
			for (int j = 0; j < 32; ++j) {
				float f  = (float) (j - 16);
				float f1 = (float) (i - 16);
				float f2 = MathHelper.sqrt(f * f + f1 * f1);
				this.rainxs[i << 5 | j] = -f1 / f2;
				this.rainys[i << 5 | j] =   f / f2;
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
		renderNormalWeather(partialTicks, mc, lightmap, xIn, yIn, zIn);

		if (TFGenerationSettings.isProgressionEnforced(world) && !mc.player.isCreative() && !mc.player.isSpectator()) {
			// locked biome weather effects
			renderLockedBiome(partialTicks, world, mc, lightmap, xIn, yIn, zIn);

			// locked structures
			renderLockedStructure(partialTicks, mc, lightmap, xIn, yIn, zIn);
		}
	}

	// [VanillaCopy] exact of EntityRenderer.renderRainSnow
	private void renderNormalWeather(float partialTicks, Minecraft mc, LightTexture lightmap, double xIn, double yIn, double zIn) {
		float f = mc.world.getRainStrength(partialTicks);

		if (f > 0.0F) {
			lightmap.enableLightmap();
//			Entity entity = mc.getRenderViewEntity();
			World world = mc.world;
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
			int i1 = 5;

			if (Minecraft.isFancyGraphicsEnabled()) {
				i1 = 10;
			}

			RenderSystem.depthMask(Minecraft.isFabulousGraphicsEnabled());
			int j1 = -1;
			float f1 = (float) this.rendererUpdateCount + partialTicks;
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();

			for (int k1 = k - i1; k1 <= k + i1; ++k1) {
				for (int l1 = i - i1; l1 <= i + i1; ++l1) {
					int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
					double d3 = (double) this.rainxs[i2] * 0.5D;
					double d4 = (double) this.rainys[i2] * 0.5D;
					blockpos$mutableblockpos.setPos(l1, 0, k1);
					Biome biome = world.getBiome(blockpos$mutableblockpos);

					if (biome.getPrecipitation() != Biome.RainType.NONE) {
						int j2 = world.getHeight(Heightmap.Type.MOTION_BLOCKING, blockpos$mutableblockpos).getY();
						int k2 = j - i1;
						int l2 = j + i1;

						if (k2 < j2) {
							k2 = j2;
						}

						if (l2 < j2) {
							l2 = j2;
						}

						int i3 = j2;

						if (j2 < j) {
							i3 = j;
						}

						if (k2 != l2) {
							Random random = new Random((long) (l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761));
							blockpos$mutableblockpos.setPos(l1, k2, k1);
							float f2 = biome.getTemperature(blockpos$mutableblockpos);

							if (f2 >= 0.15F) {
								if (j1 != 0) {
									if (j1 >= 0) {
										tessellator.draw();
									}

									j1 = 0;
									mc.getTextureManager().bindTexture(RAIN_TEXTURES);
									bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
								}

								float d5 = -((float) (this.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 31) + partialTicks) / 32.0F * (3.0F + random.nextFloat());
								double d6 = (double) ((float) l1 + 0.5F) - xIn;
								double d7 = (double) ((float) k1 + 0.5F) - zIn;
								float f3 = MathHelper.sqrt(d6 * d6 + d7 * d7) / (float) i1;
								float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * f;
								blockpos$mutableblockpos.setPos(l1, i3, k1);
								int j3 = WorldRenderer.getCombinedLight(world, blockpos$mutableblockpos);
								bufferbuilder.pos((double) l1 - d3 + 0.5D, (double) l2, (double) k1 - d4 + 0.5D).tex(0.0F, (float) k2 * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(j3).endVertex();
								bufferbuilder.pos((double) l1 + d3 + 0.5D, (double) l2, (double) k1 + d4 + 0.5D).tex(1.0F, (float) k2 * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(j3).endVertex();
								bufferbuilder.pos((double) l1 + d3 + 0.5D, (double) k2, (double) k1 + d4 + 0.5D).tex(1.0F, (float) l2 * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(j3).endVertex();
								bufferbuilder.pos((double) l1 - d3 + 0.5D, (double) k2, (double) k1 - d4 + 0.5D).tex(0.0F, (float) l2 * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(j3).endVertex();
							} else {
								if (j1 != 1) {
									if (j1 >= 0) {
										tessellator.draw();
									}

									j1 = 1;
									mc.getTextureManager().bindTexture(SNOW_TEXTURES);
									bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
								}

								float d8 = (-((float) (this.rendererUpdateCount & 511) + partialTicks) / 512.0F);
								float d9 = random.nextFloat() + f1 * 0.01F * ((float) random.nextGaussian());
								float d10 = random.nextFloat() + (f1 * (float) random.nextGaussian()) * 0.001F;
								double d11 = (double) ((float) l1 + 0.5F) - xIn;
								double d12 = (double) ((float) k1 + 0.5F) - zIn;
								float f6 = MathHelper.sqrt(d11 * d11 + d12 * d12) / (float) i1;
								float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * f;
								blockpos$mutableblockpos.setPos(l1, i3, k1);
								int i4 = WorldRenderer.getCombinedLight(world, blockpos$mutableblockpos);
								int j4 = i4 >> 16 & 65535;
								int k4 = (i4 & 65535) * 3;
								int l4 = (j4 * 3 + 240) / 4;
								int l5 = (k4 * 3 + 240) / 4;
								bufferbuilder.pos((double) l1 - d3 + 0.5D, (double) l2, (double) k1 - d4 + 0.5D).tex(0.0F + d9, (float) k2 * 0.25F + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(l5, l4).endVertex();
								bufferbuilder.pos((double) l1 + d3 + 0.5D, (double) l2, (double) k1 + d4 + 0.5D).tex(1.0F + d9, (float) k2 * 0.25F + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(l5, l4).endVertex();
								bufferbuilder.pos((double) l1 + d3 + 0.5D, (double) k2, (double) k1 + d4 + 0.5D).tex(1.0F + d9, (float) l2 * 0.25F + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(l5, l4).endVertex();
								bufferbuilder.pos((double) l1 - d3 + 0.5D, (double) k2, (double) k1 - d4 + 0.5D).tex(0.0F + d9, (float) l2 * 0.25F + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(l5, l4).endVertex();
							}
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
			float combinedTicks = (float) this.rendererUpdateCount + partialTicks;
			//bufferbuilder.setTranslation(-dx, -dy, -dz);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();

			for (int z = z0 - range; z <= z0 + range; ++z) {
				for (int x = x0 - range; x <= x0 + range; ++x) {

					int idx = (z - z0 + 16) * 32 + x - x0 + 16;
					double rx = (double) this.rainxs[idx] * 0.5D;
					double ry = (double) this.rainys[idx] * 0.5D;

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

						int y = groundY;

						if (groundY < y0) {
							y = y0;
						}

						if (minY != maxY) {

							Random random = new Random((long) (x * x * 3121 + x * 45238971 ^ z * z * 418711 + z * 13761));

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
								case BLIZZARD: {
									float d5 = -((this.rendererUpdateCount + x * x * 3121 + x * 45238971 + z * z * 418711 + z * 13761 & 31) + partialTicks) / 32.0F * (3.0F + random.nextFloat());
									double d6 = (double) ((float) x + 0.5F) - xIn;
									double d7 = (double) ((float) z + 0.5F) - zIn;
									float f3 = MathHelper.sqrt(d6 * d6 + d7 * d7) / (float) range;
									float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * 1.0F;
									blockpos$mutableblockpos.setPos(x, y, z);
									int j3 = WorldRenderer.getCombinedLight(world, blockpos$mutableblockpos);
									int k3 = j3 >> 16 & 65535;
									int l3 = j3 & 65535;
									bufferbuilder.pos((double) x - rx + 0.5D, (double) maxY, (double) z - ry + 0.5D).tex(0.0F, (float) minY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
									bufferbuilder.pos((double) x + rx + 0.5D, (double) maxY, (double) z + ry + 0.5D).tex(1.0F, (float) minY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
									bufferbuilder.pos((double) x + rx + 0.5D, (double) minY, (double) z + ry + 0.5D).tex(1.0F, (float) maxY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
									bufferbuilder.pos((double) x - rx + 0.5D, (double) minY, (double) z - ry + 0.5D).tex(0.0F, (float) maxY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
								} break;
								case MOSQUITO: {
									float d8 = 0; // TF - no wiggle
									float d9 = random.nextFloat() + combinedTicks * 0.01F * (float) random.nextGaussian();
									float d10 = random.nextFloat() + (combinedTicks * (float) random.nextGaussian()) * 0.001F;
									double d11 = (double) ((float) x + 0.5F) - xIn;
									double d12 = (double) ((float) z + 0.5F) - zIn;
									float f6 = MathHelper.sqrt(d11 * d11 + d12 * d12) / (float) range;
									float r = random.nextFloat() * 0.3F; // TF - random color
									float g = random.nextFloat() * 0.3F;
									float b = random.nextFloat() * 0.3F;
									float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * 1.0F;
									int i4 = 15 << 20 | 15 << 4; // TF - fullbright
									int j4 = i4 >> 16 & 65535;
									int k4 = i4 & 65535;
									bufferbuilder.pos((double) x - rx + 0.5D, (double) maxY, (double) z - ry + 0.5D).tex(0.0F + d9, (float) minY * 0.25F + d8 + d10).color(r, g, b, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos((double) x + rx + 0.5D, (double) maxY, (double) z + ry + 0.5D).tex(1.0F + d9, (float) minY * 0.25F + d8 + d10).color(r, g, b, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos((double) x + rx + 0.5D, (double) minY, (double) z + ry + 0.5D).tex(1.0F + d9, (float) maxY * 0.25F + d8 + d10).color(r, g, b, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos((double) x - rx + 0.5D, (double) minY, (double) z - ry + 0.5D).tex(0.0F + d9, (float) maxY * 0.25F + d8 + d10).color(r, g, b, f5).lightmap(j4, k4).endVertex();
								} break;
								case ASHES: {
									float d8 = -((float) (this.rendererUpdateCount & 511) + partialTicks) / 512.0F;
									float d9 = random.nextFloat() + combinedTicks * 0.01F * (float) random.nextGaussian();
									float d10 = random.nextFloat() + (combinedTicks * (float) random.nextGaussian()) * 0.001F;
									double d11 = (double) ((float) x + 0.5F) - xIn;
									double d12 = (double) ((float) z + 0.5F) - zIn;
									float f6 = MathHelper.sqrt(d11 * d11 + d12 * d12) / (float) range;
									float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * 1.0F;
									int i4 = 15 << 20 | 15 << 4; // TF - fullbright
									int j4 = i4 >> 16 & 65535;
									int k4 = i4 & 65535;
									float color = random.nextFloat() * 0.2F + 0.8F; // TF - random color
									bufferbuilder.pos((double) x - rx + 0.5D, (double) maxY, (double) z - ry + 0.5D).tex(0.0F + d9, (float) minY * 0.25F + d8 + d10).color(color, color, color, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos((double) x + rx + 0.5D, (double) maxY, (double) z + ry + 0.5D).tex(1.0F + d9, (float) minY * 0.25F + d8 + d10).color(color, color, color, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos((double) x + rx + 0.5D, (double) minY, (double) z + ry + 0.5D).tex(1.0F + d9, (float) maxY * 0.25F + d8 + d10).color(color, color, color, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos((double) x - rx + 0.5D, (double) minY, (double) z - ry + 0.5D).tex(0.0F + d9, (float) maxY * 0.25F + d8 + d10).color(color, color, color, f5).lightmap(j4, k4).endVertex();
								} break;
								case DARK_STREAM: {
									float d8 = -((float) (this.rendererUpdateCount & 511) + partialTicks) / 512.0F;
									float d9 = 0; // TF - no u wiggle
									float d10 = random.nextFloat() + (combinedTicks * (float) random.nextGaussian()) * 0.001F;
									double d11 = (double) ((float) x + 0.5F) - xIn;
									double d12 = (double) ((float) z + 0.5F) - zIn;
									float f6 = MathHelper.sqrt(d11 * d11 + d12 * d12) / (float) range;
									float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * random.nextFloat(); // TF - random alpha multiplier
									int i4 = 15 << 20 | 15 << 4; // TF - fullbright
									int j4 = i4 >> 16 & 65535;
									int k4 = i4 & 65535;
									bufferbuilder.pos((double) x - rx + 0.5D, (double) maxY, (double) z - ry + 0.5D).tex(0.0F + d9, (float) minY * 0.25F + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos((double) x + rx + 0.5D, (double) maxY, (double) z + ry + 0.5D).tex(1.0F + d9, (float) minY * 0.25F + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos((double) x + rx + 0.5D, (double) minY, (double) z + ry + 0.5D).tex(1.0F + d9, (float) maxY * 0.25F + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
									bufferbuilder.pos((double) x - rx + 0.5D, (double) minY, (double) z - ry + 0.5D).tex(0.0F + d9, (float) maxY * 0.25F + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
								} break;
								case BIG_RAIN: {
									float d5 = -((this.rendererUpdateCount + x * x * 3121 + x * 45238971 + z * z * 418711 + z * 13761 & 31) + partialTicks) / 32.0F * (3.0F + random.nextFloat());
									double d6 = (double) ((float) x + 0.5F) - xIn;
									double d7 = (double) ((float) z + 0.5F) - zIn;
									float f3 = MathHelper.sqrt(d6 * d6 + d7 * d7) / (float) range;
									float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * 1.0F;
									blockpos$mutableblockpos.setPos(x, y, z);
									int j3 = WorldRenderer.getCombinedLight(world, blockpos$mutableblockpos);
									int k3 = j3 >> 16 & 65535;
									int l3 = j3 & 65535;
									bufferbuilder.pos((double) x - rx + 0.5D, (double) maxY, (double) z - ry + 0.5D).tex(0.0F, (float) minY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
									bufferbuilder.pos((double) x + rx + 0.5D, (double) maxY, (double) z + ry + 0.5D).tex(1.0F, (float) minY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
									bufferbuilder.pos((double) x + rx + 0.5D, (double) minY, (double) z + ry + 0.5D).tex(1.0F, (float) maxY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
									bufferbuilder.pos((double) x - rx + 0.5D, (double) minY, (double) z - ry + 0.5D).tex(0.0F, (float) maxY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
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
					double d3 = (double) this.rainxs[i2] * 0.5D;
					double d4 = (double) this.rainys[i2] * 0.5D;

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
							Random random = new Random((long) (l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761));
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
							double d6 = (double) ((float) l1 + 0.5F) - xIn;
							double d7 = (double) ((float) k1 + 0.5F) - zIn;
							float f3 = MathHelper.sqrt(d6 * d6 + d7 * d7) / (float) i1;
							// TF - "f" was rain strength for alpha
							float f = random.nextFloat();
							float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * f;
							int j3 = 15 << 20 | 15 << 4; // TF - fullbright
							int k3 = j3 >> 16 & 65535;
							int l3 = j3 & 65535;
							bufferbuilder.pos((double) l1 - d3 + 0.5D, (double) l2, (double) k1 - d4 + 0.5D).tex(0.0F, (float) k2 * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
							bufferbuilder.pos((double) l1 + d3 + 0.5D, (double) l2, (double) k1 + d4 + 0.5D).tex(1.0F, (float) k2 * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
							bufferbuilder.pos((double) l1 + d3 + 0.5D, (double) k2, (double) k1 + d4 + 0.5D).tex(1.0F, (float) l2 * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
							bufferbuilder.pos((double) l1 - d3 + 0.5D, (double) k2, (double) k1 - d4 + 0.5D).tex(0.0F, (float) l2 * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
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

		if (this.protectedBox != null && this.protectedBox.intersectsWith(px - range, pz - range, px + range, pz + range)) {
			return true;
		}

		return false;
	}

	public void setProtectedBox(MutableBoundingBox protectedBox) {
		this.protectedBox = protectedBox;
	}

	// TODO: move to biome
	// or just anywhere, really
	private RenderType getRenderType(Biome biome) {
		/*if (biome instanceof TFBiomeSnow || biome instanceof TFBiomeGlacier) {
			return RenderType.BLIZZARD;
		} else if (biome instanceof TFBiomeSwamp) {
			return RenderType.MOSQUITO;
		} else if (biome instanceof TFBiomeFireSwamp) {
			return RenderType.ASHES;
		} else if (biome instanceof TFBiomeDarkForest) {
			return random.nextInt(2) == 0 ? RenderType.DARK_STREAM : null;
		} else if (biome instanceof TFBiomeHighlands || biome instanceof TFBiomeThornlands || biome instanceof TFBiomeFinalPlateau) {
			return RenderType.BIG_RAIN;
		}*/
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
