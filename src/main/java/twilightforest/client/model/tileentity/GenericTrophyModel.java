package twilightforest.client.model.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;

//[VanillaCopy] of SkullModelBase, this is just so our trophies use it
public abstract class GenericTrophyModel extends Model {

	public GenericTrophyModel() {
		super(RenderType::entityTranslucent);
	}

	public abstract void setRotations(float x, float y, float z);

	public void renderHelmToBuffer(PoseStack matrix, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
	}

	public void openMouthForTrophy(float mouthOpen) {}
}
