package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import org.jspecify.annotations.Nullable;

public class NoOpTrophyBlockModel implements TrophyBlockModel {

	@Override
	public void setupRotationsForTrophy(float animationTicks, float yaw, float mouthAngle) {
		// NO-OP
	}

	@Override
	public void renderTrophy(PoseStack stack, SubmitNodeCollector collector, int light, int overlay, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, ItemDisplayContext context) {
		// NO-OP
	}
}
