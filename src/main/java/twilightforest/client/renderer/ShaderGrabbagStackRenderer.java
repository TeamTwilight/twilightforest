package twilightforest.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.opengl.GL11;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.client.TFClientEvents;
import twilightforest.client.shader.ShaderManager;

//FIXME I know how to make TEs do fun stuff but not Items
public class ShaderGrabbagStackRenderer extends TileEntityRenderer<ShaderGrabbagStackRenderer.DummyTile> {

    public static class DummyTile extends TileEntity {
        public DummyTile(TileEntityType<?> tileEntityTypeIn) {
            super(tileEntityTypeIn);
        }
    }

    public static ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_twilight")));
    private final ItemCameraTransforms.TransformType transform = ItemCameraTransforms.TransformType.NONE;

    public ShaderGrabbagStackRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(DummyTile tileEntityIn, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
        ms.push(); // Stack + 1
        GlStateManager.disableCull();

        ModelResourceLocation silver_back = new ModelResourceLocation(TwilightForestMod.prefix("trophy_minor"), "inventory");
        ModelResourceLocation shaderCase = new ModelResourceLocation(TwilightForestMod.prefix("shader"), "inventory");
        ResourceLocation bg = TwilightForestMod.prefix("textures/items/star_burst_mask.png");

        IBakedModel modelBack = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getModelManager().getModel(silver_back);
        IBakedModel modelCase = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getModelManager().getModel(shaderCase);

        if (transform == ItemCameraTransforms.TransformType.GUI) {
            ms.push(); // Stack + 2
            ms.translate(0.5F, 0.5F, -1.5F);
            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GUI, false, ms, buffers, light, overlay, ForgeHooksClient.handleCameraTransforms(ms, modelBack, transform, false));
            ms.pop(); // Stack + 1
            ms.translate(0.5F, 0.5F, 0F);
            ms.rotate(Vector3f.YP.rotation(TFConfig.CLIENT_CONFIG.rotateTrophyHeadsGui.get() ? TFClientEvents.rotationTicker : 0));

            ms.push(); // Stack + 2
            ms.translate(0.5F, 0.5F, 0.5F);
            // Render the lunchbox
            Minecraft.getInstance().getItemRenderer().renderItem(stack, transform, false, ms, buffers, light, overlay, ForgeHooksClient.handleCameraTransforms(ms, modelCase, transform, false));
            ms.pop(); // Stack + 1

            // Render the star burst
            ms.pop(); // Stack + 0
            ms.push(); // Stack + 1
            ms.push(); // Stack + 2
            ms.translate(0.5F, 0.5F, -1.0F);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            Minecraft.getInstance().getTextureManager().bindTexture(bg);
            int c = stack.getRarity().color.getColor();
            float r = (c >> 16 & 0xFF) / 255.0f;
            float g = (c >> 8 & 0xFF) / 255.0f;
            float b = (c & 0xFF) / 255.0f;

            buffer.pos( -1, 1, 0)
                    .tex(0, 1)
                    .color(r, g, b, 1f)
                    .endVertex();
            buffer.pos(1, 1, 0)
                    .tex(1, 1)
                    .color(r, g, b, 1f)
                    .endVertex();
            buffer.pos(1, -1, 0)
                    .tex(1, 0)
                    .color(r, g, b, 1f)
                    .endVertex();
            buffer.pos(-1, -1, 0)
                    .tex(0, 0)
                    .color(r, g, b, 1f)
                    .endVertex();

            ShaderManager.useShader(ShaderManager.starburstShader, ShaderManager.Uniforms.TIME);
            // Blur the star burst mask
            GlStateManager.getTexLevelParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            tessellator.draw();

            // Deblur, so we don't blur all of the textures in rendering calls afterwards
            GlStateManager.getTexLevelParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            ShaderManager.releaseShader();
            RenderSystem.color4f(1f, 1f, 1f, 1f);
            ms.pop(); // Stack + 1
        } else {
            Minecraft.getInstance().getItemRenderer().renderItem(stack, transform, false, ms, buffers, light, overlay, ForgeHooksClient.handleCameraTransforms(ms, modelCase, transform, false));
        }
        GlStateManager.enableCull();
        ms.pop(); // Stack + 0
    }
}
