package twilightforest.advancements;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.NBTPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.IProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockStateContainer;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Optional;

public class BlockPredicate {
    public static final BlockPredicate ANY = new BlockPredicate(ImmutableSet.of(), Blocks.AIR, NBTPredicate.ANY) {
        @Override public boolean test(World world, BlockPos pos) { return true; }
    };
    private ImmutableSet<PropertyPredicate> propertyPredicates;
    private final Block block;
    private final NBTPredicate nbtPredicate;

    private BlockPredicate(ImmutableSet<PropertyPredicate> propertyPredicates, Block block, NBTPredicate nbt) {
        this.propertyPredicates = propertyPredicates;
        this.block = block;
        this.nbtPredicate = nbt;
    }

    public static BlockPredicate deserialize(@Nullable JsonElement element) {
        if (element == null || element.isJsonNull())
            return ANY;

        JsonObject json = element.getAsJsonObject();

        Block block = Block.REGISTRY.getObject(new ResourceLocation(JSONUtils.getString(json, "block")));
        BlockStateContainer container = block.getBlockState();
        HashSet<PropertyPredicate<?>> properties = new HashSet<>();

        if (json.has("properties")) {
            for (JsonElement propertyRawGroup : JSONUtils.getJsonArray(json, "properties")) {
                JsonObject propertyGroup = propertyRawGroup.getAsJsonObject();

                IProperty<?> propertyKey = container.getProperty(JSONUtils.getString(propertyGroup, "property"));

                if (propertyKey != null)
                    createPropertyPredicateAndAddToSet(
                            properties,
                            propertyKey,
                            JSONUtils.getString(propertyGroup, "value"),
                            JSONUtils.getString(propertyGroup, "comparator")
                    );
            }
        }

        NBTPredicate nbtPredicate = json.has("nbt") ? NBTPredicate.deserialize(json.get("nbt")) : NBTPredicate.ANY;

        return new BlockPredicate(new ImmutableSet.Builder<PropertyPredicate>().addAll(properties).build(), block, nbtPredicate);
    }

    private static <T extends Comparable<T>> void createPropertyPredicateAndAddToSet(
            HashSet<PropertyPredicate<?>> predicateSet,
            IProperty<T> key,
            String value,
            String comparisonType) {

        Optional<T> schrodingersVar = key.parseValue(value);
        PropertyPredicate.ComparisonType predicateComparator = PropertyPredicate.ComparisonType.get(comparisonType);

        if (predicateComparator == null || !schrodingersVar.isPresent()) return; // Skip

        predicateSet.add(new PropertyPredicate<>(key, schrodingersVar.get(), predicateComparator));
    }

    public boolean test(World world, BlockPos pos) {
        if (!test(world.getBlockState(pos))) // If BlockState check fails, we're done
            return false;

        if (nbtPredicate == NBTPredicate.ANY) // Do we accept any NBT including lack thereof?
            return true;

        TileEntity te = world.getTileEntity(pos);

        return te != null && nbtPredicate.test(te.serializeNBT());
    }

    private boolean test(BlockState state) {
        if (block != state.getBlock()) return false; // Not same block

        for (PropertyPredicate propertyPredicate : propertyPredicates)
            if (!propertyPredicate.test(state))
                return false;

        return true;
    }

    private static class PropertyPredicate<T extends Comparable<T>> {
        private final IProperty<T> property;
        private final T value;
        private ComparisonType comparisonType;

        private PropertyPredicate(IProperty<T> key, T value, ComparisonType comparisonType) {
            this.property = key;
            this.value = value;
            this.comparisonType = comparisonType;
        }

        private boolean test(BlockState state) {
            return state.getProperties().contains(property) && comparisonType.test(value, state.get(property));
        }

        private enum ComparisonType {
            EQUAL   { @Override <T extends Comparable<T>> boolean test(T k, T v) { return k.compareTo(v) == 0; } },
            NOT     { @Override <T extends Comparable<T>> boolean test(T k, T v) { return k.compareTo(v) != 0; } },
            LESSER  { @Override <T extends Comparable<T>> boolean test(T k, T v) { return k.compareTo(v) <  0; } },
            GREATER { @Override <T extends Comparable<T>> boolean test(T k, T v) { return k.compareTo(v) >  0; } };

            abstract <T extends Comparable<T>> boolean test(T key, T value);

            @Nullable
            private static ComparisonType get(String type) {
                switch (type) {
                    case "equal":
                    case "same": return EQUAL;
                    case "not":
                    case "different":
                    case "equaln't": return NOT;
                    case "lesser":
                    case "lesser_than": return LESSER;
                    case "greater":
                    case "greater_than": return GREATER;
                }

                return null;
            }
        }
    }
}
