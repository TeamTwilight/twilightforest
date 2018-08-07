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
import twilightforest.client.shader.ShaderManager;
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
        GlStateManager.pushMatrix(); // Stack + 1
        GlStateManager.disableCull();

        if (modelCase == null) modelCase = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(new ModelResourceLocation(TwilightForestMod.ID + ":shader", "inventory"));

        // Render the trophy-backing if it's in a GUI.
        if (transform == ItemCameraTransforms.TransformType.GUI) {
            // Lazy init. If modelBack is null, pull a new instance.
            if (modelBack == null) modelBack = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(new ModelResourceLocation(TwilightForestMod.ID + ":trophy_minor", "inventory"));

            GlStateManager.disableLighting();
            GlStateManager.pushMatrix(); // Stack + 2
            GlStateManager.translate(0.5F, 0.5F, -1.5F);

            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ForgeHooksClient.handleCameraTransforms(modelBack, transform, transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND));

            GlStateManager.popMatrix(); // Stack + 1
            GlStateManager.enableLighting();

            GlStateManager.disableLighting();
            GlStateManager.translate(0.5F, 0.5F, 0F);
            // Rotate the lunchbox if we're in the Gui. This is a setup for the next bit of rendering.
            GlStateManager.rotate(TFConfig.rotateTrophyHeadsGui ? TFClientEvents.rotationTicker : 0F, 0.125F, 1F, 0.125F);
            //GlStateManager.translate(0F, -0.5F, 0F);

        }

        GlStateManager.pushMatrix(); // Stack + 2
        // Translate the item render to the right place if it's in inventory.
        // Otherwise just re-scale the item because it's a bit big for the trophy sprite backing.
        if (transform != ItemCameraTransforms.TransformType.GUI) GlStateManager.translate(0.5F, 0.5F, 0.5F);
        else GlStateManager.scale(0.9F, 0.9F, 0.9F);

        // Render the lunchbox
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ForgeHooksClient.handleCameraTransforms(modelCase, transform, false));
        GlStateManager.popMatrix(); // Stack + 1

        // Render the star burst
        if (transform == ItemCameraTransforms.TransformType.GUI) {
            // Forgot why this was needed but it's needed
            GlStateManager.enableLighting();

            // Pop the previous stack and push new stack
            GlStateManager.popMatrix(); // Stack + 0
            GlStateManager.pushMatrix(); // Stack + 1

            GlStateManager.pushMatrix(); // Stack + 2

            // Since we're in a new stack different than above rendering calls, we'll also need to re-translate.
            // Z value -1 puts us behind the lunchbox but in front of trophy sprite
            GlStateManager.translate(0.5F, 0.5F, -1.0F);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            // Bind the star burst mask tex
            Minecraft.getMinecraft().getTextureManager().bindTexture(bg);

            // Just gonna borrow your code for a sec blu, thnx
            int c = blusunrize.immersiveengineering.client.ClientUtils.getFormattingColour(ItemTFShaderGrabbag.shader_bag.getRarity(stack).rarityColor);

            // unpack colors
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

            // Shader, engage!
            ShaderManager.useShader(ShaderManager.starburstShader, ShaderManager.TIME);
            // Blur the star burst mask
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            tessellator.draw();
            // Deblur, so we don't blur all of the textures in rendering calls afterwards
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            // Disengage shader
            ShaderManager.releaseShader();

            // reset color
            GlStateManager.color(1f, 1f, 1f, 1f);

            GlStateManager.popMatrix(); // Stack + 1
        }

        GlStateManager.enableCull();
        GlStateManager.popMatrix(); // Stack + 0
    }
}
