package twilightforest.client.state.entity;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.DyeColor;

public class QuestingRamRenderState extends LivingEntityRenderState {

	public int colorFlags;

	public boolean isColorPresent(DyeColor color) {
		return (this.colorFlags & (1 << color.getId())) > 0;
	}

	public int countColorsSet() {
		return Integer.bitCount(this.colorFlags);
	}

}
