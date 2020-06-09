package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFTinyFirefly;
import twilightforest.entity.passive.EntityTFMobileFirefly;

public class RenderTFMobileFirefly<T extends EntityTFMobileFirefly> extends EntityRenderer<T> {

    private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("firefly-tiny.png");
    private final ModelTFTinyFirefly fireflyModel = new ModelTFTinyFirefly();

    public RenderTFMobileFirefly(EntityRendererManager manager) {
        super(manager);
    }

    public void buildGeometry(T firefly, MatrixStack stack, IVertexBuilder buffer, ActiveRenderInfo info, float p_225606_3_, int light) {

        stack.push();
        float f = firefly.getWidth();
        stack.scale(f, f, f);
        float f1 = 0.5F;
        float f2 = 0.0F;
        float f3 = firefly.getHeight();
        float f4 = 0.0F;
        stack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-info.getYaw()));
        stack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(info.getPitch()));
        stack.translate(0.0D, 0.0D, (double) (-0.3F + (float) ((int) f3) * 0.02F));
        float f5 = 0.0F;

        float f6 = 30F / 64F;//minU
        float f7 = 0F;//minV
        float f8 = 40F / 64F;//maxU
        float f9 = 10F / 32F;//maxV
        MatrixStack.Entry matrixstack$entry = stack.peek();

        fireflyVertex(firefly, matrixstack$entry, buffer, f1 - 0.0F, 0.0F - f4, f5, f8, f9);
        fireflyVertex(firefly, matrixstack$entry, buffer, -f1 - 0.0F, 0.0F - f4, f5, f6, f9);
        fireflyVertex(firefly, matrixstack$entry, buffer, -f1 - 0.0F, 1.0F - f4, f5, f6, f7);
        fireflyVertex(firefly, matrixstack$entry, buffer, f1 - 0.0F, 1.0F - f4, f5, f8, f7);


        stack.pop();
    }

    private static void fireflyVertex(EntityTFMobileFirefly firefly, MatrixStack.Entry p_229090_0_, IVertexBuilder p_229090_1_, float p_229090_2_, float p_229090_3_, float p_229090_4_, float p_229090_5_, float p_229090_6_) {
        float light = firefly.getGlowBrightness();
        p_229090_1_.vertex(p_229090_0_.getModel(), p_229090_2_, p_229090_3_, p_229090_4_).color(1F, 1F, 1F, light).texture(p_229090_5_, p_229090_6_).overlay(OverlayTexture.DEFAULT_UV).light((int) 240).normal(p_229090_0_.getNormal(), 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public void render(T firefly, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
        stack.push();


        IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.getEntityTranslucent(this.getEntityTexture(firefly)));
        this.buildGeometry(firefly, stack, ivertexbuilder, Minecraft.getInstance().getRenderManager().info, Minecraft.getInstance().getRenderPartialTicks(), light);
        stack.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return textureLoc;
    }
}
