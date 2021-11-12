package twilightforest.client.model.entity;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import twilightforest.entity.monster.StableIceCore;

public class StableIceCoreModel extends UnstableIceCoreModel<StableIceCore> {

	public StableIceCoreModel(ModelPart root) {
		super(root);
	}

	@Override
	public void prepareMobModel(StableIceCore entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		this.alive = entity.isAlive();

		for (int i = 0; i < spikes.length; i++) {
			// rotate the spikes
			this.spikes[i].yRot = (3.14159F / 2F) + (Mth.sin((entity.tickCount + partialTicks) / 5.0F) * 0.5F);
			this.spikes[i].xRot = (entity.tickCount + partialTicks) / 5.0F;
			this.spikes[i].zRot = Mth.cos(i / 5.0F) / 4.0F;

			this.spikes[i].xRot += i * (Math.PI / 8F);

			this.cubes[i].y = 9.5F + Mth.sin((i + entity.tickCount + partialTicks) / 3F) * 3F;
		}
	}
}
