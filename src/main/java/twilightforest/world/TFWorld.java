package twilightforest.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.IChunkGenerator;
import twilightforest.TFConfig;

public class TFWorld {
	public static int SEALEVEL = 31;
	public static int CHUNKHEIGHT = 256; // more like world generation height
	public static int MAXHEIGHT = 256; // actual max height

	public static IChunkGenerator getChunkGenerator(World world) {
		return ((WorldServer) world).getChunkProvider().chunkGenerator;
	}

	public static NBTTagCompound getDimensionData(World world) {
		return world.getWorldInfo().getDimensionData(TFConfig.dimension.dimensionID);
	}

	public static void setDimensionData(World world, NBTTagCompound data) {
		world.getWorldInfo().setDimensionData(TFConfig.dimension.dimensionID, data);
	}
}
