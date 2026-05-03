package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.item.ItemDisplayContext;

public interface TrophyBlockModel {

	default void setupRotationsForTrophy(float animationTicks, float mouthAngle) {

	}

	void renderTrophy(PoseStack stack, SubmitNodeCollector collector, int light, ItemDisplayContext context);
}
