package twilightforest.world;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import twilightforest.TFConfig;

import java.util.function.Predicate;

public class TFWorld {

	public static final int SEALEVEL = 31;
	public static final int CHUNKHEIGHT = 256; // more like world generation height
	public static final int MAXHEIGHT = 256; // actual max height

	public static IChunkGenerator getChunkGenerator(World world) {
		return ((WorldServer) world).getChunkProvider().chunkGenerator;
	}

	public static NBTTagCompound getDimensionData(World world) {
		return world.getWorldInfo().getDimensionData(TFConfig.dimension.dimensionID);
	}

	public static void setDimensionData(World world, NBTTagCompound data) {
		world.getWorldInfo().setDimensionData(TFConfig.dimension.dimensionID, data);
	}

	public static int getGroundLevel(World world, int x, int z) {
		return getGroundLevel(world, x, z, block -> false);
	}

	public static int getGroundLevel(World world, int x, int z, Predicate<Block> extraBlocks) {
		// go from sea level up.  If we get grass, return that, otherwise return the last dirt, stone or gravel we got
		Chunk chunk = world.getChunk(x >> 4, z >> 4);
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		int lastDirt = SEALEVEL;
		for (int y = SEALEVEL; y < CHUNKHEIGHT - 1; y++) {
			Block block = chunk.getBlockState(pos.setPos(x, y, z)).getBlock();
			// grass = return immediately
			if (block == Blocks.GRASS) {
				return y + 1;
			} else if (block == Blocks.DIRT || block == Blocks.STONE || block == Blocks.GRAVEL || extraBlocks.test(block)) {
				lastDirt = y + 1;
			}
		}
		return lastDirt;
	}
}
