package twilightforest.client.model.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.client.TFClientEvents;
import twilightforest.client.shader.ShaderManager;
import twilightforest.compat.ie.ItemTFShaderGrabbag;

public class ShaderGrabbagStackRenderer extends TileEntitySpecialRenderer<ShaderGrabbagStackRenderer.DummyTile> {

    public static class DummyTile extends TileEntity {}

    private class BakedModel extends BuiltInItemModel {

        BakedModel() {
            super("minecraft:blocks/iron_block");
        }

        @Override
        protected void setItemStack(ItemStack stack) {
            ShaderGrabbagStackRenderer.this.stack = stack;
        }

        @Override
        protected void setTransform(ItemCameraTransforms.TransformType transform) {
            ShaderGrabbagStackRenderer.this.transform = transform;
        }
    }

    private ItemStack stack = ItemStack.EMPTY;
    private ItemCameraTransforms.TransformType transform = ItemCameraTransforms.TransformType.NONE;

    private final ResourceLocation bg = new ResourceLocation(TwilightForestMod.ID, "textures/items/star_burst_mask.png");

    private final ModelResourceLocation backModelLocation = new ModelResourceLocation(TwilightForestMod.ID + ":trophy_minor", "inventory");
    private final ModelResourceLocation caseModelLocation = new ModelResourceLocation(TwilightForestMod.ID + ":shader", "inventory");

    private final ModelResourceLocation itemModelLocation;

    public ShaderGrabbagStackRenderer(ModelResourceLocation itemModelLocation) {
        this.itemModelLocation = itemModelLocation;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        event.getModelRegistry().putObject(itemModelLocation, new BakedModel());
    }

    @Override
    public void render(DummyTile te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix(); // Stack + 1
        GlStateManager.disableCull();

        IBakedModel modelCase = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(caseModelLocation);

        // Render the trophy-backing if it's in a GUI.
        if (transform == ItemCameraTransforms.TransformType.GUI) {

            IBakedModel modelBack = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(backModelLocation);

            GlStateManager.disableLighting();
            GlStateManager.pushMatrix(); // Stack + 2
            GlStateManager.translate(0.5F, 0.5F, -1.5F);

            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ForgeHooksClient.handleCameraTransforms(modelBack, transform, false));

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
            int c = blusunrize.immersiveengineering.client.ClientUtils.getFormattingColour(ItemTFShaderGrabbag.shader_bag.getRarity(stack).color);

            // unpack colors
            float r = (c >> 16 & 0xFF) / 255.0f;
            float g = (c >> 8 & 0xFF) / 255.0f;
            float b = (c & 0xFF) / 255.0f;

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
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
            ShaderManager.useShader(ShaderManager.starburstShader, ShaderManager.Uniforms.TIME);
            // Blur the star burst mask
            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            tessellator.draw();
            // Deblur, so we don't blur all of the textures in rendering calls afterwards
            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
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
