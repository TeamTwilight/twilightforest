package twilightforest.tileentity;

import net.minecraft.tileentity.TileEntitySkull;

public class TileEntityTFTrophy extends TileEntitySkull {

	public int ticksExisted;

	@Override
	public void update() {
		super.update();
		this.ticksExisted++;
	}

}
