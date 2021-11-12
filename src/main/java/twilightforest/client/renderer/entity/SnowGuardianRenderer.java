package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.NoopModel;
import twilightforest.entity.monster.SnowGuardian;

public class SnowGuardianRenderer extends TFBipedRenderer<SnowGuardian, NoopModel<SnowGuardian>> {

	public SnowGuardianRenderer(EntityRendererProvider.Context manager, NoopModel<SnowGuardian> model) {
		super(manager, model, new NoopModel<>(manager.bakeLayer(TFModelLayers.NOOP)), new NoopModel<>(manager.bakeLayer(TFModelLayers.NOOP)), 0.25F, "textures/entity/zombie/zombie.png");
		this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(manager.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(manager.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
	}

	@Override
	protected void scale(SnowGuardian entity, PoseStack stack, float partialTicks) {
		float bounce = entity.tickCount + partialTicks;
		stack.translate(0F, Mth.sin((bounce) * 0.2F) * 0.15F, 0F);
	}
}
