package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.layer.*;
import twilightforest.world.components.layer.vanillalegacy.BiomeLayerType;
import twilightforest.world.components.layer.vanillalegacy.SmoothLayer;
import twilightforest.world.components.layer.vanillalegacy.ZoomLayer;

import java.util.function.Supplier;

public class BiomeLayerTypes {
    public static final DeferredRegister<BiomeLayerType> BIOME_LAYER_TYPES = DeferredRegister.create(TFRegistries.Keys.BIOME_LAYER_TYPE, TwilightForestMod.ID);
    public static final Codec<BiomeLayerType> CODEC = ExtraCodecs.lazyInitializedCodec(TFRegistries.BIOME_LAYER_TYPE::byNameCodec);

    public static final DeferredHolder<BiomeLayerType, BiomeLayerType> RANDOM_BIOMES = registerType("random_biomes", () -> () -> RandomBiomeLayer.Factory.CODEC);
    public static final DeferredHolder<BiomeLayerType, BiomeLayerType> KEY_BIOMES = registerType("key_biomes", () -> () -> KeyBiomesLayer.Factory.CODEC);
    public static final DeferredHolder<BiomeLayerType, BiomeLayerType> COMPANION_BIOMES = registerType("companion_biomes", () -> () -> CompanionBiomesLayer.Factory.CODEC);
    public static final DeferredHolder<BiomeLayerType, BiomeLayerType> ZOOM = registerType("zoom", () -> () -> ZoomLayer.Factory.CODEC);
    public static final DeferredHolder<BiomeLayerType, BiomeLayerType> STABILIZE = registerType("stabilize", () -> () -> StabilizeLayer.Factory.CODEC);
    public static final DeferredHolder<BiomeLayerType, BiomeLayerType> BORDER = registerType("border", () -> () -> BorderLayer.Factory.CODEC);
    public static final DeferredHolder<BiomeLayerType, BiomeLayerType> SEAM = registerType("seam", () -> () -> SeamLayer.Factory.CODEC);
    public static final DeferredHolder<BiomeLayerType, BiomeLayerType> SMOOTH = registerType("smooth", () -> () -> SmoothLayer.Factory.CODEC);
    public static final DeferredHolder<BiomeLayerType, BiomeLayerType> FILTERED = registerType("filtered", () -> () -> FilteredBiomeLayer.Factory.CODEC);
    public static final DeferredHolder<BiomeLayerType, BiomeLayerType> MEDIAN = registerType("median", () -> () -> MedianLayer.Factory.CODEC);

    private static DeferredHolder<BiomeLayerType, BiomeLayerType> registerType(String name, Supplier<BiomeLayerType> factory) {
        return BIOME_LAYER_TYPES.register(name, factory);
    }
}
