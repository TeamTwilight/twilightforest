package twilightforest.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.opengl.GL11;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.client.TFClientEvents;
import twilightforest.client.shader.ShaderManager;

//FIXME I know how to make TEs do fun stuff but not Items
public class ShaderGrabbagStackRenderer implements BlockEntityRenderer<ShaderGrabbagStackRenderer.DummyTile> {

    public static class DummyTile extends BlockEntity {
        public DummyTile(BlockEntityType<?> tileEntityTypeIn) {
            super(tileEntityTypeIn, BlockPos.ZERO, Blocks.AIR.defaultBlockState());
        }
    }

    public static ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_twilight")));
    private final ItemTransforms.TransformType transform = ItemTransforms.TransformType.NONE;

    public ShaderGrabbagStackRenderer(BlockEntityRendererProvider.Context renderer) {

    }

    @Override
    public void render(DummyTile tileEntityIn, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
        ms.pushPose(); // Stack + 1
        GlStateManager._disableCull();

        ModelResourceLocation silver_back = new ModelResourceLocation(TwilightForestMod.prefix("trophy_minor"), "inventory");
        ModelResourceLocation shaderCase = new ModelResourceLocation(TwilightForestMod.prefix("shader"), "inventory");
        ResourceLocation bg = TwilightForestMod.prefix("textures/items/star_burst_mask.png");

        BakedModel modelBack = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getModelManager().getModel(silver_back);
        BakedModel modelCase = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getModelManager().getModel(shaderCase);

        if (transform == ItemTransforms.TransformType.GUI) {
            ms.pushPose(); // Stack + 2
            ms.translate(0.5F, 0.5F, -1.5F);
            Minecraft.getInstance().getItemRenderer().render(stack, ItemTransforms.TransformType.GUI, false, ms, buffers, light, overlay, ForgeHooksClient.handleCameraTransforms(ms, modelBack, transform, false));
            ms.popPose(); // Stack + 1
            ms.translate(0.5F, 0.5F, 0F);
            ms.mulPose(Vector3f.YP.rotation(TFConfig.CLIENT_CONFIG.rotateTrophyHeadsGui.get() ? TFClientEvents.rotationTicker : 0));

            ms.pushPose(); // Stack + 2
            ms.translate(0.5F, 0.5F, 0.5F);
            // Render the lunchbox
            Minecraft.getInstance().getItemRenderer().render(stack, transform, false, ms, buffers, light, overlay, ForgeHooksClient.handleCameraTransforms(ms, modelCase, transform, false));
            ms.popPose(); // Stack + 1

            // Render the star burst
            ms.popPose(); // Stack + 0
            ms.pushPose(); // Stack + 1
            ms.pushPose(); // Stack + 2
            ms.translate(0.5F, 0.5F, -1.0F);

            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder buffer = tessellator.getBuilder();
            RenderSystem._setShaderTexture(0, bg);
            int c = stack.getRarity().color.getColor();
            float r = (c >> 16 & 0xFF) / 255.0f;
            float g = (c >> 8 & 0xFF) / 255.0f;
            float b = (c & 0xFF) / 255.0f;

            buffer.vertex( -1, 1, 0)
                    .uv(0, 1)
                    .color(r, g, b, 1f)
                    .endVertex();
            buffer.vertex(1, 1, 0)
                    .uv(1, 1)
                    .color(r, g, b, 1f)
                    .endVertex();
            buffer.vertex(1, -1, 0)
                    .uv(1, 0)
                    .color(r, g, b, 1f)
                    .endVertex();
            buffer.vertex(-1, -1, 0)
                    .uv(0, 0)
                    .color(r, g, b, 1f)
                    .endVertex();

            ShaderManager.useShader(ShaderManager.starburstShader, ShaderManager.Uniforms.TIME);
            // Blur the star burst mask
            GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            tessellator.end();

            // Deblur, so we don't blur all of the textures in rendering calls afterwards
            GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            ShaderManager.releaseShader();
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            ms.popPose(); // Stack + 1
        } else {
            Minecraft.getInstance().getItemRenderer().render(stack, transform, false, ms, buffers, light, overlay, ForgeHooksClient.handleCameraTransforms(ms, modelCase, transform, false));
        }
        GlStateManager._enableCull();
        ms.popPose(); // Stack + 0
    }
}
