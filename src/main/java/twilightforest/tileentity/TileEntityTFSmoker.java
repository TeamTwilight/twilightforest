package twilightforest.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.TFParticleType;

public class TileEntityTFSmoker extends TileEntity implements ITickable {

	private long counter = 0;

	@Override
	public void update() {
		if (world.isRemote && ++counter % 4 == 0) {
			TwilightForestMod.proxy.spawnParticle(TFParticleType.HUGE_SMOKE, pos.getX() + 0.5, pos.getY() + 0.95, pos.getZ() + 0.5,
					Math.cos(counter / 10.0) * 0.05, 0.25D, Math.sin(counter / 10.0) * 0.05
			);
		}
	}
}
