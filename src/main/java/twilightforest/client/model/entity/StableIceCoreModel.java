package twilightforest.client.model.entity;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class StableIceCoreModel extends UnstableIceCoreModel {

	public StableIceCoreModel(ModelPart root) {
		super(root);
	}

	@Override
	public void setupAnim(LivingEntityRenderState state) {
		this.resetPose();
		for (int i = 0; i < this.spikes.length; i++) {
			// rotate the spikes
			this.spikes[i].yRot = (3.14159F / 2F) + (Mth.sin((state.ageInTicks) / 5.0F) * 0.5F);
			this.spikes[i].xRot = (state.ageInTicks) / 5.0F;
			this.spikes[i].zRot = Mth.cos(i / 5.0F) / 4.0F;

			this.spikes[i].xRot += i * (Mth.PI / 8.0F);

			this.cubes[i].y = 9.5F + Mth.sin((i + state.ageInTicks) / 3.0F) * 3.0F;
		}
	}
}
