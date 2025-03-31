package twilightforest.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.Pair;
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
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.custom.Enforcements;
import twilightforest.util.IntervalUtils;
import twilightforest.util.landmarks.LandmarkUtil;
import twilightforest.util.Restriction;

import java.util.*;

/**
 * Copypasta of LevelRenderer.renderRainSnow() hacked to include progression environmental effects
 */
public class TFWeatherRenderer {

	public static final ResourceLocation RAIN_TEXTURES = ResourceLocation.withDefaultNamespace("textures/environment/rain.png");
	public static final ResourceLocation SNOW_TEXTURES = ResourceLocation.withDefaultNamespace("textures/environment/snow.png");

	private static final ResourceLocation SPARKLES_TEXTURE = TwilightForestMod.getEnvTexture("sparkles.png");

	public static final float[] rainxs = new float[1024];
	public static final float[] rainzs = new float[1024];

	@Nullable
	private static List<Pair<BoundingBox, Boolean>> boxData;
	@Nullable
	private static HashMap<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> rainIntervals;
	@Nullable
	private static BoundingBox pBoxOld;

	private static final RandomSource random = RandomSource.create();

	private static float urGhastRain = 0.0F;
	public static boolean urGhastAlive = false;

	static {
		for (int i = 0; i < 32; ++i) {
			for (int j = 0; j < 32; ++j) {
				float f = j - 16;
				float f1 = i - 16;
				float f2 = Mth.sqrt(f * f + f1 * f1);
				rainxs[i << 5 | j] = -f1 / f2;
				rainzs[i << 5 | j] = f / f2;
			}
		}
	}

	public static boolean renderSnowAndRain(ClientLevel level, int ticks, float partialTicks, LightTexture lightmap, Vec3 camera) {
		Minecraft mc = Minecraft.getInstance();
		if (LandmarkUtil.isProgressionEnforced(level) && mc.player != null && !mc.player.isCreative() && !mc.player.isSpectator()) {
			// locked biome weather effects
			renderLockedBiome(ticks, partialTicks, level, lightmap, mc.player, camera);

			// locked structures
			renderLockedStructure(ticks, partialTicks, lightmap, camera);
		}

		//render normal weather anyway
		return false;
	}

