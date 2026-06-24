package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.NoopModel;
import twilightforest.entity.monster.SnowGuardian;

public class SnowGuardianRenderer extends HumanoidMobRenderer<SnowGuardian, HumanoidRenderState, NoopModel<HumanoidRenderState>> {

	private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/entity/zombie/zombie.png");

	public SnowGuardianRenderer(EntityRendererProvider.Context context) {
		super(context, new NoopModel<>(context.bakeLayer(TFModelLayers.NOOP)), 0.25F);
		this.addLayer(new HumanoidArmorLayer<>(this, ArmorModelSet.bake(ModelLayers.ZOMBIE_ARMOR, context.getModelSet(), NoopModel::new), context.getEquipmentRenderer()));
	}

	@Override
	public HumanoidRenderState createRenderState() {
		return new HumanoidRenderState();
	}

	@Override
	public Identifier getTextureLocation(HumanoidRenderState state) {
		return TEXTURE;
	}

	@Override
	protected void scale(HumanoidRenderState state, PoseStack stack) {
		stack.translate(0.0F, Mth.sin(state.ageInTicks * 0.2F) * 0.15F, 0.0F);
	}
}
