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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import twilightforest.client.MagicPaintingTextureManager;
import twilightforest.entity.MagicPainting;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.entity.MagicPaintingVariant.Layer.OpacityModifier;
import twilightforest.entity.MagicPaintingVariant.Layer.Parallax;

import javax.annotation.Nullable;
import java.util.Optional;

public class MagicPaintingRenderer extends EntityRenderer<MagicPainting> {
    public MagicPaintingRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(MagicPainting painting, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        MagicPaintingVariant paintingVariant = painting.getVariant().orElse(null);
        if (paintingVariant == null) return;

        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        stack.scale(0.0625F, 0.0625F, 0.0625F);
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(painting)));

        this.renderPainting(stack, vertexconsumer, painting, paintingVariant);
        stack.popPose();
        super.render(painting, yaw, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(MagicPainting painting) {
        return MagicPaintingTextureManager.ATLAS_LOCATION;
    }

    private void renderPainting(PoseStack stack, VertexConsumer vertex, MagicPainting painting, MagicPaintingVariant variant) {
        ResourceLocation textureLocation = MagicPaintingVariant.getVariantResourceLocation(painting.level().registryAccess(), variant);

        int width = variant.width();
        int height = variant.height();
        int widthAsBlock = width / 16;
        int heightAsBlock = height / 16;

        PoseStack.Pose pose = stack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();

        float x = (float)(-width) / 2.0F;
        float y = (float)(-height) / 2.0F;
        float z = 0.5F;

        double widthFactor = 1.0D / (double)widthAsBlock;
        double heightFactor = 1.0D / (double)heightAsBlock;

        Direction direction = painting.getDirection();
        int posX = painting.getBlockX();
        int posZ = painting.getBlockZ();

        for (MagicPaintingVariant.Layer layer : variant.layers()) {
            float alpha = this.getAlpha(layer.opacityModifier(), painting);
            if (alpha <= 0.0F) continue;

            Parallax parallax = layer.parallax();

            int layerWidth = parallax != null ? parallax.width() : width;
            int layerHeight = parallax != null ? parallax.height() : height;

            double layerWidthAsBlock = layerWidth / 16.0D;
            double layerHeightAsBlock = layerHeight / 16.0D;

            double layerWidthFactor = 1.0D / layerWidthAsBlock;
            double layerHeightFactor = 1.0D / layerHeightAsBlock;

            double widthDiff = parallax != null ? (widthFactor - layerWidthFactor) * (double)widthAsBlock * 0.5D : 0.0D;
            double widthOffset = widthDiff != 0.0D ? this.getWidthOffset(parallax, painting, widthDiff) : 0.0D;

            double heightDiff = parallax != null ? (heightFactor - layerHeightFactor) * (double)heightAsBlock * 0.5D : 0.0D;
            double heightOffset = heightDiff != 0.0D ? this.getHeightOffset(parallax, painting, heightDiff) : 0.0D;

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
                    this.vertex(matrix4f, matrix3f, vertex, xMax, yMin, -z, xStart, yEnd, 0, 0, -1, light, alpha);
                    this.vertex(matrix4f, matrix3f, vertex, xMin, yMin, -z, xEnd, yEnd, 0, 0, -1, light, alpha);
                    this.vertex(matrix4f, matrix3f, vertex, xMin, yMax, -z, xEnd, yStart, 0, 0, -1, light, alpha);
                    this.vertex(matrix4f, matrix3f, vertex, xMax, yMax, -z, xStart, yStart, 0, 0, -1, light, alpha);
                }
            }
        }

        TextureAtlasSprite layerTexture = MagicPaintingTextureManager.instance.getFrameSprite(variant);

        for (int k = 0; k < widthAsBlock; ++k) {
            for (int l = 0; l < heightAsBlock; ++l) {
                int u = this.getFrameUV(k, widthAsBlock);
                int v = this.getFrameUV(l, heightAsBlock);

                float xMax = x + (float) ((k + 1) * 16);
                float xMin = x + (float) (k * 16);
                float yMax = y + (float) ((l + 1) * 16);
                float yMin = y + (float) (l * 16);

                float uO = layerTexture.getUOffset(u);
                float vO = layerTexture.getVOffset(v);

                if (direction == Direction.NORTH) posX = Mth.floor(painting.getX() + (double) ((xMax + xMin) / 2.0F / 16.0F));
                if (direction == Direction.WEST) posZ = Mth.floor(painting.getZ() - (double) ((xMax + xMin) / 2.0F / 16.0F));
                if (direction == Direction.SOUTH) posX = Mth.floor(painting.getX() - (double) ((xMax + xMin) / 2.0F / 16.0F));
                if (direction == Direction.EAST) posZ = Mth.floor(painting.getZ() + (double) ((xMax + xMin) / 2.0F / 16.0F));

                int light = LevelRenderer.getLightColor(painting.level(), new BlockPos(posX, Mth.floor(painting.getY() + (double) ((yMax + yMin) / 2.0F / 16.0F)), posZ));
                float xEnd = layerTexture.getU((float) (4 - u) + uO);
                float xStart = layerTexture.getU((float) (4 - (u + 1)) + uO);
                float yEnd = layerTexture.getV((float) (4 - v) + vO);
                float yStart = layerTexture.getV((float) (4 - (v + 1)) + vO);

                this.vertex(matrix4f, matrix3f, vertex, xMax, yMin, -z, xStart, yEnd, 0, 0, -1, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMin, yMin, -z, xEnd, yEnd, 0, 0, -1, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMin, yMax, -z, xEnd, yStart, 0, 0, -1, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMax, yMax, -z, xStart, yStart, 0, 0, -1, light, 1.0F);
            }
        }

        TextureAtlasSprite backSprite = MagicPaintingTextureManager.instance.getBackSprite();
        float u0 = backSprite.getU0();
        float u1 = backSprite.getU1();
        float v0 = backSprite.getV0();
        float v1 = backSprite.getV1();
        float u01 = backSprite.getU0();
        float u11 = backSprite.getU1();
        float v01 = backSprite.getV0();
        float v = backSprite.getV(1.0F);
        float u02 = backSprite.getU0();
        float u = backSprite.getU(1.0F);
        float v02 = backSprite.getV0();
        float v11 = backSprite.getV1();

        for (int w = 0; w < widthAsBlock; ++w) {
            for (int h = 0; h < heightAsBlock; ++h) {
                float xMax = x + (float)((w + 1) * 16);
                float xMin = x + (float)(w * 16);
                float yMax = y + (float)((h + 1) * 16);
                float yMin = y + (float)(h * 16);

                if (direction == Direction.NORTH) posX = Mth.floor(painting.getX() + (double) ((xMax + xMin) / 2.0F / 16.0F));
                if (direction == Direction.WEST) posZ = Mth.floor(painting.getZ() - (double) ((xMax + xMin) / 2.0F / 16.0F));
                if (direction == Direction.SOUTH) posX = Mth.floor(painting.getX() - (double) ((xMax + xMin) / 2.0F / 16.0F));
                if (direction == Direction.EAST) posZ = Mth.floor(painting.getZ() + (double) ((xMax + xMin) / 2.0F / 16.0F));

                int light = LevelRenderer.getLightColor(painting.level(), new BlockPos(posX, Mth.floor(painting.getY() + (double)((yMax + yMin) / 2.0F / 16.0F)), posZ));

                this.vertex(matrix4f, matrix3f, vertex, xMax, yMax, z, u1, v0, 0, 0, 1, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMin, yMax, z, u0, v0, 0, 0, 1, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMin, yMin, z, u0, v1, 0, 0, 1, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMax, yMin, z, u1, v1, 0, 0, 1, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMax, yMax, -z, u01, v01, 0, 1, 0, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMin, yMax, -z, u11, v01, 0, 1, 0, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMin, yMax, z, u11, v, 0, 1, 0, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMax, yMax, z, u01, v, 0, 1, 0, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMax, yMin, z, u01, v01, 0, -1, 0, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMin, yMin, z, u11, v01, 0, -1, 0, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMin, yMin, -z, u11, v, 0, -1, 0, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMax, yMin, -z, u01, v, 0, -1, 0, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMax, yMax, z, u, v02, -1, 0, 0, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMax, yMin, z, u, v11, -1, 0, 0, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMax, yMin, -z, u02, v11, -1, 0, 0, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMax, yMax, -z, u02, v02, -1, 0, 0, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMin, yMax, -z, u, v02, 1, 0, 0, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMin, yMin, -z, u, v11, 1, 0, 0, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMin, yMin, z, u02, v11, 1, 0, 0, light, 1.0F);
                this.vertex(matrix4f, matrix3f, vertex, xMin, yMax, z, u02, v02, 1, 0, 0, light, 1.0F);
            }
        }
    }

    protected void vertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertex, float x, float y, float z, float u, float v, int normX, int normY, int normZ, int light, float a) {
        vertex.vertex(matrix4f, x, y, z).color(255, 255, 255, (int)(255.0F * a)).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, normX, normY, normZ).endVertex();
    }

    protected double getWidthOffset(@Nullable Parallax parallax, MagicPainting painting, double widthDiff) {
        if (parallax != null) switch (parallax.type()) {
            case VIEW_ANGLE -> {
                float partialTick = Minecraft.getInstance().getPartialTick();
                Vec3 camPos = Minecraft.getInstance().cameraEntity != null ?
                        Minecraft.getInstance().cameraEntity.getEyePosition(partialTick) :
                        Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

                Vec3 paintPos = painting.position().relative(painting.getDirection().getOpposite(), 1.0D);

                double x = camPos.x - paintPos.x;
                double z = camPos.z - paintPos.z;
                double yRot = Mth.wrapDegrees((float)(Mth.atan2(z, x) * (double)(180F / (float)Math.PI)) - 90.0F - painting.getYRot());
                return widthDiff + Mth.clamp(yRot * parallax.multiplier() * widthDiff, -widthDiff, widthDiff);
            }
            case SINE_TIME -> {
                return widthDiff + (Math.sin((painting.tickCount + Minecraft.getInstance().getPartialTick()) * parallax.multiplier()) * widthDiff);
            }
            case LINEAR_TIME -> {
                double trueTick = (painting.tickCount + Minecraft.getInstance().getPartialTick()) * parallax.multiplier();
                double wholeDiff = widthDiff * 2.0D;
                return widthDiff + (parallax.multiplier() > 0.0D ? -widthDiff + (trueTick % wholeDiff) : widthDiff - (trueTick % wholeDiff));
            }
        }
        return 0.0D;
    }

    protected double getHeightOffset(@Nullable Parallax parallax, MagicPainting painting, double heightDiff) {
        if (parallax != null) switch (parallax.type()) {
            case VIEW_ANGLE -> {
                float partialTick = Minecraft.getInstance().getPartialTick();
                Vec3 camPos = Minecraft.getInstance().cameraEntity != null ?
                        Minecraft.getInstance().cameraEntity.getEyePosition(partialTick) :
                        Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

                Vec3 paintPos = painting.position().relative(painting.getDirection().getOpposite(), 1.0D);

                double x = camPos.x - paintPos.x;
                double y = camPos.y - paintPos.y;
                double z = camPos.z - paintPos.z;
                double pythagoras = Math.sqrt(x * x + z * z);
                double xRot = Mth.wrapDegrees((float)(-(Mth.atan2(y, pythagoras) * (double)(180F / (float)Math.PI))));

                return heightDiff - Mth.clamp(xRot * parallax.multiplier() * heightDiff, -heightDiff, heightDiff);
            }
            case SINE_TIME -> {
                return heightDiff - (Math.cos((painting.tickCount + Minecraft.getInstance().getPartialTick()) * parallax.multiplier()) * heightDiff);
            }
            case LINEAR_TIME -> {
                double trueTick = (painting.tickCount + Minecraft.getInstance().getPartialTick()) * parallax.multiplier();
                double wholeDiff = heightDiff * 2.0D;
                return heightDiff - (parallax.multiplier() > 0.0D ? -heightDiff + (trueTick % wholeDiff) : heightDiff - (trueTick % wholeDiff));
            }
        }
        return 0.0D;
    }

    protected static final float ONE_SECOND = 1.0F / 1200F;

    protected float getAlpha(@Nullable OpacityModifier opacityModifier, MagicPainting painting) {
        float a = 1.0F;
        if (opacityModifier != null) switch (opacityModifier.type()) {
            case DISTANCE -> {
                Vec3 camPos = Optional.ofNullable(Minecraft.getInstance().cameraEntity).map(Entity::getEyePosition).orElse(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition());
                a = (float) (opacityModifier.invert() ? opacityModifier.multiplier() - camPos.distanceTo(painting.position()) : camPos.distanceTo(painting.position()) - opacityModifier.multiplier());
            }
            case WEATHER -> {
                float partialTick = Minecraft.getInstance().getPartialTick();
                a = (painting.level().getRainLevel(partialTick) + painting.level().getThunderLevel(partialTick)) * 0.5F * Math.abs(opacityModifier.multiplier());
                if (opacityModifier.invert()) a = 1.0F - a;
            }
            case LIGHTNING -> {
                if (painting.level() instanceof ClientLevel clientLevel) {
                    a = clientLevel.getSkyFlashTime() * opacityModifier.multiplier();
                    if (opacityModifier.invert()) a = 1.0F - a;
                }
            }
            case DAY_TIME -> {
                float dayTime = Math.abs(painting.level().getTimeOfDay(Minecraft.getInstance().getPartialTick()) - 0.5F) * 2.0F;
                if (opacityModifier.invert()) dayTime = 1.0F - dayTime;
                a = (float) Math.pow(dayTime, opacityModifier.multiplier());
            }
            case DAY_TIME_SHARP -> {
                float dayTime = Math.abs(painting.level().getTimeOfDay(Minecraft.getInstance().getPartialTick()) - 0.5F) * 2.0F;
                if (opacityModifier.invert()) dayTime = 1.0F - dayTime;
                float threshold = 1.0F - opacityModifier.multiplier();

                if (threshold > 1.0F - ONE_SECOND) threshold = 1.0F - ONE_SECOND;
                else if (threshold < ONE_SECOND) threshold = ONE_SECOND;

                if (dayTime < threshold) a = 0.0F;
                else if (dayTime <= threshold + ONE_SECOND) a = (dayTime - threshold) / ONE_SECOND;
            }
            case SINE_TIME -> {
                a = (float) (Math.sin((painting.tickCount + Minecraft.getInstance().getPartialTick()) * opacityModifier.multiplier())) * 0.5F + 0.5F;
                if (opacityModifier.invert()) a = 1.0F - a;
            }
            case HEALTH -> {
                if (Minecraft.getInstance().getCameraEntity() instanceof LivingEntity living) {
                    a = (living.getHealth() / living.getMaxHealth()) * opacityModifier.multiplier();
                }
                if (opacityModifier.invert()) a = 1.0F - a;
            }
            case HUNGER -> {
                if (Minecraft.getInstance().getCameraEntity() instanceof Player player) {
                    FoodData food = player.getFoodData();
                    a = ((float) food.getFoodLevel() / 20.0F) * opacityModifier.multiplier();
                }
                if (opacityModifier.invert()) a = 1.0F - a;
            }
            case HOLDING_ITEM -> {
                if (Minecraft.getInstance().getCameraEntity() instanceof LivingEntity living) {
                    ItemStack key = opacityModifier.item();
                    if (key != null) {
                        if (!living.isHolding(stack -> ItemStack.isSameItemSameTags(stack, key) &&
                                (opacityModifier.multiplier() <= 0.0F || Mth.equal(stack.getCount(), opacityModifier.multiplier()))))
                            a = 0.0F;
                        if (opacityModifier.invert()) a = 1.0F - a;
                    }
                }
            }
        }
        return Mth.clamp(a, 0.0F, 1.0F);
    }

    protected int getFrameUV(int i, int maxI) {
        if (maxI <= 1) return 4;
        else if (i == 0) return 1;
        else if (i == maxI - 1) return 3;
        else return 2;
    }
}