	private static void renderLockedBiome(int ticks, float partialTicks, ClientLevel level, LightTexture lightmap, LocalPlayer player, Vec3 camera) {
		// check nearby for locked biome
		if (isNearLockedBiome(level, player)) {
			lightmap.turnOnLightLayer();

			int px = Mth.floor(camera.x());
			int py = Mth.floor(camera.y());
			int pz = Mth.floor(camera.z());

			Tesselator tessellator = Tesselator.getInstance();
			BufferBuilder bufferBuilder = null;

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
			float combinedTicks = ticks + partialTicks;
			RenderSystem.setShader(GameRenderer::getParticleShader);
			BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

			for (int dz = pz - range; dz <= pz + range; ++dz) {
				for (int dx = px - range; dx <= px + range; ++dx) {
					int rainIndex = (dz - pz + 16) * 32 + dx - px + 16;
					double rainX = rainxs[rainIndex] * 0.5D;
					double rainZ = rainzs[rainIndex] * 0.5D;

					pos.set(dx, 0, dz);
					Biome biome = level.getBiome(pos).value();

					Optional<Restriction> restriction = Restriction.getRestrictionForBiome(biome, player);
					if (restriction.isPresent()) {
						int groundY = level.getMinBuildHeight();
						int minY = py - range;
						int maxY = py + range;

						if (minY < groundY) {
							minY = groundY;
						}

						if (maxY < groundY) {
							maxY = groundY;
						}


						if (minY != maxY) {

							random.setSeed((long) dx * dx * 3121 + dx * 45238971L ^ (long) dz * dz * 418711 + dz * 13761L);

							RenderType nextType = getRenderType(restriction.get());
							if (nextType == null) {
								continue;
							}

							if (currentType != nextType) {
								if (currentType != null) {
									BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
								}
								currentType = nextType;
								RenderSystem.setShaderTexture(0, nextType.getTextureLocation());
								bufferBuilder = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
							}

							double xRange = (double) ((float) dx + 0.5F) - camera.x();
							double zRange = (double) ((float) dz + 0.5F) - camera.z();
							float distanceToPlayer = Mth.sqrt((float) (xRange * xRange + zRange * zRange)) / (float) range;
							float alpha = ((1.0F - distanceToPlayer * distanceToPlayer) * 0.3F + 0.5F);
							int worldBrightness = LevelRenderer.getLightColor(level, pos);
							int fullbright = 15 << 20 | 15 << 4;

							switch (currentType) {
								case BLIZZARD -> {
									float countFactor = ((float) (ticks & 511) + partialTicks) / 512.0F;
									float uFactor = random.nextFloat() + combinedTicks * 0.05F * (float) random.nextGaussian();
									float vFactor = random.nextFloat() + combinedTicks * 0.0025F * (float) random.nextGaussian();
									renderEffect(bufferBuilder, rainX, rainZ, minY, maxY, camera, dx, dz, countFactor, uFactor, vFactor, new float[]{1.0F, 1.0F, 1.0F, alpha}, fullbright);
								}
								case MOSQUITO -> {
									float countFactor = 0;
									float uFactor = random.nextFloat() + combinedTicks * 0.03F * (float) random.nextGaussian();
									float vFactor = random.nextFloat() + combinedTicks * 0.003F * (float) random.nextGaussian();
									float red = random.nextFloat() * 0.3F;
									float green = random.nextFloat() * 0.3F;
									float blue = random.nextFloat() * 0.3F;
									renderEffect(bufferBuilder, rainX, rainZ, minY, maxY, camera, dx, dz, countFactor, uFactor, vFactor, new float[]{red, green, blue, 1.0F}, fullbright);
								}
								case ASHES -> {
									float countFactor = -((float) (ticks & 1023) + partialTicks) / 1024.0F;
									float uFactor = random.nextFloat() + combinedTicks * 0.0025F * (float) random.nextGaussian();
									float vFactor = random.nextFloat() + combinedTicks * 0.005F * (float) random.nextGaussian();
									float color = random.nextFloat() * 0.2F + 0.8F;
									renderEffect(bufferBuilder, rainX, rainZ, minY, maxY, camera, dx, dz, countFactor, uFactor, vFactor, new float[]{color, color, color, alpha}, fullbright);
								}
								case DARK_STREAM -> {
									float countFactor = -((ticks & 511) + partialTicks) / 512.0F;
									float uFactor = 0; //no moving horizontally
									float vFactor = random.nextFloat() + combinedTicks * 0.005F * (float) random.nextGaussian();
									renderEffect(bufferBuilder, rainX, rainZ, minY, maxY, camera, dx, dz, countFactor, uFactor, vFactor, new float[]{1.0F, 1.0F, 1.0F, alpha}, fullbright);
								}
								case BIG_RAIN -> {
									float countFactor = ((float) (ticks + dx * dx * 3121 + dx * 45238971 + dz * dz * 418711 + dz * 13761 & 31) + partialTicks) / 32.0F * (3.0F + random.nextFloat());
									float uFactor = random.nextFloat();
									float vFactor = random.nextFloat();
									renderEffect(bufferBuilder, rainX, rainZ, minY, maxY, camera, dx, dz, countFactor, uFactor, vFactor, new float[]{1.0F, 1.0F, 1.0F, alpha}, worldBrightness);
								}
							}
						}
					}
				}
			}

			if (currentType != null) {
				BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
			}

			RenderSystem.enableCull();
			RenderSystem.disableBlend();
			lightmap.turnOffLightLayer();
		}
	}

