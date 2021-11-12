package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.IceCrystalModel;
import twilightforest.entity.monster.IceCrystal;

public class IceCrystalRenderer extends MobRenderer<IceCrystal, IceCrystalModel> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("icecrystal.png");

	public IceCrystalRenderer(EntityRendererProvider.Context manager) {
		super(manager, new IceCrystalModel(manager.bakeLayer(TFModelLayers.ICE_CRYSTAL)), 0.25F);
	}

	@Override
	protected void scale(IceCrystal entity, PoseStack stack, float partialTicks) {
		float bounce = entity.tickCount + partialTicks;
		stack.translate(0F, Mth.sin((bounce) * 0.2F) * 0.15F, 0F);
	}

	@Override
	public ResourceLocation getTextureLocation(IceCrystal entity) {
		return textureLoc;
	}
}
