package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.AdherentModel;
import twilightforest.entity.monster.Adherent;

public class AdherentRenderer extends TFBipedRenderer<Adherent, AdherentModel> {
	public AdherentRenderer(EntityRendererProvider.Context manager) {
		super(manager, new AdherentModel(manager.bakeLayer(TFModelLayers.ADHERENT)), 0.625F, "adherent.png");
	}

	@Override
	protected void scale(Adherent e, PoseStack ms, float partialTicks) {
		float bounce = e.tickCount + partialTicks;
		ms.translate(0F, -0.125F - Mth.sin((bounce) * 0.133F) * 0.1F, 0F);
	}
}