	@SuppressWarnings("ConstantConditions")
	private static void renderLockedStructure(int ticks, float partialTicks, LightTexture lightmap, Vec3 camera) {
		int range = Minecraft.useFancyGraphics() ? 10 : 5;
		int px = Mth.floor(camera.x());
		int py = Mth.floor(camera.y());
		int pz = Mth.floor(camera.z());

		BoundingBox pBox = new BoundingBox(
			px - range, py - range, pz - range,
			px + range, py + 2 * range, pz + range
		);

		if (!isNearLockedPiece(pBox))
			return;

		if (!pBox.equals(pBoxOld)) {
			updateRainIntervals(pBox);
			pBoxOld = pBox;
		}

		lightmap.turnOnLightLayer();
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = null;

		RenderSystem.disableCull();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();

		float combinedTicks = ticks + partialTicks;
		int drawFlag = -1;
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		for (int x = pBox.minX(); x <= pBox.maxX(); x++) {
			for (int z = pBox.minZ(); z <= pBox.maxZ(); z++) {
				for (Pair<Integer, Integer> segment : rainIntervals.get(Pair.of(x, z))) {
					int rainMin = segment.getFirst();
					int rainMax = segment.getSecond();

					if (rainMin >= rainMax) continue;

					random.setSeed((long) x * x * 3121 + x * 45238971L ^ (long) z * z * 418711 + z * 13761L);
					if (drawFlag != 0) {
						drawFlag = 0;
						RenderSystem.setShader(GameRenderer::getParticleShader);
						RenderSystem.setShaderTexture(0, SPARKLES_TEXTURE);
						bufferbuilder = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
					}

					float countFactor = ((ticks & 511) + partialTicks) / 512.0F;
					float uFactor = random.nextFloat() + combinedTicks * 0.02F * (float) random.nextGaussian();
					float vFactor = random.nextFloat() + combinedTicks * 0.02F * (float) random.nextGaussian();
					double xRange = x + 0.5 - camera.x();
					double zRange = z + 0.5 - camera.z();
					float distanceFromPlayer = Mth.sqrt((float) (xRange * xRange + zRange * zRange)) / range;
					float alpha = ((1.0F - distanceFromPlayer * distanceFromPlayer) * 0.3F + 0.5F) * random.nextFloat();

					renderEffect(
						bufferbuilder,
						rainxs[(z - pz + 16) * 32 + x - px + 16] * 0.5,
						rainzs[(z - pz + 16) * 32 + x - px + 16] * 0.5,
						rainMin, rainMax,
						camera, x, z,
						countFactor, uFactor, vFactor,
						new float[] {1.0F, 1.0F, 1.0F, alpha},
						(15 << 20) | (15 << 4)
					);
				}
			}
		}

		if (drawFlag == 0) {
			BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
		}

		RenderSystem.enableCull();
		RenderSystem.disableBlend();
		lightmap.turnOffLightLayer();
	}

	private static void updateRainIntervals(BoundingBox pBox) {
		if (boxData == null) {
			rainIntervals = null;
			return;
		}

		List<Pair<BoundingBox, Boolean>> intersectingBoxesData = boxData.stream()
			.filter(boxData -> boxData.getFirst().intersects(pBox))
			.toList();
		List<BoundingBox> protectedBoxes = new ArrayList<>();
		List<BoundingBox> unprotectedBoxes = new ArrayList<>();
		for (Pair<BoundingBox, Boolean> pair : intersectingBoxesData) {
			if (pair.getSecond()) protectedBoxes.add(pair.getFirst());
			else unprotectedBoxes.add(pair.getFirst());
		}

		rainIntervals = new HashMap<>();
		for (int x = pBox.minX(); x <= pBox.maxX(); x++) {
			for (int z = pBox.minZ(); z <= pBox.maxZ(); z++) {
				List<Pair<Integer, Integer>> unprotectedIntervals = getRainIntervals(x, z, pBox, unprotectedBoxes);
				List<Pair<Integer, Integer>> protectedIntervals = getRainIntervals(x, z, pBox, protectedBoxes);

				List<Pair<Integer, Integer>> mergedUnprotected = IntervalUtils.mergeAndSortIntervals(unprotectedIntervals);
				List<Pair<Integer, Integer>> mergedProtected = IntervalUtils.mergeAndSortIntervals(protectedIntervals);

				rainIntervals.put(Pair.of(x, z), IntervalUtils.subtractIntervals(mergedProtected, mergedUnprotected));
			}
		}
	}

	private static List<Pair<Integer, Integer>> getRainIntervals(int x, int z, BoundingBox pBox, List<BoundingBox> boxes) {
		List<Pair<Integer, Integer>> intervals = new ArrayList<>();
		for (BoundingBox box : boxes) {
			if (!box.intersects(x, z, x, z)) continue;
			int rainMin = Math.max(pBox.minY(), box.minY());
			int rainMax = Math.min(pBox.maxY(), box.maxY() + 1);  // + 1 because renderEffect() renders from rainMin to rainMax as floats
			if (rainMin < rainMax) {
				intervals.add(Pair.of(rainMin, rainMax));
			}
		}
		return intervals;
	}

