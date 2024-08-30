package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

public interface TrophyBlockModel {

	void setupRotationsForTrophy(float x, float y, float z, float mouthAngle);

	void renderTrophy(PoseStack stack, MultiBufferSource buffer, int light, int overlay, int color, boolean itemForm);
}
