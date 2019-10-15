package twilightforest;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import twilightforest.world.TFWorld;

public class TFMazeMapData extends MapData {

	public int yCenter;

	public TFMazeMapData(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.yCenter = nbt.getInteger("yCenter");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound ret = super.writeToNBT(nbt);
		ret.setInteger("yCenter", this.yCenter);
		return ret;
	}

	public void calculateMapCenter(World world, double x, double y, double z, int mapScale) {
		super.calculateMapCenter(x, z, mapScale);
		this.yCenter = MathHelper.floor(y);

		// when we are in a labyrinth, snap to the LABYRINTH
		if (TFWorld.isTwilightForest(world) && TFFeature.getFeatureForRegion(MathHelper.floor(x) >> 4, MathHelper.floor(z) >> 4, world) == TFFeature.LABYRINTH) {
			BlockPos mc = TFFeature.getNearestCenterXYZ(MathHelper.floor(x) >> 4, MathHelper.floor(z) >> 4, world);
			this.xCenter = mc.getX();
			this.zCenter = mc.getZ();
			this.yCenter = MathHelper.floor(y);
		}
	}
}
