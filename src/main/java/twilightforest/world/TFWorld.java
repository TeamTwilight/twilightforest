package twilightforest.world;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.IChunkGenerator;

public class TFWorld {
	public static final int SEALEVEL = 31;
	public static final int CHUNKHEIGHT = 256; // more like world generation height
	public static final int MAXHEIGHT = 256; // actual max height

	public static IChunkGenerator getChunkGenerator(World world) {
		return ((WorldServer) world).getChunkProvider().chunkGenerator;
	}
}
