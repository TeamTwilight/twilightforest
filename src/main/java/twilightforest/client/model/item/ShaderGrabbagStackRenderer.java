package twilightforest.client.model.item;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.client.TFClientEvents;
import twilightforest.client.shader.ShaderHelper;
import twilightforest.compat.ie.ItemTFShaderGrabbag;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;

public class ShaderGrabbagStackRenderer extends TileEntitySpecialRenderer<ShaderGrabbagStackRenderer.DummyTile> {
    public static class DummyTile extends TileEntity {}

    @MethodsReturnNonnullByDefault
    @ParametersAreNonnullByDefault
    public class BakedModel implements IBakedModel {
        private class Overrides extends ItemOverrideList {
            public Overrides() {
                super(Collections.EMPTY_LIST);
            }

            @Override
            public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
                ShaderGrabbagStackRenderer.this.stack = stack;
                return BakedModel.this;
            }
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
            return Collections.EMPTY_LIST;
        }

        @Override
        public boolean isAmbientOcclusion() {
            return true;
        }

        @Override
        public boolean isGui3d() {
            return true;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return true;
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/iron_block");
        }

        @Override
        public ItemOverrideList getOverrides() {
            return new Overrides();
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
            ShaderGrabbagStackRenderer.this.transform = cameraTransformType;
            return Pair.of(this, null);
        }
    }

    public final BakedModel baked = new BakedModel();

    private ItemStack stack;
    private ItemCameraTransforms.TransformType transform;
    private IBakedModel modelBack;
    private IBakedModel modelCase;

    private final ResourceLocation bg = new ResourceLocation(TwilightForestMod.ID, "textures/items/star_burst_mask.png");

    @Override
    public void render(DummyTile te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();

        if (modelCase == null) modelCase = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(new ModelResourceLocation(TwilightForestMod.ID + ":shader", "inventory"));

        if (transform == ItemCameraTransforms.TransformType.GUI) {
            if (modelBack == null) modelBack = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(new ModelResourceLocation(TwilightForestMod.ID + ":trophy_minor", "inventory"));

            GlStateManager.disableLighting();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.5F, 0.5F, -1.5F);

            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ForgeHooksClient.handleCameraTransforms(modelBack, transform, transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND));

            GlStateManager.popMatrix();
            GlStateManager.enableLighting();

            GlStateManager.disableLighting();
            GlStateManager.translate(0.5F, 0.5F, 0F);
            GlStateManager.rotate(TFConfig.rotateTrophyHeadsGui ? TFClientEvents.rotationTicker : 0F, 0.125F, 1F, 0.125F);
            //GlStateManager.translate(0F, -0.5F, 0F);

        }

        // FIXME transformations; Why are they all over the place??
        GlStateManager.pushMatrix();
        if (transform != ItemCameraTransforms.TransformType.GUI) GlStateManager.translate(0.5F, 0.5F, 0.5F);
        else GlStateManager.scale(0.9F, 0.9F, 0.9F);
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ForgeHooksClient.handleCameraTransforms(modelCase, transform, false));
        GlStateManager.popMatrix();

        if (transform == ItemCameraTransforms.TransformType.GUI) {
            GlStateManager.enableLighting();

            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.5F, 0.5F, -1.0F);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            Minecraft.getMinecraft().getTextureManager().bindTexture(bg);

            int c = blusunrize.immersiveengineering.client.ClientUtils.getFormattingColour(ItemTFShaderGrabbag.shader_bag.getRarity(stack).rarityColor);
            float r = (c >> 16 & 0xFF) / 255.0f;
            float g = (c >> 8 & 0xFF) / 255.0f;
            float b = (c & 0xFF) / 255.0f;

            buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(x - 1, y + 1, 0)
                    .tex(0, 1)
                    .color(r, g, b, 1f)
                    .endVertex();
            buffer.pos(x + 1, y + 1, 0)
                    .tex(1, 1)
                    .color(r, g, b, 1f)
                    .endVertex();
            buffer.pos(x + 1, y - 1, 0)
                    .tex(1, 0)
                    .color(r, g, b, 1f)
                    .endVertex();
            buffer.pos(x - 1, y - 1, 0)
                    .tex(0, 0)
                    .color(r, g, b, 1f)
                    .endVertex();

            ShaderHelper.useShader(ShaderHelper.starburstShader, ShaderHelper.TIME);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            tessellator.draw();
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            ShaderHelper.releaseShader();

            GlStateManager.color(1f, 1f, 1f, 1f);

            GlStateManager.popMatrix();
        }

        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }
}
