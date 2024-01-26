package twilightforest.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.chunkgenerators.AbsoluteDifferenceFunction;
import twilightforest.world.components.chunkgenerators.TerrainDensityRouter;
import twilightforest.world.components.chunkgenerators.FocusedDensityFunction;
import twilightforest.world.components.chunkgenerators.HollowHillFunction;

public class TFDensityFunctions {
    public static final DeferredRegister<Codec<? extends DensityFunction>> DENSITY_FUNCTION_TYPES = DeferredRegister.create(Registries.DENSITY_FUNCTION_TYPE, TwilightForestMod.ID);

    public static final DeferredHolder<Codec<? extends DensityFunction>, Codec<TerrainDensityRouter>> BIOME_DRIVEN = register("biome_driven", TerrainDensityRouter.CODEC);
    public static final DeferredHolder<Codec<? extends DensityFunction>, Codec<FocusedDensityFunction>> FOCUSED = register("focused", FocusedDensityFunction.CODEC);
    public static final DeferredHolder<Codec<? extends DensityFunction>, Codec<HollowHillFunction>> HOLLOW_HILL = register("hollow_hill", HollowHillFunction.CODEC);
    public static final DeferredHolder<Codec<? extends DensityFunction>, Codec<AbsoluteDifferenceFunction.Min>> COORD_MIN = register("coord_min", AbsoluteDifferenceFunction.Min.CODEC);
    public static final DeferredHolder<Codec<? extends DensityFunction>, Codec<AbsoluteDifferenceFunction.Max>> COORD_MAX = register("coord_max", AbsoluteDifferenceFunction.Max.CODEC);

    private static <T extends DensityFunction> DeferredHolder<Codec<? extends DensityFunction>, Codec<T>> register(String name, KeyDispatchDataCodec<T> keyCodec) {
        return DENSITY_FUNCTION_TYPES.register(name, keyCodec::codec);
    }
}