	private static void renderEffect(BufferBuilder bufferBuilder, double rainX, double rainZ, int minY, int maxY, Vec3 camera, int dx, int dz, float countFactor, float uFactor, float vFactor, float[] color, int light) {
		int blockLight = light >> 16 & 65535;
		int skyLight = light & 65535;
		bufferBuilder
			.addVertex((float) (dx - camera.x() - rainX + 0.5F), (float) (minY - camera.y()), (float) (dz - camera.z() - rainZ + 0.5F))
			.setUv(0.0F + uFactor, minY * 0.25F + countFactor + vFactor)
			.setColor(color[0], color[1], color[2], color[3])
			.setUv2(blockLight, skyLight);
		bufferBuilder
			.addVertex((float) (dx - camera.x() + rainX + 0.5F), (float) (minY - camera.y()), (float) (dz - camera.z() + rainZ + 0.5F))
			.setUv(1.0F + uFactor, minY * 0.25F + countFactor + vFactor)
			.setColor(color[0], color[1], color[2], color[3])
			.setUv2(blockLight, skyLight);
		bufferBuilder
			.addVertex((float) (dx - camera.x() + rainX + 0.5F), (float) (maxY - camera.y()), (float) (dz - camera.z() + rainZ + 0.5F))
			.setUv(1.0F + uFactor, maxY * 0.25F + countFactor + vFactor)
			.setColor(color[0], color[1], color[2], color[3])
			.setUv2(blockLight, skyLight);
		bufferBuilder
			.addVertex((float) (dx - camera.x() - rainX + 0.5F), (float) (maxY - camera.y()), (float) (dz - camera.z() - rainZ + 0.5F))
			.setUv(0.0F + uFactor, maxY * 0.25F + countFactor + vFactor)
			.setColor(color[0], color[1], color[2], color[3])
			.setUv2(blockLight, skyLight);
	}

	private static boolean isNearLockedBiome(Level level, Entity viewEntity) {
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		final int range = 15;
		int px = Mth.floor(viewEntity.getX());
		int pz = Mth.floor(viewEntity.getZ());

		for (int z = pz - range; z <= pz + range; ++z) {
			for (int x = px - range; x <= px + range; ++x) {
				Biome biome = level.getBiome(pos.set(x, 0, z)).value();
				if (!Restriction.isBiomeSafeFor(biome, viewEntity)) {
					return true;
				}
			}
		}

		return false;
	}

	private static boolean isNearLockedPiece(BoundingBox pBox) {
		return boxData != null && boxData.stream().anyMatch(boxData -> boxData.getFirst().intersects(pBox));
	}

	public static void setProtectedBoxes(@Nullable List<Pair<BoundingBox, Boolean>> protectedBoxes) {
		TFWeatherRenderer.boxData = protectedBoxes;
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
	public static boolean tickRain(ClientLevel level, int partialTicks, BlockPos blockpos) {
		//TF - render rain if the Ur-Ghast is alive as well
		if (urGhastAlive) {
			urGhastRain = Math.min(1.0F, urGhastRain + 0.1F);
			urGhastAlive = false;
		} else urGhastRain = Math.max(0.0F, urGhastRain - 0.02F);

		//TF - factor in the Ur-Ghast being alive when determining rain level
		float rainLevel = Math.max(level.getRainLevel(1.0F), urGhastRain) / (Minecraft.useFancyGraphics() ? 1.0F : 2.0F);
		if (rainLevel > 0.0F) {
			RandomSource randomsource = RandomSource.create((long) partialTicks * 312987231L);
			BlockPos blockpos1 = null;
			int i = (int) (100.0F * rainLevel * rainLevel) / (Minecraft.getInstance().options.particles().get() == ParticleStatus.DECREASED ? 2 : 1);

			for (int j = 0; j < i; ++j) {
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
					level.addParticle(particleoptions, (double) blockpos1.getX() + d0, (double) blockpos1.getY() + d4, (double) blockpos1.getZ() + d1, 0.0D, 0.0D, 0.0D);
				}
			}

			if (blockpos1 != null && randomsource.nextInt(4) < Minecraft.getInstance().levelRenderer.rainSoundTime++) {
				Minecraft.getInstance().levelRenderer.rainSoundTime = 0;
				if (blockpos1.getY() > blockpos.getY() + 1 && level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos).getY() > Mth.floor((float) blockpos.getY())) {
					level.playLocalSound(blockpos1, SoundEvents.WEATHER_RAIN_ABOVE, SoundSource.WEATHER, 0.1F, 0.5F, false);
				} else {
					level.playLocalSound(blockpos1, SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.2F, 1.0F, false);
				}
			}

		}
		return true;
	}
}
