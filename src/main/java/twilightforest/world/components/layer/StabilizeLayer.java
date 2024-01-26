package twilightforest.world.components.layer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.init.custom.BiomeLayerTypes;
import twilightforest.world.components.layer.vanillalegacy.Area;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerFactory;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerType;
import twilightforest.world.components.layer.vanillalegacy.area.LazyArea;
import twilightforest.world.components.layer.vanillalegacy.context.BigContext;
import twilightforest.world.components.layer.vanillalegacy.context.LazyAreaContext;
import twilightforest.world.components.layer.vanillalegacy.traits.AreaTransformer1;

import java.util.function.LongFunction;

public enum StabilizeLayer implements AreaTransformer1 {
	INSTANCE;

	@Override
	public int getParentX(int x) {
		return x & 3;
	}

	@Override
	public int getParentY(int z) {
		return z & 3;
	}

	/**
	 * When we are near the center of each biome, make nearby areas that biome too
	 */
//	@Override
//	public int[] getInts(int x, int z, int width, int depth) {
//		int nx = x - 1;
//		int nz = z - 1;
//		int nwidth = width + 2;
//		int ndepth = depth + 2;
//		int input[] = parent.getInts(nx, nz, nwidth, ndepth);
//		int output[] = IntCache.getIntCache(width * depth);
//
//		int offX = x & 3;
//		int offZ = z & 3;
//
//		for (int dz = 0; dz < depth; dz++) {
//			for (int dx = 0; dx < width; dx++) {
//				int centerX = ((dx + offX + 1) & 0xFFFFFFFC) - offX;
//				int centerZ = ((dz + offZ + 1) & 0xFFFFFFFC) - offZ;
//
////            	if (dx == centerX && dz == centerZ)
////            	{
////            		output[dx + dz * width] = input[centerX + 1 + (centerZ + 1) * nwidth];
//////            		output[dx + dz * width] = BiomeLibrary.glacier.biomeID;
////            	}
////            	else
//				if (dx <= centerX + 1 && dx >= centerX - 1 && dz <= centerZ + 1 && dz >= centerZ - 1) {
//					output[dx + dz * width] = input[centerX + 1 + (centerZ + 1) * nwidth];
////            		output[dx + dz * width] = Biome.desert.biomeID;
////            		output[dx + dz * width] = input[dx + 1 + (dz + 1) * nwidth];
//				} else {
//					output[dx + dz * width] = input[dx + 1 + (dz + 1) * nwidth];
//				}
//			}
//		}
//
//		return output;
//	}

	@Override
	public ResourceKey<Biome> applyPixel(BigContext<?> context, Area layer, int x, int z) {
		int offX = getParentX(x << 4);
		int offZ = getParentY(z << 4);
		int centerX = ((x + offX + 1) & -4) - offX;
		int centerZ = ((z + offZ + 1) & -4) - offZ;

//            	if (dx == centerX && dz == centerZ)
//            	{
//            		output[dx + dz * width] = input[centerX + 1 + (centerZ + 1) * nwidth];
////            		output[dx + dz * width] = BiomeLibrary.glacier.biomeID;
//            	}
//            	else
		if (x <= centerX + 1 && x >= centerX - 1 && z <= centerZ + 1 && z >= centerZ - 1) {
			return layer.getBiome(centerX, centerZ);
//            		output[dx + dz * width] = Biome.desert.biomeID;
//            		output[dx + dz * width] = input[dx + 1 + (dz + 1) * nwidth];
		} else {
			return layer.getBiome(x, z);
		}
	}

	public record Factory(long salt, Holder<BiomeLayerFactory> parent) implements BiomeLayerFactory {
		public static final Codec<Factory> CODEC = RecordCodecBuilder.create(inst -> inst.group(
				Codec.LONG.fieldOf("salt").forGetter(Factory::salt),
				BiomeLayerStack.HOLDER_CODEC.fieldOf("parent").forGetter(Factory::parent)
		).apply(inst, Factory::new));

		@Override
		public LazyArea build(LongFunction<LazyAreaContext> contextFactory) {
			return INSTANCE.run(contextFactory.apply(this.salt), this.parent.value().build(contextFactory));
		}

		@Override
		public BiomeLayerType getType() {
			return BiomeLayerTypes.STABILIZE.get();
		}
	}
}
