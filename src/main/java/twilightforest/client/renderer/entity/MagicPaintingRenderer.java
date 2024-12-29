package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import twilightforest.client.MagicPaintingTextureManager;
import twilightforest.entity.MagicPainting;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.entity.MagicPaintingVariant.Layer.OpacityModifier;
import twilightforest.entity.MagicPaintingVariant.Layer.Parallax;

import javax.annotation.Nullable;
import java.util.Optional;

public class MagicPaintingRenderer extends EntityRenderer<MagicPainting> {
	public static long lastLightning = 0L;

	public MagicPaintingRenderer(EntityRendererProvider.Context pContext) {
		super(pContext);
	}

	@Override
	public void render(MagicPainting painting, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
		MagicPaintingVariant paintingVariant = painting.getVariant().value();

		stack.pushPose();
		stack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
		stack.scale(0.0625F, 0.0625F, 0.0625F);
		VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(painting)));

		this.renderPainting(stack, vertexconsumer, painting, paintingVariant, partialTicks);
		stack.popPose();
		super.render(painting, yaw, partialTicks, stack, buffer, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(MagicPainting painting) {
		return MagicPaintingTextureManager.ATLAS_LOCATION;
	}

	private void renderPainting(PoseStack stack, VertexConsumer vertex, MagicPainting painting, MagicPaintingVariant variant, float partialTicks) {
		ResourceLocation textureLocation = MagicPaintingVariant.getVariantResourceLocation(painting.level().registryAccess(), variant);

		int width = variant.width();
		int height = variant.height();
		int widthAsBlock = width / 16;
		int heightAsBlock = height / 16;

		PoseStack.Pose pose = stack.last();

		float x = (float) (-width) / 2.0F;
		float y = (float) (-height) / 2.0F;
		float z = 0.5F;

		double widthFactor = 1.0D / (double) widthAsBlock;
		double heightFactor = 1.0D / (double) heightAsBlock;

		Direction direction = painting.getDirection();
		int posX = painting.getBlockX();
		int posZ = painting.getBlockZ();

		for (MagicPaintingVariant.Layer layer : variant.layers()) {
			float alpha = this.getAlpha(layer.opacityModifier(), painting, partialTicks);
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
			double widthOffset = widthDiff != 0.0D ? this.getWidthOffset(parallax, painting, widthDiff, partialTicks) : 0.0D;

			double heightDiff = parallax != null ? (heightFactor - layerHeightFactor) * (double) heightAsBlock * 0.5D : 0.0D;
			double heightOffset = heightDiff != 0.0D ? this.getHeightOffset(parallax, painting, heightDiff, partialTicks) : 0.0D;

			TextureAtlasSprite layerTexture = MagicPaintingTextureManager.instance.getLayerSprite(textureLocation, layer);

			for (int k = 0; k < widthAsBlock; ++k) {
				for (int l = 0; l < heightAsBlock; ++l) {
					float xMax = x + (float) ((k + 1) * 16);
					float xMin = x + (float) (k * 16);
					float yMax = y + (float) ((l + 1) * 16);
					float yMin = y + (float) (l * 16);

					if (direction == Direction.NORTH) posX = Mth.floor(painting.getX() + (double) ((xMax + xMin) / 2.0F / 16.0F));
					if (direction == Direction.WEST) posZ = Mth.floor(painting.getZ() - (double) ((xMax + xMin) / 2.0F / 16.0F));
					if (direction == Direction.SOUTH) posX = Mth.floor(painting.getX() - (double) ((xMax + xMin) / 2.0F / 16.0F));
					if (direction == Direction.EAST) posZ = Mth.floor(painting.getZ() + (double) ((xMax + xMin) / 2.0F / 16.0F));

					int light = layer.fullbright() ? 15728850 : LevelRenderer.getLightColor(painting.level(), new BlockPos(posX, Mth.floor(painting.getY() + (double) ((yMax + yMin) / 2.0F / 16.0F)), posZ));
					float xEnd = layerTexture.getU((float) (layerWidthFactor * (double) (widthAsBlock - k) + widthOffset));
					float xStart = layerTexture.getU((float) (layerWidthFactor * (double) (widthAsBlock - (k + 1)) + widthOffset));
					float yEnd = layerTexture.getV((float) (layerHeightFactor * (double) (heightAsBlock - l) + heightOffset));
					float yStart = layerTexture.getV((float) (layerHeightFactor * (double) (heightAsBlock - (l + 1)) + heightOffset));
					this.vertex(pose, vertex, xMax, yMin, -z, xStart, yEnd, 0, 0, -1, light, alpha, localLighting);
					this.vertex(pose, vertex, xMin, yMin, -z, xEnd, yEnd, 0, 0, -1, light, alpha, localLighting);
					this.vertex(pose, vertex, xMin, yMax, -z, xEnd, yStart, 0, 0, -1, light, alpha, localLighting);
					this.vertex(pose, vertex, xMax, yMax, -z, xStart, yStart, 0, 0, -1, light, alpha, localLighting);
				}
			}
		}

		TextureAtlasSprite backSprite = MagicPaintingTextureManager.instance.getBackSprite(variant);

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

			if (direction == Direction.NORTH) posX = Mth.floor(painting.getX() + (double) ((xMax + xMin) / 2.0F / 16.0F));
			if (direction == Direction.WEST) posZ = Mth.floor(painting.getZ() - (double) ((xMax + xMin) / 2.0F / 16.0F));
			if (direction == Direction.SOUTH) posX = Mth.floor(painting.getX() - (double) ((xMax + xMin) / 2.0F / 16.0F));
			if (direction == Direction.EAST) posZ = Mth.floor(painting.getZ() + (double) ((xMax + xMin) / 2.0F / 16.0F));

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

				int light = LevelRenderer.getLightColor(painting.level(), new BlockPos(posX, Mth.floor(painting.getY() + (double) ((yMax + yMin) / 2.0F / 16.0F)), posZ));

				// Back
				this.vertex(pose, vertex, xMax, yMax, z, u1, v0, 0, 0, 1, light, false);
				this.vertex(pose, vertex, xMin, yMax, z, u0, v0, 0, 0, 1, light, false);
				this.vertex(pose, vertex, xMin, yMin, z, u0, v1, 0, 0, 1, light, false);
				this.vertex(pose, vertex, xMax, yMin, z, u1, v1, 0, 0, 1, light, false);

				// Top
				this.vertex(pose, vertex, xMax, yMax, -z, u1, v0, 0, 1, 0, light, false);
				this.vertex(pose, vertex, xMin, yMax, -z, u0, v0, 0, 1, 0, light, false);
				this.vertex(pose, vertex, xMin, yMax, z, u0, v, 0, 1, 0, light, false);
				this.vertex(pose, vertex, xMax, yMax, z, u1, v, 0, 1, 0, light, false);

				// Bottom
				this.vertex(pose, vertex, xMax, yMin, z, u1, vI, 0, -1, 0, light, false);
				this.vertex(pose, vertex, xMin, yMin, z, u0, vI, 0, -1, 0, light, false);
				this.vertex(pose, vertex, xMin, yMin, -z, u0, v1, 0, -1, 0, light, false);
				this.vertex(pose, vertex, xMax, yMin, -z, u1, v1, 0, -1, 0, light, false);

				// Left
				this.vertex(pose, vertex, xMax, yMax, z, uI, v0, -1, 0, 0, light, false);
				this.vertex(pose, vertex, xMax, yMin, z, uI, v1, -1, 0, 0, light, false);
				this.vertex(pose, vertex, xMax, yMin, -z, u1, v1, -1, 0, 0, light, false);
				this.vertex(pose, vertex, xMax, yMax, -z, u1, v0, -1, 0, 0, light, false);

				// Right
				this.vertex(pose, vertex, xMin, yMax, -z, u0, v0, 1, 0, 0, light, false);
				this.vertex(pose, vertex, xMin, yMin, -z, u0, v1, 1, 0, 0, light, false);
				this.vertex(pose, vertex, xMin, yMin, z, u, v1, 1, 0, 0, light, false);
				this.vertex(pose, vertex, xMin, yMax, z, u, v0, 1, 0, 0, light, false);
			}
		}
	}

	protected void vertex(PoseStack.Pose pose, VertexConsumer vertex, float x, float y, float z, float u, float v, int normX, int normY, int normZ, int light, boolean localLighting) {
		this.vertex(pose, vertex, x, y, z, u, v, normX, normY, normZ, light, 1.0F, localLighting);
	}

	protected void vertex(PoseStack.Pose pose, VertexConsumer vertex, float x, float y, float z, float u, float v, int normX, int normY, int normZ, int light, float a, boolean localLighting) {
		vertex.addVertex(pose, x, y, z).setColor(255, 255, 255, (int) (255.0F * a)).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light);

		if (localLighting)
			vertex.setNormal(normX, normY, normZ);
		else
			vertex.setNormal(pose, normX, normY, normZ);
	}

	protected double getWidthOffset(@Nullable Parallax parallax, MagicPainting painting, double widthDiff, float partialTicks) {
		if (parallax != null) switch (parallax.type()) {
			case VIEW_ANGLE -> {
				Vec3 camPos = Minecraft.getInstance().cameraEntity != null ?
					Minecraft.getInstance().cameraEntity.getEyePosition(partialTicks) :
					Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

				Vec3 paintPos = painting.position().relative(painting.getDirection().getOpposite(), 1.0D);

				double x = camPos.x - paintPos.x;
				double z = camPos.z - paintPos.z;
				double yRot = Mth.wrapDegrees((float) (Mth.atan2(z, x) * (double) (180F / (float) Math.PI)) - 90.0F - painting.getYRot());
				return widthDiff + Mth.clamp(yRot * parallax.multiplier() * widthDiff, -widthDiff, widthDiff);
			}
			case SINE_TIME -> {
				return widthDiff + (Math.sin((painting.tickCount + partialTicks) * parallax.multiplier()) * widthDiff);
			}
			case LINEAR_TIME -> {
				double trueTick = (painting.tickCount + partialTicks) * parallax.multiplier();
				double wholeDiff = widthDiff * 2.0D;
				return widthDiff + (parallax.multiplier() > 0.0D ? -widthDiff + (trueTick % wholeDiff) : widthDiff - (trueTick % wholeDiff));
			}
		}
		return 0.0D;
	}

	protected double getHeightOffset(@Nullable Parallax parallax, MagicPainting painting, double heightDiff, float partialTicks) {
		if (parallax != null) switch (parallax.type()) {
			case VIEW_ANGLE -> {
				Vec3 camPos = Minecraft.getInstance().cameraEntity != null ?
					Minecraft.getInstance().cameraEntity.getEyePosition(partialTicks) :
					Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

				Vec3 paintPos = painting.position().relative(painting.getDirection().getOpposite(), 1.0D);

				double x = camPos.x - paintPos.x;
				double y = camPos.y - paintPos.y;
				double z = camPos.z - paintPos.z;
				double pythagoras = Math.sqrt(x * x + z * z);
				double xRot = Mth.wrapDegrees((float) (-(Mth.atan2(y, pythagoras) * (double) (180F / (float) Math.PI))));

				return heightDiff - Mth.clamp(xRot * parallax.multiplier() * heightDiff, -heightDiff, heightDiff);
			}
			case SINE_TIME -> {
				return heightDiff - (Math.cos((painting.tickCount + partialTicks) * parallax.multiplier()) * heightDiff);
			}
			case LINEAR_TIME -> {
				double trueTick = (painting.tickCount + partialTicks) * parallax.multiplier();
				double wholeDiff = heightDiff * 2.0D;
				return heightDiff - (parallax.multiplier() > 0.0D ? -heightDiff + (trueTick % wholeDiff) : heightDiff - (trueTick % wholeDiff));
			}
		}
		return 0.0D;
	}

	protected static final float DAY_LENGTH = 24000.0F;

	protected float getAlpha(@Nullable OpacityModifier opacityModifier, MagicPainting painting, float partialTicks) {
		if (opacityModifier == null) return 1.0F;

		float a = 1.0F;
		switch (opacityModifier.type()) {
			case DISTANCE -> {
				Vec3 camPos = Optional.ofNullable(Minecraft.getInstance().cameraEntity).map(Entity::getEyePosition).orElse(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition());
				a = fromTo(opacityModifier.from(), opacityModifier.to(), (float) camPos.distanceTo(painting.position()));
			}
			case WEATHER -> a = painting.level().getRainLevel(partialTicks);
			case STORM -> {
				a = (painting.level().getRainLevel(partialTicks) + painting.level().getThunderLevel(partialTicks)) * 0.5F;
			}
			case LIGHTNING -> {
				if (painting.level() instanceof ClientLevel clientLevel) {
					a = 1.0F - ((float) (clientLevel.getGameTime() - lastLightning) - partialTicks) * opacityModifier.multiplier();
					if (a > 0.0F) a = a * a;
				}
			}
			case DAY_TIME -> {
				if (painting.level() instanceof ClientLevel level) {
					float time = level.dimensionType().fixedTime().orElse(level.dayTime()) + partialTicks;

					if (opacityModifier.from() < opacityModifier.to()) {
						a = 1.0F - Math.abs(((time - opacityModifier.from()) / (opacityModifier.to() - opacityModifier.from())) - 0.5F) * 2.0F;
					} else {
						if (time < opacityModifier.to()) time += DAY_LENGTH;
						a = 1.0F - Math.abs(((time - opacityModifier.from()) / (opacityModifier.to() + DAY_LENGTH - opacityModifier.from())) - 0.5F) * 2.0F;
					}
				}
			}
			case SINE_TIME -> a = (float) (Math.sin((painting.tickCount + partialTicks) * opacityModifier.multiplier())) * 0.5F + 0.5F;
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
}