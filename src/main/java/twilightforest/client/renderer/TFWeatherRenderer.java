package twilightforest.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.custom.Enforcements;
import twilightforest.util.Enforcement;
import twilightforest.init.custom.Restrictions;
import twilightforest.util.LandmarkUtil;
import twilightforest.util.Restriction;

import java.util.Optional;
import java.util.Random;

/**
 * Copypasta of EntityRenderer.renderRainSnow() hacked to include progression environmental effects
 */
public class TFWeatherRenderer {

	public static final ResourceLocation RAIN_TEXTURES = new ResourceLocation("textures/environment/rain.png");
	public static final ResourceLocation SNOW_TEXTURES = new ResourceLocation("textures/environment/snow.png");

	private static final ResourceLocation SPARKLES_TEXTURE = TwilightForestMod.getEnvTexture("sparkles.png");

	public static final float[] rainxs = new float[1024];
	public static final float[] rainzs = new float[1024];

	private static int rendererUpdateCount;
	@Nullable
	private static BoundingBox protectedBox;

	private static final Random random = new Random();

	private static float urGhastRain = 0.0F;
	public static boolean urGhastAlive = false;

	public TFWeatherRenderer() {
		for (int i = 0; i < 32; ++i) {
			for (int j = 0; j < 32; ++j) {
				float f  = j - 16;
				float f1 = i - 16;
				float f2 = Mth.sqrt(f * f + f1 * f1);
				rainxs[i << 5 | j] = -f1 / f2;
				rainzs[i << 5 | j] =   f / f2;
			}
		}
	}

	public static void tick(ClientLevel level) {
		if (level.tickRateManager().runsNormally()) {
			++rendererUpdateCount;
		}
	}

	//Helpful tip: x, y, and z relate to the looking entity's position.
	public static boolean renderSnowAndRain(ClientLevel level, int ticks, float partialTicks, LightTexture lightmap, double camX, double camY, double camZ) {
		Minecraft mc = Minecraft.getInstance();
		// do normal weather rendering

		renderNormalWeather(lightmap, level, partialTicks, camX, camY, camZ, Math.max(urGhastRain, level.getRainLevel(partialTicks)));

		if (LandmarkUtil.isProgressionEnforced(level) && mc.player != null && !mc.player.isCreative() && !mc.player.isSpectator()) {
			// locked biome weather effects
			renderLockedBiome(partialTicks, level, mc, lightmap, mc.player, camX, camY, camZ);

			// locked structures
			renderLockedStructure(partialTicks, mc, lightmap, camX, camY, camZ);
		}
		return true;
	}

