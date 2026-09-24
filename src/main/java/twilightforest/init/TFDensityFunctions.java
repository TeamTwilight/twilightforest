package twilightforest.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.chunkgenerators.*;

@SuppressWarnings("unused")
public class TFDensityFunctions {
	public static final DeferredRegister<MapCodec<? extends DensityFunction>> DENSITY_FUNCTION_TYPES = DeferredRegister.create(Registries.DENSITY_FUNCTION_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<MapCodec<? extends DensityFunction>, MapCodec<TerrainDensityRouter>> BIOME_DRIVEN_TERRAIN = register("biome_driven_terrain", TerrainDensityRouter.CODEC);
	public static final DeferredHolder<MapCodec<? extends DensityFunction>, MapCodec<NoiseDensityRouter>> BIOME_DRIVEN_NOISE = register("biome_driven_noise", NoiseDensityRouter.CODEC);
	public static final DeferredHolder<MapCodec<? extends DensityFunction>, MapCodec<FocusedDensityFunction>> FOCUSED = register("focused", FocusedDensityFunction.CODEC);
	public static final DeferredHolder<MapCodec<? extends DensityFunction>, MapCodec<HollowHillFunction>> HOLLOW_HILL = register("hollow_hill", HollowHillFunction.CODEC);
	public static final DeferredHolder<MapCodec<? extends DensityFunction>, MapCodec<AbsoluteDifferenceFunction.Min>> COORD_MIN = register("coord_min", AbsoluteDifferenceFunction.Min.CODEC);
	public static final DeferredHolder<MapCodec<? extends DensityFunction>, MapCodec<AbsoluteDifferenceFunction.Max>> COORD_MAX = register("coord_max", AbsoluteDifferenceFunction.Max.CODEC);
	public static final DeferredHolder<MapCodec<? extends DensityFunction>, MapCodec<SqrtDensityFunction>> SQRT = register("sqrt", SqrtDensityFunction.CODEC);

	private static <T extends DensityFunction> DeferredHolder<MapCodec<? extends DensityFunction>, MapCodec<T>> register(String name, MapCodec<T> keyCodec) {
		return DENSITY_FUNCTION_TYPES.register(name, () -> keyCodec);
	}

}
