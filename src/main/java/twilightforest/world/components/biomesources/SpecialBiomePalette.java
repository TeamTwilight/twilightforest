package twilightforest.world.components.biomesources;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.floats.Float2ObjectSortedMap;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;
import twilightforest.util.Codecs;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/*

So what I had planned to do was keep the same idea from our old system. The way I understood it worked seeing it on the Magic Maps, is that you subdivide each map into quarters.
In each quarter, you place the center of a *Progression Landmark*. You will need to factor in spacing centers away from centers of other quadrants, or you will see an obvious cut with Biome placement.

*Progression Landmarks* are the Twilight Swamps, Snowy Lands, Dark Forest, and the Highlands.
Programmatically, a Progression Landmark is a system that determines biome placement as well as the required terraforming.
For the ease of logic I enforced having specifically 4 because a very nice algorithm skips needing to shuffle Progression Landmarks around.

You will need to make a class `ProgressionLandmarkConfig` that determines the influences (most likely `Depth` and `Scale`) on noise generation that you wish for each biome.
You could also instead build an Enum for handling the noise generation influence, and each entry has their own functions implemented to handle the algorithms you want on the terrain.

Progression Landmark systems should have a common implementation that we can worry about extending later when we REALLY wanna customize the biomes.
For now, I've made the system test for the centers, then it determines which biome to place based on distance from center.
In 3 of our cases, this is just two biomes. However our 4th case are the Highlands. These will need 3 biomes because of the Thornlands biome.

You'll need to worry about deterministically placing the centers randomly inside the blockworld, so the systems that back the Progression Landmark systems can function with positions relative to the center of the Progression Landmark System.
*/
public record SpecialBiomePalette(Holder<Biome> river, Climate.ParameterList<Holder<Biome>> regularBiomes, HolderSet<Biome> specialBiomes, List<Float2ObjectSortedMap<Holder<Biome>>> landmarkGradients, List<Holder<Biome>> allBiomes) {
    public static final Codec<SpecialBiomePalette> CODEC = Util.make(() -> {
        Codec<SpecialBiomePalette> codec = RecordCodecBuilder.create(instance -> instance.group(
                Biome.CODEC.fieldOf("river").forGetter(SpecialBiomePalette::river), // This is so we can predictably subdivide biomes in the voronoi mesh
                Codecs.CLIMATE_SYSTEM.fieldOf("common").forGetter(SpecialBiomePalette::regularBiomes), // Regular biomes like Canopy Forest, Savanna, etc.
                Biome.LIST_CODEC.fieldOf("special").forGetter(SpecialBiomePalette::specialBiomes), // Special ones like Dense Mushroom Forest, Enchanted Grove, Spooky Forest, etc
                Codecs.floatTreeCodec(Biome.CODEC).listOf().comapFlatMap(l -> Util.fixedSize(l, 4), Function.identity()).fieldOf("landmarks").forGetter(SpecialBiomePalette::landmarkGradients)
        ).apply(instance, SpecialBiomePalette::create));

        // Very important
        return codec.orElseGet(SpecialBiomePalette::create);
    });

    private static SpecialBiomePalette create() {
        // FIXME populate correct biomes
        return create(ServerLifecycleHooks.getCurrentServer().registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(Biomes.THE_VOID), new Climate.ParameterList<>(Collections.emptyList()), HolderSet.direct(), Collections.emptyList());
    }

    private static SpecialBiomePalette create(Holder<Biome> river, Climate.ParameterList<Holder<Biome>> common, HolderSet<Biome> rare, List<Float2ObjectSortedMap<Holder<Biome>>> clusters) {
        return new SpecialBiomePalette(river, common, rare, clusters, ImmutableList.<Holder<Biome>>builder().add(river).addAll(common.values().stream().map(Pair::getSecond).iterator()).addAll(rare).addAll(clusters.stream().flatMap(tree -> tree.float2ObjectEntrySet().stream()).map(Map.Entry::getValue).iterator()).build());
    }

    @Nullable
    public Holder<Biome> getNearestLandmark(float distanceRelative, int landmarkIndex, long permutation) {
        if (distanceRelative > 1) return null; // Outside the gradient

        return this.getNearestLandmark(landmarkIndex, permutation).get(distanceRelative);
    }

    private Float2ObjectSortedMap<Holder<Biome>> getNearestLandmark(int landmarkIndex, long permutation) {
        return this.landmarkGradients().get(BINARY_GRID_PERMUTATIONS[(int) (permutation % 24)][landmarkIndex]);
    }

    // Precomputed cache of every single possible permutation of a binary 2x2 grid
    private static final int[][] BINARY_GRID_PERMUTATIONS = new int[][] {
            new int[] { 0, 1, 2, 3 },
            new int[] { 0, 1, 3, 2 },
            new int[] { 0, 2, 1, 3 },
            new int[] { 0, 2, 3, 1 },
            new int[] { 0, 3, 1, 2 },
            new int[] { 0, 3, 2, 1 },
            new int[] { 1, 0, 2, 3 },
            new int[] { 1, 0, 3, 2 },
            new int[] { 1, 2, 0, 3 },
            new int[] { 1, 2, 3, 0 },
            new int[] { 1, 3, 0, 2 },
            new int[] { 1, 3, 2, 0 },
            new int[] { 2, 0, 1, 3 },
            new int[] { 2, 0, 3, 1 },
            new int[] { 2, 1, 0, 3 },
            new int[] { 2, 1, 3, 0 },
            new int[] { 2, 3, 0, 1 },
            new int[] { 2, 3, 1, 0 },
            new int[] { 3, 0, 1, 2 },
            new int[] { 3, 0, 2, 1 },
            new int[] { 3, 1, 0, 2 },
            new int[] { 3, 1, 2, 0 },
            new int[] { 3, 2, 0, 1 },
            new int[] { 3, 2, 1, 0 }
    };
}