	public static void renderNormalWeather(LightTexture lightmap, ClientLevel level, float ticks, double x, double y, double z, float rainLevel) {
		if (rainLevel > 0.0F) {
			lightmap.turnOnLightLayer();
			int i = Mth.floor(x);
			int j = Mth.floor(y);
			int k = Mth.floor(z);
			Tesselator tesselator = Tesselator.getInstance();
			BufferBuilder bufferbuilder = tesselator.getBuilder();
			RenderSystem.disableCull();
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();

			int range = 5;
			if (Minecraft.useFancyGraphics()) {
				range = 10;
			}

			RenderSystem.depthMask(Minecraft.useShaderTransparency());
			int i1 = -1;
			float f1 = (float)rendererUpdateCount + ticks;
			RenderSystem.setShader(GameRenderer::getParticleShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

			for(int j1 = k - range; j1 <= k + range; ++j1) {
				for(int k1 = i - range; k1 <= i + range; ++k1) {
					int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
					double d0 = (double)rainxs[l1] * 0.5D;
					double d1 = (double)rainzs[l1] * 0.5D;
					blockpos$mutableblockpos.set(k1, 0, j1);
					Biome biome = level.getBiome(blockpos$mutableblockpos).value();
					if (biome.hasPrecipitation()) {
						int i2 = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos$mutableblockpos).getY();
						int j2 = j - range;
						int k2 = j + range;
						if (j2 < i2) {
							j2 = i2;
						}

						if (k2 < i2) {
							k2 = i2;
						}

						int l2 = Math.max(i2, j);

						if (j2 != k2) {
							Random random = new Random((long) k1 * k1 * 3121 + k1 * 45238971L ^ (long) j1 * j1 * 418711 + j1 * 13761L);
							blockpos$mutableblockpos.set(k1, j2, j1);
							if (biome.warmEnoughToRain(blockpos$mutableblockpos)) {
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
								float f4 = (float)Math.sqrt(d2 * d2 + d4 * d4) / (float)range;
								float f5 = ((1.0F - f4 * f4) * 0.5F + 0.5F) * rainLevel;
								blockpos$mutableblockpos.set(k1, l2, j1);
								int j3 = LevelRenderer.getLightColor(level, blockpos$mutableblockpos);
								bufferbuilder.vertex((double)k1 - x - d0 + 0.5D, (double)k2 - y, (double)j1 - z - d1 + 0.5D).uv(0.0F, (float)j2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).uv2(j3).endVertex();
								bufferbuilder.vertex((double)k1 - x + d0 + 0.5D, (double)k2 - y, (double)j1 - z + d1 + 0.5D).uv(1.0F, (float)j2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).uv2(j3).endVertex();
								bufferbuilder.vertex((double)k1 - x + d0 + 0.5D, (double)j2 - y, (double)j1 - z + d1 + 0.5D).uv(1.0F, (float)k2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).uv2(j3).endVertex();
								bufferbuilder.vertex((double)k1 - x - d0 + 0.5D, (double)j2 - y, (double)j1 - z - d1 + 0.5D).uv(0.0F, (float)k2 * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).uv2(j3).endVertex();
							} else {
								if (i1 != 1) {
									if (i1 == 0) {
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
								float f9 = (float)Math.sqrt(d3 * d3 + d5 * d5) / (float)range;
								float f10 = ((1.0F - f9 * f9) * 0.3F + 0.5F) * rainLevel;
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
	private static void renderLockedBiome(float partialTicks, ClientLevel level, Minecraft mc, LightTexture lightmap, LocalPlayer player, double xIn, double yIn, double zIn) {
		// check nearby for locked biome
		if (isNearLockedBiome(level, player)) {

			lightmap.turnOnLightLayer();

			int x0 = Mth.floor(xIn);
			int y0 = Mth.floor(yIn);
			int z0 = Mth.floor(zIn);

			Tesselator tessellator = Tesselator.getInstance();
			BufferBuilder bufferBuilder = tessellator.getBuilder();

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
			float combinedTicks = rendererUpdateCount + partialTicks;
			RenderSystem.setShader(GameRenderer::getParticleShader);
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

			for (int z = z0 - range; z <= z0 + range; ++z) {
				for (int x = x0 - range; x <= x0 + range; ++x) {

					int idx = (z - z0 + 16) * 32 + x - x0 + 16;
					double rx = rainxs[idx] * 0.5D;
					double ry = rainzs[idx] * 0.5D;

					blockpos$mutableblockpos.set(x, 0, z);
					Biome biome = level.getBiome(blockpos$mutableblockpos).value();

					// TF - check for our own biomes
					Optional<Restriction> restriction = Restriction.getRestrictionForBiome(biome, player);
					if (restriction.isPresent()) {
						int groundY = 0; // TF - extend through full height
						int minY = y0 - range;
						int maxY = y0 + range;

						if (minY < groundY) {
							minY = groundY;
						}

						if (maxY < groundY) {
							maxY = groundY;
						}


						if (minY != maxY) {

							random.setSeed((long) x * x * 3121 + x * 45238971L ^ (long) z * z * 418711 + z * 13761L);

							// TF - replace temperature check with biome check
							RenderType nextType = getRenderType(restriction.get());
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
								bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
							}

							// TF - replicate for each render type with own changes
							switch (currentType) {
								case BLIZZARD, BIG_RAIN -> {
									float d5 = -((rendererUpdateCount + x * x * 3121 + x * 45238971 + z * z * 418711 + z * 13761 & 31) + partialTicks) / 32.0F * (3.0F + random.nextFloat());
									double d6 = x + 0.5F - xIn;
									double d7 = z + 0.5F - zIn;
									float f3 = Mth.sqrt((float) (d6 * d6 + d7 * d7)) / range;
									float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F);
									blockpos$mutableblockpos.set(x, maxY, z);
									int j3 = LevelRenderer.getLightColor(level, blockpos$mutableblockpos);
									int k3 = j3 >> 16 & 65535;
									int l3 = j3 & 65535;
									bufferBuilder.vertex(x - xIn - rx + 0.5D, maxY - yIn, z - zIn - ry + 0.5D).uv(0.0F, minY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).uv2(k3, l3).endVertex();
									bufferBuilder.vertex(x - xIn + rx + 0.5D, maxY - yIn, z - zIn + ry + 0.5D).uv(1.0F, minY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).uv2(k3, l3).endVertex();
									bufferBuilder.vertex(x - xIn + rx + 0.5D, minY - yIn, z - zIn + ry + 0.5D).uv(1.0F, maxY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).uv2(k3, l3).endVertex();
									bufferBuilder.vertex(x - xIn - rx + 0.5D, minY - yIn, z - zIn - ry + 0.5D).uv(0.0F, maxY * 0.25F + d5).color(1.0F, 1.0F, 1.0F, f4).uv2(k3, l3).endVertex();
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
									bufferBuilder.vertex(x - xIn - rx + 0.5D, maxY - yIn, z - zIn - ry + 0.5D).uv(0.0F + d9, minY * 0.25F + d8 + d10).color(r, g, b, f5).uv2(j4, k4).endVertex();
									bufferBuilder.vertex(x - xIn + rx + 0.5D, maxY - yIn, z - zIn + ry + 0.5D).uv(1.0F + d9, minY * 0.25F + d8 + d10).color(r, g, b, f5).uv2(j4, k4).endVertex();
									bufferBuilder.vertex(x - xIn + rx + 0.5D, minY - yIn, z - zIn + ry + 0.5D).uv(1.0F + d9, maxY * 0.25F + d8 + d10).color(r, g, b, f5).uv2(j4, k4).endVertex();
									bufferBuilder.vertex(x - xIn - rx + 0.5D, minY - yIn, z - zIn - ry + 0.5D).uv(0.0F + d9, maxY * 0.25F + d8 + d10).color(r, g, b, f5).uv2(j4, k4).endVertex();
								}
								case ASHES -> {
									float d8 = -((rendererUpdateCount & 511) + partialTicks) / 512.0F;
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
									bufferBuilder.vertex(x - xIn - rx + 0.5D, maxY - yIn, z - zIn - ry + 0.5D).uv(0.0F + d9, minY * 0.25F + d8 + d10).color(color, color, color, f5).uv2(j4, k4).endVertex();
									bufferBuilder.vertex(x - xIn + rx + 0.5D, maxY - yIn, z - zIn + ry + 0.5D).uv(1.0F + d9, minY * 0.25F + d8 + d10).color(color, color, color, f5).uv2(j4, k4).endVertex();
									bufferBuilder.vertex(x - xIn + rx + 0.5D, minY - yIn, z - zIn + ry + 0.5D).uv(1.0F + d9, maxY * 0.25F + d8 + d10).color(color, color, color, f5).uv2(j4, k4).endVertex();
									bufferBuilder.vertex(x - xIn - rx + 0.5D, minY - yIn, z - zIn - ry + 0.5D).uv(0.0F + d9, maxY * 0.25F + d8 + d10).color(color, color, color, f5).uv2(j4, k4).endVertex();
								}
								case DARK_STREAM -> {
									float d8 = -((rendererUpdateCount & 511) + partialTicks) / 512.0F;
									float d9 = 0; // TF - no u wiggle
									float d10 = random.nextFloat() + (combinedTicks * (float) random.nextGaussian()) * 0.001F;
									double d11 = x + 0.5F - xIn;
									double d12 = z + 0.5F - zIn;
									float f6 = Mth.sqrt((float) (d11 * d11 + d12 * d12)) / range;
									float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * random.nextFloat(); // TF - random alpha multiplier
									int i4 = 15 << 20 | 15 << 4; // TF - fullbright
									int j4 = i4 >> 16 & 65535;
									int k4 = i4 & 65535;
									float z1 = (float)yIn * 0.125F;
									bufferBuilder.vertex(x - xIn - rx + 0.5D, range, z - zIn - ry + 0.5D).uv(0.0F + d9, d8 + d10 - z1).color(1.0F, 1.0F, 1.0F, f5).uv2(j4, k4).endVertex();
									bufferBuilder.vertex(x - xIn + rx + 0.5D, range, z - zIn + ry + 0.5D).uv(1.0F + d9, d8 + d10 - z1).color(1.0F, 1.0F, 1.0F, f5).uv2(j4, k4).endVertex();
									float pV = range * 0.25F + d8 + d10 - z1;
									bufferBuilder.vertex(x - xIn + rx + 0.5D, -range, z - zIn + ry + 0.5D).uv(1.0F + d9, pV).color(1.0F, 1.0F, 1.0F, f5).uv2(j4, k4).endVertex();
									bufferBuilder.vertex(x - xIn - rx + 0.5D, -range, z - zIn - ry + 0.5D).uv(0.0F + d9, pV).color(1.0F, 1.0F, 1.0F, f5).uv2(j4, k4).endVertex();
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
	private static void renderLockedStructure(float partialTicks, Minecraft mc, LightTexture lightmap, double xIn, double yIn, double zIn) {
		// draw locked structure thing
		if (isNearLockedStructure(xIn, zIn)) {
			lightmap.turnOnLightLayer();
			int i = Mth.floor(xIn);
			int k = Mth.floor(zIn);
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
			int j1 = -1;
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

			for (int k1 = k - range; k1 <= k + range; ++k1) {
				for (int l1 = i - range; l1 <= i + range; ++l1) {
					int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
					double d3 = rainxs[i2] * 0.5D;
					double d4 = rainzs[i2] * 0.5D;

					// TF - replace biome check with box check
					if (protectedBox != null && protectedBox.intersects(l1, k1, l1, k1)) {
						int structureMin = protectedBox.minY() - 4;
						int structureMax = protectedBox.maxY() + 4;
						float k2 = (float) yIn - range;
						float l2 = (float) yIn + range * 2;

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

							float d5 = -((l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 31) + rendererUpdateCount + partialTicks) / 32.0F * (3.0F + random.nextFloat()) - k2 * 0.5F;
							double d6 = l1 + 0.5F - xIn;
							double d7 = k1 + 0.5F - zIn;
							float f3 = Math.min(Mth.sqrt((float) (d6 * d6 + d7 * d7)) / range, 1.0F);
							// TF - "f" was rain strength for alpha
							float f = random.nextFloat();
							float f4 = Math.min((1.0F - f3 * f3) * 2.0F, 1.0F) * f;
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

			if (j1 == 0) {
				tessellator.end();
			}

			RenderSystem.enableCull();
			RenderSystem.disableBlend();
			lightmap.turnOffLightLayer();
		}
	}

	private static boolean isNearLockedBiome(Level world, Entity viewEntity) {
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		final int range = 15;
		int px = Mth.floor(viewEntity.getX());
		int pz = Mth.floor(viewEntity.getZ());

		for (int z = pz - range; z <= pz + range; ++z) {
			for (int x = px - range; x <= px + range; ++x) {
				Biome biome = world.getBiome(pos.set(x, 0, z)).value();
				if (!Restriction.isBiomeSafeFor(biome, viewEntity)) {
					return true;
				}
			}
		}

		return false;
	}

	private static boolean isNearLockedStructure(double xIn, double zIn) {
		final int range = 15;
		int px = Mth.floor(xIn);
		int pz = Mth.floor(zIn);

		return protectedBox != null && protectedBox.intersects(px - range, pz - range, px + range, pz + range);
	}

	public static void setProtectedBox(@Nullable BoundingBox protectedBox) {
		TFWeatherRenderer.protectedBox = protectedBox;
	}

	private static @Nullable RenderType getRenderType(Restriction restriction) {
		if (restriction.enforcement().equals(Enforcements.FROST.getKey())) return RenderType.BLIZZARD;
		else if (restriction.enforcement().equals(Enforcements.HUNGER.getKey())) return RenderType.MOSQUITO;
		else if (restriction.enforcement().equals(Enforcements.FIRE.getKey())) return RenderType.ASHES;
		else if (restriction.enforcement().equals(Enforcements.DARKNESS.getKey())) return random.nextBoolean() ? RenderType.DARK_STREAM : null;
		else if (restriction.enforcement().equals(Enforcements.ACID_RAIN.getKey())) return RenderType.BIG_RAIN;
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

	/**
	 * [VanillaCopy]:<br>
	 * {@link net.minecraft.client.renderer.LevelRenderer#tickRain(Camera)}<br>
	 */
	public static boolean tickRain(ClientLevel level, int ticks, BlockPos blockpos) {
		if (urGhastAlive) {
			urGhastRain = Math.min(1.0F, urGhastRain + 0.1F);
			urGhastAlive = false;
		} else urGhastRain = Math.max(0.0F, urGhastRain - 0.02F);


		float rainLevel = Math.max(level.getRainLevel(1.0F), urGhastRain) / (Minecraft.useFancyGraphics() ? 1.0F : 2.0F);
		if (rainLevel > 0.0F) {
			RandomSource randomsource = RandomSource.create((long)ticks * 312987231L);
			BlockPos blockpos1 = null;
			int i = (int)(100.0F * rainLevel * rainLevel) / (Minecraft.getInstance().options.particles().get() == ParticleStatus.DECREASED ? 2 : 1);

			for(int j = 0; j < i; ++j) {
				int k = randomsource.nextInt(21) - 10;
				int l = randomsource.nextInt(21) - 10;
				BlockPos blockpos2 = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos.offset(k, 0, l));
				Biome biome = level.getBiome(blockpos2).value();
				if (blockpos2.getY() > level.getMinBuildHeight() && blockpos2.getY() <= blockpos.getY() + 10 && blockpos2.getY() >= blockpos.getY() - 10 && biome.hasPrecipitation() && biome.warmEnoughToRain(blockpos2)) {
					blockpos1 = blockpos2.below();
					if (Minecraft.getInstance().options.particles().get() == ParticleStatus.MINIMAL) {
						break;
					}

					double d0 = randomsource.nextDouble();
					double d1 = randomsource.nextDouble();
					BlockState blockstate = level.getBlockState(blockpos1);
					FluidState fluidstate = level.getFluidState(blockpos1);
					VoxelShape voxelshape = blockstate.getCollisionShape(level, blockpos1);
					double d2 = voxelshape.max(Direction.Axis.Y, d0, d1);
					double d3 = fluidstate.getHeight(level, blockpos1);
					double d4 = Math.max(d2, d3);
					ParticleOptions particleoptions = !fluidstate.is(FluidTags.LAVA) && !blockstate.is(Blocks.MAGMA_BLOCK) && !CampfireBlock.isLitCampfire(blockstate) ? ParticleTypes.RAIN : ParticleTypes.SMOKE;
					level.addParticle(particleoptions, (double)blockpos1.getX() + d0, (double)blockpos1.getY() + d4, (double)blockpos1.getZ() + d1, 0.0D, 0.0D, 0.0D);
				}
			}

			if (blockpos1 != null && randomsource.nextInt(4) < Minecraft.getInstance().levelRenderer.rainSoundTime++) {
				Minecraft.getInstance().levelRenderer.rainSoundTime = 0;
				if (blockpos1.getY() > blockpos.getY() + 1 && level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos).getY() > Mth.floor((float)blockpos.getY())) {
					level.playLocalSound(blockpos1, SoundEvents.WEATHER_RAIN_ABOVE, SoundSource.WEATHER, 0.1F, 0.5F, false);
				} else {
					level.playLocalSound(blockpos1, SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.2F, 1.0F, false);
				}
			}

		}
		return true;
	}
}
