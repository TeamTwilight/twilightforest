package twilightforest.client.model.entity.legacy;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import twilightforest.entity.boss.EntityTFHydraHead;
import twilightforest.entity.boss.EntityTFHydraPart;

public class ModelTFHydraHeadLegacy  extends SegmentedModel<EntityTFHydraHead> {

    ModelRenderer head;
    ModelRenderer jaw;
    ModelRenderer frill;

    public ModelTFHydraHeadLegacy() {
        textureWidth = 512;
        textureHeight = 256;

        head = new ModelRenderer(this/*, "head"*/);
        head.setTextureOffset(272, 0).addBox(-16F, -14F, -32F, 32, 24, 32);
        head.setTextureOffset(272, 56).addBox(-15F, -2F, -56F, 30, 12, 24);
        head.setTextureOffset(272, 132).addBox(-15F, 10F, -20F, 30, 8, 16);
        head.setTextureOffset(128, 200).addBox(-2F, -30F, -12F, 4, 24, 24);
        head.setTextureOffset(272, 156).addBox(-12F, 10, -49F, 2, 5, 2);
        head.setTextureOffset(272, 156).addBox(10F, 10, -49F, 2, 5, 2);
        head.setTextureOffset(280, 156).addBox(-8F, 9, -49F, 16, 2, 2);
        head.setTextureOffset(280, 160).addBox(-10F, 9, -45F, 2, 2, 16);
        head.setTextureOffset(280, 160).addBox(8F, 9, -45F, 2, 2, 16);
        head.setRotationPoint(0F, 0F, 0F);

        jaw = new ModelRenderer(this/*, "jaw"*/);
        jaw.setRotationPoint(0F, 10F, -20F);
        jaw.setTextureOffset(272, 92).addBox(-15F, 0F, -32F, 30, 8, 32);
        jaw.setTextureOffset(272, 156).addBox(-10F, -5, -29F, 2, 5, 2);
        jaw.setTextureOffset(272, 156).addBox(8F, -5, -29F, 2, 5, 2);
        jaw.setTextureOffset(280, 156).addBox(-8F, -1, -29F, 16, 2, 2);
        jaw.setTextureOffset(280, 160).addBox(-10F, -1, -25F, 2, 2, 16);
        jaw.setTextureOffset(280, 160).addBox(8F, -1, -25F, 2, 2, 16);
        setRotation(jaw, 0F, 0F, 0F);
        head.addChild(jaw);

        frill = new ModelRenderer(this/*, "frill"*/);
        frill.setRotationPoint(0F, 0F, -14F);
        frill.setTextureOffset(272, 200).addBox(-24F, -40.0F, 0F, 48, 48, 4);
        setRotation(frill, -0.5235988F, 0F, 0F);
        head.addChild(frill);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

//	@Override
//	public void render(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
//		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
//		setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
//		head.render(scale);
//	}

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(head);
    }

//	@Override
//	public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
////		head.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
////		head.rotateAngleX = headPitch / (180F / (float)Math.PI);
//	}

    @Override
    public void setRotationAngles(EntityTFHydraHead entity, float v, float v1, float v2, float v3, float v4) { }

    @Override
    public void setLivingAnimations(EntityTFHydraHead entity, float limbSwing, float limbSwingAmount, float partialTicks) {
        head.rotateAngleY = getRotationY(entity, partialTicks);
        head.rotateAngleX = getRotationX(entity, partialTicks);

        float mouthOpenLast = entity.getMouthOpenLast();
        float mouthOpenReal = entity.getMouthOpen();
        float mouthOpen = MathHelper.lerp(partialTicks, mouthOpenLast, mouthOpenReal);
        head.rotateAngleX -= (float) (mouthOpen * (Math.PI / 12.0));
        jaw.rotateAngleX = (float) (mouthOpen * (Math.PI / 3.0));
    }

    public void openMouthForTrophy(float mouthOpen) {
        head.rotateAngleY = 0;
        head.rotateAngleX = 0;

        head.rotateAngleX -= (float) (mouthOpen * (Math.PI / 12.0));
        jaw.rotateAngleX = (float) (mouthOpen * (Math.PI / 3.0));
    }

    public float getRotationY(EntityTFHydraPart whichHead, float time) {
        //float yawOffset = hydra.prevRenderYawOffset + (hydra.renderYawOffset - hydra.prevRenderYawOffset) * time;
        float yaw = whichHead.prevRotationYaw + (whichHead.rotationYaw - whichHead.prevRotationYaw) * time;

        return yaw / 57.29578F;
    }

    public float getRotationX(EntityTFHydraPart whichHead, float time) {
        return (whichHead.prevRotationPitch + (whichHead.rotationPitch - whichHead.prevRotationPitch) * time) / 57.29578F;
    }
}