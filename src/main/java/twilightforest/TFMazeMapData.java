package twilightforest;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import twilightforest.world.WorldProviderTwilightForest;


public class TFMazeMapData extends MapData {
	public int yCenter;

	public TFMazeMapData(String par1Str) {
		super(par1Str);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.yCenter = par1NBTTagCompound.getInteger("yCenter");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound) {
		NBTTagCompound ret = super.writeToNBT(par1NBTTagCompound);
		ret.setInteger("yCenter", this.yCenter);
		return ret;
	}

	public void calculateMapCenter(World world, double x, double y, double z, int mapScale) {
		super.calculateMapCenter(x, z, mapScale);
		this.yCenter = MathHelper.floor(y);

		// when we are in a labyrinth, snap to the labyrinth
		if (world.provider instanceof WorldProviderTwilightForest && TFFeature.getFeatureForRegion(MathHelper.floor(x) >> 4, MathHelper.floor(z) >> 4, world) == TFFeature.labyrinth) {
			BlockPos mc = TFFeature.getNearestCenterXYZ(MathHelper.floor(x) >> 4, MathHelper.floor(z) >> 4, world);
			this.xCenter = mc.getX();
			this.zCenter = mc.getZ();
			this.yCenter = MathHelper.floor(y);
		}
	}
}
