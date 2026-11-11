package twilightforest.client.event;


import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.apache.commons.lang3.tuple.Pair;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import twilightforest.block.CloudBlock;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.config.TFConfig;
import twilightforest.util.Vec2i;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Component
public class CloudEvents {

	private final List<PrecipitationRenderHelper> RENDER_HELPER = new ArrayList<>();

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(this::renderPrecipitation);
		bus.addListener(this::tickWeatherEffects);
	}

	record PrecipitationRenderHelper(BlockPos cloudPos, Biome.Precipitation precipitation, float precipitationLevel, int rainOnY) {

	}

	private void tickWeatherEffects(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();

		if (!mc.isPaused()) {
			if (mc.level != null && TFConfig.getClientCloudBlockPrecipitationDistance() > 0) { // Semi vanilla copy of the weather tick, but made to work with cloud blocks instead
				Vec3 vec3 = mc.gameRenderer.mainCamera().position();
				if (mc.level.getGameTime() % 10L == 0L) {
					RENDER_HELPER.clear();

					double camX = vec3.x();
					double camY = vec3.y();
					double camZ = vec3.z();

					int floorX = Mth.floor(camX);
					int floorY = Mth.floor(camY);
					int floorZ = Mth.floor(camZ);

					int renderDistance = mc.options.weatherRadius().get();
					int precipitationDistance = TFConfig.getClientCloudBlockPrecipitationDistance();
					BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

					for (int roofZ = floorZ - renderDistance; roofZ <= floorZ + renderDistance; ++roofZ) {
						for (int roofX = floorX - renderDistance; roofX <= floorX + renderDistance; ++roofX) {
							int lastBadYLevel = Integer.MIN_VALUE;
							for (int roofY = floorY - renderDistance; roofY < floorY + precipitationDistance + renderDistance; roofY++) {
								boolean skipLoop = roofY == lastBadYLevel + 1; // Cloud can't rain if there is an invalid blockState right below it, so might as well skip the loop
								pos.set(roofX, roofY, roofZ);
								if (Heightmap.Types.MOTION_BLOCKING.isOpaque().test(mc.level.getBlockState(pos)))
									lastBadYLevel = roofY; // Check if we skip next loop
								if (skipLoop) continue;

								if (mc.level.getBlockState(pos).getBlock() instanceof CloudBlock cloudBlock) {
									Pair<Biome.Precipitation, Float> precipitationRainLevelPair = cloudBlock.getCurrentPrecipitation(pos, mc.level, mc.level.getRainLevel(1.0F));
									if (precipitationRainLevelPair.getLeft() == Biome.Precipitation.NONE)
										continue; // No rain no gain

									int highestRainyBlock = roofY;
									for (int y = roofY - 1; y > roofY - precipitationDistance; y--) {
										if (!Heightmap.Types.MOTION_BLOCKING.isOpaque().test(mc.level.getBlockState(pos.atY(y))))
											highestRainyBlock = y;
										else break;
									}
									if (highestRainyBlock == roofY) continue;

									RENDER_HELPER.add(new PrecipitationRenderHelper(pos.immutable(), precipitationRainLevelPair.getLeft(), precipitationRainLevelPair.getRight(), highestRainyBlock));
								}
							}
						}
					}
					RENDER_HELPER.sort(Comparator.comparing(PrecipitationRenderHelper::precipitation));
				}

				if (!RENDER_HELPER.isEmpty()) {
					RandomSource randomsource = RandomSource.create(mc.level.getGameTime() * 312987231L);
					BlockPos particlePos = null;
					int weatherRadius = mc.options.weatherRadius().get();
					int weatherDiameter = 2 * weatherRadius + 1;
					int weatherArea = weatherDiameter * weatherDiameter;
					int particleCount = (int) (0.225F * weatherArea) / (mc.options.particles().get() == ParticleStatus.DECREASED ? 2 : 1);

					boolean yetToMakeASound = true;
					BlockPos camPos = BlockPos.containing(vec3);

					List<Vec2i> particleChecks = new ArrayList<>();
					for (int i = 0; i < particleCount; ++i) {
						particleChecks.add(new Vec2i(randomsource.nextInt(weatherDiameter) - weatherRadius + camPos.getX(), randomsource.nextInt(weatherDiameter) - weatherRadius + camPos.getZ()));
					}

					for (PrecipitationRenderHelper helper : RENDER_HELPER) {
						if (helper.precipitation() == Biome.Precipitation.RAIN) {
							for (Vec2i vec2 : particleChecks) {
								if (vec2.x == helper.cloudPos().getX() && vec2.z == helper.cloudPos().getZ()) {
									BlockPos highestRainyPos = helper.cloudPos().atY(helper.rainOnY());
									if (!Heightmap.Types.MOTION_BLOCKING.isOpaque().test(mc.level.getBlockState(highestRainyPos.below())))
										continue;

									if (yetToMakeASound && particlePos != null && randomsource.nextInt(3) < mc.level.rainSoundTime++) {
										mc.level.rainSoundTime = 0;
										if (particlePos.getY() > camPos.getY() + 1 && mc.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, camPos).getY() > Mth.floor((float) camPos.getY())) {
											mc.level.playLocalSound(particlePos, SoundEvents.WEATHER_RAIN_ABOVE, SoundSource.WEATHER, 0.1F, 0.5F, false);
										} else {
											mc.level.playLocalSound(particlePos, SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.2F, 1.0F, false);
										}
										yetToMakeASound = false;
									}

									if (highestRainyPos.getY() > mc.level.getMinY() && highestRainyPos.getY() <= camPos.getY() + 10 && highestRainyPos.getY() >= camPos.getY() - 10) {
										particlePos = highestRainyPos.below();
										if (mc.options.particles().get() == ParticleStatus.MINIMAL) break;

										double particleX = randomsource.nextDouble();
										double particleZ = randomsource.nextDouble();
										BlockState blockstate = mc.level.getBlockState(particlePos);
										FluidState fluidstate = mc.level.getFluidState(particlePos);
										VoxelShape voxelshape = blockstate.getCollisionShape(mc.level, particlePos);
										double voxelMax = voxelshape.max(Direction.Axis.Y, particleX, particleZ);
										double fluidMax = fluidstate.getHeight(mc.level, particlePos);
										double particleY = Math.max(voxelMax, fluidMax);
										ParticleOptions particleoptions = !fluidstate.is(FluidTags.LAVA) && !blockstate.is(Blocks.MAGMA_BLOCK) && !CampfireBlock.isLitCampfire(blockstate) ? ParticleTypes.RAIN : ParticleTypes.SMOKE;
										mc.level.addParticle(particleoptions, (double) particlePos.getX() + particleX, (double) particlePos.getY() + particleY, (double) particlePos.getZ() + particleZ, 0.0D, 0.0D, 0.0D);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void renderPrecipitation(RenderLevelStageEvent.AfterWeather event) {
		if (TFConfig.getClientCloudBlockPrecipitationDistance() > 0 && !RENDER_HELPER.isEmpty()) {
			Minecraft minecraft = Minecraft.getInstance();
			if (minecraft.level == null) return;
			float partialTick = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
			int ticks = (int) (minecraft.level.getGameTime() & 131071L);

			Vec3 vec3 = event.getLevelRenderState().cameraRenderState.pos;
			double camX = vec3.x();
			double camY = vec3.y();
			double camZ = vec3.z();

			int floorY = Mth.floor(camY);

			int renderDistance = minecraft.options.weatherRadius().get();

			float fullTick = (float) ticks + partialTick;
			BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
			ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(RENDER_HELPER.size() * DefaultVertexFormat.PARTICLE.getVertexSize() * 4);
			BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.PARTICLE);
			int rainColumns = 0;
			int snowColumns = 0;

			for (PrecipitationRenderHelper helper : RENDER_HELPER) {
				BlockPos pos = helper.cloudPos();
				int roofX = pos.getX();
				int roofZ = pos.getZ();

				int botY = Math.max(helper.rainOnY(), floorY - renderDistance);
				int topY = Math.min(pos.getY(), floorY + renderDistance);
				if (topY - botY <= 0) continue;

				double xDiff = (double) roofX + 0.5D - camX;
				double zDiff = (double) roofZ + 0.5D - camZ;
				double columnDistance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
				double rainX = columnDistance == 0.0D ? 0.0D : -zDiff / columnDistance * 0.5D;
				double rainZ = columnDistance == 0.0D ? 0.0D : xDiff / columnDistance * 0.5D;

				RandomSource random = RandomSource.create((long) roofX * roofX * 3121 + roofX * 45238971L ^ (long) roofZ * roofZ * 418711 + roofZ * 13761L);
				if (helper.precipitation() == Biome.Precipitation.RAIN) {
					int offset = ticks + roofX * roofX * 3121 + roofX * 45238971 + roofZ * roofZ * 418711 + roofZ * 13761 & 31;
					float uvOffset = -((float) offset + partialTick) / 32.0F * (3.0F + random.nextFloat());
					float distance = (float) (columnDistance / (double) renderDistance);
					float alpha = ((1.0F - distance * distance) * 0.5F + 0.5F) * helper.precipitationLevel();
					mutableBlockPos.set(roofX, Math.max(helper.rainOnY(), floorY), roofZ);
					int lightCoords = LightCoordsUtil.getLightCoords(minecraft.level, mutableBlockPos);

					bufferBuilder.addVertex((float) (roofX - camX - rainX + 0.5D), (float) (topY - camY), (float) (roofZ - camZ - rainZ + 0.5D)).setUv(0.0F, (float) botY * 0.25F + uvOffset).setColor(1.0F, 1.0F, 1.0F, alpha).setLight(lightCoords);
					bufferBuilder.addVertex((float) (roofX - camX + rainX + 0.5D), (float) (topY - camY), (float) (roofZ - camZ + rainZ + 0.5D)).setUv(1.0F, (float) botY * 0.25F + uvOffset).setColor(1.0F, 1.0F, 1.0F, alpha).setLight(lightCoords);
					bufferBuilder.addVertex((float) (roofX - camX + rainX + 0.5D), (float) (botY - camY), (float) (roofZ - camZ + rainZ + 0.5D)).setUv(1.0F, (float) topY * 0.25F + uvOffset).setColor(1.0F, 1.0F, 1.0F, alpha).setLight(lightCoords);
					bufferBuilder.addVertex((float) (roofX - camX - rainX + 0.5D), (float) (botY - camY), (float) (roofZ - camZ - rainZ + 0.5D)).setUv(0.0F, (float) topY * 0.25F + uvOffset).setColor(1.0F, 1.0F, 1.0F, alpha).setLight(lightCoords);
					rainColumns++;
				} else if (helper.precipitation() == Biome.Precipitation.SNOW) {
					float offset = -((float) (ticks & 511) + partialTick) / 512.0F;
					float uOffset = (float) (random.nextDouble() + (double) fullTick * 0.01D * (double) ((float) random.nextGaussian()));
					float vOffset = (float) (random.nextDouble() + (double) (fullTick * (float) random.nextGaussian()) * 0.001D);
					float distance = (float) (columnDistance / (double) renderDistance);
					float alpha = ((1.0F - distance * distance) * 0.3F + 0.5F) * helper.precipitationLevel();
					mutableBlockPos.set(roofX, Math.max(helper.rainOnY(), floorY), roofZ);

					int lightCoords = LightCoordsUtil.getLightCoords(minecraft.level, mutableBlockPos);
					int u = lightCoords & '\uffff';
					int v = lightCoords >> 16 & '\uffff';
					u = (u * 3 + 240) / 4;
					v = (v * 3 + 240) / 4;

					bufferBuilder.addVertex((float) (roofX - camX - rainX + 0.5D), (float) (topY - camY), (float) (roofZ - camZ - rainZ + 0.5D)).setUv(0.0F + uOffset, (float) botY * 0.25F + offset + vOffset).setColor(1.0F, 1.0F, 1.0F, alpha).setUv2(u, v);
					bufferBuilder.addVertex((float) (roofX - camX + rainX + 0.5D), (float) (topY - camY), (float) (roofZ - camZ + rainZ + 0.5D)).setUv(1.0F + uOffset, (float) botY * 0.25F + offset + vOffset).setColor(1.0F, 1.0F, 1.0F, alpha).setUv2(u, v);
					bufferBuilder.addVertex((float) (roofX - camX + rainX + 0.5D), (float) (botY - camY), (float) (roofZ - camZ + rainZ + 0.5D)).setUv(1.0F + uOffset, (float) topY * 0.25F + offset + vOffset).setColor(1.0F, 1.0F, 1.0F, alpha).setUv2(u, v);
					bufferBuilder.addVertex((float) (roofX - camX - rainX + 0.5D), (float) (botY - camY), (float) (roofZ - camZ - rainZ + 0.5D)).setUv(0.0F + uOffset, (float) topY * 0.25F + offset + vOffset).setColor(1.0F, 1.0F, 1.0F, alpha).setUv2(u, v);
					snowColumns++;
				}
			}

			try (byteBufferBuilder; MeshData mesh = bufferBuilder.build()) {
				if (mesh != null)
					drawPrecipitation(mesh, rainColumns, snowColumns);
			}
		}
	}

	private void drawPrecipitation(MeshData mesh, int rainColumns, int snowColumns) {
		RenderTarget weatherTarget = OutputTarget.WEATHER_TARGET.getRenderTarget();
		GpuTextureView colorTexture = weatherTarget.getColorTextureView();
		if (colorTexture == null)
			return;

		Minecraft minecraft = Minecraft.getInstance();
		GpuDevice device = RenderSystem.getDevice();
		RenderPipeline renderPipeline = minecraft.gameRenderer.gameRenderState().useShaderTransparency() ? RenderPipelines.WEATHER_DEPTH_WRITE : RenderPipelines.WEATHER_NO_DEPTH_WRITE;
		RenderSystem.AutoStorageIndexBuffer indices = RenderSystem.getSequentialBuffer(mesh.drawState().primitiveTopology());
		GpuBuffer indexBuffer = indices.getBuffer(mesh.drawState().indexCount());
		GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(RenderSystem.getModelViewMatrixCopy());

		try (
			GpuBuffer vertexBuffer = device.createBuffer(
				() -> "Twilight Forest cloud precipitation vertices",
				GpuBuffer.USAGE_VERTEX,
				mesh.vertexBuffer()
			);
			RenderPass renderPass = device.createCommandEncoder().createRenderPass(
				() -> "Cloud Precipitation",
				colorTexture,
				Optional.empty(),
				weatherTarget.getDepthTextureView(),
				OptionalDouble.empty()
			)
		) {
			renderPass.setPipeline(renderPipeline);
			RenderSystem.bindDefaultUniforms(renderPass);
			renderPass.setUniform("DynamicTransforms", dynamicTransforms);
			renderPass.bindTexture("Sampler2", minecraft.gameRenderer.lightmap(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
			renderPass.setIndexBuffer(indexBuffer, indices.type());
			renderPass.setVertexBuffer(0, vertexBuffer.slice());
			drawColumns(renderPass, TFWeatherRenderer.RAIN_TEXTURES, 0, rainColumns);
			drawColumns(renderPass, TFWeatherRenderer.SNOW_TEXTURES, rainColumns, snowColumns);
		}
	}

	private void drawColumns(RenderPass renderPass, Identifier textureLocation, int startColumn, int columnCount) {
		AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(textureLocation);
		renderPass.bindTexture("Sampler0", texture.getTextureView(), texture.getSampler());
		renderPass.drawIndexed(columnCount * 6, 1, startColumn * 6, 0, 0);
	}
}
