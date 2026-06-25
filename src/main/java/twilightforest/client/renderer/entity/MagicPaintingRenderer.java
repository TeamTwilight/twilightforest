package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import twilightforest.client.MagicPaintingAtlasInfo;
import twilightforest.client.state.entity.MagicPaintingRenderState;
import twilightforest.entity.MagicPainting;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.entity.MagicPaintingVariant.Layer.OpacityModifier;
import twilightforest.entity.MagicPaintingVariant.Layer.Parallax;

import javax.annotation.Nullable;

public class MagicPaintingRenderer extends EntityRenderer<MagicPainting, MagicPaintingRenderState> {

	private final TextureAtlas paintingAtlas;
	public static long lastLightning = 0L;

	public MagicPaintingRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.paintingAtlas = context.getAtlas(MagicPaintingAtlasInfo.ATLAS_INFO_LOCATION);
	}

	@Override
	public void submit(MagicPaintingRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
		MagicPaintingVariant variant = state.variant;

		if (variant != null) {
			stack.pushPose();
			stack.mulPose(Axis.YP.rotationDegrees(180.0F - state.direction.get2DDataValue() * 90));
			stack.scale(0.0625F, 0.0625F, 0.0625F);
			TextureAtlasSprite textureatlassprite = this.paintingAtlas.getSprite(variant.backTexture());
			this.renderPainting(state, camera, stack, collector, RenderTypes.entityTranslucent(textureatlassprite.atlasLocation()), state.lightCoords, variant.width(), variant.height(), textureatlassprite);
			stack.popPose();
			super.submit(state, stack, collector, camera);
		}
	}

	public TextureAtlasSprite getLayerSprite(Identifier variant, MagicPaintingVariant.Layer layer) {
		return this.paintingAtlas.getSprite(variant.withPrefix(MagicPaintingAtlasInfo.MAGIC_PAINTING_PATH + "/").withSuffix("/" + layer.path()));
	}

	private void renderPainting(MagicPaintingRenderState state, CameraRenderState cameraState, PoseStack stack, SubmitNodeCollector collector, net.minecraft.client.renderer.rendertype.RenderType renderType, int[] worldLight, int width, int height, TextureAtlasSprite backSprite) {
		collector.submitCustomGeometry(stack, renderType, (pose, consumer) -> {
			Identifier textureLocation = state.texture;

			int widthAsBlock = width / 16;
			int heightAsBlock = height / 16;

			float x = (float) (-width) / 2.0F;
			float y = (float) (-height) / 2.0F;
			float z = 0.5F;

			double widthFactor = 1.0D / (double) widthAsBlock;
			double heightFactor = 1.0D / (double) heightAsBlock;

			for (MagicPaintingVariant.Layer layer : state.variant.layers()) {
				float alpha = this.getAlpha(layer.opacityModifier(), state, cameraState, state.partialTick);
				if (alpha <= 0.0F) continue;

				Parallax parallax = layer.parallax();

				boolean localLighting = layer.localLighting();

				int layerWidth = parallax != null ? parallax.width() : width;
				int layerHeight = parallax != null ? parallax.height() : height;

				double layerWidthAsBlock = layerWidth / 16.0D;
				double layerHeightAsBlock = layerHeight / 16.0D;

				double layerWidthFactor = 1.0D / layerWidthAsBlock;
				double layerHeightFactor = 1.0D / layerHeightAsBlock;

				double widthDiff = parallax != null ? (widthFactor - layerWidthFactor) * (double) widthAsBlock * 0.5D : 0.0D;
				double widthOffset = widthDiff != 0.0D ? this.getWidthOffset(parallax, state, cameraState, widthDiff) : 0.0D;

				double heightDiff = parallax != null ? (heightFactor - layerHeightFactor) * (double) heightAsBlock * 0.5D : 0.0D;
				double heightOffset = heightDiff != 0.0D ? this.getHeightOffset(parallax, state, cameraState, heightDiff) : 0.0D;

				TextureAtlasSprite layerTexture = this.getLayerSprite(textureLocation, layer);

				for (int w = 0; w < widthAsBlock; ++w) {
					for (int h = 0; h < heightAsBlock; ++h) {
						float xMax = x + (float) ((w + 1) * 16);
						float xMin = x + (float) (w * 16);
						float yMax = y + (float) ((h + 1) * 16);
						float yMin = y + (float) (h * 16);

						int light = layer.fullbright() ? 15728850 : worldLight[w + h * widthAsBlock];
						float xEnd = layerTexture.getU((float) (layerWidthFactor * (double) (widthAsBlock - w) + widthOffset));
						float xStart = layerTexture.getU((float) (layerWidthFactor * (double) (widthAsBlock - (w + 1)) + widthOffset));
						float yEnd = layerTexture.getV((float) (layerHeightFactor * (double) (heightAsBlock - h) + heightOffset));
						float yStart = layerTexture.getV((float) (layerHeightFactor * (double) (heightAsBlock - (h + 1)) + heightOffset));
						this.vertex(pose, consumer, xMax, yMin, -z, xStart, yEnd, 0, 0, -1, light, alpha, localLighting);
						this.vertex(pose, consumer, xMin, yMin, -z, xEnd, yEnd, 0, 0, -1, light, alpha, localLighting);
						this.vertex(pose, consumer, xMin, yMax, -z, xEnd, yStart, 0, 0, -1, light, alpha, localLighting);
						this.vertex(pose, consumer, xMax, yMax, -z, xStart, yStart, 0, 0, -1, light, alpha, localLighting);
					}
				}
			}

			for (int w = 0; w < widthAsBlock; ++w) {
				boolean leftBorder = w == 0;
				boolean rightBorder = w == widthAsBlock - 1;
				float wShift = (leftBorder ? (rightBorder ? 3 : 0) : (rightBorder ? 2 : 1)) * 0.25f;

				float u0 = backSprite.getU(wShift);
				float u1 = backSprite.getU(wShift + 0.25f);
				float u = Mth.lerp(0.0625F, u0, u1);
				float uI = Mth.lerp(0.0625F, u1, u0);

				float xMax = x + (float) ((w + 1) * 16);
				float xMin = x + (float) (w * 16);

				for (int h = 0; h < heightAsBlock; ++h) {
					boolean bottomBorder = h == 0;
					boolean topBorder = h == heightAsBlock - 1;
					float hShift = (bottomBorder ? (topBorder ? 3 : 2) : (topBorder ? 0 : 1)) * 0.25f;

					float v0 = backSprite.getV(hShift);
					float v1 = backSprite.getV(hShift + 0.25f);
					float v = Mth.lerp(0.0625F, v0, v1);
					float vI = Mth.lerp(0.0625F, v1, v0);

					float yMax = y + (float) ((h + 1) * 16);
					float yMin = y + (float) (h * 16);

					int light = worldLight[w + h * widthAsBlock];

					// Back
					this.vertex(pose, consumer, xMax, yMax, z, u1, v0, 0, 0, 1, light, false);
					this.vertex(pose, consumer, xMin, yMax, z, u0, v0, 0, 0, 1, light, false);
					this.vertex(pose, consumer, xMin, yMin, z, u0, v1, 0, 0, 1, light, false);
					this.vertex(pose, consumer, xMax, yMin, z, u1, v1, 0, 0, 1, light, false);

					// Top
					this.vertex(pose, consumer, xMax, yMax, -z, u1, v0, 0, 1, 0, light, false);
					this.vertex(pose, consumer, xMin, yMax, -z, u0, v0, 0, 1, 0, light, false);
					this.vertex(pose, consumer, xMin, yMax, z, u0, v, 0, 1, 0, light, false);
					this.vertex(pose, consumer, xMax, yMax, z, u1, v, 0, 1, 0, light, false);

					// Bottom
					this.vertex(pose, consumer, xMax, yMin, z, u1, vI, 0, -1, 0, light, false);
					this.vertex(pose, consumer, xMin, yMin, z, u0, vI, 0, -1, 0, light, false);
					this.vertex(pose, consumer, xMin, yMin, -z, u0, v1, 0, -1, 0, light, false);
					this.vertex(pose, consumer, xMax, yMin, -z, u1, v1, 0, -1, 0, light, false);

					// Left
					this.vertex(pose, consumer, xMax, yMax, z, uI, v0, -1, 0, 0, light, false);
					this.vertex(pose, consumer, xMax, yMin, z, uI, v1, -1, 0, 0, light, false);
					this.vertex(pose, consumer, xMax, yMin, -z, u1, v1, -1, 0, 0, light, false);
					this.vertex(pose, consumer, xMax, yMax, -z, u1, v0, -1, 0, 0, light, false);

					// Right
					this.vertex(pose, consumer, xMin, yMax, -z, u0, v0, 1, 0, 0, light, false);
					this.vertex(pose, consumer, xMin, yMin, -z, u0, v1, 1, 0, 0, light, false);
					this.vertex(pose, consumer, xMin, yMin, z, u, v1, 1, 0, 0, light, false);
					this.vertex(pose, consumer, xMin, yMax, z, u, v0, 1, 0, 0, light, false);
				}
			}
		});
	}

	protected void vertex(PoseStack.Pose pose, VertexConsumer consumer, float x, float y, float z, float u, float v, int normX, int normY, int normZ, int light, boolean localLighting) {
		this.vertex(pose, consumer, x, y, z, u, v, normX, normY, normZ, light, 1.0F, localLighting);
	}

	protected void vertex(PoseStack.Pose pose, VertexConsumer consumer, float x, float y, float z, float u, float v, int normX, int normY, int normZ, int light, float a, boolean localLighting) {
		consumer.addVertex(pose, x, y, z).setColor(255, 255, 255, (int) (255.0F * a)).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light);

		if (localLighting) consumer.setNormal(normX, normY, normZ);
		else consumer.setNormal(pose, normX, normY, normZ);
	}

	protected double getWidthOffset(@Nullable Parallax parallax, MagicPaintingRenderState state, CameraRenderState cameraState, double widthDiff) {
		if (parallax != null) switch (parallax.type()) {
			case VIEW_ANGLE -> {
				Vec3 camPos = cameraState.pos;

				Vec3 paintPos = state.position.relative(state.direction.getOpposite(), 1.0D);

				double x = camPos.x - paintPos.x;
				double z = camPos.z - paintPos.z;
				double yRot = Mth.wrapDegrees((float) (Mth.atan2(z, x) * (double) (180F / (float) Math.PI)) - 90.0F - state.yRot);
				return widthDiff + Mth.clamp(yRot * parallax.multiplier() * widthDiff, -widthDiff, widthDiff);
			}
			case SINE_TIME -> {
				return widthDiff + (Math.sin(state.ageInTicks * parallax.multiplier()) * widthDiff);
			}
			case LINEAR_TIME -> {
				double trueTick = state.ageInTicks * parallax.multiplier();
				double wholeDiff = widthDiff * 2.0D;
				return widthDiff + (parallax.multiplier() > 0.0D ? -widthDiff + (trueTick % wholeDiff) : widthDiff - (trueTick % wholeDiff));
			}
		}
		return 0.0D;
	}

	protected double getHeightOffset(@Nullable Parallax parallax, MagicPaintingRenderState state, CameraRenderState cameraState, double heightDiff) {
		if (parallax != null) switch (parallax.type()) {
			case VIEW_ANGLE -> {
				Vec3 camPos = cameraState.pos;

				Vec3 paintPos = state.position.relative(state.direction.getOpposite(), 1.0D);

				double x = camPos.x - paintPos.x;
				double y = camPos.y - paintPos.y;
				double z = camPos.z - paintPos.z;
				double pythagoras = Math.sqrt(x * x + z * z);
				double xRot = Mth.wrapDegrees((float) (-(Mth.atan2(y, pythagoras) * (double) (180F / (float) Math.PI))));

				return heightDiff - Mth.clamp(xRot * parallax.multiplier() * heightDiff, -heightDiff, heightDiff);
			}
			case SINE_TIME -> {
				return heightDiff - (Math.cos(state.ageInTicks * parallax.multiplier()) * heightDiff);
			}
			case LINEAR_TIME -> {
				double trueTick = state.ageInTicks * parallax.multiplier();
				double wholeDiff = heightDiff * 2.0D;
				return heightDiff - (parallax.multiplier() > 0.0D ? -heightDiff + (trueTick % wholeDiff) : heightDiff - (trueTick % wholeDiff));
			}
		}
		return 0.0D;
	}

	protected static final float DAY_LENGTH = 24000.0F;

	protected float getAlpha(@Nullable OpacityModifier opacityModifier, MagicPaintingRenderState state, CameraRenderState cameraState, float partialTicks) {
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null || opacityModifier == null) return 1.0F;

		float a = 1.0F;
		switch (opacityModifier.type()) {
			case DISTANCE -> {
				Vec3 camPos = cameraState.pos;
				a = fromTo(opacityModifier.from(), opacityModifier.to(), (float) camPos.distanceTo(state.position));
			}
			case WEATHER -> a = level.getRainLevel(partialTicks);
			case STORM -> a = (level.getRainLevel(partialTicks) + level.getThunderLevel(partialTicks)) * 0.5F;
			case LIGHTNING -> {
				var flashState = level.endFlashState();
				a = flashState != null ? flashState.getIntensity(partialTicks) * opacityModifier.multiplier() : 0.0F;
			}
			case DAY_TIME -> {
				float time = level.getDefaultClockTime();

				if (opacityModifier.from() < opacityModifier.to()) {
					a = 1.0F - Math.abs(((time - opacityModifier.from()) / (opacityModifier.to() - opacityModifier.from())) - 0.5F) * 2.0F;
				} else {
					if (time < opacityModifier.to()) time += DAY_LENGTH;
					a = 1.0F - Math.abs(((time - opacityModifier.from()) / (opacityModifier.to() + DAY_LENGTH - opacityModifier.from())) - 0.5F) * 2.0F;
				}
			}
			case SINE_TIME -> a = (float) (Math.sin(state.ageInTicks * opacityModifier.multiplier())) * 0.5F + 0.5F;
			case HEALTH -> {
				if (Minecraft.getInstance().getCameraEntity() instanceof LivingEntity living) {
					a = fromTo(opacityModifier.from(), opacityModifier.to(), living.getHealth());
				}
			}
			case HUNGER -> {
				if (Minecraft.getInstance().getCameraEntity() instanceof Player player) {
					FoodData food = player.getFoodData();
					a = fromTo(opacityModifier.from(), opacityModifier.to(), (float) food.getFoodLevel());
				}
			}
			case HOLDING_ITEM -> {
				if (Minecraft.getInstance().getCameraEntity() instanceof LivingEntity living) {
					ItemStack key = opacityModifier.item();
					if (key != null && !living.isHolding(stack -> ItemStack.isSameItemSameComponents(stack, key)))
						a = 0.0F;
				}
			}
			case MOB_EFFECT_CATEGORY -> {
				if (Minecraft.getInstance().getCameraEntity() instanceof LivingEntity living) {
					boolean flag = false;
					for (MobEffectInstance effect : living.getActiveEffects()) {
						if (opacityModifier.effectCategory().isPresent()) {
							if (effect.getEffect().value().getCategory() == opacityModifier.effectCategory().get()) {
								flag = true;
								break;
							}
						}
					}
					a = flag ? 1.0F : 0.0F;
				}
			}
		}

		a = Mth.clamp(a, 0.0F, 1.0F);
		if (opacityModifier.type().powerOfMultiplier()) a = (float) Math.pow(a, opacityModifier.multiplier());
		if (opacityModifier.invert()) a = 1.0F - a;
		a = a * (opacityModifier.max() - opacityModifier.min()) + opacityModifier.min();
		return a;
	}

	protected static float fromTo(float from, float to, float value) {
		if (from < to) return (value - from) / (to - from);
		else return (from - value) / (from - to);
	}

	protected int getFrameUV(int i, int maxI) {
		if (maxI <= 1) return 4;
		else if (i == 0) return 1;
		else if (i == maxI - 1) return 3;
		else return 2;
	}

	@Override
	public MagicPaintingRenderState createRenderState() {
		return new MagicPaintingRenderState();
	}

	@Override
	public void extractRenderState(MagicPainting painting, MagicPaintingRenderState state, float partialTick) {
		super.extractRenderState(painting, state, partialTick);
		Direction direction = painting.getDirection();
		MagicPaintingVariant variant = painting.getVariant().value();
		state.direction = direction;
		state.variant = variant;
		state.texture = MagicPaintingVariant.getVariantIdentifier(painting.level().registryAccess(), variant);
		state.yRot = Mth.lerp(partialTick, painting.yRotO, painting.getYRot());
		state.position = painting.position();

		int widthAsBlock = variant.width() / 16;
		int heightAsBlock = variant.height() / 16;
		if (state.lightCoords.length != widthAsBlock * heightAsBlock) state.lightCoords = new int[widthAsBlock * heightAsBlock];

		float halfWidth = (float) (-widthAsBlock) / 2.0F;
		float halfHeight = (float) (-heightAsBlock) / 2.0F;
		Level level = painting.level();

		for (int w = 0; w < widthAsBlock; w++) {
			for (int h = 0; h < heightAsBlock; h++) {
				float widthOffset = (float) w + halfWidth + 0.5F;
				float heightOffset = (float) h + halfHeight + 0.5F;
				int lightX = painting.getBlockX();
				int lightY = Mth.floor(painting.getY() + (double) heightOffset);
				int lightZ = painting.getBlockZ();
				switch (direction) {
					case NORTH:
						lightX = Mth.floor(lightX + (double) widthOffset);
						break;
					case WEST:
						lightZ = Mth.floor(lightZ - (double) widthOffset);
						break;
					case SOUTH:
						lightX = Mth.floor(lightX - (double) widthOffset);
						break;
					case EAST:
						lightZ = Mth.floor(lightZ + (double) widthOffset);
				}

				state.lightCoords[w + h * widthAsBlock] = LevelRenderer.getLightCoords(level, new BlockPos(lightX, lightY, lightZ));
			}
		}
	}
}
